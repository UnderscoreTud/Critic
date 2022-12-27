package me.tud.critic.parser.node;

import me.tud.critic.data.Expression;
import me.tud.critic.data.types.Type;
import me.tud.critic.data.types.Types;
import me.tud.critic.exception.ParseException;

import java.util.Collections;
import java.util.List;

public class NegateNode extends ExpressionNode {

    private final ExpressionNode expression;

    public NegateNode(ExpressionNode expression) {
        super(ASTNode.ASTNodeType.NEGATE);
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
        int i = 0;
        int i1 = i++ + ++i;
        expression.init();
        if (!expression.getReturnType().is("boolean"))
            throw new ParseException("Expected BOOLEAN expression but got: " + expression);
    }

    @Override
    public Boolean evaluate() {
        return !(Boolean) expression.evaluate();
    }

    @Override
    public Type getReturnType() {
        return Types.getPrimitiveType("boolean");
    }

}
