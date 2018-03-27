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

import javax.money.MonetaryAmount;
import javax.money.MonetaryOperator;
import java.util.Objects;

import static org.javamoney.calc.CalculationContext.one;

/**
 * <img src= "http://www.financeformulas.net/Formula%20Images/Annuity%20Payment%20(FV)%201.gif" />
 * <p>
 * The annuity payment formula shown above is used to calculate the cash flows of an annuity when
 * future value is known. An annuity is denoted as a series of periodic payments. The annuity
 * payment formula shown here is specifically used when the future value is known, as opposed to the
 * annuity payment formula used when present value is known. There are not only mathematical
 * differences between calculating an annuity when present value is known and when future value is
 * known, but also differences in the real life application of the formulas. For example, if an
 * individual is wanting to calculate the payments on a loan, the original loan balance would be
 * considered the present value and the payment formula would accommodate this known variable.
 * However, if an individual is wanting to calculate how much they need to save per year in an
 * interest bearing account to have a certain balance after a specific period of time, then this
 * wanted balance would be considered the future value. The latter example would use the annuity
 * payment using future value formula as the balance is increasing instead of decreasing:
 *
 * @author Anatole Tresch
 * @author Werner Keil
 * @link http://www.financeformulas.net/Annuity-Payment-from-Future-Value.html
 */
public abstract class AbstractRateAndPeriodBasedOperator implements MonetaryOperator {

    /**
     * the target rate, not null.
     */
    protected RateAndPeriods rateAndPeriods;

    /**
     * Private constructor.
     *
     * @param rateAndPeriods    the target rate and periods, not null.
     */
    protected AbstractRateAndPeriodBasedOperator(RateAndPeriods rateAndPeriods) {
        this.rateAndPeriods = Objects.requireNonNull(rateAndPeriods);
    }

    public RateAndPeriods getRateAndPeriods() {
        return rateAndPeriods;
    }

    public Rate getRate(){
        return rateAndPeriods.getRate();
    }

    public int getPeriods(){
        return rateAndPeriods.getPeriods();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(getClass().equals(o.getClass()))) return false;

        AbstractRateAndPeriodBasedOperator that = (AbstractRateAndPeriodBasedOperator) o;

        return rateAndPeriods.equals(that.rateAndPeriods);
    }

    @Override
    public int hashCode() {
        return rateAndPeriods.hashCode();
    }

    @Override
    public String toString() {
        return "AbstractRateAndPeriodBasedOperator{" +
                "rateAndPeriods=" + rateAndPeriods +
                '}';
    }
}
