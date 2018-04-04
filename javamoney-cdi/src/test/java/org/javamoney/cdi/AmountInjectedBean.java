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
 * Contributors: @atsticks, @keilw, @otjava
 */
package org.javamoney.cdi;

import org.javamoney.moneta.Money;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.money.MonetaryAmountFactory;
import java.util.Collection;

/**
 * Test bean for injection of amount related beans and providers.
 */
@Dependent
public class AmountInjectedBean {

    @Inject @Amount(Money.class)
    MonetaryAmountFactory moneyFactory;

    @Inject @Amount(precision = 10)
    MonetaryAmountFactory precisionFactory;

    @Inject @Amount(maxScale = 2)
    MonetaryAmountFactory scaledFactory;

    @Inject @Amount(providers = "ISO")
    Collection<MonetaryAmountFactory> providerFactories;

    @Inject @Amount(fixedScale = true, precision = 12)
    Collection<MonetaryAmountFactory> fixedScaleFactories;

}
