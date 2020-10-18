package ru.xezard.rules.punishments.data.action;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@AllArgsConstructor
public enum PunishmentActionType
{
    CONSOLE("[console]"),
    COMMAND("[command]"),
    MESSAGE("[message]"),
    BROADCAST("[broadcast_message]"),
    BROADCAST_SOUND("[broadcast_sound]"),
    PLAY_SOUND("[sound]");

    @Getter
    private String identifier;

    public static Optional<PunishmentActionType> get(String identifier)
    {
        return Stream.of(values())
                     .filter((actionType) -> identifier.startsWith(actionType.getIdentifier()))
                     .findFirst();
    }
}