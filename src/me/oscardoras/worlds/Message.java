package me.oscardoras.worlds;

import me.oscardoras.spigotutils.io.TranslatableMessage;

public class Message extends TranslatableMessage {
	
	public Message(String path, String... args) {
		super(WorldsPlugin.plugin, path, args);
	}
	
}