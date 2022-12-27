package me.tud.critic.parser.node;

import com.google.common.base.Preconditions;
import me.tud.critic.data.variables.Variable;
import me.tud.critic.data.variables.VariablesMap;
import me.tud.critic.exception.ParseException;
import me.tud.critic.util.CollectionUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AssignmentNode extends StatementNode {

    @Nullable
    private final TypeNode type;
    private final IdentifierNode identifier;
    @Nullable
    private final ExpressionNode expression;
    private final VariablesMap variablesMap;
    private Variable variable;

    public AssignmentNode(IdentifierNode identifier, @NotNull ExpressionNode expression, VariablesMap variablesMap) {
        this(null, identifier, expression, variablesMap);
    }

    public AssignmentNode(@NotNull TypeNode type, IdentifierNode identifier, VariablesMap variablesMap) {
        this(type, identifier, null, variablesMap);
    }

    @Contract("null, _, null, _ -> fail")
    public AssignmentNode(@Nullable TypeNode type, IdentifierNode identifier, @Nullable ExpressionNode expression, VariablesMap variablesMap) {
        super(ASTNodeType.ASSIGNMENT);
        Preconditions.checkArgument(type != null || expression != null, "TypeNode and ExpressionNode cannot be null at the same time");
        this.type = type;
        this.identifier = identifier;
        this.expression = expression;
        this.variablesMap = variablesMap;
    }

    public @Nullable TypeNode getType() {
        return type;
    }

    public IdentifierNode getIdentifier() {
        return identifier;
    }

    public @Nullable ExpressionNode getExpression() {
        return expression;
    }

    @Override
    public List<ASTNode> getChildren() {
        return CollectionUtils.listOfNonNullables(type, identifier, expression);
    }

    private boolean isDeclaration() {
        return type != null;
    }

    @Override
    public void init() {
        super.init();
        if (isDeclaration()) {
            variable = variablesMap.declareVariable(type.evaluate(), identifier.evaluate());
        } else {
            variable = variablesMap.getVariable(identifier.evaluate());
            if (variable == null)
                throw new ParseException("The variable '" + identifier.evaluate() + "' does not exist");
        }
        if (expression != null) {
            if (variable.getType() != expression.getReturnType())
                throw new ParseException("Expected type '" + variable.getType().getName() + "' but got '" + expression.getReturnType().getName() + '\'');
        }
    }

    @Override
    public Object evaluate() {
        if (expression != null)
            variable.setValue(expression.evaluateAsExpression());
        return null;
    }

}
