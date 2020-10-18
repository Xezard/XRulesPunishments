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