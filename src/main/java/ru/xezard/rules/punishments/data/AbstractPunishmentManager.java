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

import lombok.Getter;
import ru.xezard.rules.punishments.data.repository.PunishmentsRepository;
import ru.xezard.rules.punishments.data.rule.Rule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public abstract class AbstractPunishmentManager
implements IPunishmentsManager
{
    @Getter
    protected final PunishmentsRepository repository = new PunishmentsRepository();

    protected Map<String, Rule> rules = new HashMap<> ();

    protected Logger logger;

    public AbstractPunishmentManager(Logger logger)
    {
        this.logger = logger;
    }

    @Override
    public void loadRules(List<Rule> rules)
    {
        this.rules.putAll
        (
                rules.stream()
                     .collect(Collectors.toMap(Rule::getIdentifier, Function.identity()))
        );
    }

    public Optional<Rule> getRuleByIdentifier(String identifier)
    {
        return Optional.ofNullable(this.rules.get(identifier));
    }
}