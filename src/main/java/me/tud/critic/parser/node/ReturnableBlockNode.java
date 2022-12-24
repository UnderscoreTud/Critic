package me.tud.critic.parser.node;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ReturnableBlockNode extends BlockNode {

    private final TypeNode returnType;

    public ReturnableBlockNode(TypeNode returnType, ProgramNode program) {
        super(program);
        this.returnType = returnType;
    }

    public TypeNode getReturnType() {
        return returnType;
    }

    @Override
    public @NotNull ProgramNode getProgram() {
        assert super.getProgram() != null;
        return super.getProgram();
    }

    @Override
    public List<ASTNode> getChildren() {
        return List.of(returnType, getProgram());
    }

    @Override
    public Object evaluate() {
        // TODO: IMPLEMENT
        return null;
    }

}
