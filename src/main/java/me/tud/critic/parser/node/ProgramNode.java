package me.tud.critic.parser.node;

import me.tud.critic.util.CollectionUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ProgramNode extends ASTNode {

    private final StatementNode statement;
    @Nullable
    private final ProgramNode program;

    public ProgramNode(StatementNode statement) {
        this(statement, null);
    }

    public ProgramNode(StatementNode statement, @Nullable ProgramNode program) {
        super(ASTNodeType.PROGRAM);
        this.statement = statement;
        this.program = program;
    }

    public StatementNode getStatement() {
        return statement;
    }

    public @Nullable ProgramNode getProgram() {
        return program;
    }

    @Override
    public List<ASTNode> getChildren() {
        return CollectionUtils.listOfNonNullables(statement, program);
    }

    @Override
    public Object evaluate() {
        statement.evaluate();
        if (program != null)
            program.evaluate();
        return null;
    }

}
