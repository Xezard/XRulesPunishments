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
package ru.xezard.rules.punishments.configurations;

import lombok.Getter;
import ru.xezard.configurations.Configuration;
import ru.xezard.configurations.ConfigurationComments;
import ru.xezard.configurations.ConfigurationField;
import ru.xezard.rules.punishments.data.rule.Rule;

import java.io.File;
import java.util.*;

@Getter
public class RulesPunishmentsConfiguration
extends Configuration
{
    public RulesPunishmentsConfiguration(File folder)
    {
        super(folder.getAbsolutePath() + File.separator + "config.yml");
    }

    private Map<String, List<String>> exampleRuleViolations = new HashMap<String, List<String>> ()
    {{
        this.put("1", Collections.singletonList("[console] mute {player_name} 5m The first violation of rule " +
                "1.1, you are muted for 5 minutes."));

        this.put("2", Collections.singletonList("[console] mute {player_name} 15m The second violation of rule " +
                "1.1, you are muted for 15 minutes."));

        this.put("3", Collections.singletonList("[console] mute {player_name} 15m The third violation of rule " +
                "1.1, you are banned for 5 minutes."));
    }};

    @ConfigurationField("Rules")
    @ConfigurationComments
    ({
            "# Supported types of actions: ",
            "# [console] - execute command from console",
            "# [message] - send message to punished player",
            "# [broadcast_message] - broadcast message to all online players",
            "# [broadcast_sound] (works only on server, not proxy) - broadcast sound to all online players",
            "# [sound] (works only on server, not proxy) - play sound for punished player"
    })
    private List<Rule> rules = Collections.singletonList
    (
            new Rule(this.exampleRuleViolations, "1.1", false)
    );
}