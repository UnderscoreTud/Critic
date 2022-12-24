package me.tud.critic.parser.node;

import me.tud.critic.exception.ParseException;

import java.util.Collections;
import java.util.List;

public class ExprConditionNode extends ConditionNode {

    private final ExpressionNode expression;

    public ExprConditionNode(ExpressionNode expression) {
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
        if (!expression.getReturnType().is("boolean"))
            throw new ParseException("Expected BOOLEAN expression but got: " + expression);
    }

    @Override
    public Boolean evaluate() {
        return (Boolean) expression.evaluate();
    }

}
