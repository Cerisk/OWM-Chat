package com.owmchat;

import com.google.inject.Provides;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.widgets.Widget;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.NPCManager;
import net.runelite.client.game.npcoverlay.HighlightedNpc;
import net.runelite.client.game.npcoverlay.NpcOverlayService;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.plugins.groundmarkers.GroundMarkerOverlay;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.Text;
import net.runelite.client.util.WildcardMatcher;


import javax.annotation.Nullable;
import javax.inject.Inject;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;


@Slf4j
@PluginDescriptor(
		name = "Old Wise Man Chat"
)



public class OWMChatPlugin extends Plugin {

	@Inject
	private Client client;

	@Inject
	private OWMChatConfig config;

	@Inject
	private ConfigManager configManager;

	@Inject
	private ChatMessageManager chatMessageManager;

	@Inject
	private NPCManager npcManager;


	private Actor actor = null;
	private String lastNPCText = "";


	private List<Integer> npcWhitelist = new ArrayList<>();


	@Override
	protected void startUp() throws Exception {
		log.info("[OWM-Chat] Plugin Initialized Successfully");
		sendChatMessage("[OWM-Chat] Plugin Initialized Successfully");


		sendChatMessage("[OWM-Chat] New Menu Entry: ");
		sendRightClickOptions();
		getWhitelist();
		rebuild();

	}


	private void addToWhitelist(int npcId) {
		if (!npcWhitelist.contains(npcId)) {
			npcWhitelist.add(npcId);
		}
	}


	private ChatMessageType getMenuOption() {
		ChatMessage menuEntry = null;
		return menuEntry.getType();
	}


	public void sendRightClickOptions() {
		// Retrieve the current right-click options
		MenuEntry[] menuEntries = client.getMenuEntries();

		// Format the right-click options into a string
		StringBuilder sb = new StringBuilder();
		sb.append("Your right-click options: ");
		for (int i = 0; i < menuEntries.length; i++) {
			sb.append(menuEntries[i].getOption());
			if (i < menuEntries.length - 1) {
				sb.append(", ");
			}
		}

		// Send the right-click options to the chat
		sendChatMessage(sb.toString());
	}


	@Override
	protected void shutDown() throws Exception {
		log.info("Looks like that's enough, back to bed bud.");
		sendChatMessage("[OWM-Chat] Looks like that's enough, back to bed bud.");


	}

	@Subscribe
	public void onOverheadTextChanged(OverheadTextChanged event) {

		// log.info("animation changed for: " + otChanged.getActor().getName());
		final String bot = event.getActor().getName();
		final String message = event.getOverheadText();

		if (whitelistMatchesNPCName(bot)) {
			// Do something with the NPC
			sendChatMessage("[OWM-Notification] " + bot + ": " + message);
		}


	}


	private void sendChatMessage(String chatMessage) {
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
	OWMChatConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(OWMChatConfig.class);
	}


	private List<String> whitelist = new ArrayList<>();

	List<String> getWhitelist() {
		final String configNpcs = config.getNpcWhitelist();

		if (configNpcs.isEmpty()) {
			return Collections.emptyList();
		}

		return Text.fromCSV(configNpcs);
	}

	private boolean whitelistMatchesNPCName(String npcName) {
		whitelist = getWhitelist();
		for (String highlight : whitelist) {
			if (WildcardMatcher.matches(highlight, npcName)) {
				return true;
			}
		}

		return false;
	}

	private final Set<Integer> npcTags = new HashSet<>();
	private static final String TAG = "Whitelist";
	private static final String UNTAG = "Un-Whitelist";
	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
		final MenuEntry menuEntry = event.getMenuEntry();
		final MenuAction menuAction = menuEntry.getType();
		final NPC npc = menuEntry.getNpc();

		if (npc == null)
		{
			return;
		}
		final String option = menuEntry.getOption();
		final MenuEntry[] menuEntries = client.getMenuEntries();
		final boolean rightClickOpened = option.equalsIgnoreCase("Whitelist") && menuEntries.length > 0;

		if (menuAction == MenuAction.EXAMINE_NPC && client.isKeyPressed(KeyCode.KC_SHIFT)) {
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "You are selecting \"Whitelist\" from your right-click menu!", null);
			// Add tag and tag-all options
			if (npc.getName() == null) {
				return;
			}


			final String npcName = npc.getName();
			final boolean nameMatch = whitelist.stream().anyMatch(npcName::equalsIgnoreCase);
			final boolean idMatch = npcTags.contains(npc.getIndex());
			final boolean wildcardMatch = whitelist.stream()
					.filter(highlight -> !highlight.equalsIgnoreCase(npcName))
					.anyMatch(highlight -> WildcardMatcher.matches(highlight, npcName));
			int idx = -1;

			client.createMenuEntry(idx--)
					.setOption(idMatch ? UNTAG : TAG)
					.setTarget(event.getTarget())
					.setIdentifier(event.getIdentifier())
					.setType(MenuAction.RUNELITE)
					.onClick(this::tag);

		}

	}


	private void tag(MenuEntry menuEntry) {
		final int id = menuEntry.getIdentifier();
		final NPC[] cachedNPCs = client.getCachedNPCs();
		final NPC npc = cachedNPCs[id];

		if (npc == null || npc.getName() == null) {
			return;
		}

		if (menuEntry.getOption().equals(TAG) || menuEntry.getOption().equals(UNTAG)) {
			final boolean removed = npcTags.remove(id);

			if (removed) {
				if (!whitelistMatchesNPCName(npc.getName())) {
					npcWhitelist.remove(npc);

				}
			} else {
				final String name = npc.getName();
				final List<String> whitelistedNpcs = new ArrayList<>(whitelist);

				if (!whitelistedNpcs.removeIf(name::equalsIgnoreCase)) {
					whitelistedNpcs.add(name);
				}

				// this trips a config change which triggers the overlay rebuild
				config.setNpcToWhitelist(Text.toCSV(whitelistedNpcs));
			}
		}


	}


	@Getter(AccessLevel.PACKAGE)
	private final Map<NPC, HighlightedNpc> whitelistedNpc = new HashMap<>();


	private final Function<NPC, HighlightedNpc> isHighlighted = whitelistedNpc::get;

	private NpcOverlayService npcOverlayService;




	void rebuild()
	{
		whitelist = getWhitelist();
		whitelistedNpc.clear();

		if (client.getGameState() != GameState.LOGGED_IN &&
				client.getGameState() != GameState.LOADING)
		{
			// NPCs are still in the client after logging out,
			// but we don't want to highlight those.
			return;
		}

		for (NPC npc : client.getNpcs())
		{
			final String npcName = npc.getName();

			if (npcName == null)
			{
				continue;
			}

		}

		npcOverlayService.rebuild();
	}




}