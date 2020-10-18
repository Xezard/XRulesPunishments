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