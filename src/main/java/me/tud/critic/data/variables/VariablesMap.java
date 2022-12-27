package me.tud.critic.data.variables;

import me.tud.critic.data.types.Type;
import me.tud.critic.exception.ParseException;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class VariablesMap {

    @Nullable
    private final VariablesMap parentMap;
    private final HashMap<String, Variable> variableMap = new HashMap<>();

    public VariablesMap() {
        this(null);
    }

    public VariablesMap(@Nullable VariablesMap parentMap) {
        this.parentMap = parentMap;
    }

    public Variable declareVariable(Type type, String identifier) {
        if (variableExists(identifier))
            throw new ParseException("The variable '" + identifier + "' is already declared");
        Variable variable = new Variable(type, identifier);
        variableMap.put(identifier, variable);
        return variable;
    }

    public Variable getVariable(String identifier) {
        Variable variable = variableMap.get(identifier);
        if (variable == null && parentMap != null)
            return parentMap.getVariable(identifier);
        return variable;
    }

    public boolean variableExists(String identifier) {
        return (getParentMap() != null && getParentMap().variableExists(identifier)) || variableMap.containsKey(identifier);
    }

    public @Nullable VariablesMap getParentMap() {
        return parentMap;
    }

    private HashMap<String, Variable> collectMaps() {
        HashMap<String, Variable> map = new HashMap<>(variableMap);
        if (parentMap != null)
            map.putAll(parentMap.collectMaps());
        return map;
    }

    @Override
    public String toString() {
        return collectMaps().toString();
    }

}
