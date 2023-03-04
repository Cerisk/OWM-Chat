package com.owmchat;

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

	default void enableChatDialog() {

    }

    @ConfigItem(
            position = 7,
            keyName = "getNpcWhitelist",
            name = "Add NPCs to Whitelist",
            description = "List of NPC names to Whitelist. Format: (NPC), (NPC)"
    )
    default String getNpcWhitelist()
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
