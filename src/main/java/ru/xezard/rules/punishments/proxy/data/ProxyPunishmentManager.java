package ru.xezard.rules.punishments.proxy.data;

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

            if (type == PunishmentActionType.BROADCAST_SOUND || type == PunishmentActionType.PLAY_SOUND)
            {
                this.logger.warning("Can't execute '" + type.name() + "' action in rule '" +
                        rule.getIdentifier() + "', proxy does not support this action!");
                return;
            }

            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerName);

            if (type == PunishmentActionType.MESSAGE || type == PunishmentActionType.COMMAND)
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

            String executable = action.getExecutable().replace("{player_name}", playerName);

            switch (action.getType())
            {
                case CONSOLE:
                    ProxyServer.getInstance()
                               .getPluginManager()
                               .dispatchCommand(ProxyServer.getInstance().getConsole(), executable);
                    break;

                case COMMAND:
                    ProxyServer.getInstance()
                               .getPluginManager()
                               .dispatchCommand(player, executable);
                    break;

                case MESSAGE:
                    player.sendMessage(TextComponent.fromLegacyText(executable));
                    break;

                case BROADCAST:
                    ProxyServer.getInstance()
                               .getPlayers()
                               .forEach((p) -> p.sendMessage(TextComponent.fromLegacyText(executable)));
                    break;
            }
        });
    }
}