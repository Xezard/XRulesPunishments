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
    PUNISHED_COMMAND("[punished_command]"),
    EXECUTOR_COMMAND("[executor_command]"),
    PUNISHED_MESSAGE("[punished_message]"),
    EXECUTOR_MESSAGE("[executor_message]"),
    BROADCAST_MESSAGE("[broadcast_message]"),
    BROADCAST_SOUND("[broadcast_sound]"),
    PUNISHED_PLAY_SOUND("[punished_sound]"),
    EXECUTOR_PLAY_SOUND("[executor_sound]");

    @Getter
    private String identifier;

    public static Optional<PunishmentActionType> get(String identifier)
    {
        return Stream.of(values())
                     .filter((actionType) -> identifier.startsWith(actionType.getIdentifier()))
                     .findFirst();
    }
}