package org.bukkitplugin.worlds.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkitplugin.worlds.Message;
import org.bukkitplugin.worlds.WorldsPlugin;
import org.bukkitutils.command.v1_15_V1.CustomArgument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class WorldFolderArgument extends CustomArgument<File> {
	
	public WorldFolderArgument() {
		withSuggestionsProvider((cmd) -> {
			List<String> list = new ArrayList<String>();
			for (File folder : new File(WorldsPlugin.plugin.worldContainer).listFiles()) if (WorldsPlugin.plugin.isWorld(folder)) list.add(folder.getName());
			return list;
		});
	}
	
	@Override
	public File parse(String arg, SuggestedCommand cmd) throws CommandSyntaxException {
		File folder = new File(WorldsPlugin.plugin.worldContainer + "/" + arg);
		if (WorldsPlugin.plugin.isWorld(folder)) return folder;
		else throw getCustomException(new Message("world.does_not_exist").getMessage(cmd.getLanguage(), arg));
	}
	
}