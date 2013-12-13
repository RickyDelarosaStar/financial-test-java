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
package org.javamoney.format;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.javamoney.format.ItemFormat;
import org.javamoney.format.ItemFormatException;
import org.javamoney.format.ItemParseException;
import org.javamoney.format.LocalizationStyle;
import org.javamoney.format.TokenizableFormats;
import org.javamoney.format.spi.MonetaryFormatsSingletonSpi;

/**
 * Tests class registered into {@link TokenizableFormats} to test
 * {@link TokenizableFormats}.
 * 
 * @author Anatole Tresch
 * 
 */
public class TestMonetaryFormatsSpi implements MonetaryFormatsSingletonSpi {

	@Override
	public Collection<String> getSupportedStyleIds(Class<?> targetType) {
		Set<String> res = new HashSet<String>();
		res.add(targetType.getSimpleName());
		return res;
	}

	@Override
	public boolean isSupportedStyle(Class<?> targetType, String styleId) {
		if (styleId.equals(targetType.getSimpleName())) {
			return true;
		}
		return false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> ItemFormat<T> getItemFormat(Class<T> targetType,
			LocalizationStyle style) throws ItemFormatException {
		if (style.isDefaultStyle()) {
			return new DummyItemFormatter(style, targetType);
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public static final class DummyItemFormatter implements ItemFormat {

		private LocalizationStyle style;

		private Class targetClass;

		public DummyItemFormatter(LocalizationStyle style, Class type) {
			this.style = style;
			this.targetClass = type;
		}

		@Override
		public Class getTargetClass() {
			return targetClass;
		}

		@Override
		public LocalizationStyle getStyle() {
			return style;
		}

		@Override
		public String format(Object item, Locale locale) {
			return String.valueOf(item);
		}

		@Override
		public void print(Appendable appendable, Object item, Locale locale)
				throws IOException {
			appendable.append(String.valueOf(item));
		}

		@Override
		public Object parse(CharSequence text, Locale locale) throws ItemParseException {
			try {
				return targetClass.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

	}

	@Override
	public LocalizationStyle getLocalizationStyle(Class<?> targetType, String styleId) {
	    if(targetType.getSimpleName().equals(styleId)){
		LocalizationStyle style = LocalizationStyle.of(targetType, styleId);
		if(style==null){
		    style = new LocalizationStyle.Builder(targetType, styleId).build(true);
		}
		return style;
	    }
	    return null;
	}

}
