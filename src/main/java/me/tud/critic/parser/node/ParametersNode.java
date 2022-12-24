package me.tud.critic.parser.node;

import me.tud.critic.util.CollectionUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ParametersNode extends ASTNode {

    private final TypeNode type;
    private final IdentifierNode identifier;
    @Nullable
    private final ParametersNode parameters;

    public ParametersNode(TypeNode type, IdentifierNode identifier) {
        this(type, identifier, null);
    }

    public ParametersNode(TypeNode type, IdentifierNode identifier, @Nullable ParametersNode parameters) {
        super(ASTNodeType.PARAMETERS);
        this.type = type;
        this.identifier = identifier;
        this.parameters = parameters;
    }

    public TypeNode getType() {
        return type;
    }

    public IdentifierNode getIdentifier() {
        return identifier;
    }

    public @Nullable ParametersNode getParameters() {
        return parameters;
    }

    @Override
    public List<ASTNode> getChildren() {
        return CollectionUtils.listOfNonNullables(type, identifier, parameters);
    }

    @Override
    public Object evaluate() {
        // TODO: IMPLEMENT
        return null;
    }

}
