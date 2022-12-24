package me.tud.critic.data;

import me.tud.critic.data.types.Type;

@SuppressWarnings("ClassCanBeRecord")
public class Expression {

    private final Type type;
    private final Object value;

    public Expression(Type type, Object value) {
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }
}
