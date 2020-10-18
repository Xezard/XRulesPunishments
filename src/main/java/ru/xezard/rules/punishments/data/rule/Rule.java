package ru.xezard.rules.punishments.data.rule;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import ru.xezard.configurations.bukkit.serialization.SerializableAs;
import ru.xezard.rules.punishments.data.action.PunishmentAction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@SerializableAs("Rule")
public class Rule
implements IRule
{
    // <violation level, list of actions>
    private Multimap<Integer, PunishmentAction> actions = ArrayListMultimap.create();

    private String identifier;

    private boolean canBeExecutedOnOfflineTarget;

    public Rule(Map<String, List<String>> violations, String identifier, boolean canBeExecutedOnOfflineTarget)
    {
        violations.forEach((violation, actions) ->
        {
            this.actions.putAll
            (
                    Integer.parseInt(violation),

                    actions.stream()
                           .map(PunishmentAction::deserialize)
                           .collect(Collectors.toList())
            );
        });

        this.identifier = identifier;

        this.canBeExecutedOnOfflineTarget = canBeExecutedOnOfflineTarget;
    }

    @Override
    public Map<String, Object> serialize()
    {
        Map<String, Object> serializedRule = new HashMap<> (),
                            violations = new HashMap<> ();

        this.actions.forEach((violation, actions) ->
        {
            violations.put
            (
                    Integer.toString(violation),

                    this.actions.get(violation)
                                .stream()
                                .map(PunishmentAction::serialize)
                                .collect(Collectors.toList())
            );
        });

        serializedRule.put(IDENTIFIER_SERIALIZATION_KEY, this.identifier);
        serializedRule.put(VIOLATIONS_SERIALIZATION_KEY, violations);

        if (this.canBeExecutedOnOfflineTarget)
        {
            serializedRule.put(CAN_BE_EXECUTED_ON_OFFLINE_TARGET_SERIALIZATION_KEY, true);
        }

        return serializedRule;
    }

    @SuppressWarnings("unchecked")
    public static Rule deserialize(Map<String, Object> serializedRule)
    {
        boolean canBeExecutedOnOfflineTarget = false;

        if (serializedRule.containsKey(CAN_BE_EXECUTED_ON_OFFLINE_TARGET_SERIALIZATION_KEY))
        {
            canBeExecutedOnOfflineTarget = (boolean) serializedRule.get(CAN_BE_EXECUTED_ON_OFFLINE_TARGET_SERIALIZATION_KEY);
        }

        return new Rule
        (
                (Map<String, List<String>>) serializedRule.get(VIOLATIONS_SERIALIZATION_KEY),
                (String) serializedRule.get(IDENTIFIER_SERIALIZATION_KEY),
                canBeExecutedOnOfflineTarget
        );
    }
}