package me.tud.critic.parser.node;

import me.tud.critic.data.Expression;
import me.tud.critic.data.types.Type;
import me.tud.critic.data.types.Types;
import me.tud.critic.data.variables.Variable;
import me.tud.critic.exception.ParseException;
import me.tud.critic.util.ArithmeticOperator;

public class IncrementNode extends ExpressionNode {

    private final ExpressionNode expression;
    boolean returnFirst;

    public IncrementNode(ExpressionNode expression, boolean returnFirst) {
        super(ASTNodeType.ASSIGN_EXPRESSION);
        this.expression = expression;
        this.returnFirst = returnFirst;
        if (!(expression instanceof VariableNode))
            throw new ParseException("Variable expected, got: " + expression);
    }

    @Override
    public void init() {
        expression.init();
        if (!Types.isNumberPrimitive(expression.getReturnType()))
            throw new ParseException("Expected NUMBER type, got: " + expression);
    }

    @Override
    public Number evaluate() {
        Variable variable = ((VariableNode) expression).getVariable();
        Number original = (Number) expression.evaluate();
        Number after =  ArithmeticOperator.PLUS.calculate(original, 1);
        variable.setValue(new Expression(variable.getType(), after));
        return returnFirst ? original : after;
    }

    @Override
    public Type getReturnType() {
        return expression.getReturnType();
    }

}
