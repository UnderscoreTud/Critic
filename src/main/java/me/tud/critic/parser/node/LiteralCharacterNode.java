package me.tud.critic.parser.node;

import me.tud.critic.data.types.Type;
import me.tud.critic.data.types.Types;

public class LiteralCharacterNode extends LiteralNode {

    public LiteralCharacterNode(char character) {
        super(ASTNodeType.LITERAL_CHARACTER, character);
    }

    @Override
    public Type getReturnType() {
        return Types.getPrimitiveType("char");
    }

}
