package me.tud.critic.parser.node;

import me.tud.critic.data.types.Type;
import me.tud.critic.data.types.Types;
import me.tud.critic.exception.ParseException;

import java.util.Collections;
import java.util.List;

public class NegativeNode extends ExpressionNode {

    private final ExpressionNode expression;

    public NegativeNode(ExpressionNode expression) {
        super(ASTNodeType.NEGATIVE);
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
    public void init() {
        expression.init();
        if (!Types.isNumberPrimitive(expression.getReturnType()))
            throw new ParseException("Expected NUMBER expression but got: " + expression);
    }

    @Override
    public Number evaluate() {
        Number negative = (Number) expression.evaluate();
        if (expression.getReturnType().is("int"))
            negative = -negative.intValue();
        else if (expression.getReturnType().is("float"))
            negative = -negative.floatValue();
        else if (expression.getReturnType().is("double"))
            negative = -negative.doubleValue();
        return negative;
    }

    @Override
    public Type getReturnType() {
        return expression.getReturnType();
    }

}
