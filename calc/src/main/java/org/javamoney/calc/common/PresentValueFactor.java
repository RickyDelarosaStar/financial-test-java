/*
 * Copyright (c) 2012, 2018, Werner Keil, Anatole Tresch and others.
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
 * Contributors: @atsticks, @keilw
 */
package org.javamoney.calc.common;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * The formula for the future value factor is used to calculate the future value of an amount
 * per dollar of its present value. The future value factor is generally found on a table which is
 * used to simplify calculations for amounts greater than one dollar (see example below). The future
 * value factor formula is based on the concept of time value of money. The concept of time value of
 * money is that an amount today is worth more than if that same nominal amount is received at a
 * future date. Any amount received today can be invested and receive earnings, as opposed to
 * waiting to receive the same amount with no earnings. An amount of $105 to be received a year from
 * now may be okay if the individual wants $100 today, assuming that the individual can earn 5%
 * otherwise in one year.
 *
 * @author Anatole Tresch
 * @see <a href="http://www.financeformulas.net/Present_Value_Factor.html">http://www.financeformulas.net/Present_Value_Factor.html</a>
 */
public final class PresentValueFactor {

	private PresentValueFactor() {
	}

    /**
     * Calculate big decimal.
     *
     * @param rateAndPeriods the rate and periods
     * @return the big decimal
     */
    public static BigDecimal calculate(RateAndPeriods rateAndPeriods) {
        Objects.requireNonNull(rateAndPeriods, "rate required.");
		// (1+r)^n
		return BigDecimal.ONE.add(rateAndPeriods.getRate().get()).pow(rateAndPeriods.getPeriods());
	}

}
