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

import java.util.Set;

import org.spout.api.UnsafeMethod;
import org.spout.api.chat.ChatArguments;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandRegistrationsFactory;
import org.spout.api.command.CommandSource;
import org.spout.api.command.RootCommand;
import org.spout.api.command.annotated.AnnotatedCommandRegistrationFactory;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.command.annotated.SimpleAnnotatedCommandExecutorFactory;
import org.spout.api.command.annotated.SimpleInjector;
import org.spout.api.component.impl.ObserverComponent;
import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
import org.spout.api.exception.ConfigurationException;
import org.spout.api.generator.GeneratorPopulator;
import org.spout.api.generator.biome.Biome;
import org.spout.api.geo.LoadOption;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector3;
import org.spout.api.plugin.CommonPlugin;
import org.spout.api.plugin.PluginLogger;
import org.spout.api.plugin.ServiceManager;
import org.spout.api.plugin.services.ProtectionService;
import org.spout.api.util.FlatIterator;
import org.spout.vanilla.plugin.VanillaPlugin;

import org.spout.vanilla.api.data.Difficulty;
import org.spout.vanilla.api.data.Dimension;
import org.spout.vanilla.api.data.GameMode;
import org.spout.vanilla.plugin.data.VanillaData;
import org.spout.vanilla.plugin.component.world.sky.NormalSky;
import org.spout.vanilla.plugin.configuration.VanillaConfiguration;
import org.spout.vanilla.plugin.configuration.WorldConfigurationNode;
import org.spout.vanilla.plugin.lighting.VanillaLighting;
import org.spout.vanilla.plugin.service.VanillaProtectionService;
import org.spout.vanilla.plugin.service.protection.SpawnProtection;
import org.spout.vanilla.plugin.thread.SpawnLoader;
import org.spout.vanilla.plugin.world.generator.VanillaGenerator;
import org.spout.vanilla.plugin.world.generator.VanillaGenerators;
import org.spout.vanilla.plugin.world.generator.nether.NetherGenerator;

public class UltraWGEN extends CommonPlugin{
	private static final int LOADER_THREAD_COUNT = 16;
	

	@Override
	@UnsafeMethod
	public void onEnable() {
		final CommandRegistrationsFactory<Class<?>> commandRegFactory = new AnnotatedCommandRegistrationFactory(new SimpleInjector(this), new SimpleAnnotatedCommandExecutorFactory());
		final RootCommand root = getEngine().getRootCommand();
		root.addSubCommands(this, DebugCommands.class, commandRegFactory);
		
		World world = this.getEngine().loadWorld("world_test", new UltraGenerator());
		
		WorldConfigurationNode worldNode = VanillaConfiguration.WORLDS.get("world_test");
		try {
			worldNode.load();
		} catch (ConfigurationException e1) {
			e1.printStackTrace();
		}

		world.getDataMap().put(VanillaData.GAMEMODE, GameMode.get(worldNode.GAMEMODE.getString()));
		world.getDataMap().put(VanillaData.DIFFICULTY, Difficulty.get(worldNode.DIFFICULTY.getString()));
		world.getDataMap().put(VanillaData.DIMENSION, Dimension.get(worldNode.SKY_TYPE.getString()));

		world.addLightingManager(VanillaLighting.BLOCK_LIGHT);
		world.addLightingManager(VanillaLighting.SKY_LIGHT);
		
		//this.getEngine().setDefaultWorld(world);
		
		final int radius = VanillaConfiguration.SPAWN_RADIUS.getInt();
		final int protectionRadius = VanillaConfiguration.SPAWN_PROTECTION_RADIUS.getInt();
		SpawnLoader loader = new SpawnLoader(LOADER_THREAD_COUNT);
		this.getEngine().getServiceManager().register(ProtectionService.class, new VanillaProtectionService(), this, ServiceManager.ServicePriority.Highest);
		
		boolean newWorld = world.getAge() <= 0;
		if (worldNode.LOADED_SPAWN.getBoolean() || newWorld) {
			// Initialize the first chunks
			Point point = world.getSpawnPoint().getPosition();
			int cx = point.getBlockX() >> Chunk.BLOCKS.BITS;
			int cz = point.getBlockZ() >> Chunk.BLOCKS.BITS;
	
			((VanillaProtectionService) getEngine().getServiceManager().getRegistration(ProtectionService.class).getProvider()).addProtection(new SpawnProtection(world.getName() + " Spawn Protection", world, point, protectionRadius));

			// Load or generate spawn area
			int effectiveRadius = newWorld ? (2 * radius) : radius;
			loader.load(world, cx, cz, effectiveRadius, newWorld);
	
			//if (worldConfig.LOADED_SPAWN.getBoolean()) { // TODO - this doesn't really work for anything since it doesn't hold all chunks
			Entity e = world.createAndSpawnEntity(point, ObserverComponent.class, LoadOption.LOAD_GEN);
			e.setObserver(new FlatIterator(cx, 0, cz, 16, effectiveRadius));
			//}
	
			// Grab safe spawn if newly created world.
			if (newWorld && world.getGenerator() instanceof VanillaGenerator) {
				Point spawn = ((VanillaGenerator) world.getGenerator()).getSafeSpawn(world);
				world.setSpawnPoint(new Transform(spawn, Quaternion.IDENTITY, Vector3.ONE));
			}
	
			// Grab safe spawn if newly created world.
			if (newWorld && world.getGenerator() instanceof VanillaGenerator) {
				Point spawn = ((VanillaGenerator) world.getGenerator()).getSafeSpawn(world);
				world.setSpawnPoint(new Transform(spawn, Quaternion.IDENTITY, Vector3.ONE));
			}
		}
		world.getComponentHolder().add(NormalSky.class);
		
	}
	
	public void onLoad(){
		((PluginLogger) getLogger()).setTag(new ChatArguments(ChatStyle.RESET, "[", ChatStyle.GOLD, "UltraWGEN", ChatStyle.RESET, "] "));
		//Remove bedrock from vanilla here
		//NetherGenerator.HEIGHT = 256;
		//GeneratorPopulator[] populators = VanillaGenerators.NORMAL.getGeneratorPopulators();
	}

	@Override
	@UnsafeMethod
	public void onDisable() {
		// TODO Auto-generated method stub
		
	}
	

	
	public ChatArguments getPrefix() {
		return ((PluginLogger) getLogger()).getTag();
	}

}
