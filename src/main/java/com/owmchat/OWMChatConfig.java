package com.owmchat;

import net.runelite.api.NpcID;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("userChatFilterConfig")
public interface OWMChatConfig extends Config
{

    @ConfigItem(
			keyName = "OWM Chat",
			name = "Welcome to Old Wise Man chat",
			description = "Login and put some specs on to see the chat old man! Code used from several plugins (ie Emoji, NPC Chat Overhead Dialogue)"
	)

	default boolean enableChatDialog() { return false; }

    @ConfigItem(
            position = 7,
            keyName = "getNpcWhitelist",
            name = "Add NPCs to Whitelist",
            description = "List of NPC names to Whitelist. Format: (NPC), (NPC)"
    )
    default String getNpcWhitelist(String id)
    {
        return "";
    }

    @ConfigItem(
            keyName = "npcToWhitelist",
            name = "",
            description = ""
    )
    void setNpcToWhitelist(String npcToWhitelist);

}
