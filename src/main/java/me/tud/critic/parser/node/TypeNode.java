package me.tud.critic.parser.node;

import hu.webarticum.treeprinter.text.ConsoleText;
import me.tud.critic.data.types.Type;

public class TypeNode extends ASTNode {

    private final Type type;

    public TypeNode(Type type) {
        super(ASTNodeType.TYPE);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public Type evaluate() {
        return type;
    }

    @Override
    public ConsoleText content() {
        return ConsoleText.of("TYPE: '" + type.getName() + '\'');
    }

}
