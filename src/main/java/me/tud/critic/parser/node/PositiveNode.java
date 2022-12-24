package me.tud.critic.parser.node;

import me.tud.critic.data.Expression;
import me.tud.critic.data.types.Type;
import me.tud.critic.data.types.Types;
import me.tud.critic.exception.ParseException;

import java.util.Collections;
import java.util.List;

public class PositiveNode extends ExpressionNode {

    private final ExpressionNode expression;

    public PositiveNode(ExpressionNode expression) {
        super(ASTNodeType.POSITIVE);
        if (!Types.isNumberPrimitive(expression.getReturnType()))
            throw new ParseException("Expected NUMBER expression but got: " + expression);
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
    public Number evaluate() {
        return (Number) expression.evaluate();
    }

    @Override
    public Type getReturnType() {
        return expression.getReturnType();
    }

}
