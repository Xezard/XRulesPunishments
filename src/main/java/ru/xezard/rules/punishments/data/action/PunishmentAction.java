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