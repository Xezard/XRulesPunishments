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