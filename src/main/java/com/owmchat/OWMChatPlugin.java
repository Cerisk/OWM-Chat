package com.owmchat;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.OverheadTextChanged;
import net.runelite.api.widgets.Widget;
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


import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.function.Consumer;


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





		MenuEntry newOption = new MenuEntry() {
			/**
			 * @return
			 */
			@Override
			public String getOption() {
				return null;
			}

			/**
			 * @param option
			 * @return
			 */
			@Override
			public MenuEntry setOption(String option) {
				return null;
			}

			/**
			 * @return
			 */
			@Override
			public String getTarget() {
				return null;
			}

			/**
			 * @param target
			 * @return
			 */
			@Override
			public MenuEntry setTarget(String target) {
				return null;
			}

			/**
			 * @return
			 */
			@Override
			public int getIdentifier() {
				return 0;
			}

			/**
			 * @param identifier
			 * @return
			 */
			@Override
			public MenuEntry setIdentifier(int identifier) {
				return null;
			}

			/**
			 * @return
			 */
			@Override
			public MenuAction getType() {
				return null;
			}

			/**
			 * @param type
			 * @return
			 */
			@Override
			public MenuEntry setType(MenuAction type) {
				return null;
			}

			/**
			 * @return
			 */
			@Override
			public int getParam0() {
				return 0;
			}

			/**
			 * @param param0
			 * @return
			 */
			@Override
			public MenuEntry setParam0(int param0) {
				return null;
			}

			/**
			 * @return
			 */
			@Override
			public int getParam1() {
				return 0;
			}

			/**
			 * @param param1
			 * @return
			 */
			@Override
			public MenuEntry setParam1(int param1) {
				return null;
			}

			/**
			 * @return
			 */
			@Override
			public boolean isForceLeftClick() {
				return false;
			}

			/**
			 * @param forceLeftClick
			 * @return
			 */
			@Override
			public MenuEntry setForceLeftClick(boolean forceLeftClick) {
				return null;
			}

			/**
			 * @return
			 */
			@Override
			public boolean isDeprioritized() {
				return false;
			}

			/**
			 * @param deprioritized
			 * @return
			 */
			@Override
			public MenuEntry setDeprioritized(boolean deprioritized) {
				return null;
			}

			/**
			 * @param callback
			 * @return
			 */
			@Override
			public MenuEntry onClick(Consumer<MenuEntry> callback) {
				return null;
			}

			/**
			 * @param parent
			 * @return
			 */
			@Override
			public MenuEntry setParent(MenuEntry parent) {
				return null;
			}

			/**
			 * @return
			 */
			@Nullable
			@Override
			public MenuEntry getParent() {
				return null;
			}

			/**
			 * @return
			 */
			@Override
			public boolean isItemOp() {
				return false;
			}

			/**
			 * @return
			 */
			@Override
			public int getItemOp() {
				return 0;
			}

			/**
			 * @return
			 */
			@Override
			public int getItemId() {
				return 0;
			}

			/**
			 * @return
			 */
			@Nullable
			@Override
			public Widget getWidget() {
				return null;
			}

			/**
			 * @return
			 */
			@Nullable
			@Override
			public NPC getNpc() {
				return null;
			}

			/**
			 * @return
			 */
			@Nullable
			@Override
			public Player getPlayer() {
				return null;
			}

			/**
			 * @return
			 */
			@Nullable
			@Override
			public Actor getActor() {
				return null;
			}
		};
		newOption.setOption("My Option"); // set the text of the menu option
		newOption.setTarget(""); // set the target to empty string if it doesn't apply
		newOption.setType(MenuAction.RUNELITE); // set the type of the action
		newOption.setIdentifier(1); // set an identifier for the action (can be any int value)

		MenuEntry[] menuEntries = client.getMenuEntries();

		if (menuEntries.length > 0) {
			menuEntries[0].setForceLeftClick(true);
			menuEntries[0].setDeprioritized(true);
		}

		MenuEntry[] newMenuEntries = client.getMenuEntries();
		if (newMenuEntries.length > 0) {
			newMenuEntries[newMenuEntries.length - 1] = newOption;
		}

		client.setMenuEntries(newMenuEntries);





		//MenuEntry[] menuEntries = new MenuEntry[0];
		if (client != null) {
			// Stores menu entries in a string array

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