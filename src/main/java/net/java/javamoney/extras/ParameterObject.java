/*
 *  Copyright (c) 2012, 2013, Werner Keil, Credit Suisse (Anatole Tresch).
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
 *
 * Contributors:
 *    Anatole Tresch - initial version.
 */
package net.java.javamoney.extras;

import java.util.Map;

/**
 * Defines a {@link ParameterObject} containing several results. Hereby the
 * different results are identified by arbitrary keys. Additionally each
 * {@link ParameterObject} has a <i>leading</i> item that identifies the type of
 * result.<br/>
 * A {@link ParameterObject} instance is defined to be implemented as immutable
 * object and therefore is very useful for modeling multidimensional results
 * objects or input parameters as they are common in financial applications.
 * 
 * @author Anatole Tresch
 */
public interface ParameterObject extends Map<String,Object>{

	/**
	 * A {@link ParameterObject} may have an identifier that helps to identify,
	 * what type of items object is returned.
	 * 
	 * @return the {@link ParameterObject}'s type, never null.
	 */
	public String getId();

	/**
	 * Get the compound type of this instance.
	 * 
	 * @return the compound type, never {@code null}.
	 */
	public ParameterObjectType getParameterObjectType();
	
}
