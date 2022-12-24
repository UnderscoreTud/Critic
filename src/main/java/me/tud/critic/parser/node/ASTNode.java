package me.tud.critic.parser.node;

import hu.webarticum.treeprinter.Insets;
import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.decorator.BorderTreeNodeDecorator;
import hu.webarticum.treeprinter.decorator.PadTreeNodeDecorator;
import hu.webarticum.treeprinter.decorator.ShadowTreeNodeDecorator;
import hu.webarticum.treeprinter.printer.traditional.TraditionalTreePrinter;
import hu.webarticum.treeprinter.text.AnsiFormat;
import hu.webarticum.treeprinter.text.ConsoleText;
import me.tud.critic.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class ASTNode implements TreeNode {

    private final ASTNodeType nodeType;
    private final Object value;

    public ASTNode(ASTNodeType nodeType) {
        this(nodeType, null);
    }

    public ASTNode(ASTNodeType nodeType, Object value) {
        this.nodeType = nodeType;
        this.value = value;
//        System.out.println(this);
    }

    public ASTNodeType getNodeType() {
        return nodeType;
    }

    public Object getValue() {
        return value;
    }

    public List<ASTNode> getChildren() {
        return List.of();
    }

    public void visualize() {
        new TraditionalTreePrinter().print(new BorderTreeNodeDecorator(new PadTreeNodeDecorator(this, new Insets(0, 2))));
    }

    public void init() {
        for (ASTNode child : getChildren())
            child.init();
    }

    public abstract Object evaluate();

    @Override
    public ConsoleText content() {
        return ConsoleText.of(nodeType + (value != null ? ": " + value : ""));
    }

    @Override
    public List<TreeNode> children() {
        return new ArrayList<>(getChildren());
    }

    @Override
    public String toString() {
        return "ASTNode{nodeType=" + nodeType + '}';
    }

    public enum ASTNodeType {
        ARGUMENTS,
        ASSIGNMENT,
        BLOCK,
        CONDITION,
        FUNCTION_DECLARATION,
        EMPTY,
        EXPRESSION,
        FACTOR,
        FOR,
        FUNCTION_CALL,
        IDENTIFIER,
        IF,
        IF_ELSE,
        ELSE_IF,
        LITERAL_CHARACTER,
        LITERAL_STRING,
        NEGATE,
        NEGATIVE,
        NUMBER,
        OPERATOR,
        PARAMETERS,
        POSITIVE,
        PROGRAM,
        RETURN,
        TERM,
        TYPE,
        VARIABLE,
        LITERAL_BOOLEAN,
        ASSIGN_EXPRESSION,
        WHILE

    }

}
