package me.tud.critic.parser.node;

import me.tud.critic.data.types.Type;
import me.tud.critic.data.types.Types;

public class LiteralBooleanNode extends LiteralNode {

    public LiteralBooleanNode(boolean value) {
        super(ASTNodeType.LITERAL_BOOLEAN, value);
    }

    @Override
    public Type getReturnType() {
        return Types.getPrimitiveType("boolean");
    }

}
