package com.owmchat;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("example")
public interface OWMChatConfig extends Config
{
	@ConfigItem(
			keyName = "OWM Chat",
			name = "Welcome to Old Wise Man chat",
			description = "Login and put some specs on to see the chat old man! Code used from several plugins (ie Emoji, NPC Chat Overhead Dialogue)"
	)
	default String greeting()
	{
		return "Replace here";
	}

	@ConfigItem(
			keyName = "dialogBoxText",
			name = "Display Dialog Box Text Overhead",
			description = "Displays dialog in the dialog box above the corresponding NPC"
	)
	default boolean showDialogBoxText()
	{
		return false;
	}
	@ConfigItem(
			keyName = "ambientDialog",
			name = "Display Ambient Dialog Overhead",
			description = "Displays ambient dialog above NPCs"
	)
	default boolean showAmbientDialog()
	{
		return false;
	}
	@ConfigItem(
			keyName = "damageDialog",
			name = "Display Damage Dialog Overhead",
			description = "Displays damage dialog above NPCs"
	)
	default boolean showDamageDialog()
	{
		return false;
	}
	@ConfigItem(
			keyName = "deathDialog",
			name = "Display Death Dialog Overhead",
			description = "Displays death dialog above NPCs"
	)
	default boolean showDeathDialog()
	{
		return false;
	}
	@ConfigItem(
			keyName = "walkingDialog",
			name = "Display Walking Dialog Overhead",
			description = "Displays walking dialog above NPCs"
	)
	default boolean showWalkingDialog()
	{
		return false;
	}
	@ConfigItem(
			keyName = "chatDialog",
			name = "Display Overhead Dialog in Chat",
			description = "Displays all enabled dialog in the chat"
	)
	default boolean enableChatDialog() { return false; }
}
