package com.owmchat;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.events.OverheadTextChanged;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.NPCManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;


@Slf4j
@PluginDescriptor(
		name = "Old Wise Man Chat"
)
public class OWMChatPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private OWMChatConfig config;

	@Inject
	private ChatMessageManager chatMessageManager;

	@Inject
	private NPCManager npcManager;


	private Actor actor = null;
	private String lastNPCText = "";


	@Override
	protected void startUp() throws Exception
	{
		log.info("[OWM-Chat] Plugin Initialized Successfully");
		sendChatMessage("[OWM-Chat] Plugin Initialized Successfully");


	}

	private ChatMessageType getMenuOption()
	{
		ChatMessage menuEntry = null;
		return menuEntry.getType();
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Looks like that's enough, back to bed bud.");
		sendChatMessage("[OWM-Chat] Looks like that's enough, back to bed bud.");
	}

	@Subscribe
	public void onOverheadTextChanged(OverheadTextChanged event)
	{

		// log.info("animation changed for: " + otChanged.getActor().getName());
		final String bot = event.getActor().getName();
		final String message = event.getOverheadText();

		sendChatMessage(bot + ": " + message);


	}


	private void sendChatMessage(String chatMessage)
	{
		final String message = new ChatMessageBuilder()
				.append(ChatColorType.HIGHLIGHT)
				.append(chatMessage)
				.build();

		chatMessageManager.queue(
				QueuedMessage.builder()
						.type(ChatMessageType.CONSOLE)
						.runeLiteFormattedMessage(message)
						.build());
	}

	@Provides
	OWMChatConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(OWMChatConfig.class);
	}
}