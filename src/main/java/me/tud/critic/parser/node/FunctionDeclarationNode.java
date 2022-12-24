package me.tud.critic.parser.node;

import me.tud.critic.util.CollectionUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FunctionDeclarationNode extends StatementNode {

    private final TypeNode type;
    private final IdentifierNode identifierNode;
    @Nullable
    private final ParametersNode parameters;
    private final BlockNode block;

    public FunctionDeclarationNode(TypeNode type, IdentifierNode identifierNode, @Nullable ParametersNode parameters, BlockNode block) {
        super(ASTNodeType.FUNCTION_DECLARATION);
        this.type = type;
        this.identifierNode = identifierNode;
        this.parameters = parameters;
        this.block = block;
    }

    public TypeNode getType() {
        return type;
    }

    public IdentifierNode getIdentifierNode() {
        return identifierNode;
    }

    public @Nullable ParametersNode getParameters() {
        return parameters;
    }

    public BlockNode getBlock() {
        return block;
    }

    @Override
    public List<ASTNode> getChildren() {
        return CollectionUtils.listOfNonNullables(type, identifierNode, parameters, block);
    }

    @Override
    public Object evaluate() {
        // TODO: IMPLEMENT
        return null;
    }

}
