package me.tud.critic.parser.node;

import me.tud.critic.data.types.Type;
import me.tud.critic.data.types.Types;

public abstract class ConditionNode extends ExpressionNode {

    protected ConditionNode() {
        super(ASTNodeType.CONDITION);
    }

    @Override
    public abstract Boolean evaluate();

    @Override
    public Type getReturnType() {
        return Types.getPrimitiveType("boolean");
    }

}
