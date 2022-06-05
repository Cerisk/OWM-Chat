package com.owmchat;


import com.google.inject.Provides;
import joptsimple.internal.Strings;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.events.OverheadTextChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.NPCManager;
//import net.runelite.client.plugins.OWMChat.dialog.DialogNpc;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.devtools.DevToolsButton;
import net.runelite.client.util.Text;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


@Slf4j
@PluginDescriptor(
		name = "Old Wise Man Chat"
)
public class OWMChatPlugin extends Plugin
{
	@Inject
	private Client client;
	private DevToolsButton lineOfSight;

	@Inject
	private OWMChatConfig config;

	@Inject
	private ChatMessageManager chatMessageManager;


	@Inject
	private ClientThread clientThread;

	@Inject
	private NPCManager npcManager;


	private Actor actor = null;
	private String lastNPCText = "";
	private int actorTextTick = 0;
	private String lastPlayerText = "";
	private int playerTextTick = 0;

	//private final Actor actor;

	//private final String overheadText;

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

/*
	@Subscribe
	public void onGameStateChanged(GameStateChanged c)
	{
		if (!c.getGameState().equals(GameState.LOADING))
		{
			return;
		}

		actor = null;
		lastNPCText = "";
		lastPlayerText = "";
	}
*/


	/*@Subscribe
	public void onAnimationChanged(AnimationChanged animationChanged)
	{
		final NPC npc = (NPC) animationChanged.getActor();
		log.info("animation changed for: " + animationChanged.getActor().getName());
		/*sendChatMessage("[OWM-Chat]: " + npc);
		if (!(animationChanged.getActor() instanceof NPC))
		{
			sendChatMessage("[OWM-Chat]: " + npc);

		}
		if (animationChanged.getActor().getName() != null)
		{

			sendChatMessage("[OWM-Chat]: " + animationChanged.getActor().getName() + " says " + animationChanged.getActor().getOverheadText());

		}

	}
*/
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
