package ru.xezard.rules.punishments.proxy;

import net.md_5.bungee.api.plugin.Plugin;
import ru.xezard.configurations.bukkit.serialization.ConfigurationSerializable;
import ru.xezard.configurations.bukkit.serialization.ConfigurationSerialization;
import ru.xezard.rules.punishments.configurations.MessagesConfiguration;
import ru.xezard.rules.punishments.configurations.RulesPunishmentsConfiguration;
import ru.xezard.rules.punishments.data.rule.Rule;
import ru.xezard.rules.punishments.proxy.commands.RulesPunishmentsCommand;
import ru.xezard.rules.punishments.proxy.data.ProxyPunishmentManager;

public class RulesPunishmentsPlugin
extends Plugin
{
    private MessagesConfiguration messagesConfiguration = new MessagesConfiguration(this.getDataFolder());
    private RulesPunishmentsConfiguration rulesPunishmentsConfiguration = new RulesPunishmentsConfiguration(this.getDataFolder());

    private ProxyPunishmentManager punishmentsManager = new ProxyPunishmentManager(this.getLogger());

    static
    {
        ConfigurationSerialization.registerClass(Rule.class);
    }

    @Override
    public void onEnable()
    {
        this.loadConfigurations();
        this.registerCommands();
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
        this.getProxy().getPluginManager().registerCommand(this, new RulesPunishmentsCommand
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
    }
}