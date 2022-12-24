package me.tud.critic.parser.node;

import me.tud.critic.data.Expression;
import me.tud.critic.data.types.Type;

import java.util.Collections;
import java.util.List;

public class ParenthesizedExpressionNode extends ExpressionNode {

    private final ExpressionNode expression;

    public ParenthesizedExpressionNode(ExpressionNode expression) {
        super(ASTNodeType.EXPRESSION);
        this.expression = expression;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    @Override
    public List<ASTNode> getChildren() {
        return Collections.singletonList(expression);
    }

    @Override
    public Object evaluate() {
        return expression.evaluate();
    }

    @Override
    public Type getReturnType() {
        return expression.getReturnType();
    }

}
