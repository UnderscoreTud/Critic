package me.tud.critic.parser.node;

import me.tud.critic.data.types.Type;
import me.tud.critic.data.variables.Variable;
import me.tud.critic.data.variables.Variables;
import me.tud.critic.exception.ParseException;

import java.util.Collections;
import java.util.List;

public class VariableNode extends ExpressionNode {

    private final IdentifierNode identifier;
    private Variable variable;

    public VariableNode(IdentifierNode identifier) {
        super(ASTNodeType.VARIABLE);
        this.identifier = identifier;
    }

    public IdentifierNode getIdentifier() {
        return identifier;
    }

    public Variable getVariable() {
        return variable;
    }

    @Override
    public List<ASTNode> getChildren() {
        return Collections.singletonList(identifier);
    }

    @Override
    public void init() {
        super.init();
        variable = Variables.getVariable(identifier.evaluate());
        if (variable == null)
            throw new ParseException("The variable '" + identifier.evaluate() + "' does not exist");
    }

    @Override
    public Object evaluate() {
        return variable.getValue();
    }

    @Override
    public Type getReturnType() {
        return variable.getType();
    }

}
