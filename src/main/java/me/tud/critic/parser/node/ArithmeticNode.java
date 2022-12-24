package me.tud.critic.parser.node;

import me.tud.critic.data.Expression;
import me.tud.critic.data.types.Type;
import me.tud.critic.data.types.Types;
import me.tud.critic.exception.ParseException;
import me.tud.critic.util.ArithmeticOperator;

import java.util.List;

public class ArithmeticNode extends ExpressionNode {

    private final ExpressionNode leftOperand;
    private final OperatorNode operator;
    private final ExpressionNode rightOperand;
    private final ArithmeticOperator arithmeticOperator;
    private Type returnType;

    public ArithmeticNode(ExpressionNode leftOperand, OperatorNode operator, ExpressionNode rightOperand) {
        super(ASTNodeType.EXPRESSION);
        this.leftOperand = leftOperand;
        this.operator = operator;
        this.rightOperand = rightOperand;
        this.arithmeticOperator = ArithmeticOperator.bySign(operator.getOperator().charAt(0));
    }

    public ExpressionNode getLeftOperand() {
        return leftOperand;
    }

    public OperatorNode getOperator() {
        return operator;
    }

    public ExpressionNode getRightOperand() {
        return rightOperand;
    }

    @Override
    public List<ASTNode> getChildren() {
        return List.of(leftOperand, operator, rightOperand);
    }

    @Override
    public void init() {
        super.init();
        Type[] types = {leftOperand.getReturnType(), rightOperand.getReturnType()};

        if (!Types.isNumberPrimitive(types[0]))
            throw new ParseException("Expected type NUMBER but got: " + leftOperand);
        if (!Types.isNumberPrimitive(types[1]))
            throw new ParseException("Expected type NUMBER but got: " + rightOperand);

        returnType = Types.getNumberPrimitive(types);
        if (returnType == null)
            throw new IllegalStateException("returnType cannot be null");
    }

    @Override
    public Number evaluate() {
        return arithmeticOperator.calculate((Number) leftOperand.evaluate(), (Number) rightOperand.evaluate());
    }

    @Override
    public Type getReturnType() {
        return Types.getNumberPrimitive();
    }

}
