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
package ru.xezard.rules.punishments.data.rule;

import com.google.common.collect.Multimap;
import ru.xezard.configurations.bukkit.serialization.ConfigurationSerializable;
import ru.xezard.rules.punishments.data.action.PunishmentAction;

public interface IRule
extends ConfigurationSerializable
{
    String VIOLATIONS_SERIALIZATION_KEY = "Violations",
           IDENTIFIER_SERIALIZATION_KEY = "Identifier",
           CAN_BE_EXECUTED_ON_OFFLINE_TARGET_SERIALIZATION_KEY = "Can-be-executed-on-offline-target";

    Multimap<Integer, PunishmentAction> getActions();

    boolean isCanBeExecutedOnOfflineTarget();
}