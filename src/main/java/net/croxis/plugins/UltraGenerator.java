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
import org.spout.api.generator.biome.BiomeSelector;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.discrete.Point;
import org.spout.api.util.cuboid.CuboidBlockMaterialBuffer;
import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.material.block.Liquid;
import org.spout.vanilla.plugin.world.generator.VanillaGenerator;
import org.spout.vanilla.plugin.world.generator.VanillaGenerators;
import org.spout.vanilla.plugin.world.generator.normal.NormalGenerator;

public class UltraGenerator implements VanillaGenerator{
	private NewNetherGenerator nether = new NewNetherGenerator();
	public NewNormalGenerator normal = new NewNormalGenerator();

	public void generate(CuboidBlockMaterialBuffer blockData, int chunkX,
			int chunkY, int chunkZ, World world) {
		System.out.println("Starting Chunk: " 
				+ Integer.toString(chunkX) + ", " 
				+ Integer.toString(chunkY) + ", "
				+ Integer.toString(chunkZ) + ". Size: "
				+ blockData.getSize().toString());
		if (chunkY == -16){
			nether.generate(blockData, chunkX, chunkY, chunkZ, world);
		} else if (chunkY >= 0 && chunkY <= 32) {
			System.out.println("Normalgen: " + normal.toString());
			System.out.println("Externalgen: " + VanillaGenerators.NORMAL.toString());
			//VanillaGenerators.NORMAL.generate(blockData, chunkX, 0, chunkZ, world);
			normal.generate(blockData, chunkX, chunkY, chunkZ, world);
		}
		
		System.out.println("Ending Chunk: " 
				+ Integer.toString(chunkX) + ", " 
				+ Integer.toString(chunkY) + ", "
				+ Integer.toString(chunkZ));
		
	}

	public int[][] getSurfaceHeight(World world, int chunkX, int chunkZ) {
		//From Vanilla
		int[][] heights = new int[Chunk.BLOCKS.SIZE][Chunk.BLOCKS.SIZE];
		for (int x = 0; x < Chunk.BLOCKS.SIZE; x++) {
			for (int z = 0; z < Chunk.BLOCKS.SIZE; z++) {
				heights[x][z] = NormalGenerator.SEA_LEVEL;
			}
		}
		return heights;
	}
	
	private int getHighestSolidBlock(World world, int x, int z) {
		int y = world.getHeight() - 1;
		while (world.getBlockMaterial(x, y, z) == VanillaMaterials.AIR) {
			y--;
			if (y == 0 || world.getBlockMaterial(x, y, z) instanceof Liquid) {
				return -1;
			}
		}
		return ++y;
	}

	public String getName() {
		return "UltraWGEN";
	}

	public Populator[] getPopulators() {
		//return VanillaGenerators.NORMAL.getPopulators();
		return new Populator[0];
	}

	public GeneratorPopulator[] getGeneratorPopulators() {
		//return VanillaGenerators.NORMAL.getGeneratorPopulators();
		return new GeneratorPopulator[0];
	}

	public Point getSafeSpawn(World world) {
		//return new Point(world, 0, 0 + 3, 0);
		return normal.getSafeSpawn(world);
		/*short shift = 0;
		final BiomeSelector selector = getSelector();
		while (LogicUtil.equalsAny(selector.pickBiome(shift, 0, world.getSeed()),
		VanillaBiomes.OCEAN, VanillaBiomes.BEACH, VanillaBiomes.RIVER,
		VanillaBiomes.SWAMP, VanillaBiomes.MUSHROOM_SHORE, VanillaBiomes.MUSHROOM)
		&& shift < 1600) {
		shift += 16;
		}
		final Random random = new Random();
		for (byte attempts = 0; attempts < 32; attempts++) {
		final int x = random.nextInt(256) - 127 + shift;
		final int z = random.nextInt(256) - 127;
		final int y = getHighestSolidBlock(world, x, z);
		if (y != -1) {
		return new Point(world, x, y + 0.5f, z);
		}
		}
		return new Point(world, shift, 80, 0);*/
	}

	private BiomeSelector getSelector() {
		return VanillaGenerators.NORMAL.getSelector();
	}
	

}
