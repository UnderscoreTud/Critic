package me.tud.critic.parser.node;

import me.tud.critic.data.types.Type;
import me.tud.critic.data.types.Types;
import me.tud.critic.exception.ParseException;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TernaryOperator extends ExpressionNode {

    private final ExpressionNode condition;
    private final ExpressionNode thenExpression, elseExpression;
    @Nullable
    private final Type returnType;

    public TernaryOperator(ExpressionNode condition, ExpressionNode thenExpression, ExpressionNode elseExpression, @Nullable Type returnType) {
        super(ASTNodeType.TERNARY_OPERATOR);
        this.condition = condition;
        this.thenExpression = thenExpression;
        this.elseExpression = elseExpression;
        this.returnType = returnType;
    }

    @Override
    public List<ASTNode> getChildren() {
        return List.of(condition, thenExpression, elseExpression);
    }

    @Override
    public void init() {
        condition.init();
        thenExpression.init();
        elseExpression.init();
        if (!condition.getReturnType().is("boolean"))
            throw new ParseException("Expected type 'boolean', got: '" + condition.getReturnType() + '\'');
        if (returnType != null) {
            if (!returnType.is(thenExpression.getReturnType()))
                throw new ParseException("Expected type '" + returnType + "', got: '" + thenExpression.getReturnType() + '\'');
            if (!returnType.is(elseExpression.getReturnType()))
                throw new ParseException("Expected type '" + returnType + "', got: '" + elseExpression.getReturnType() + '\'');
        }
    }

    @Override
    public Object evaluate() {
        if ((Boolean) condition.evaluate()) {
            return thenExpression.evaluate();
        } else {
            return elseExpression.evaluate();
        }
    }

    @Override
    public Type getReturnType() {
        return returnType == null ? Types.lookupType("Object") : returnType;
    }

}
