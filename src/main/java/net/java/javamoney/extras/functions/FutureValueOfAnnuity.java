package net.java.javamoney.extras.functions;

import java.math.BigDecimal;

import javax.money.MonetaryAmount;
import javax.money.MonetaryOperator;

import net.java.javamoney.extras.Rate;

/**
 * The future value of an annuity formula is used to calculate what the value at
 * a future date would be for a series of periodic payments. The future value of
 * an annuity formula assumes that
 * 
 * <nl>
 * <li>The rate does not change
 * <li>The first payment is one period away
 * <li>The periodic payment does not change
 * </nl>
 * If the rate or periodic payment does change, then the sum of the future value
 * of each individual cash flow would need to be calculated to determine the
 * future value of the annuity. If the first cash flow, or payment, is made
 * immediately, the future value of annuity due formula would be used.
 * <p>
 * <img src="http://www.financeformulas.net/Formula%20Images/FV%20of%20Annuity%204.gif" />
 * <p>
 * or...
 * <pre>
 * &lt;amount> * (((1 + &lt;rate>).pow(&lt;periods>))-1/&lt;rate>)
 * </pre>
 * 
 * @see http://www.financeformulas.net/Future_Value_of_Annuity.html
 * @author Anatole
 * 
 */
public class FutureValueOfAnnuity implements MonetaryOperator {

	private Rate rate;
	private int periods;

	public FutureValueOfAnnuity(Rate rate, int periods) {
		if (rate == null) {
			throw new IllegalArgumentException("rate null.");
		}
		this.rate = rate;
		this.periods = periods;
	}

	@Override
	public MonetaryAmount apply(MonetaryAmount value) {
		// Am * (((1 + r).pow(n))-1/rate)
		return value.multiply(BigDecimal.ONE.add(rate.getRate()).pow(periods)
				.subtract(BigDecimal.ONE).divide(rate.getRate()));
	}
}
