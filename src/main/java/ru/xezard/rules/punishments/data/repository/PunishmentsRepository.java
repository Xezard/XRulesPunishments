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
package ru.xezard.rules.punishments.data.repository;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import ru.xezard.rules.punishments.data.rule.Rule;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PunishmentsRepository
implements IPunishmentRepository
{
    // <player name, <punishment rule, violation level>>
    private Cache<String, Map<Rule, Integer>> violations =
            CacheBuilder.newBuilder()
                        .expireAfterWrite(5, TimeUnit.HOURS)
                        .build();

    @Override
    public int addViolation(Rule rule, String playerName)
    {
        Map<Rule, Integer> violations = this.violations.getIfPresent(playerName);

        if (violations == null)
        {
            violations = new HashMap<> ();

            violations.put(rule, 1);

            this.violations.put(playerName, violations);

            return 1;
        }

        return violations.merge(rule, 1, Integer::sum);
    }

    @Override
    public void resetViolation(Rule rule, String playerName)
    {
        Map<Rule, Integer> violations = this.violations.getIfPresent(playerName);

        if (violations == null)
        {
            return;
        }

        violations.remove(rule);
    }

    @Override
    public void resetViolations(String playerName)
    {
        this.violations.invalidate(playerName);
    }
}