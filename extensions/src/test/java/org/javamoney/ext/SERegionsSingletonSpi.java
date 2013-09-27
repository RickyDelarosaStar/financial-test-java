/*
 * Copyright (c) 2012, 2013, Werner Keil, Credit Suisse (Anatole Tresch).
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
 * 
 * Contributors: Anatole Tresch - initial version. Wernner Keil - extensions and
 * adaptions.
 */
package org.javamoney.ext;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.javamoney.ext.spi.RegionProviderSpi;
import org.javamoney.ext.spi.RegionsSingletonSpi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class models the singleton defined by JSR 354 that provides accessors
 * for {@link RegionImpl}.
 * 
 * @author Anatole Tresch
 */
public class SERegionsSingletonSpi implements RegionsSingletonSpi {

	private static final Logger LOG = LoggerFactory
			.getLogger(SERegionsSingletonSpi.class);

	/**
	 * Loaded region providers.
	 */
	private AbstractRegionProviderService regionProviderService = new SERegionProviderService();

	/**
	 * Loaded region providers.
	 */
	private AbstractRegionTreeProviderService regionTreeProviderService = new SERegionTreeProviderService();
	
	/**
	 * Loaded region providers.
	 */
	private AbstractExtendedRegionDataService extendedRegionDataService = new SEExtendedRegionDataService();


	public SERegionsSingletonSpi() {
		Map<Class, RegionProviderSpi> regionProviders = regionProviderService.getRegisteredRegionProviderSpis();
		regionTreeProviderService.init(regionProviders);
	}
	
	@Override
	public Region getRegion(RegionType type, int numericId) {
		return regionProviderService.getRegion(type, numericId);
	}

	@Override
	public Region getRegion(RegionType type, String code) {
		try{
			return regionProviderService.getRegion(type, code);
		}
		catch(Exception e){
			try{
				return regionProviderService.getRegion(type, Integer.parseInt(code));
			}
			catch(Exception e2){
				// give up, TODO log it!
				return null;
			}
		}
	}

	@Override
	public Set<RegionType> getRegionTypes() {
		return regionProviderService.getRegionTypes();
	}

	@Override
	public Region getRegion(Locale locale) {
		return regionProviderService.getRegion(locale);
	}

	@Override
	public Collection<Region> getRegions(RegionType type) {
		return regionProviderService.getRegions(type);
	}

	@Override
	public RegionTreeNode getRegionTree(String treeId) {
		return regionTreeProviderService.getRegionTree(treeId);
	}

	@Override
	public Set<String> getRegionTreeIds() {
		return regionTreeProviderService.getRegionTreeIds();
	}

	@Override
	public Collection<Class> getExtendedRegionDataTypes(Region region) {
		return extendedRegionDataService.getExtendedRegionDataTypes(region);
	}

	@Override
	public <T> T getExtendedRegionData(Region region, Class<T> type) {
		return extendedRegionDataService.getExtendedRegionData(region, type);
	}

}
