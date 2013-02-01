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

import org.spout.api.generator.GeneratorPopulator;
import org.spout.api.generator.Populator;
import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.util.cuboid.CuboidBlockMaterialBuffer;
import org.spout.vanilla.plugin.world.generator.VanillaGenerator;

public class SkyLands implements VanillaGenerator{

	public void generate(CuboidBlockMaterialBuffer blockData, int chunkX,
			int chunkY, int chunkZ, World world) {
		// TODO Auto-generated method stub
		
	}

	public int[][] getSurfaceHeight(World world, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub
		return null;
	}

	public Populator[] getPopulators() {
		// TODO Auto-generated method stub
		return null;
	}

	public GeneratorPopulator[] getGeneratorPopulators() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	public Point getSafeSpawn(World arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
