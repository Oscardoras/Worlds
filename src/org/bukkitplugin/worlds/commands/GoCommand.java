package org.bukkitplugin.worlds.commands;

import java.util.Collection;
import java.util.LinkedHashMap;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.permissions.Permission;
import org.bukkitplugin.worlds.Message;
import org.bukkitutils.command.v1_15_V1.Argument;
import org.bukkitutils.command.v1_15_V1.CommandRegister;
import org.bukkitutils.command.v1_15_V1.CommandRegister.CommandExecutorType;
import org.bukkitutils.command.v1_15_V1.arguments.EntitySelectorArgument;
import org.bukkitutils.command.v1_15_V1.arguments.WorldArgument;
import org.bukkitutils.command.v1_15_V1.arguments.EntitySelectorArgument.EntitySelector;

public final class GoCommand {
	private GoCommand() {}
	
	
	@SuppressWarnings("unchecked")
	public static void go() {
		LinkedHashMap<String, Argument<?>> arguments = new LinkedHashMap<>();
		arguments.put("targets", new EntitySelectorArgument(EntitySelector.MANY_ENTITIES));
		arguments.put("world", new WorldArgument(new Message("world.does_not_exist")));
		CommandRegister.register("go", arguments, new Permission("worlds.command.go"), CommandExecutorType.ALL, (cmd) -> {
			World world = (World) cmd.getArg(1);
			Collection<Entity> entities = (Collection<Entity>) cmd.getArg(0);
			for (Entity entity : entities) {
				entity.teleport(world.getSpawnLocation());
				cmd.broadcastMessage(new Message("command.go", world.getName()));
			}
			return entities.size();
		});
	}
	
}