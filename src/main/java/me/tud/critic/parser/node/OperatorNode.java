package me.tud.critic.parser.node;

public class OperatorNode extends ASTNode {

    public OperatorNode(String operator) {
        super(ASTNodeType.OPERATOR, operator);
    }

    public String getOperator() {
        return (String) getValue();
    }

    @Override
    public Object evaluate() {
        // TODO: IMPLEMENT
        return null;
    }

}
