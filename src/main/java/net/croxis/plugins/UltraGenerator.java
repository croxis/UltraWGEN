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

import java.util.Random;

import org.spout.api.Spout;
import org.spout.api.generator.GeneratorPopulator;
import org.spout.api.generator.Populator;
import org.spout.api.generator.WorldGenerator;
import org.spout.api.generator.biome.BiomeGenerator;
import org.spout.api.generator.biome.BiomeManager;
import org.spout.api.generator.biome.BiomePopulator;
import org.spout.api.generator.biome.BiomeSelector;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.discrete.Point;
import org.spout.api.util.LogicUtil;
import org.spout.api.util.cuboid.CuboidBlockMaterialBuffer;
import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.material.block.Liquid;
import org.spout.vanilla.plugin.world.generator.VanillaBiomeGenerator;
import org.spout.vanilla.plugin.world.generator.VanillaGenerator;
import org.spout.vanilla.plugin.world.generator.VanillaGenerators;
import org.spout.vanilla.plugin.world.generator.biome.VanillaBiomes;
import org.spout.vanilla.plugin.world.generator.normal.NormalGenerator;
import org.spout.vanilla.plugin.world.generator.normal.populator.CavePopulator;
import org.spout.vanilla.plugin.world.generator.normal.populator.DungeonPopulator;
import org.spout.vanilla.plugin.world.generator.normal.populator.FallingLiquidPopulator;
import org.spout.vanilla.plugin.world.generator.normal.populator.GroundCoverPopulator;
import org.spout.vanilla.plugin.world.generator.normal.populator.MineshaftPopulator;
import org.spout.vanilla.plugin.world.generator.normal.populator.OrePopulator;
import org.spout.vanilla.plugin.world.generator.normal.populator.PondPopulator;
import org.spout.vanilla.plugin.world.generator.normal.populator.RavinePopulator;
import org.spout.vanilla.plugin.world.generator.normal.populator.RockyShieldPopulator;
import org.spout.vanilla.plugin.world.generator.normal.populator.SnowPopulator;
import org.spout.vanilla.plugin.world.generator.normal.populator.StrongholdPopulator;
import org.spout.vanilla.plugin.world.generator.normal.populator.TemplePopulator;

public class UltraGenerator implements VanillaGenerator{
	

	public void generate(CuboidBlockMaterialBuffer blockData, int chunkX,
			int chunkY, int chunkZ, World world) {
		if (chunkY < -8)
			VanillaGenerators.NORMAL.generate(blockData, chunkX, chunkY, chunkZ, world);
		else if (chunkY == -8 && chunkY < 0)
			VanillaGenerators.NETHER.generate(blockData, chunkX, chunkY+8, chunkZ, world);
		//else
		//	VanillaGenerators.NORMAL.generate(blockData, chunkX, chunkY, chunkZ, world);
		
		
		//VanillaGenerators.NETHER.generate(blockData, chunkX, chunkY, chunkZ, world);
		//VanillaGenerators.NORMAL.generate(blockData, chunkX, chunkY, chunkZ, world);
		//((Vanilla) Spout.getPluginManager().getPlugin("Vanilla"));
		
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
		// TODO Auto-generated method stub
		
		return null;
	}

	public GeneratorPopulator[] getGeneratorPopulators() {
		// TODO Auto-generated method stub
		return null;
	}

	public Point getSafeSpawn(World world) {
		short shift = 0;
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
		return new Point(world, shift, 80, 0);
	}

	private BiomeSelector getSelector() {
		return VanillaGenerators.NORMAL.getSelector();
	}
	

}
