package ru.xezard.rules.punishments.data.action;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PunishmentAction
{
    private PunishmentActionType type;

    private String executable;

    public String serialize()
    {
        return this.type.getIdentifier() + " " + this.executable;
    }

    public static PunishmentAction deserialize(String serializedPunishmentAction)
    {
        if (serializedPunishmentAction == null || serializedPunishmentAction.isEmpty())
        {
            return null;
        }

        String[] split = serializedPunishmentAction.split("]");

        if (split.length < 2)
        {
            return null;
        }

        String executable = split[1].substring(1);

        return PunishmentActionType.get(serializedPunishmentAction)
                                   .map(punishmentActionType -> new PunishmentAction(punishmentActionType, executable))
                                   .orElse(null);
    }
}