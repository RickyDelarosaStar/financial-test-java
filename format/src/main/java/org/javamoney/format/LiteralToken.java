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
import java.io.Serializable;
import java.util.Locale;

/**
 * {@link FormatToken} which adds an arbitrary literal constant value to the
 * output.
 * <p>
 * This class is thread safe, immutable and serializable.
 * 
 * @author Anatole Tresch
 * 
 * @param <T>
 *            The item type.
 */
public final class LiteralToken<R> implements FormatToken<R>, Serializable {

	/**
	 * The literal part.
	 */
	private String token;
	/** The token's id, or null. */
	private String tokenId;

	/**
	 * Constructor.
	 * 
	 * @param token
	 *            The literal token part.
	 */
	public LiteralToken(String token) {
		if (token == null) {
			throw new IllegalArgumentException("Token is required.");
		}
		this.token = token;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.money.format.FormatToken#getTokenId()
	 */
	@Override
	public String getTokenId() {
		return tokenId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.money.format.FormatToken#parse(javax.money.format.ParseContext,
	 * java.util.Locale, javax.money.format.LocalizationStyle)
	 */
	@Override
	public void parse(ParseContext<R> context, Locale locale,
			LocalizationStyle style)
			throws ItemParseException {
		if (!context.consume(token)) {
			throw new ItemParseException("Expected '" + token + "' in "
					+ context.getInput().toString());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.money.format.FormatToken#print(java.lang.Appendable,
	 * java.lang.Object, java.util.Locale, javax.money.format.LocalizationStyle)
	 */
	@Override
	public void print(Appendable appendable, R item, Locale locale,
			LocalizationStyle style)
			throws IOException {
		appendable.append(this.token);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LiteralToken [token=" + token + "]";
	}

}
