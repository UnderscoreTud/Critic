package me.tud.critic.parser.node;

import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class BlockNode extends ASTNode {

    @Nullable
    private final ProgramNode program;

    public BlockNode() {
        this(null);
    }

    public BlockNode(@Nullable ProgramNode program) {
        super(ASTNodeType.BLOCK);
        this.program = program;
    }

    public @Nullable ProgramNode getProgram() {
        return program;
    }

    @Override
    public List<ASTNode> getChildren() {
        return Collections.singletonList(program == null ? new EmptyNode() : program);
    }

    @Override
    public Object evaluate() {
        if (program != null)
            return program.evaluate();
        return null;
    }

}
