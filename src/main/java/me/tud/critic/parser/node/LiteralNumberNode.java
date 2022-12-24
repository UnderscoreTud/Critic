package me.tud.critic.parser.node;

import me.tud.critic.data.types.Type;
import me.tud.critic.data.types.Types;
import me.tud.critic.exception.ParseException;

public class LiteralNumberNode extends LiteralNode {

    private final Type numberType;

    public LiteralNumberNode(Number number) {
        super(ASTNodeType.NUMBER, number);
        if (number.getClass() == Long.class)
            numberType = Types.getPrimitiveType("int");
        else if (number.getClass() == Float.class)
            numberType = Types.getPrimitiveType("float");
        else if (number.getClass() == Double.class)
            numberType = Types.getPrimitiveType("double");
        else throw new ParseException("Unexpected number class: '" + number.getClass() + '\'');
    }

    @Override
    public Type getReturnType() {
        return numberType;
    }

}
