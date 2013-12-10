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
 */
package org.javamoney.currencies.data;

import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Singleton;
import javax.money.CurrencyUnit;
import javax.money.MonetaryCurrencies;
import javax.money.spi.CurrencyProviderSpi;

import org.javamoney.currencies.spi.CurrencyUnitNamespaceSpi;

/**
 * Basic implementation of a {@link CurrencyUnitNamespaceSpi} that provides the
 * ISO 4217 currencies available from the JDK {@link Currency} class.
 * 
 * @author Anatole Tresch
 * @author Werner Keil
 */
@Singleton
public class IsoCurrencyJDKProvider implements CurrencyProviderSpi {

	private final Map<String, CurrencyUnit> currencies = new ConcurrentHashMap<String, CurrencyUnit>();

	public IsoCurrencyJDKProvider() {
		Set<Currency> jdkCurrencies = Currency.getAvailableCurrencies();
		for (Currency jdkCurrency : jdkCurrencies) {
			CurrencyUnit currency = MonetaryCurrencies.getCurrency(jdkCurrency
					.getCurrencyCode());
			this.currencies.put(currency.getCurrencyCode(), currency);
		}
	}

	@Override
	public CurrencyUnit getCurrencyUnit(String currencyCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CurrencyUnit getCurrencyUnit(Locale locale) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CurrencyUnit getCurrencyUnit(String currencyCode, long timestamp) {
		return null;
	}

	@Override
	public CurrencyUnit getCurrencyUnit(Locale locale, long timestamp) {
		return null;
	}

}
