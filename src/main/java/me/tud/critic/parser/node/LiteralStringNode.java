package me.tud.critic.parser.node;

import me.tud.critic.data.types.Type;
import me.tud.critic.data.types.Types;
import me.tud.critic.data.variables.Variable;
import me.tud.critic.data.variables.VariablesMap;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LiteralStringNode extends LiteralNode {

    private static final Pattern ESCAPE_PATTERN = Pattern.compile("\\\\(.)");
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("(?i)(?<!\\\\)\\$\\{([a-z_\\$][a-z\\d_\\$]*)\\}");
    private final String rawString;
    @Nullable
    private final VariablesMap variablesMap;

    public LiteralStringNode(String string) {
        this(string, null);
    }

    public LiteralStringNode(String string, @Nullable VariablesMap variablesMap) {
        super(ASTNodeType.LITERAL_STRING, escapeBackslashes(string));
        this.rawString = string;
        this.variablesMap = variablesMap;
    }

    public String getString() {
        return (String) getValue();
    }

    public String getRawString() {
        return rawString;
    }

    @Override
    public Object evaluate() {
        if (variablesMap == null)
            return getString();
        return escapeBackslashes(VARIABLE_PATTERN.matcher(rawString).replaceAll(matchResult -> {
            String identifier = matchResult.group(1);
            Variable variable = variablesMap.getVariable(identifier);
            if (variable == null)
                return "undefined";
            return variable.getValue() + "";
        }));
    }

    @Override
    public Type getReturnType() {
        return Types.getPrimitiveType("string");
    }

    private static String escapeBackslashes(String string) {
        return ESCAPE_PATTERN.matcher(string).replaceAll(matchResult -> Matcher.quoteReplacement(matchResult.group(1)));
    }

}
