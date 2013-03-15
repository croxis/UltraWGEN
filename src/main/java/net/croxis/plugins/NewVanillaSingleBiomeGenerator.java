/*
 * This file is part of UltraWGEN.
 *
 * Copyright (c) ${project.inceptionYear}-2012, croxis <https://github.com/croxis/>
 *
 * UltraWGEN is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UltraWGEN is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with UltraWGEN. If not, see <http://www.gnu.org/licenses/>.
 */
package net.croxis.plugins;

import java.util.Arrays;

import org.spout.api.generator.biome.BiomeManager;
import org.spout.api.generator.biome.Simple2DBiomeManager;
import org.spout.api.generator.biome.selector.PerBlockBiomeSelector;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.vanilla.world.generator.biome.VanillaBiome;


public abstract class NewVanillaSingleBiomeGenerator extends NewVanillaBiomeGenerator{
	private final VanillaBiome biome;

	public NewVanillaSingleBiomeGenerator(VanillaBiome biome) {
		this.biome = biome;
	}

	@Override
	public void registerBiomes() {
		setSelector(new PerBlockBiomeSelector(biome));
	}

	@Override
	public BiomeManager generateBiomes(int chunkX, int chunkZ, World world) {
		final BiomeManager biomeManager = new Simple2DBiomeManager(chunkX, chunkZ);
		final byte[] biomeData = new byte[Chunk.BLOCKS.AREA];
		Arrays.fill(biomeData, (byte) biome.getId());
		biomeManager.deserialize(biomeData);
		return biomeManager;
	}
}
