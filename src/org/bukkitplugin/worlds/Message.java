package org.bukkitplugin.worlds;

import org.bukkitutils.io.TranslatableMessage;

public class Message extends TranslatableMessage {
	
	public Message(String path, String... args) {
		super(WorldsPlugin.plugin, path, args);
	}
	
}