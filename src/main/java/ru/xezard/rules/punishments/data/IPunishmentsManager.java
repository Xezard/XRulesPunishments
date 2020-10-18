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
package ru.xezard.rules.punishments.data;

import ru.xezard.rules.punishments.data.repository.IPunishmentRepository;
import ru.xezard.rules.punishments.data.rule.Rule;

import java.util.List;
import java.util.Optional;

public interface IPunishmentsManager
{
    IPunishmentRepository getRepository();

    Optional<Rule> getRuleByIdentifier(String identifier);

    void punish(Rule rule, String playerName);

    void loadRules(List<Rule> rules);
}