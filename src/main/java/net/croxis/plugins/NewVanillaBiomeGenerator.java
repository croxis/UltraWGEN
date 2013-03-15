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

import org.spout.api.generator.biome.BiomeGenerator;
import org.spout.api.geo.World;
import org.spout.api.util.cuboid.CuboidBlockMaterialBuffer;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.VanillaGenerator;

public abstract class NewVanillaBiomeGenerator extends BiomeGenerator implements VanillaGenerator {

	private int lowestChunkY = 0;
	private int highestChunkY = 15;

	@Override
	public void generate(CuboidBlockMaterialBuffer blockData, int chunkX, int chunkY, int chunkZ, World world) {
		if (chunkY < lowestChunkY || chunkY > highestChunkY) {
			blockData.flood(VanillaMaterials.AIR);
		} else {
			super.generate(blockData, chunkX, chunkY, chunkZ, world);
		}
	}

	public int getLowestChunkY() {
		return lowestChunkY;
	}

	public void setLowestChunkY(int lowestChunkY) {
		this.lowestChunkY = lowestChunkY;
	}

	public int getHighestChunkY() {
		return highestChunkY;
	}

	public void setHighestChunkY(int highestChunkY) {
		this.highestChunkY = highestChunkY;
	}
}
