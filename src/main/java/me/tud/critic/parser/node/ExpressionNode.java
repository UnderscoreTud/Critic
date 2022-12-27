package me.tud.critic.parser.node;

import me.tud.critic.data.Expression;
import me.tud.critic.data.types.Type;

public abstract class ExpressionNode extends StatementNode {

    public ExpressionNode(ASTNodeType type) {
        super(type);
    }

    public ExpressionNode(ASTNodeType nodeType, Object value) {
        super(nodeType, value);
    }

    public Expression evaluateAsExpression() {
        return new Expression(getReturnType(), evaluate());
    }

    public abstract Type getReturnType();

//    @Override
//    public String toString() {
//        return "ExpressionNode{" + "nodeType=" + getNodeType() +
//                ", returnType=" + getReturnType() +
//                '}';
//    }
}
