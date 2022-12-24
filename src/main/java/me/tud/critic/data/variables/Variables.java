package me.tud.critic.data.variables;

import me.tud.critic.data.types.Type;
import me.tud.critic.exception.ParseException;

import java.util.HashMap;

public final class Variables {

    private Variables() {}

    private static final HashMap<String, Variable> variableMap = new HashMap<>();

    public static Variable declareVariable(Type type, String identifier) {
        if (variableExists(identifier))
            throw new ParseException("The variable '" + identifier + "' is already declared");
        Variable variable = new Variable(type, identifier);
        variableMap.put(identifier, variable);
        return variable;
    }

    public static Variable getVariable(String identifier) {
        return variableMap.get(identifier);
    }

    public static boolean variableExists(String identifier) {
        return variableMap.containsKey(identifier);
    }

}
