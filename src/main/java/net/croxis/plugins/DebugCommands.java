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

import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;

public class DebugCommands {
	private UltraWGEN plugin;


	public DebugCommands(UltraWGEN plugin){
		this.plugin = plugin;
	}
	
	
	@Command(aliases = "pos", usage = "[player]", desc = "prints pos", min = 0, max = 1)
	@CommandPermissions("ultrawgen.debug")
	public void pos(CommandContext args, CommandSource source) throws CommandException{
		Player player = (Player) source;
		player.sendMessage(plugin.getPrefix(), ChatStyle.BLUE, "Pos: " + player.getScene().getPosition().toString());
		player.sendMessage(plugin.getPrefix(), ChatStyle.BLUE, "Chunk: " + Integer.toString(player.getChunk().getY()));
	}
}
