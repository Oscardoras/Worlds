package org.bukkitplugin.worlds.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkitplugin.worlds.Message;
import org.bukkitplugin.worlds.WorldsPlugin;
import org.bukkitutils.command.v1_14_3_V1.Argument;
import org.bukkitutils.command.v1_14_3_V1.CommandRegister;
import org.bukkitutils.command.v1_14_3_V1.CommandRegister.CommandExecutorType;
import org.bukkitutils.command.v1_14_3_V1.LiteralArgument;
import org.bukkitutils.command.v1_14_3_V1.arguments.StringArgument;
import org.bukkitutils.command.v1_14_3_V1.arguments.WorldArgument;

public final class WorldCommand {
	private WorldCommand() {}
	
	
	public static void list() {
		LinkedHashMap<String, Argument<?>> arguments = new LinkedHashMap<>();
		arguments.put("list", new LiteralArgument("list"));
		CommandRegister.register("world", arguments, new Permission("worlds.command.world"), CommandExecutorType.ALL, (cmd) -> {
			List<String> list = new ArrayList<String>();
			for (World world : Bukkit.getWorlds()) list.add(world.getName());
			cmd.sendListMessage(list, new Object[] {new Message("command.world.list.list")}, new Object[] {new Message("command.world.list.empty")});
			return list.size();
		});
		
		arguments.put("world", new WorldArgument(new Message("world.does_not_exist")));
		CommandRegister.register("world", arguments, new Permission("worlds.command.world"), CommandExecutorType.ALL, (cmd) -> {
			World world = (World) cmd.getArg(0);
			List<String> list = new ArrayList<String>();
			for (Player player : world.getPlayers()) list.add(player.getName());
			cmd.sendListMessage(list, new Object[] {new Message("command.world.players.list", world.getName())}, new Object[] {new Message("command.world.players.empty", world.getName())});
			return list.size();
		});
	}
	
	public static void create() {
		List<String> worldTypes = new ArrayList<String>();
		for (WorldType worldType : WorldType.values()) worldTypes.add(worldType.name().toLowerCase());
		worldTypes.add(Environment.NETHER.name().toLowerCase());
		worldTypes.add(Environment.THE_END.name().toLowerCase());
		
		Map<String, Boolean> structures = new HashMap<String, Boolean>();
		structures.put("structures", true);
		structures.put("nostructures", false);
		
		LinkedHashMap<String, Argument<?>> arguments = new LinkedHashMap<>();
		arguments.put("create", new LiteralArgument("create"));
		arguments.put("world", new StringArgument());
		CommandRegister.register("world", arguments, new Permission("worlds.command.world"), CommandExecutorType.ALL, (cmd) -> {
			File folder = new File(WorldsPlugin.plugin.worldContainer + "/" + (String) cmd.getArg(0));
			if (Bukkit.getWorld((String) cmd.getArg(0)) == null && !WorldsPlugin.plugin.isWorld(folder)) {
				WorldCreator worldCreator = new WorldCreator((String) cmd.getArg(0));
				worldCreator.createWorld();
				cmd.broadcastMessage(new Message("command.world.create", worldCreator.name()));
				return 1;
			} else {
				cmd.sendFailureMessage(new Message("world.already_exists", (String) cmd.getArg(0)));
				return 0;
			}
		});
		
		for (String worldType : worldTypes) {
			arguments = new LinkedHashMap<>();
			arguments.put("create", new LiteralArgument("create"));
			arguments.put("world", new StringArgument());
			arguments.put("type", new LiteralArgument(worldType));
			CommandRegister.register("world", arguments, new Permission("worlds.command.world"), CommandExecutorType.ALL, (cmd) -> {
				File folder = new File(WorldsPlugin.plugin.worldContainer + "/" + (String) cmd.getArg(0));
				if (Bukkit.getWorld((String) cmd.getArg(0)) == null && !WorldsPlugin.plugin.isWorld(folder)) {
					WorldCreator worldCreator = new WorldCreator((String) cmd.getArg(0));
					try {
						worldCreator.type(WorldType.valueOf(worldType.toUpperCase()));
					} catch (IllegalArgumentException e) {
						worldCreator.environment(Environment.valueOf(worldType.toUpperCase()));
					}
					worldCreator.createWorld();
					cmd.broadcastMessage(new Message("command.world.create", worldCreator.name()));
					return 1;
				} else {
					cmd.sendFailureMessage(new Message("world.already_exists", (String) cmd.getArg(0)));
					return 0;
				}
			});
			
			for (String strcuture : structures.keySet()) {
				arguments = new LinkedHashMap<>();
				arguments.put("create", new LiteralArgument("create"));
				arguments.put("world", new StringArgument());
				arguments.put("type", new LiteralArgument(worldType));
				arguments.put("structures", new LiteralArgument(strcuture));
				CommandRegister.register("world", arguments, new Permission("worlds.command.world"), CommandExecutorType.ALL, (cmd) -> {
					File folder = new File(WorldsPlugin.plugin.worldContainer + "/" + (String) cmd.getArg(0));
					if (Bukkit.getWorld((String) cmd.getArg(0)) == null && !WorldsPlugin.plugin.isWorld(folder)) {
						WorldCreator worldCreator = new WorldCreator((String) cmd.getArg(0));
						try {
							worldCreator.type(WorldType.valueOf(worldType.toUpperCase()));
						} catch (IllegalArgumentException e) {
							worldCreator.environment(Environment.valueOf(worldType.toUpperCase()));
						}
						worldCreator.generateStructures(structures.get(strcuture));
						worldCreator.createWorld();
						cmd.broadcastMessage(new Message("command.world.create", worldCreator.name()));
						return 1;
					} else {
						cmd.sendFailureMessage(new Message("world.already_exists", (String) cmd.getArg(0)));
						return 0;
					}
				});
				
				arguments.put("seed", new StringArgument());
				CommandRegister.register("world", arguments, new Permission("worlds.command.world"), CommandExecutorType.ALL, (cmd) -> {
					File folder = new File(WorldsPlugin.plugin.worldContainer + "/" + (String) cmd.getArg(0));
					if (Bukkit.getWorld((String) cmd.getArg(0)) == null && !WorldsPlugin.plugin.isWorld(folder)) {
						WorldCreator worldCreator = new WorldCreator((String) cmd.getArg(0));
						try {
							worldCreator.type(WorldType.valueOf(worldType.toUpperCase()));
						} catch (IllegalArgumentException e) {
							worldCreator.environment(Environment.valueOf(worldType.toUpperCase()));
						}
						worldCreator.generateStructures(structures.get(strcuture));
						try {
							long seed = Long.parseLong((String) cmd.getArg(1));
							if (seed != 0) worldCreator.seed(seed);
							worldCreator.createWorld();
							cmd.broadcastMessage(new Message("command.world.create", worldCreator.name()));
							return 1;
						} catch (IllegalArgumentException e) {
							cmd.sendFailureMessage(new Message("world.incorrect_seed", (String) cmd.getArg(1)));
							return 0;
						}
					} else {
						cmd.sendFailureMessage(new Message("world.already_exists", (String) cmd.getArg(0)));
						return 0;
					}
				});
			}
		}
	}
	
	public static void delete() {
		LinkedHashMap<String, Argument<?>> arguments = new LinkedHashMap<>();
		arguments.put("delete", new LiteralArgument("delete"));
		arguments.put("world", new WorldFolderArgument());
		CommandRegister.register("world", arguments, new Permission("worlds.command.world"), CommandExecutorType.ALL, (cmd) -> {
			File folder = (File) cmd.getArg(0);
			World world = Bukkit.getWorld(folder.getName());
			if (world != null) {
				for (Player player : world.getPlayers()) player.kickPlayer(new Message("world.unloaded").getMessage(player));
				Bukkit.unloadWorld(world, world.isAutoSave());
			}
			new File(folder.getPath() + "/session.lock").delete();
			cmd.broadcastMessage(new Message("command.world.delete", folder.getName()));
			return 1;
		});
	}
	
	public static void load() {
		LinkedHashMap<String, Argument<?>> arguments = new LinkedHashMap<>();
		arguments.put("load", new LiteralArgument("load"));
		arguments.put("world", new WorldFolderArgument().withSuggestionsProvider((cmd) -> {
			List<String> list = new ArrayList<String>();
			for (File folder : new File(WorldsPlugin.plugin.worldContainer).listFiles())
				if (WorldsPlugin.plugin.isWorld(folder) && Bukkit.getWorld(folder.getName()) == null) list.add(folder.getName());
			return list;
		}));
		CommandRegister.register("world", arguments, new Permission("worlds.command.world"), CommandExecutorType.ALL, (cmd) -> {
			File folder = (File) cmd.getArg(0);
			if (Bukkit.getWorld(folder.getName()) == null) {
				WorldsPlugin.plugin.load(folder);
				cmd.broadcastMessage(new Message("command.world.load", folder.getName()));
				return 1;
			} else {
				cmd.sendFailureMessage(new Message("world.already_loaded", folder.getName()));
				return 0;
			}
		});
	}
	
	public static void unload() {
		LinkedHashMap<String, Argument<?>> arguments = new LinkedHashMap<>();
		arguments.put("unload", new LiteralArgument("unload"));
		arguments.put("world", new WorldArgument(new Message("world.does_not_exist")));
		CommandRegister.register("world", arguments, new Permission("worlds.command.world"), CommandExecutorType.ALL, (cmd) -> {
			World world = (World) cmd.getArg(0);
			for (Player player : world.getPlayers()) player.kickPlayer(new Message("world.unloaded").getMessage(player));
			Bukkit.unloadWorld(world, world.isAutoSave());
			cmd.broadcastMessage(new Message("command.world.unload", world.getName()));
			return 1;
		});
	}
	
	public static void reload() {
		LinkedHashMap<String, Argument<?>> arguments = new LinkedHashMap<>();
		arguments.put("reload", new LiteralArgument("reload"));
		arguments.put("world", new WorldArgument(new Message("world.does_not_exist")));
		CommandRegister.register("world", arguments, new Permission("worlds.command.world"), CommandExecutorType.ALL, (cmd) -> {
			World world = (World) cmd.getArg(0);
			Environment environment = world.getEnvironment();
			for (Player player : world.getPlayers()) player.kickPlayer(new Message("world.unloaded").getMessage(player));
			Bukkit.unloadWorld(world, world.isAutoSave());
			WorldCreator worldCreator = new WorldCreator(world.getName());
			worldCreator.environment(environment);
			worldCreator.createWorld();
			cmd.broadcastMessage(new Message("command.world.reload", world.getName()));
			return 1;
		});
	}
	
	public static void build() {
		LinkedHashMap<String, Argument<?>> arguments = new LinkedHashMap<>();
		arguments.put("build", new LiteralArgument("build"));
		arguments.put("world", new WorldFolderArgument());
		CommandRegister.register("world", arguments, new Permission("worlds.command.world"), CommandExecutorType.ALL, (cmd) -> {
			File folder = (File) cmd.getArg(0);
			WorldsPlugin.plugin.build(folder);
			cmd.broadcastMessage(new Message("command.world.build", folder.getName()));
			return 1;
		});
	}
	
}