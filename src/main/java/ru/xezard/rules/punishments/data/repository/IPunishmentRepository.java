package ru.xezard.rules.punishments.data.repository;

import ru.xezard.rules.punishments.data.rule.Rule;

public interface IPunishmentRepository
{
    int addViolation(Rule rule, String playerName);

    void resetViolation(Rule rule, String playerName);

    void resetViolations(String playerName);
}