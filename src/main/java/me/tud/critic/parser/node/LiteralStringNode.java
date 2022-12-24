package me.tud.critic.parser.node;

import me.tud.critic.data.Expression;
import me.tud.critic.data.types.Type;
import me.tud.critic.data.types.Types;
import me.tud.critic.data.variables.Variable;
import me.tud.critic.data.variables.Variables;
import me.tud.critic.exception.ParseException;
import me.tud.critic.lexer.LexicalAnalyser;
import me.tud.critic.parser.AbstractSyntaxTree;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LiteralStringNode extends LiteralNode {

    private static final Pattern ESCAPE_PATTERN = Pattern.compile("\\\\(.)");
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("(?i)(?<!\\\\)\\$\\{([a-z_\\$][a-z\\d_\\$]*)\\}");
    private final String rawString;

    public LiteralStringNode(String string) {
        super(ASTNodeType.LITERAL_STRING, escapeBackslashes(string));
        this.rawString = string;
    }

    public String getString() {
        return (String) getValue();
    }

    public String getRawString() {
        return rawString;
    }

    @Override
    public Object evaluate() {
        return escapeBackslashes(VARIABLE_PATTERN.matcher(rawString).replaceAll(matchResult -> {
//            String firstChar = matchResult.group().charAt(0) + "";
            String identifier = matchResult.group(1);
            Variable variable = Variables.getVariable(identifier);
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
