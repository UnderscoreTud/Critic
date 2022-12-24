package me.tud.critic.parser.node;

import me.tud.critic.util.CollectionUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class IfStatementNode extends ControlNode {

    private final ConditionNode condition;
    @Nullable
    private final IfStatementNode elseIfStatement;
    @Nullable
    private final BlockNode elseBlock;

    public IfStatementNode(ConditionNode condition, BlockNode thenBlock) {
        this(condition, thenBlock, null, null);
    }

    public IfStatementNode(ConditionNode condition, BlockNode thenBlock, @Nullable BlockNode elseBlock) {
        this(condition, thenBlock, null, elseBlock);
    }

    public IfStatementNode(ConditionNode condition, BlockNode thenBlock, @Nullable IfStatementNode elseIfStatement) {
        this(condition, thenBlock, elseIfStatement, null);
    }

    public IfStatementNode(ConditionNode condition, BlockNode thenBlock, @Nullable IfStatementNode elseIfStatement, @Nullable BlockNode elseBlock) {
        super(elseBlock == null ? ASTNodeType.IF : ASTNodeType.IF_ELSE, thenBlock);
        this.condition = condition;
        this.elseIfStatement = elseIfStatement;
        this.elseBlock = elseBlock;
    }

    public ConditionNode getCondition() {
        return condition;
    }

    @Nullable
    public BlockNode getElseBlock() {
        return elseBlock;
    }

    @Override
    public List<ASTNode> getChildren() {
        return CollectionUtils.listOfNonNullables(condition, getBlock(), elseIfStatement, elseBlock);
    }

    @Override
    public Object evaluate() {
        if (condition.evaluate())
            return getBlock().evaluate();
        else if (elseIfStatement != null)
            elseIfStatement.evaluate();
        else if (elseBlock != null)
            return elseBlock.evaluate();
        return null;
    }

}
