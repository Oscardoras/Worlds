package me.oscardoras.worlds.commands;

import java.util.Collection;
import java.util.LinkedHashMap;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.permissions.Permission;

import me.oscardoras.spigotutils.command.v1_16_1_V1.Argument;
import me.oscardoras.spigotutils.command.v1_16_1_V1.CommandRegister;
import me.oscardoras.spigotutils.command.v1_16_1_V1.CommandRegister.CommandExecutorType;
import me.oscardoras.spigotutils.command.v1_16_1_V1.arguments.EntitySelectorArgument;
import me.oscardoras.spigotutils.command.v1_16_1_V1.arguments.WorldArgument;
import me.oscardoras.spigotutils.command.v1_16_1_V1.arguments.EntitySelectorArgument.EntitySelector;
import me.oscardoras.worlds.Message;

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