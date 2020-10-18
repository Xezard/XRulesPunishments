package ru.xezard.rules.punishments.server;

import org.bukkit.plugin.java.JavaPlugin;
import ru.xezard.configurations.bukkit.serialization.ConfigurationSerialization;
import ru.xezard.rules.punishments.configurations.MessagesConfiguration;
import ru.xezard.rules.punishments.configurations.RulesPunishmentsConfiguration;
import ru.xezard.rules.punishments.data.rule.Rule;
import ru.xezard.rules.punishments.server.commands.RulesPunishmentsCommand;
import ru.xezard.rules.punishments.server.data.ServerPunishmentManager;

public class RulesPunishmentsPlugin
extends JavaPlugin
{
    private MessagesConfiguration messagesConfiguration = new MessagesConfiguration(this.getDataFolder());
    private RulesPunishmentsConfiguration rulesPunishmentsConfiguration = new RulesPunishmentsConfiguration(this.getDataFolder());

    private ServerPunishmentManager punishmentsManager = new ServerPunishmentManager(this.getLogger());

    static
    {
        ConfigurationSerialization.registerClass(Rule.class);
    }

    @Override
    public void onEnable()
    {
        this.loadConfigurations();
        this.registerCommands();

        this.punishmentsManager.loadRules(this.rulesPunishmentsConfiguration.getRules());
    }

    @Override
    public void onDisable()
    {
        this.messagesConfiguration = null;
        this.rulesPunishmentsConfiguration = null;
        this.punishmentsManager = null;
    }

    private void registerCommands()
    {
        this.getCommand("rulespunishments").setExecutor(new RulesPunishmentsCommand
        (
                this.messagesConfiguration,
                this.punishmentsManager,
                this
        ));
    }

    private void loadConfigurations()
    {
        this.messagesConfiguration.load();
        this.rulesPunishmentsConfiguration.load();
    }

    public void reload()
    {
        this.loadConfigurations();

        this.punishmentsManager.loadRules(this.rulesPunishmentsConfiguration.getRules());
    }
}