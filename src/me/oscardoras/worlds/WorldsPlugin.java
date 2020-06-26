package me.oscardoras.worlds;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

import me.oscardoras.spigotutils.BukkitPlugin;
import me.oscardoras.spigotutils.io.ConfigurationFile;
import me.oscardoras.worlds.commands.GoCommand;
import me.oscardoras.worlds.commands.WorldCommand;

public final class WorldsPlugin extends BukkitPlugin implements Listener {
	
	public static WorldsPlugin plugin;
	
	public WorldsPlugin() {
		plugin = this;
	}
	
	
	public String worldContainer = ".";
	
	@Override
	public void onLoad() {
		GoCommand.go();
		WorldCommand.list();
		WorldCommand.create();
		WorldCommand.delete();
		WorldCommand.load();
		WorldCommand.unload();
		WorldCommand.reload();
		WorldCommand.build();
	}
	
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		
		ConfigurationFile config = new ConfigurationFile("bukkit.yml");
		if (config.contains("settings.world-container")) {
			worldContainer = config.getString("settings.world-container");
			if (worldContainer.endsWith("/")) worldContainer = worldContainer.substring(0, worldContainer.length() - 1);
		}
		for (File folder : new File(worldContainer).listFiles()) if (isWorld(folder) && Bukkit.getWorld(folder.getName()) == null) load(folder);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCommand(ServerCommandEvent e) {
		if (!e.isCancelled()) {
			CommandSender sender = e.getSender();
			String command = e.getCommand();
			if (command.startsWith("/difficulty") && sender.hasPermission("minecraft.command.difficulty")) {
				try {
					Difficulty difficulty = Difficulty.valueOf(command.split(" ")[1].toUpperCase());
					for (World world : Bukkit.getWorlds()) world.setDifficulty(difficulty);
				} catch (IndexOutOfBoundsException | IllegalArgumentException ex) {}
			}
		}
	}
	
	public boolean isWorld(File folder) {
	    return folder.isDirectory() && new File(folder.getPath() + "/session.lock").exists() && new File(folder.getPath() + "/playerdata").exists();
	}
	
	public void load(File folder) {
		if (new File(folder.getPath() + "/region").isDirectory()) {
			new WorldCreator(folder.getName()).createWorld();
			File nether = new File(folder.getPath() + "/DIM-1");
			if (nether.isDirectory()) {
				File to = new File(folder.getPath() + "_nether/DIM-1");
				if (!to.exists()) {
					try {
						Files.move(nether.toPath(), to.toPath());
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					new WorldCreator(folder.getName() + "_nether").environment(Environment.NETHER).createWorld();
				}
			}
			File ender = new File(folder.getPath() + "/DIM1");
			if (ender.isDirectory()) {
				File to = new File(folder.getPath() + "_the_end/DIM1");
				if (!to.exists()) {
					try {
						Files.move(ender.toPath(), to.toPath());
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					new WorldCreator(folder.getName() + "_the_end").environment(Environment.THE_END).createWorld();
				}
			}
		} else {
			WorldCreator worldCreator = new WorldCreator(folder.getName());
			if (new File(folder.getPath() + "/DIM-1").isDirectory()) worldCreator.environment(Environment.NETHER);
			else if (new File(folder.getPath() + "/DIM1").isDirectory()) worldCreator.environment(Environment.THE_END);
			worldCreator.createWorld();
		}
	}
	
	public void build(File folder) {
		World world = Bukkit.getWorld(folder.getName());
		if (world != null) Bukkit.unloadWorld(world, true);
		if (new File(folder.getPath() + "/region").isDirectory()) {
			File nether = new File(folder.getPath() + "_nether/DIM-1");
			if (nether.isDirectory()) {
				world = Bukkit.getWorld(folder.getName() + "_nether");
				if (world != null) Bukkit.unloadWorld(world, true);
				new File(folder.getName() + "_nether/session.lock").delete();
				File to = new File(folder.getPath() + "/DIM-1");
				try {
					Files.move(nether.toPath(), to.toPath());
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			File ender = new File(folder.getPath() + "_the_end/DIM1");
			if (ender.isDirectory()) {
				world = Bukkit.getWorld(folder.getName() + "_the_end");
				if (world != null) Bukkit.unloadWorld(world, true);
				new File(folder.getName() + "_the_end/session.lock").delete();
				File to = new File(folder.getPath() + "/DIM1");
				try {
					Files.move(ender.toPath(), to.toPath());
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
}