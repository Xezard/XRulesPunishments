package ru.xezard.rules.punishments.configurations;

import lombok.Getter;
import ru.xezard.configurations.Configuration;
import ru.xezard.configurations.ConfigurationField;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@Getter
public class MessagesConfiguration
extends Configuration
{
    public MessagesConfiguration(File folder)
    {
        super(folder.getAbsolutePath() + File.separator + "messages.yml");
    }

    @ConfigurationField("Chat-messages.Help")
    private List<String> helpMessage = Arrays.asList
    (
            "§7§m╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴§7 [§6§lXRulesPunishments§7] §7§m╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴",
            " §7'§d§l[]§7', '§e§l<>§7' - обязательные и не обязательные аргументы",
            "",
            " §6❯ §7/rl §e§l<§7help§e§l> §7- показать эту подсказку",
            " §6❯ §7/rl reload - перезагрузить конфигурацию плагина",
            " §6❯ §7/rl punish [правило] [никнейм игрока] - наказать игрока по правилу",
            "§7§m╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴╴"
    );

    @ConfigurationField("Chat-messages.You-dont-have-enough-permissions")
    private String youDontHaveEnoughPermissionsMessage = "§cУ вас недостаточно прав.";

    @ConfigurationField("Chat-messages.Plugin-successfully-reloaded")
    private String pluginReloadedMessage = "§aПлагин перезагружен.";

    @ConfigurationField("Chat-messages.Rule-with-that-identifier-does-not-exists")
    private String ruleWithThatIdentifierDoesNotExistsMessage = "§cПравило с идентификатором '{rule_identifier}' не найдено!";

    @ConfigurationField("Chat-messages.Player-not-online-and-rule-cant-be-executed-on-offline-target")
    private String playerNotOnlineAndRuleCantBeExecutedOnOfflineTarget =
            "§cИгрок '{player_name}' оффлайн, а наказание по правилу с идентификатором '{rule_identifier}' " +
                    "не может быть применено на оффлайн игроке.";
}