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