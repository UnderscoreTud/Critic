package me.tud.critic.parser.node;

import me.tud.critic.data.types.Type;
import me.tud.critic.data.variables.Variable;
import me.tud.critic.data.variables.VariablesMap;
import me.tud.critic.exception.ParseException;

import java.util.Collections;
import java.util.List;

public class VariableNode extends ExpressionNode {

    private final IdentifierNode identifier;
    private final VariablesMap variablesMap;
    private Variable variable;

    public VariableNode(IdentifierNode identifier, VariablesMap variablesMap) {
        super(ASTNodeType.VARIABLE);
        this.identifier = identifier;
        this.variablesMap = variablesMap;
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
        variable = variablesMap.getVariable(identifier.evaluate());
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
