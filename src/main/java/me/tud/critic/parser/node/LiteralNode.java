package me.tud.critic.parser.node;

import me.tud.critic.data.Expression;
import me.tud.critic.data.types.Type;

public abstract class LiteralNode extends ExpressionNode {

    public LiteralNode(ASTNodeType nodeType, Object value) {
        super(nodeType, value);
    }

    @Override
    public Object evaluate() {
        return getValue();
    }

}
