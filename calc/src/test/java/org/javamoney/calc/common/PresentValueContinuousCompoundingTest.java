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

import org.javamoney.moneta.Money;
import org.junit.Test;

import javax.money.Monetary;
import javax.money.MonetaryOperator;
import javax.money.RoundingQueryBuilder;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link PresentValue} formula calculator.
 *
 * @link http ://www.financeformulas.net/Present_Value.html#calcHeader
 */
public class PresentValueContinuousCompoundingTest {

    /**
     * Method: of(Rate rate, int periods)
     *
     * @throws Exception the exception
     */
    @Test
    public void testOfAndApply() throws Exception {
        Money money = Money.of(100, "CHF");
        MonetaryOperator rounding = Monetary.getRounding(RoundingQueryBuilder.of().setScale(2).set(RoundingMode.HALF_EVEN)
                .build());
        assertEquals(Money.of(BigDecimal.valueOf(95.12), "CHF"), money.with(PresentValueContinuousCompounding.of(RateAndPeriods.of(0.05, 1))).with(rounding));
        assertEquals(Money.of(BigDecimal.valueOf(90.48), "CHF"), money.with(PresentValueContinuousCompounding.of(RateAndPeriods.of(0.05, 2))).with(rounding));
        assertEquals(Money.of(BigDecimal.valueOf(46.3), "CHF"), money.with(PresentValueContinuousCompounding.of(RateAndPeriods.of(0.07, 11))).with(rounding));

        assertEquals(Money.of(BigDecimal.valueOf(100.00), "CHF"), money.with(PresentValueContinuousCompounding.of(RateAndPeriods.of(0.05, 0))).with(rounding));
        assertEquals(Money.of(BigDecimal.valueOf(100.00), "CHF"), money.with(PresentValueContinuousCompounding.of(RateAndPeriods.of(-0.05, 0))).with(rounding));

        assertEquals(Money.of(BigDecimal.valueOf(105.13), "CHF"), money.with(PresentValueContinuousCompounding.of(RateAndPeriods.of(-0.05, 1))).with(rounding));
        assertEquals(Money.of(BigDecimal.valueOf(110.52), "CHF"), money.with(PresentValueContinuousCompounding.of(RateAndPeriods.of(-0.05, 2))).with(rounding));
        assertEquals(Money.of(BigDecimal.valueOf(215.98), "CHF"), money.with(PresentValueContinuousCompounding.of(RateAndPeriods.of(-0.07, 11))).with(rounding));
    }

    /**
     * Method: calculate(MonetaryAmount amount, Rate rate, int periods)
     *
     * @throws Exception the exception
     */
    @Test
    public void testCalculate() throws Exception {
        Money money = Money.of(100, "CHF");
        MonetaryOperator rounding = Monetary.getRounding(RoundingQueryBuilder.of().setScale(2).set(RoundingMode.HALF_EVEN)
                .build());
        assertEquals(Money.of(BigDecimal.valueOf(95.12), "CHF"), PresentValueContinuousCompounding.calculate(money, RateAndPeriods.of(0.05, 1)).with(rounding));
        assertEquals(Money.of(BigDecimal.valueOf(90.48), "CHF"), PresentValueContinuousCompounding.calculate(money, RateAndPeriods.of(0.05, 2)).with(rounding));
        assertEquals(Money.of(BigDecimal.valueOf(46.3), "CHF"), PresentValueContinuousCompounding.calculate(money, RateAndPeriods.of(0.07, 11)).with(rounding));
    }

    /**
     * Method: toString()
     *
     * @throws Exception the exception
     */
    @Test
    public void testToString() throws Exception {
        assertEquals("PresentValueContinuousCompounding{\n" +
                " RateAndPeriods{\n" +
                "  rate=Rate[0.05]\n" +
                "  periods=1}}", PresentValueContinuousCompounding.of(RateAndPeriods.of(0.05, 1)).toString());
        assertEquals("PresentValueContinuousCompounding{\n" +
                " RateAndPeriods{\n" +
                "  rate=Rate[0.05]\n" +
                "  periods=2}}", PresentValueContinuousCompounding.of(RateAndPeriods.of(0.05, 2)).toString());
        assertEquals("PresentValueContinuousCompounding{\n" +
                " RateAndPeriods{\n" +
                "  rate=Rate[0.07]\n" +
                "  periods=11}}", PresentValueContinuousCompounding.of(RateAndPeriods.of(0.07, 11)).toString());
    }

}
