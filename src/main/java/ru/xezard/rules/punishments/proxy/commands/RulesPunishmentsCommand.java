/*
 *  This file is part of XRulesPunishments,
 *  licensed under the Apache License, Version 2.0.
 *
 *  Copyright (c) Xezard (Zotov Ivan)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package ru.xezard.rules.punishments.proxy.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import ru.xezard.rules.punishments.configurations.MessagesConfiguration;
import ru.xezard.rules.punishments.data.IPunishmentsManager;
import ru.xezard.rules.punishments.data.rule.Rule;
import ru.xezard.rules.punishments.proxy.RulesPunishmentsPlugin;
import ru.xezard.rules.punishments.proxy.data.ProxyPunishmentManager;
import ru.xezard.rules.punishments.proxy.utils.Chat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class RulesPunishmentsCommand
extends Command
{
    private static final List<String> CONSOLE_HELP_MESSAGE = Arrays.asList
    (
            "--------------------- [XRulesPunishments] ---------------------",
            "'[]', '<>' - required and optional arguments",
            "",
            "> 'rp <help>' - show help page",
            "> 'rp reload' - reload plugin",
            "> 'rp punish [rule] [player name]' - punish the player by rule"
    );

    private static final String CONSOLE_PLUGIN_RELOADED_MESSAGE = "[XRulesPunishments] Plugin successfully reloaded!",
                                RULE_WITH_THAT_IDENTIFIER_DOES_NOT_EXISTS_MESSAGE =
                                        "[XRulesPunishments] Rule with identifier '{rule_identifier}' does not exists.",
                                PLAYER_NOT_ONLINE_AND_RULE_CANT_BE_EXECUTED_ON_OFFLINE_TARGET =
                                        "[XRulesPunishments] The player '{target_name}' is not online and the punishment" +
                                        " under the rule with identifier '{rule_identifier}' can't be applied to an offline player.";

    private MessagesConfiguration messagesConfiguration;

    private IPunishmentsManager punishmentsManager;

    private RulesPunishmentsPlugin plugin;

	public RulesPunishmentsCommand(MessagesConfiguration messagesConfiguration,
                                   ProxyPunishmentManager punishmentsManager,
                                   RulesPunishmentsPlugin plugin)
	{
		super("rulespunishments", "", "rp");

		this.messagesConfiguration = messagesConfiguration;

		this.punishmentsManager = punishmentsManager;

		this.plugin = plugin;
	}

	@Override
	public void execute(CommandSender sender, String[] arguments)
    {
        switch (arguments.length)
        {
            case 0:
                this.sendHelp(sender);
                break;

            case 1:
                switch (arguments[0].toLowerCase())
                {
                    case "help":
                        this.sendHelp(sender);
                        break;

                    case "reload":
                        this.reload(sender);
                        break;
                }
                break;

            case 3:
                if (!arguments[0].equalsIgnoreCase("punish"))
                {
                    break;
                }

                this.punish(sender, arguments[1], arguments[2]);
                break;
        }
    }

    private void punish(CommandSender sender, String ruleIdentifier, String targetName)
    {
        if (!sender.hasPermission("rules.punishments.commands.punish"))
        {
            sender.sendMessage(TextComponent.fromLegacyText(
                    this.messagesConfiguration.getYouDontHaveEnoughPermissionsMessage()));
            return;
        }

        Optional<Rule> optionalRule = this.punishmentsManager.getRuleByIdentifier(ruleIdentifier);

        if (!optionalRule.isPresent())
        {
            Chat.message(sender,
                    this.messagesConfiguration.getRuleWithThatIdentifierDoesNotExistsMessage()
                            .replace("{rule_identifier}", ruleIdentifier),
                    RULE_WITH_THAT_IDENTIFIER_DOES_NOT_EXISTS_MESSAGE
                            .replace("{rule_identifier}", ruleIdentifier));
            return;
        }

        Rule rule = optionalRule.get();

        if (!sender.hasPermission("rules.punishments.commands.punish.*") &&
                !sender.hasPermission("rules.punishments.commands.punish." + ruleIdentifier))
        {
            sender.sendMessage(TextComponent.fromLegacyText(
                    this.messagesConfiguration.getYouDontHaveEnoughPermissionsMessage()));
            return;
        }

        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetName);

        if (target == null && !rule.isCanBeExecutedOnOfflineTarget())
        {
            Chat.message(sender,
                    this.messagesConfiguration.getPlayerNotOnlineAndRuleCantBeExecutedOnOfflineTarget()
                            .replace("{target_name}", targetName)
                            .replace("{rule_identifier}", ruleIdentifier),
                    PLAYER_NOT_ONLINE_AND_RULE_CANT_BE_EXECUTED_ON_OFFLINE_TARGET
                            .replace("{target_name}", targetName)
                            .replace("{rule_identifier}", ruleIdentifier));
            return;
        }

        this.punishmentsManager.punish
        (
                rule,
                sender instanceof ProxiedPlayer ? sender.getName() : "console",
                targetName
        );
    }

    private void reload(CommandSender sender)
    {
        if (!sender.hasPermission("rules.punishments.commands.reload"))
        {
            sender.sendMessage(TextComponent.fromLegacyText(
                    this.messagesConfiguration.getYouDontHaveEnoughPermissionsMessage()));
            return;
        }

        this.plugin.reload();

        Chat.message(sender, this.messagesConfiguration.getPluginReloadedMessage(), CONSOLE_PLUGIN_RELOADED_MESSAGE);
    }

    private void sendHelp(CommandSender sender)
    {
        if (!sender.hasPermission("rules.punishments.commands.help"))
        {
            sender.sendMessage(TextComponent.fromLegacyText(
                    this.messagesConfiguration.getYouDontHaveEnoughPermissionsMessage()));
            return;
        }

        Chat.message(sender, this.messagesConfiguration.getHelpMessage(), CONSOLE_HELP_MESSAGE);
    }
}