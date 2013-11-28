/*
 * Copyright (c) 2012, 2013, Credit Suisse (Anatole Tresch), Werner Keil.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * Contributors: Anatole Tresch - initial implementation.
 */
package org.javamoney.convert.provider;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Singleton;
import javax.money.CurrencyUnit;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.javamoney.convert.ConversionProvider;
import org.javamoney.convert.CurrencyConverter;
import org.javamoney.convert.ExchangeRate;
import org.javamoney.convert.ExchangeRateType;
import org.javamoney.moneta.MoneyCurrency;
import org.javamoney.util.loader.AbstractResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class implements an {@link ExchangeRateProviderSpi} that loads data from
 * the European Central Bank data feed (XML). It loads the current exchange
 * rates, as well as historic rates for the past 90 days. By calling
 * {@link #loadHistoric()} the provider loads all data up to 1999 into its
 * historic data cache.
 * 
 * @author Anatole Tresch
 * @author Werner Keil
 */
@Singleton
public class EZBHistoricConversionProvider extends AbstractResource
		implements ConversionProvider {

	/** Historic data feed. */
	private static final String HISTORIC_URL = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-hist.xml";

	private static final String BASE_CURRENCY_CODE = "EUR";
	/** Base currency of the loaded rates is always EUR. */
	public static final CurrencyUnit BASE_CURRENCY = MoneyCurrency
			.of(BASE_CURRENCY_CODE);
	/** The logger used. */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(EZBHistoricConversionProvider.class);

	/** Historic exchange rates, rate timestamp as UTC long. */
	private Map<Long, Map<String, ExchangeRate>> historicRates = new ConcurrentHashMap<Long, Map<String, ExchangeRate>>();
	/** Parser factory. */
	private SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
	/** The {@link ExchangeRateType} of this provider. */
	public static final ExchangeRateType RATE_TYPE = ExchangeRateType
			.of("EZB");

	private CurrencyConverter currencyConverter = new DefaultCurrencyConverter(
			this);

	/**
	 * Constructor, also loads initial data.
	 * @throws MalformedURLException 
	 */
	public EZBHistoricConversionProvider() throws MalformedURLException {
		super("EZBHistoric", new URL(HISTORIC_URL),
				"/java-money/defaults/EZB/eurofxref-hist.xml");
		saxParserFactory.setNamespaceAware(false);
		saxParserFactory.setValidating(false);
		loadAsynch();
	}

	/**
	 * (Re)load the given data feed.
	 */
	protected void loadData(InputStream is) {
		final int oldSize = this.historicRates.size();
		try {
			SAXParser parser = saxParserFactory.newSAXParser();
			parser.parse(is, new RateReadingHandler());
		} catch (Exception e) {
			LOGGER.debug("Error", e);
		}
		int newSize = this.historicRates.size();
		LOGGER.info("Loaded " + getId() + " exchange rates for days:"
				+ (newSize - oldSize));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.money.convert.spi.ExchangeRateProviderSpi#getExchangeRateType
	 * ()
	 */
	@Override
	public ExchangeRateType getExchangeRateType() {
		return RATE_TYPE;
	}

	protected ExchangeRate getExchangeRateInternal(CurrencyUnit base,
			CurrencyUnit term,
			Long timestamp) {
		ExchangeRate.Builder builder = new ExchangeRate.Builder();
		builder.withProvider("European Central Bank");
		builder.withExchangeRateType(RATE_TYPE);
		builder.setBase(base);
		builder.setTerm(term);
		ExchangeRate sourceRate = null;
		ExchangeRate target = null;
		if (timestamp == null) {
			return null;
		} else {
			if (historicRates.isEmpty()) {
				return null;
			}
			final Calendar cal = new GregorianCalendar(
					TimeZone.getTimeZone("UTC"));
			if (timestamp != null) {
				cal.setTimeInMillis(timestamp);
			}
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Long targetTS = Long.valueOf(cal.getTimeInMillis());
			builder.withValidFromMillis(targetTS);
			Map<String, ExchangeRate> targets = this.historicRates
					.get(targetTS);
			if (targets == null) {
				return null;
			}
			sourceRate = targets.get(base.getCurrencyCode());
			target = targets.get(term.getCurrencyCode());
		}
		if (BASE_CURRENCY_CODE.equals(base.getCurrencyCode())
				&& BASE_CURRENCY_CODE.equals(term.getCurrencyCode())) {
			builder.withFactor(BigDecimal.ONE);
			return builder.build();
		} else if (BASE_CURRENCY_CODE.equals(term.getCurrencyCode())) {
			if (sourceRate == null) {
				return null;
			}
			return reverse(sourceRate);
		} else if (BASE_CURRENCY_CODE.equals(base.getCurrencyCode())) {
			return target;
		} else {
			// Get Conversion base as derived rate: base -> EUR -> term
			ExchangeRate rate1 = getExchangeRateInternal(base,
					MoneyCurrency.of(BASE_CURRENCY_CODE), timestamp);
			ExchangeRate rate2 = getExchangeRateInternal(
					MoneyCurrency.of(BASE_CURRENCY_CODE),
					term,
					timestamp);
			if (rate1 != null || rate2 != null) {
				builder.withFactor(rate1.getFactor().multiply(rate2.getFactor()));
				builder.setExchangeRateChain(rate1, rate2);
				return builder.build();
			}
			return null;
		}
	}

	private static ExchangeRate reverse(ExchangeRate rate) {
		if (rate == null) {
			throw new IllegalArgumentException("Rate null is not reversable.");
		}
		return new ExchangeRate.Builder().setExchangeRate(rate)
				.setBase(rate.getTerm()).setTerm(rate.getBase())
				.withFactor(BigDecimal.ONE.divide(rate.getFactor(),
						MathContext.DECIMAL64)).build();
	}

	/**
	 * SAX Event Handler that reads the quotes.
	 * <p>
	 * Format: <gesmes:Envelope
	 * xmlns:gesmes="http://www.gesmes.org/xml/2002-08-01"
	 * xmlns="http://www.ecb.int/vocabulary/2002-08-01/eurofxref">
	 * <gesmes:subject>Reference rates</gesmes:subject> <gesmes:Sender>
	 * <gesmes:name>European Central Bank</gesmes:name> </gesmes:Sender> <Cube>
	 * <Cube time="2013-02-21">...</Cube> <Cube time="2013-02-20">...</Cube>
	 * <Cube time="2013-02-19"> <Cube currency="USD" rate="1.3349"/> <Cube
	 * currency="JPY" rate="124.81"/> <Cube currency="BGN" rate="1.9558"/> <Cube
	 * currency="CZK" rate="25.434"/> <Cube currency="DKK" rate="7.4599"/> <Cube
	 * currency="GBP" rate="0.8631"/> <Cube currency="HUF" rate="290.79"/> <Cube
	 * currency="LTL" rate="3.4528"/> ...
	 * 
	 * @author Anatole Tresch
	 */
	private class RateReadingHandler extends DefaultHandler {

		/** Date parser. */
		private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		/** Current timestamp for the given section. */
		private Long timestamp;

		/** Flag, if current or historic data is loaded. */
		// private boolean loadCurrent;

		/**
		 * Creates a new parser.
		 * 
		 * @param loadCurrent
		 *            Flag, if current or historic data is loaded.
		 */
		public RateReadingHandler() {
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
		 * java.lang.String, java.lang.String, org.xml.sax.Attributes)
		 */
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			try {
				if ("Cube".equals(qName)) {
					if (attributes.getValue("time") != null) {
						Date date = dateFormat.parse(attributes
								.getValue("time"));
						timestamp = Long.valueOf(date.getTime());
					} else if (attributes.getValue("currency") != null) {
						// read data <Cube currency="USD" rate="1.3349"/>
						CurrencyUnit tgtCurrency = MoneyCurrency.of(attributes
								.getValue("currency"));
						addRate(tgtCurrency, timestamp,
								BigDecimal.valueOf(Double
										.parseDouble(attributes
												.getValue("rate"))));
					}
				}
				super.startElement(uri, localName, qName, attributes);
			} catch (ParseException e) {
				throw new SAXException("Failed to read.", e);
			}
		}

	}

	/**
	 * Method to add a currency exchange rate.
	 * 
	 * @param term
	 *            the term (target) currency, mapped from EUR.
	 * @param timestamp
	 *            The target day.
	 * @param rate
	 *            The rate.
	 * @param loadCurrent
	 *            Flag, if current or historic data is loaded.
	 */
	void addRate(CurrencyUnit term, Long timestamp, BigDecimal rate) {
		ExchangeRate.Builder builder = new ExchangeRate.Builder();
		builder.setBase(BASE_CURRENCY);
		builder.setTerm(term);
		builder.withValidFromMillis(timestamp);
		builder.withProvider("European Central Bank");
		builder.withFactor(rate);
		builder.withExchangeRateType(RATE_TYPE);
		ExchangeRate exchangeRate = builder.build();
		Map<String, ExchangeRate> rateMap = this.historicRates
				.get(timestamp);
		if (rateMap == null) {
			synchronized (this.historicRates) {
				rateMap = this.historicRates.get(timestamp);
				if (rateMap == null) {
					rateMap = new ConcurrentHashMap<String, ExchangeRate>();
					this.historicRates.put(timestamp, rateMap);
				}
			}
		}
		rateMap.put(term.getCurrencyCode(), exchangeRate);
	}

	@Override
	public boolean isAvailable(CurrencyUnit src, CurrencyUnit target) {
		return getExchangeRate(src, target) != null;
	}

	@Override
	public boolean isAvailable(CurrencyUnit src, CurrencyUnit target,
			long timestamp) {
		return getExchangeRate(src, target, timestamp) != null;
	}

	@Override
	public ExchangeRate getExchangeRate(CurrencyUnit source, CurrencyUnit target) {
		return getExchangeRateInternal(source, target, null);
	}

	@Override
	public ExchangeRate getReversed(ExchangeRate rate) {
		return getExchangeRate(rate.getTerm(), rate.getBase(),
				rate.getValidFromMillis());
	}

	@Override
	public CurrencyConverter getConverter() {
		return currencyConverter;
	}

	@Override
	public ExchangeRate getExchangeRate(CurrencyUnit base,
			CurrencyUnit term, long timestamp) {
		return getExchangeRateInternal(base, term, timestamp);
	}
}