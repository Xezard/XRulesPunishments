package ru.xezard.rules.punishments.proxy.utils;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@UtilityClass
public class Chat
{
    public void message(CommandSender sender, Iterable<String> playerMessage, Iterable<String> consoleMessage)
    {
        if (sender instanceof ProxiedPlayer)
        {
            playerMessage.forEach((message) -> sender.sendMessage(TextComponent.fromLegacyText(message)));
            return;
        }

        consoleMessage.forEach((message) -> sender.sendMessage(TextComponent.fromLegacyText(message)));
    }

    public void message(CommandSender sender, String playerMessage, String consoleMessage)
    {
        sender.sendMessage(sender instanceof ProxiedPlayer ?
                TextComponent.fromLegacyText(playerMessage) :
                TextComponent.fromLegacyText(consoleMessage));
    }
}