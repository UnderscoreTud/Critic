package me.tud.critic.parser.node;

public class IdentifierNode extends ASTNode {

    public IdentifierNode(String identifier) {
        super(ASTNodeType.IDENTIFIER, identifier);
    }

    @Override
    public String evaluate() {
        return (String) getValue();
    }

}
