/*
 * Copyright (c) 2012, 2013, Credit Suisse (Anatole Tresch), Werner Keil. Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.javamoney.calc.common;

import static org.javamoney.calc.CalculationContext.mathContext;
import static org.javamoney.calc.CalculationContext.one;

import javax.money.MonetaryException;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * <img src= "http://www.financeformulas.net/Formula%20Images/PV%20of%20Annuity%201.gif" />
 * <p>
 * The present value of annuity formula determines the value of a series of future periodic payments
 * at a given time. The present value of annuity formula relies on the concept of time value of
 * money, in that one dollar present day is worth more than that same dollar at a future date.
 * <p>
 * <h3>Rate Per Period</h3>
 * <p>
 * As with any financial formula that involves a rate, it is important to make sure that the rate is
 * consistent with the other variables in the formula. If the payment is per month, then the rate
 * needs to be per month, and similarly, the rate would need to be the annual rate if the payment is
 * annual.
 * <p>
 * An example would be an annuity that has a 12% annual rate and payments are made monthly. The
 * monthly rate of 1% would need to be used in the formula.
 * 
 * @link http://www.financeformulas.net/Present_Value_of_Annuity.html
 * @author Anatole Tresch
 * // TODO Check the correctness here og the above as well as the code in the class...
 */
public final class PresentValueOfAnnuityPaymentFactor {

	private PresentValueOfAnnuityPaymentFactor() {
	}

    public static BigDecimal calculate(Rate rate, int periods) {
        Objects.requireNonNull(rate, "Rate required.");
		if(periods<0){
			throw new MonetaryException("Can only caclulate PresentValueOfAnnuityFactor with period >= 0.");
		}
		if(periods==0){
			return BigDecimal.ZERO;
		}
		// PVofA = P * [ (1 - (1 + r).pow(-n)) / r ]
		BigDecimal fact1 = one().add(rate.get()).pow(-periods, mathContext());
		BigDecimal counter = one().subtract(fact1);
		return counter.divide(rate.get(), mathContext());
	}

}
