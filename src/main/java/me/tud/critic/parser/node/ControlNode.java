package me.tud.critic.parser.node;

public abstract class ControlNode extends StatementNode {

    private final BlockNode block;

    public ControlNode(ASTNodeType type, BlockNode block) {
        super(type);
        this.block = block;
    }

    public BlockNode getBlock() {
        return block;
    }

}
