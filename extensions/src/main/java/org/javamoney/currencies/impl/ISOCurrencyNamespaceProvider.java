package org.javamoney.currencies.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;

import javax.money.CurrencyUnit;
import javax.money.MonetaryCurrencies;

import org.javamoney.currencies.spi.CurrencyUnitNamespaceSpi;
import org.javamoney.currencies.spi.MonetaryCurrenciesSingletonSpi;

import com.ibm.icu.util.Currency;

/**
 * Default implementation of {@link MonetaryCurrenciesSingletonSpi}, active
 * if no instance of {@link MonetaryCurrenciesSingletonSpi} was registered
 * using the {@link ServiceLoader}.
 * 
 * @author Anatole Tresch
 */
public final class ISOCurrencyNamespaceProvider implements
		CurrencyUnitNamespaceSpi {
	
	private static final String ISO_NS = "ISO-4217";
	
	private static final Set<String> NAMESPACES = new HashSet<String>();

	static{
		NAMESPACES.add(ISO_NS);
	}
	
	/**
	 * This method allows to evaluate, if the given currency namespace is
	 * defined. {@code "ISO-4217"} should be defined in all environments
	 * (default).
	 * 
	 * @param namespace
	 *            the required namespace
	 * @return {@code true}, if the namespace exists.
	 */
	@Override
	public boolean isNamespaceAvailable(String namespace) {
		return NAMESPACES.contains(namespace);
	}

	/**
	 * This method allows to access all namespaces currently defined.
	 * "ISO-4217" should be defined in all environments (default).
	 * 
	 * @return the collection of currently defined namespace, never
	 *         {@code null}.
	 */
	@Override
	public Collection<String> getNamespaces() {
		return NAMESPACES;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.money.ext.spi.MonetaryCurrenciesSingletonSpi#getAll(java.lang
	 * .String)
	 */
	@Override
	public Collection<CurrencyUnit> getCurrencies(String namespace) {
		if(ISO_NS.equals(namespace)){
			List<CurrencyUnit> result = new ArrayList<>();
			for(Currency currency:Currency.getAvailableCurrencies()){
				result.add(MonetaryCurrencies.getCurrency(currency.getCurrencyCode()));
			}
			return result;
		}
		return Collections.emptySet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.money.ext.spi.MonetaryCurrenciesSingletonSpi#getNamespace(java
	 * .lang.String)
	 */
	@Override
	public Set<String> getNamespaces(String code) {
		if(Currency.isAvailable(code, null, null)){
			return NAMESPACES;
		}
		return Collections.emptySet();
	}
	
}