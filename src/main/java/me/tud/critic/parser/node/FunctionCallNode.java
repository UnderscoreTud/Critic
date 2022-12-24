package me.tud.critic.parser.node;

import me.tud.critic.data.Expression;
import me.tud.critic.data.types.Type;
import me.tud.critic.util.CollectionUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FunctionCallNode extends ExpressionNode {

    private final IdentifierNode identifier;
    @Nullable
    private final ArgumentsNode arguments;

    public FunctionCallNode(IdentifierNode identifier, @Nullable ArgumentsNode arguments) {
        super(ASTNodeType.FUNCTION_CALL);
        this.identifier = identifier;
        this.arguments = arguments;
    }

    public IdentifierNode getIdentifier() {
        return identifier;
    }

    public @Nullable ArgumentsNode getArguments() {
        return arguments;
    }

    @Override
    public List<ASTNode> getChildren() {
        return CollectionUtils.listOfNonNullables(identifier, arguments);
    }

    @Override
    public Object evaluate() {
        if (identifier.evaluate().equals("print")) {
            if (arguments != null)
                for (ExpressionNode expression : arguments.getAllArguments()) {
                    System.out.println(expression.evaluate());
                }
            else
                System.out.println();
        }
        return null;
    }

    @Override
    public Type getReturnType() {
        return null;
    }

}
