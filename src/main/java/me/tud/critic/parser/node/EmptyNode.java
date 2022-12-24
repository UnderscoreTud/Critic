package me.tud.critic.parser.node;

public class EmptyNode extends StatementNode {

    public EmptyNode() {
        super(ASTNodeType.EMPTY);
    }

    @Override
    public Object evaluate() {
        return null;
    }

}
