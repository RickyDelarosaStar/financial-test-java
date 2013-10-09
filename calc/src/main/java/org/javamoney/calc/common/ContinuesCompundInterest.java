/*
 *  Copyright (c) 2012, 2013, Credit Suisse (Anatole Tresch), Werner Keil.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.javamoney.calc.common;

import java.math.BigDecimal;

import javax.money.MonetaryAdjuster;
import javax.money.MonetaryAmount;

import org.javamoney.calc.function.CompoundFunction;
import org.javamoney.calc.function.CompoundType;
import org.javamoney.calc.function.CompoundValue;
import org.javamoney.moneta.Money;

/**
 * The continuous compounding formula is used to determine the interest earned
 * on an account that is constantly compounded, essentially leading to an
 * infinite amount of compounding periods. The effect of compounding is earning
 * interest on an investment, or at times paying interest on a debt, that is
 * reinvested to earn additional monies that would not have been gained based on
 * the principal balance alone. By earning interest on prior interest, one can
 * earn at an exponential rate. The continuous compounding formula takes this
 * effect of compounding to the furthest limit. Instead of compounding interest
 * on an monthly, quarterly, or annual basis, continuous compounding will
 * effectively reinvest gains perpetually.
 * 
 * <p>
 * <img src=
 * "http://www.financeformulas.net/Formula%20Images/Continuous%20Compounding%201.gif"
 * />
 * <p>
 * or...
 * 
 * <pre>
 * int power = &lt;rate> * &lt;periods>
 * factor = <i>e</i>.pow(&lt;power>);
 * f(&lt;amount>) = &lt;amopunt> * &lt;factor>
 * </pre>
 * 
 * @author Anatole
 * 
 */
public class ContinuesCompundInterest implements MonetaryAdjuster,
		CompoundFunction<MonetaryAmount> {

	private int periods;
	private Rate rate;
	private BigDecimal factor;

	private static final CompoundType INPUT_TYPE = new CompoundType.Builder()
			.withIdForInput(ContinuesCompundInterest.class)
			.withRequiredArg("periods", Integer.class)
			.withRequiredArg("rate", Rate.class)
			.withRequiredArg("amount", MonetaryAmount.class).build();

	public ContinuesCompundInterest(Rate rate, int periods) {
		this.rate = rate;
		this.periods = periods;
		int power = rate.getRate().multiply(BigDecimal.valueOf(periods))
				.intValue();
		factor = BigDecimal.valueOf(Math.E).pow(power);
	}

	/**
	 * The factor to be multiplied to get the compounded value from an amount a,
	 * where {@code f(a) = factor * a}.
	 * 
	 * @return the compounded interest value factor based on the given rate and
	 *         period.
	 */
	public BigDecimal getFactor() {
		return factor;
	}

	/**
	 * The number of periods.
	 * 
	 * @return the number of periods.
	 */
	public int getPeriods() {
		return periods;
	}

	/**
	 * The rate of return.
	 * 
	 * @return the rate of return.
	 */
	public Rate getRate() {
		return rate;
	}

	/**
	 * Calculates the continuously compounded value of {@code value}. The result
	 * hereby represents the compounded value and interest with the given rate
	 * and for n periods.
	 */
	@Override
	public MonetaryAmount adjustInto(MonetaryAmount amount) {
		return Money.from(amount).multiply(factor);
	}

	@Override
	public CompoundType getInputTape() {
		return INPUT_TYPE;
	}

	@Override
	public Class<MonetaryAmount> getResultType() {
		return MonetaryAmount.class;
	}

	@Override
	public MonetaryAmount calculate(CompoundValue input) {
		INPUT_TYPE.checkInput(input);
		Rate rate = input.get("rate",  Rate.class);
		int periods = input.get("periods", Integer.class);
		MonetaryAmount amount = input.get("amount",  MonetaryAmount.class);
		int power = rate.getRate().multiply(BigDecimal.valueOf(periods))
				.intValue();
		BigDecimal f = BigDecimal.valueOf(Math.E).pow(power);
		return Money.from(amount).multiply(f);
	}

}
