package com.furnaghan.home.policy;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.script.ParameterMap;
import com.furnaghan.home.script.Script;

public class ScriptedPolicy extends Policy {

    private final Script script;

    public ScriptedPolicy(final Class<? extends Component.Listener> type, final String event,
                          final Class<?>[] parameterTypes, final Script script) {
        super(type, event, parameterTypes);

        this.script = script;
    }

    @Override
    public <T> T run(final ParameterMap params) throws Exception {
        return script.run(params);
    }
}
