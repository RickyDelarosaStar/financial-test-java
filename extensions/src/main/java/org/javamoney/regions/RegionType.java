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
package org.javamoney.regions;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Defines the different region types available. This allows to segregate
 * different grouping strategy types.
 * <P>
 * This class is thread-safe, immutable, {@link Serializable} and
 * {@link Comparable}.
 * 
 * @author Anatole Tresch
 */
public final class RegionType implements Serializable, Comparable<RegionType> {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = -921476180849747654L;

	/** Shared cache of types instantiated with the #of(String) method. */
	private static final Map<String, RegionType> TYPE_CACHE = new ConcurrentHashMap<String, RegionType>();

	/** Type representing a continent. */
	public static final RegionType CONTINENT = of("CONTINENT");

	/**
	 * Type representing a region whose code has been deprecated, usually due to
	 * a country splitting into multiple territories or changing its name.
	 */
	public static final RegionType DEPRECATED = of("DEPRECATED");
	/**
	 * Type representing a grouping of territories that is not to be used in the
	 * normal WORLD/CONTINENT/SUBCONTINENT/TERRITORY containment tree.
	 */
	public static final RegionType GROUPING = of("GROUPING");
	/** Type representing a sub-continent. */
	public static final RegionType SUBCONTINENT = of("SUBCONTINENT");
	/** Type representing a territory. */
	public static final RegionType TERRITORY = of("TERRITORY");
	/** Type representing the unknown region. */
	public static final RegionType UNKNOWN = of("UNKNOWN");
	/** Type representing the whole world. */
	public static final RegionType WORLD = of("WORLD");

	/** The type's id. */
	private String id;

	/**
	 * Constructor for the instance.
	 * 
	 * @param id
	 *            The type's identifier, not {@code null}
	 */
	public RegionType(String id) {
		if (id == null || id.isEmpty()) {
			throw new IllegalArgumentException("id null or empty.");
		}
		this.id = id;
	}

	/**
	 * Access an {@link RegionType} by id. Instances with the same id will be
	 * shared.
	 * 
	 * @param id
	 *            The rate's identifier.
	 * @return the corresponding type, never {@code null}.
	 */
	public static RegionType of(String id) {
		RegionType type = TYPE_CACHE.get(id);
		if (type != null) {
			return type;
		}
		type = new RegionType(id);
		TYPE_CACHE.put(id, type);
		return type;
	}

	/**
	 * Access all region types.
	 * 
	 * @return
	 */
	public static Collection<RegionType> getTypes() {
		return TYPE_CACHE.values();
	}

	/**
	 * Get the (non localized) identifier of the {@link ExchangeRateType}.
	 * 
	 * @return The identifier, never null.
	 */
	public String getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RegionType other = (RegionType) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RegionType [id=" + id + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(RegionType o) {
		if (o == null) {
			return -1;
		}
		return this.id.compareTo(o.id);
	}

}
