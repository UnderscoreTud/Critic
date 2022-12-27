package me.tud.critic.parser.node;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class BlockNode extends ASTNode {

    @Nullable
    private final ProgramNode program;
    @Nullable
    private final StatementNode statement;

    public BlockNode() {
        this(null, null);
    }

    public BlockNode(@Nullable ProgramNode program) {
        this(program, null);
    }

    public BlockNode(@Nullable StatementNode statement) {
        this(null, statement);
    }

    @Contract("!null, !null -> fail")
    public BlockNode(@Nullable ProgramNode program, @Nullable StatementNode statement) {
        super(ASTNodeType.BLOCK);
        Preconditions.checkArgument(program != null ^ statement != null, "program and statement cannot be both not null");
        this.program = program;
        this.statement = statement;
    }

    public @Nullable ProgramNode getProgram() {
        return program;
    }

    @Override
    public List<ASTNode> getChildren() {
        return Collections.singletonList(program == null ? statement == null ? new EmptyNode() : statement : program);
    }

    @Override
    public Object evaluate() {
        if (program != null)
            return program.evaluate();
        else if (statement != null)
            return statement.evaluate();
        return null;
    }

}
