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