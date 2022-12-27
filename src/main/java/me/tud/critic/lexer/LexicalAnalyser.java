package me.tud.critic.lexer;

import me.tud.critic.exception.ParseException;
import me.tud.critic.lexer.token.Token;
import me.tud.critic.lexer.token.TokenType;

import java.util.LinkedList;
import java.util.function.BiPredicate;

/**
 * This class is responsible for performing lexical analysis on a given string of value.
 * It returns a list of {@link Token} objects, each representing a single element in the input value.
 */
public class LexicalAnalyser {

    /**
     * A list of primitive types that are recognized by this lexer.
     */
    public static final String[] PRIMITIVE_TYPES = {
            "int", "float", "double", "char", "string", "boolean", "void"
    };


    /**
     * A list of keywords that are recognized by this lexer.
     */
    private static final String[] KEYWORDS = {
            "if", "else", "for", "while", "continue", "break", "function"
    };

    /**
     * The current position in the input value.
     */
    private int pos = 0;

    /**
     * A cached position in the input value, used to backtrack when necessary.
     */
    private int cachedPos = 0;

    private char previousChar;

    /**
     * The current line number in the input value.
     */
    private int line = 1;

    /**
     * An array storing the position of each line break in the input value.
     */
    private final int[] lines;

    /**
     * The length of the current line in the input value.
     */
    private int cachedLineLength = 0;

    /**
     * The input value to be lexed.
     */
    private final String data;

    /**
     * Constructs a new {@link LexicalAnalyser} object for the given input value.
     *
     * @param data the input value to be lexed
     */
    public LexicalAnalyser(String data) {
        this.data = data;
        this.lines = new int[data.length() - data.replace("\n", "").length() + 1];
    }

    /**
     * Performs lexical analysis on the input value and returns a list of {@link Token} objects.
     *
     * @return a list of {@link Token} objects representing the input value
     */
    public LinkedList<Token> lex() {
        LinkedList<Token> tokens = new LinkedList<>();
        Token token;
        do {
            token = nextToken();
            if (token.type() == TokenType.COMMENT)
                continue;
            tokens.add(token);
        } while (token.type() != TokenType.EOF);
        return tokens;
    }

    /**
     * Retrieves the next {@link Token} from the input value.
     *
     * @return the next {@link Token} from the input value
     */
    public Token nextToken() {
        if (!hasNext())
            return new Token(TokenType.EOF, "", lookBack(), line, lines[line - 1] + 1);

        char c = step();

        // Skip over any leading whitespace
        while (Character.isWhitespace(c)) {
            if (!hasNext())
                return new Token(TokenType.EOF, "", lookBack(), line, lines[line - 1] + 1);
            c = step();
        }

        cachePos();
        previousChar = lookBack();

        switch (c) {
            case '#':
                return handleComment();
            case ';':
                return newToken(TokenType.EOL, ";");
            case '"':
            case '\'':
                return handleQuotedLiteral(c);
            case '{':
                return newToken(TokenType.LEFT_CURLY_BRACE, "{");
            case '}':
                return newToken(TokenType.RIGHT_CURLY_BRACE, "}");
            case '(':
                return newToken(TokenType.LEFT_PAREN, "(");
            case ')':
                return newToken(TokenType.RIGHT_PAREN, ")");
            case '[':
                return newToken(TokenType.LEFT_BRACE, "[");
            case ']':
                return newToken(TokenType.RIGHT_BRACE, "]");
            case ',':
                return newToken(TokenType.COMMA, ",");
            case '?':
                return newToken(TokenType.QUESTION_MARK, "?");
            case ':':
                return newToken(TokenType.COLON, ":");
            case '+':
            case '-':
                TokenType tokenType = c == '+' ? TokenType.PLUS : TokenType.MINUS;
                String value = c + "";
                if (peek() == c) {
                    tokenType = tokenType == TokenType.PLUS ? TokenType.INCREMENT : TokenType.DECREMENT;
                    value = c + (step() + "");
                } else if (peek() == '=') {
                    tokenType = tokenType == TokenType.PLUS ? TokenType.PLUS_EQUAL : TokenType.MINUS_EQUAL;
                    value = c + (step() + "");
                }
                return newToken(tokenType, value);
            case '*':
                return newToken(TokenType.MULTIPLY, "*");
            case '/':
                return newToken(TokenType.DIVIDE, "/");
            case '%':
                return newToken(TokenType.MODULO, "%");
            case '=':
                if (peek() == '=') {
                    step();
                    return newToken(TokenType.COMPARISON_OPERATOR, "==");
                }
                return newToken(TokenType.ASSIGNMENT, "=");
            case '!':
                if (peek() == '=') {
                    step();
                    return newToken(TokenType.COMPARISON_OPERATOR, "!=");
                }
                return newToken(TokenType.EXCLAMATION, "!");
            case '<':
                if (peek() == '=') {
                    step();
                    return newToken(TokenType.COMPARISON_OPERATOR, "<=");
                }
                return newToken(TokenType.COMPARISON_OPERATOR, "<");
            case '>':
                if (peek() == '=') {
                    step();
                    return newToken(TokenType.COMPARISON_OPERATOR, ">=");
                }
                return newToken(TokenType.COMPARISON_OPERATOR, ">");
        }

        if (Character.isDigit(c) || c == '.')
            return handleNumber();

        Token token = handleBoolean();

        if (token == null) {
            revertToCachedPos();
            token = handleKeyword();
        }

        if (token == null) {
            revertToCachedPos();
            token = handlePrimitiveTypes();
        }

        if (token == null) {
            revertToCachedPos();
            token = handleIdentifier();
        }

        if (token != null)
            return token;

        revertToCachedPos();
        return (Token) parseException(pos);
    }

    /**
     * Handles the parsing of a number in the input value.
     *
     * @return a {@link Token} representing the number
     */
    private Token handleNumber() {
        char c = current();
        StringBuilder stringBuilder = new StringBuilder();
        TokenType tokenType = TokenType.INT;
        byte radix = 10;
        boolean usedDecimal = false;
        boolean usedExponents = false;

        // Determine base of number based on prefix
        if (c == '0') {
            radix = switch (peek()) {
                case 'b' -> 2;
                case '0' -> 8;
                case 'x' -> 16;
                default -> 10;
            };
            if (radix != 10) {
                stringBuilder.append(c).append(step());
                c = step();
            }
        }

        loop: while (true) {
            if (c != '_')
                stringBuilder.append(c);

            switch (Character.toLowerCase(c)) {
                case '_':
                    break;
                case '.':
                    if (radix != 10 || usedDecimal) {
                        parseException();
                    }
                    else {
                        usedDecimal = true;
                    }
                    tokenType = TokenType.DOUBLE;
                    break;
                case 'e':
                    if (radix == 10) {
                        if (usedDecimal || usedExponents) {
                            parseException();
                        }
                        else {
                            c = peek();
                            if (c == '+' || c == '-') {
                                c = current();
                                stringBuilder.append(step());
                            }

                            if (!isDigit(c, radix))
                                parseException();

                            usedExponents = true;
                            tokenType = TokenType.DOUBLE;
                        }
                        break;
                    }
                    // $FALL THROUGH$
                case 'f':
                case 'd':
                    if (radix == 10) {
                        if (lookBack() == '_')
                            parseException(pos - 1);
                        tokenType = c == 'f' ? TokenType.FLOAT : TokenType.DOUBLE;
                        break loop;
                    }
                    // $FALL THROUGH$
                default:
                    if (!isDigit(c, radix)) {
                        if (radix == 2 && isDigit(c, 10))
                            parseException("Integer too large.", pos);
                        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                        stepBack();
                        break loop;
                    }
                    break;
            }

            if (pos >= data.length()) {
                if (c == '_')
                    parseException();
                break;
            }
            c = step();
        }

        String number = stringBuilder.toString();

        if (number.toLowerCase().endsWith("f"))
            tokenType = TokenType.FLOAT;
        else if (number.toLowerCase().endsWith("d"))
            tokenType = TokenType.DOUBLE;

        return newToken(tokenType, number);
    }

    private boolean isDigit(char c, int radix) {
        return Character.digit(c, radix) >= 0;
    }

    /**
     * Handles the parsing of a quoted literal in the input value.
     *
     * @return a {@link Token} representing the quoted literal
     */
    private Token handleQuotedLiteral(char quote) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean escaped = false;
        boolean comment = false;
        while (hasNext()) {
            char c = step();

            if (c == '#') {
                if (!comment && peek() == '#') {
                    stringBuilder.append('#');
                    step();
                } else {
                    comment = !comment;
                }
            }
            if (comment)
                continue;

            if (c == '\\' && !escaped) {
                escaped = true;
                continue;
            }
            if (c == quote && !escaped) {
                return newToken(quote == '"' ? TokenType.STRING : TokenType.CHARACTER, stringBuilder.toString());
            }

            if (quote == '\'' && stringBuilder.length() > 0)
                parseException("Invalid character literal", pos);

            escaped = false;
            if (c != '#')
                stringBuilder.append(c);
        }
        return (Token) parseException("Unterminated quoted literal", pos);
    }
    /**
     * Handles the parsing of a boolean value in the input value.
     *
     * @return a {@link Token} representing the boolean value, or null if the current input does not represent a boolean value
     */
    private Token handleBoolean() {
        String data = readDelimiter("true", "false");
        if (data == null)
            return null;
        return newToken(TokenType.BOOLEAN, data);
    }

    /**
     * Handles the parsing of a keyword in the input value.
     *
     * @return a {@link Token} representing the keyword, or null if the current input does not represent a keyword
     */
    private Token handleKeyword() {
        String data = readDelimiter(KEYWORDS);
        if (data == null)
            return null;
        return newToken(TokenType.KEYWORD, data);
    }

    /**
     * Handles the parsing of a primitive type in the input value.
     *
     * @return a {@link Token} representing the primitive type, or null if the current input does not represent a primitive type
     */
    private Token handlePrimitiveTypes() {
        String data = readDelimiter(PRIMITIVE_TYPES);
        if (data == null)
            return null;
        return newToken(TokenType.TYPE, data);
    }

    /**
     * Handles the parsing of an identifier in the input value.
     *
     * @return a {@link Token} representing the identifier
     */
    private Token handleIdentifier() {
        String data = readDelimiter((c, pos) -> {
            if (Character.isAlphabetic(c) || c == '$' || c == '_')
                return true;
            return pos != 0 && Character.isDigit(c);
        });
        if (data == null)
            return null;
        return newToken(TokenType.IDENTIFIER, data);
    }

    private String readDelimiter(String... delimiters) {
        return readDelimiter((c, pos) -> Character.isAlphabetic(c), delimiters);
    }

    private String readDelimiter(BiPredicate<Character, Integer> predicate, String... delimiters) {
        char c = current();
        if (!predicate.test(c, 0))
            return null;
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(c));
        c = peek();
        while (hasNext() && predicate.test(c, (pos - cachedPos) + 1)) {
            stringBuilder.append(step());
            c = peek();
        }
        String data = stringBuilder.toString();

        if (delimiters.length == 0)
            return data;

        for (String delimiter : delimiters) {
            if (data.equals(delimiter))
                return data;
        }

        return null;
    }

    private Token handleComment() {
        if (hasNext()) {
            char current = step();
            while (current != '\n' && current != '#' && hasNext())
                current = step();
        }
        return newToken(TokenType.COMMENT, "");
    }

    private char stepBack() {
        char c = data.charAt(--pos);
        if (c == '\n')
            line--;
        return c;
    }

    private char lookBack() {
        if (pos < 2)
            return '\0';
        return data.charAt(pos - 2);
    }

    /**
     * Returns the current character in the input value without incrementing the position.
     *
     * @return the current character in the input value
     */
    private char current() {
        return data.charAt(pos - 1);
    }

    /**
     * Returns the current character in the input value and increments the position.
     *
     * @return the current character in the input value
     */
    private char step() {
        char c = data.charAt(pos++);
        lines[line - 1]++;
        if (c == '\n') {
            line++;
        }
        return c;
    }

    /**
     * Returns the character after the current one in the input value without incrementing the position.
     *
     * @return the character after the current one in the input value
     */
    private char peek() {
        if (pos >= data.length())
            return '\0';
        return data.charAt(pos);
    }

    /**
     * Determines whether there is more input value to be processed.
     *
     * @return true if there is more input value to be processed, false otherwise
     */
    private boolean hasNext() {
        return pos < data.length();
    }

    /**
     * Caches the current position in the input value.
     */
    private void cachePos() {
        cachedPos = pos;
        cachedLineLength = lines[line - 1];
    }

    /**
     * Reverts the position in the input value back to the previously cached position.
     */
    private void revertToCachedPos() {
        pos = cachedPos;
        lines[line - 1] = cachedLineLength;
    }

    /**
     * Creates a new {@link Token} with the given type and value.
     *
     * @param type the type of the token
     * @param value the value of the token
     * @return a new token with the given type and value
     */
    private Token newToken(TokenType type, String value) {
        return new Token(type, value, previousChar, line, cachedLineLength);
    }

    public String getData() {
        return data;
    }

    private Object parseException() throws ParseException {
        return parseException(cachedLineLength);
    }

    /**
     * Returns a {@link ParseException} for the given position in the input value.
     *
     * @param pos the position in the input value where the parse exception occurred
     * @return a parse exception for the given position
     */
    private Object parseException(int pos) throws ParseException {
        return parseException("Unrecognized character '" + data.charAt(pos - 1) + "'.", pos);
    }

    private Object parseException(String error, int pos) throws ParseException {
        throw new ParseException(error + " " + line + ":" + pos);
    }

}
