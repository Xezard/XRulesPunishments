package ru.xezard.rules.punishments.server.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@UtilityClass
public class Chat
{
    public void message(CommandSender sender, Iterable<String> playerMessage, Iterable<String> consoleMessage)
    {
        if (sender instanceof Player)
        {
            playerMessage.forEach(sender::sendMessage);
            return;
        }

        consoleMessage.forEach(sender::sendMessage);
    }

    public void message(CommandSender sender, String playerMessage, String consoleMessage)
    {
        sender.sendMessage(sender instanceof Player ? playerMessage : consoleMessage);
    }
}