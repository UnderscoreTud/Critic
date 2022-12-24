package me.tud.critic.data.types;

import me.tud.critic.data.Expression;

public class Type {

    private final String name;
    private final boolean primitive;

    Type(String name, boolean primitive) {
        this.name = name;
        this.primitive = primitive;
    }

    public String getName() {
        return name;
    }

    public boolean isPrimitive() {
        return primitive;
    }

    public boolean isInstance(Expression expression) {
        return this.name.equals(expression.getType().name);
    }

    public boolean is(String typeName) {
        return this == Types.lookupType(typeName);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Type{");
        sb.append("name='").append(name).append('\'');
        sb.append(", primitive=").append(primitive);
        sb.append('}');
        return sb.toString();
    }
}
