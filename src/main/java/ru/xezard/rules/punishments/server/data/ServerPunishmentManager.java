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
package ru.xezard.rules.punishments.server.data;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import ru.xezard.rules.punishments.data.AbstractPunishmentManager;
import ru.xezard.rules.punishments.data.action.PunishmentActionType;
import ru.xezard.rules.punishments.data.rule.Rule;

import java.util.logging.Logger;

public class ServerPunishmentManager
extends AbstractPunishmentManager
{
    public ServerPunishmentManager(Logger logger)
    {
        super(logger);
    }

    @Override
    public void punish(Rule rule, String playerName)
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

            Player player = Bukkit.getPlayerExact(playerName);

            if (type == PunishmentActionType.PLAY_SOUND ||
                type == PunishmentActionType.MESSAGE ||
                type == PunishmentActionType.COMMAND)
            {
                if (rule.isCanBeExecutedOnOfflineTarget())
                {
                    this.logger.warning("Can't execute '" + type.name() + "' action in rule '" +
                            rule.getIdentifier() + "' on offline player!");
                    return;
                }

                if (player == null || !player.isOnline())
                {
                    this.logger.warning("Can't execute '" + type.name() + "' action in rule '" +
                            rule.getIdentifier() + "', target '" + playerName + "' not found!");
                    return;
                }
            }

            String executable = action.getExecutable().replace("{player_name}", playerName);

            switch (action.getType())
            {
                case CONSOLE:
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), executable);
                    break;

                case COMMAND:
                    Bukkit.dispatchCommand(player, executable);
                    break;

                case MESSAGE:
                    player.sendMessage(executable);
                    break;

                case BROADCAST:
                    Bukkit.getOnlinePlayers().forEach((p) -> p.sendMessage(executable));
                    break;

                case BROADCAST_SOUND:
                    Bukkit.getOnlinePlayers().forEach((p) -> p.playSound(p.getLocation(), Sound.valueOf(executable), 1.0f, 1.0f));
                    break;

                case PLAY_SOUND:
                    player.playSound(player.getLocation(), Sound.valueOf(executable), 1.0f, 1.0f);
                    break;
            }
        });
    }
}