package me.tud.critic.parser.node;

import me.tud.critic.util.CollectionUtils;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ArgumentsNode extends ASTNode {

    private final ExpressionNode expression;
    @Nullable
    private final ArgumentsNode arguments;

    public ArgumentsNode(ExpressionNode expression) {
        this(expression, null);
    }

    public ArgumentsNode(ExpressionNode expression, @Nullable ArgumentsNode arguments) {
        super(ASTNodeType.PARAMETERS);
        this.expression = expression;
        this.arguments = arguments;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    public @Nullable ArgumentsNode getArguments() {
        return arguments;
    }

    public List<ExpressionNode> getAllArguments() {
        List<ExpressionNode> expressionNodes = new ArrayList<>();
        expressionNodes.add(expression);
        ArgumentsNode argumentsNode = getArguments();
        while (argumentsNode != null) {
            expressionNodes.add(argumentsNode.expression);
            argumentsNode = argumentsNode.getArguments();
        }
        return expressionNodes;
    }

    @Override
    public List<ASTNode> getChildren() {
        return CollectionUtils.listOfNonNullables(expression, arguments);
    }

    @Override
    public Object evaluate() {
        // TODO: IMPLEMENT
        return null;
    }

}
