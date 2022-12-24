package me.tud.critic.parser.node;

import me.tud.critic.util.ComparisonOperator;

import java.util.List;

public class ComparisonNode extends ConditionNode {

    private final ExpressionNode leftOperand;
    private final OperatorNode operator;
    private final ExpressionNode rightOperand;
    private final ComparisonOperator comparisonOperator;

    public ComparisonNode(ExpressionNode leftOperand, OperatorNode operator, ExpressionNode rightOperand) {
        this.leftOperand = leftOperand;
        this.operator = operator;
        this.rightOperand = rightOperand;
        this.comparisonOperator = ComparisonOperator.bySign(operator.getOperator());
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
    public Boolean evaluate() {
        return comparisonOperator.test((Number) leftOperand.evaluate(), (Number) rightOperand.evaluate());
    }

}
