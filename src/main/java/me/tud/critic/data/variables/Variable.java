package me.tud.critic.data.variables;

import me.tud.critic.data.Expression;
import me.tud.critic.data.types.Type;
import me.tud.critic.exception.ParseException;
import org.jetbrains.annotations.Nullable;

public class Variable {

    private final Type type;
    private final String identifier;
    @Nullable
    private Expression value;

    Variable(Type type, String identifier) {
        this.type = type;
        this.identifier = identifier;
    }

    public Type getType() {
        return type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public @Nullable Expression getExpression() {
        return value;
    }

    public @Nullable Object getValue() {
        return value == null ? null : value.getValue();
    }

    public void setValue(@Nullable Expression value) {
        if (value != null && !type.isInstance(value))
            throw new ParseException(value + " is not of type '" + type.getName() + '\'');
        this.value = value;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Variable{");
        sb.append("type=").append(type);
        sb.append(", identifier='").append(identifier).append('\'');
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }

}
