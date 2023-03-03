package com.owmchat;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.MenuOptionClicked;
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
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuOptionClicked;


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
	protected void startUp() throws Exception {
		log.info("[OWM-Chat] Plugin Initialized Successfully");
		sendChatMessage("[OWM-Chat] Plugin Initialized Successfully");


		//MenuEntry[] menuEntries = new MenuEntry[0];
		if (client != null) {
			// Stores menu entries in a string array
			MenuEntry[] menuEntries = client.getMenuEntries();

			sendChatMessage("[OWM-Chat] New Menu Entry: " + menuEntries.toString());
			sendRightClickOptions();

		}


	}

	private ChatMessageType getMenuOption()
	{
		ChatMessage menuEntry = null;
		return menuEntry.getType();
	}


		public void sendRightClickOptions()
		{
			// Retrieve the current right-click options
			MenuEntry[] menuEntries = client.getMenuEntries();

			// Format the right-click options into a string
			StringBuilder sb = new StringBuilder();
			sb.append("Your right-click options: ");
			for (int i = 0; i < menuEntries.length; i++)
			{
				sb.append(menuEntries[i].getOption());
				if (i < menuEntries.length - 1)
				{
					sb.append(", ");
				}
			}

			// Send the right-click options to the chat
			sendChatMessage(sb.toString());
		}



		// Use this method to recognize when a specific button is pressed from the right-click menu
	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		// Check if the player opened their right-click menu
		final String option = event.getMenuOption();
		final MenuEntry[] menuEntries = client.getMenuEntries();
		// Change boolean below to match name of new menu option for adding id to whitelist
		final boolean rightClickOpened = option.equalsIgnoreCase("Cancel") && menuEntries.length > 0;

		if (rightClickOpened)
		{
			// Do something if the player opened their right-click menu
			// For example:
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "You selected \"Cancel\" from your right-click menu!", null);
		}
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