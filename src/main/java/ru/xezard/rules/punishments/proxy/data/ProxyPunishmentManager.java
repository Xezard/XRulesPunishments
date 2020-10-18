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
package ru.xezard.rules.punishments.proxy.data;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import ru.xezard.rules.punishments.data.AbstractPunishmentManager;
import ru.xezard.rules.punishments.data.action.PunishmentActionType;
import ru.xezard.rules.punishments.data.rule.Rule;

import java.util.logging.Logger;

public class ProxyPunishmentManager
extends AbstractPunishmentManager
{
    public ProxyPunishmentManager(Logger logger)
    {
        super(logger);
    }

    @Override
    public void punish(Rule rule, String executorName, String playerName)
    {
        int violationLevel = this.repository.addViolation(rule, playerName);

        if (rule.getActions().size() < violationLevel)
        {
            this.repository.resetViolation(rule, playerName);

            violationLevel = this.repository.addViolation(rule, playerName);
        }

        rule.getActions().get(violationLevel).forEach((action) ->
        {
            PunishmentActionType type = action.getType();

            if (type == PunishmentActionType.BROADCAST_SOUND ||
                type == PunishmentActionType.EXECUTOR_PLAY_SOUND ||
                type == PunishmentActionType.PUNISHED_PLAY_SOUND)
            {
                this.logger.warning("Can't execute '" + type.name() + "' action in rule '" +
                        rule.getIdentifier() + "', proxy does not support this action!");
                return;
            }

            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerName);

            if (type == PunishmentActionType.PUNISHED_MESSAGE || type == PunishmentActionType.PUNISHED_COMMAND)
            {
                if (rule.isCanBeExecutedOnOfflineTarget())
                {
                    this.logger.warning("Can't execute '" + type.name() + "' action in rule '" +
                            rule.getIdentifier() + "' on offline player!");
                    return;
                }

                if (player == null || !player.isConnected())
                {
                    this.logger.warning("Can't execute '" + type.name() + "' action in rule '" +
                            rule.getIdentifier() + "', target '" + playerName + "' not found!");
                    return;
                }
            }

            CommandSender executor = executorName.equalsIgnoreCase("console") ?
                    ProxyServer.getInstance().getConsole() :
                    ProxyServer.getInstance().getPlayer(executorName);

            if (executor == null)
            {
                // is is even possible to get here?
                this.logger.warning("Can't execute actions because '" +
                        executorName + "', is offline!");
                return;
            }

            String executable = action.getExecutable()
                    .replace("{player_name}", playerName)
                    .replace("{executor_name}", executorName);

            switch (action.getType())
            {
                case CONSOLE:
                    ProxyServer.getInstance()
                               .getPluginManager()
                               .dispatchCommand(ProxyServer.getInstance().getConsole(), executable);
                    break;

                case PUNISHED_COMMAND:
                    ProxyServer.getInstance()
                               .getPluginManager()
                               .dispatchCommand(player, executable);
                    break;

                case EXECUTOR_COMMAND:
                    ProxyServer.getInstance()
                               .getPluginManager()
                               .dispatchCommand(executor, executable);
                    break;

                case PUNISHED_MESSAGE:
                    player.sendMessage(TextComponent.fromLegacyText(executable));
                    break;

                case EXECUTOR_MESSAGE:
                    executor.sendMessage(TextComponent.fromLegacyText(executable));
                    break;

                case BROADCAST_MESSAGE:
                    ProxyServer.getInstance()
                               .getPlayers()
                               .forEach((p) -> p.sendMessage(TextComponent.fromLegacyText(executable)));
                    break;
            }
        });
    }
}