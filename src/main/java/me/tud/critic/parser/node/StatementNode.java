package me.tud.critic.parser.node;

public abstract class StatementNode extends ASTNode {

    public StatementNode(ASTNodeType nodeType) {
        super(nodeType);
    }

    public StatementNode(ASTNodeType nodeType, Object value) {
        super(nodeType, value);
    }

}
