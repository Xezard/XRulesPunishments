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