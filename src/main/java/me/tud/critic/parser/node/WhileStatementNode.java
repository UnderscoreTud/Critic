package me.tud.critic.parser.node;

import java.util.List;

public class WhileStatementNode extends ControlNode {

    private final ConditionNode condition;

    public WhileStatementNode(ConditionNode condition, BlockNode block) {
        super(ASTNodeType.WHILE, block);
        this.condition = condition;
    }

    public ConditionNode getCondition() {
        return condition;
    }

    @Override
    public List<ASTNode> getChildren() {
        return List.of(condition, getBlock());
    }

    @Override
    public Object evaluate() {
        while (condition.evaluate())
            getBlock().evaluate();
        return null;
    }

}
