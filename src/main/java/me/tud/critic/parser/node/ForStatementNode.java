package me.tud.critic.parser.node;

import me.tud.critic.util.CollectionUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ForStatementNode extends ControlNode {

    @Nullable
    private final AssignmentNode assignment;
    @Nullable
    private final ConditionNode condition;
    @Nullable
    private final ExpressionNode expression;

    public ForStatementNode(@Nullable AssignmentNode assignment, @Nullable ConditionNode condition, @Nullable ExpressionNode expression, BlockNode block) {
        super(ASTNodeType.WHILE, block);
        this.assignment = assignment;
        this.condition = condition;
        this.expression = expression;
    }

    public @Nullable AssignmentNode getAssignment() {
        return assignment;
    }

    public @Nullable ConditionNode getCondition() {
        return condition;
    }

    public @Nullable ExpressionNode getExpression() {
        return expression;
    }

    @Override
    public List<ASTNode> getChildren() {
        return CollectionUtils.listOfNonNullables(assignment, condition, expression);
    }

    @Override
    public Object evaluate() {
        if (assignment != null)
            assignment.evaluate();
        while (condition == null || condition.evaluate()) {
            if (expression != null)
                expression.evaluate();
            getBlock().evaluate();
        }
        return null;
    }

}
