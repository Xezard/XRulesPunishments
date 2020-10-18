package ru.xezard.rules.punishments.proxy.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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
            "> 'rl <help>' - show help page",
            "> 'rl reload' - reload plugin",
            "> 'rl punish [rule] [player name] - punish the player by rule'"
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

        Player target = Bukkit.getPlayerExact(targetName);

        if ((target == null || !target.isOnline()) && !rule.isCanBeExecutedOnOfflineTarget())
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

        this.punishmentsManager.punish(rule, targetName);
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