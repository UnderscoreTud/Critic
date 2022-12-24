package me.tud.critic.parser;

import me.tud.critic.parser.node.*;
import me.tud.critic.exception.ParseException;
import me.tud.critic.lexer.token.Token;
import me.tud.critic.lexer.token.TokenType;
import me.tud.critic.data.types.Types;
import me.tud.critic.util.Checker;
import me.tud.critic.util.NumberUtils;
import org.w3c.dom.Node;

import java.util.LinkedList;

public class AbstractSyntaxTree {

    private static final Checker<TokenType> numberChecker = tokenType -> tokenType == TokenType.INT || tokenType == TokenType.FLOAT || tokenType == TokenType.DOUBLE;
    private static final Checker<TokenType> comparisonOperatorChecker = tokenType ->
            tokenType == TokenType.EQUAL || tokenType == TokenType.NOT_EQUAL
                    || tokenType == TokenType.LESS_THAN || tokenType == TokenType.LESS_THAN_EQUAL
                    || tokenType == TokenType.GREATER_THAN || tokenType == TokenType.GREATER_THAN_EQUAL;

    private final LinkedList<Token> tokens;
    private Token currentToken;
    private int curlyBracesCount = 0;

    public AbstractSyntaxTree(LinkedList<Token> tokens) {
        this.tokens = tokens;
        this.currentToken = tokens.pop();
    }

    // Grammar:
    // program: statement | statement program
    public ProgramNode program() {
        if (currentToken.type() == TokenType.EOF)
            return null;
        StatementNode node = statement();
        if (currentToken.type() == TokenType.EOF || (currentToken.type() == TokenType.RIGHT_CURLY_BRACE && curlyBracesCount > 0)) {
            if (currentToken.type() == TokenType.RIGHT_CURLY_BRACE) {
                eat(TokenType.RIGHT_CURLY_BRACE);
                curlyBracesCount--;
            }
            return new ProgramNode(node);
        } else {
            return new ProgramNode(node, program());
        }
    }

    // Grammar:
    // statement: control | function_declaration | assignment | function_call | assign_expression
    public StatementNode statement() {
        if (currentToken.type() == TokenType.KEYWORD
                && (currentToken.value().equals("if") || currentToken.value().equals("while")
                || currentToken.value().equals("for"))) {
            return control();
        } else if (currentToken.type() == TokenType.TYPE && peekTokenType() == TokenType.KEYWORD && "function".equals(peekTokenValue())) {
            return functionDeclaration();
        } else if (currentToken.type() == TokenType.TYPE && peekTokenType() == TokenType.IDENTIFIER) {
            AssignmentNode node = assignment();
            eat(TokenType.EOL);
            return node;
        } else if (currentToken.type() == TokenType.EOL) {
            eat(TokenType.EOL);
            return new EmptyNode();
        } else {
            Token token = currentToken;
            ExpressionNode node = expression();
            switch (node.getNodeType()) {
                case FUNCTION_CALL, ASSIGN_EXPRESSION -> {}
                default -> throw new ParseException("Unexpected token: " + token);
            }
            eat(TokenType.EOL);
            return node;
        }
    }

    // Grammar:
    // control: "if" "(" condition ")" block ["else" "if" "(" condition ")" block] ["else" block]
    //          | "while" "(" condition ")" block
    //          | "for" "(" assignment ";" condition ";" expression ")" block
    public ControlNode control() {
        Token token = currentToken;
        if (token.value().equals("if")) {
            eat(TokenType.KEYWORD);
            eat(TokenType.LEFT_PAREN);
            ConditionNode condition = condition();
            eat(TokenType.RIGHT_PAREN);
            BlockNode thenBlock = block();
            if (currentToken.value().equals("else")) {
                eat(TokenType.KEYWORD);

                if (currentToken.type() == TokenType.KEYWORD && "if".equals(currentToken.value()))
                    return new IfStatementNode(condition, thenBlock, (IfStatementNode) control());

                BlockNode elseBlock = block();
                return new IfStatementNode(condition, thenBlock, elseBlock);
            } else {
                return new IfStatementNode(condition, thenBlock);
            }
        } else if (token.value().equals("while")) {
            eat(TokenType.KEYWORD);
            eat(TokenType.LEFT_PAREN);
            ConditionNode condition = condition();
            eat(TokenType.RIGHT_PAREN);
            BlockNode block = block();
            return new WhileStatementNode(condition, block);
        } else {
            eat(TokenType.KEYWORD);
            eat(TokenType.LEFT_PAREN);

            AssignmentNode assignment = currentToken.type() != TokenType.EOL ? assignment() : null;
            eat(TokenType.EOL);
            ConditionNode condition = currentToken.type() != TokenType.EOL ? condition() : null;
            eat(TokenType.EOL);
            ExpressionNode expression = currentToken.type() != TokenType.RIGHT_PAREN ? expression() : null;
            eat(TokenType.RIGHT_PAREN);
            BlockNode block = block();
            return new ForStatementNode(assignment, condition, expression, block);
        }
    }

    // Grammar:
    // condition: expression operator expression | expression (boolean)
    public ConditionNode condition() {
        ExpressionNode left = expression();
        Token operator = currentToken;
        if (comparisonOperatorChecker.check(operator.type())) {
            eat(operator.type());
            ExpressionNode right = expression();
            return new ComparisonNode(left, new OperatorNode(operator.value()), right);
        }
        return new ExprConditionNode(left);
    }

    // Grammar:
    // block: "{" [program] "}"
    public BlockNode block() {
        eat(TokenType.LEFT_CURLY_BRACE);
        if (currentToken.type() == TokenType.RIGHT_CURLY_BRACE) {
            eat(TokenType.RIGHT_CURLY_BRACE);
            return new BlockNode();
        }
        curlyBracesCount++;
        return new BlockNode(program());
    }

    // Grammar:
    // function_declaration: type "function" identifier "(" [parameters] ")" block
    public FunctionDeclarationNode functionDeclaration() {
        TypeNode type = type();
        eat(TokenType.KEYWORD);
        IdentifierNode identifier = identifier();
        eat(TokenType.LEFT_PAREN);
        ParametersNode parameters = parameters();
        eat(TokenType.RIGHT_PAREN);
        BlockNode block = block();
        return new FunctionDeclarationNode(type, identifier, parameters, block);
    }

    // Grammar:
    // parameters: type identifier | type identifier "," parameters
    public ParametersNode parameters() {
        if (currentToken.type() == TokenType.RIGHT_PAREN) {
            return null;
        } else {
            TypeNode type = type();
            IdentifierNode identifier = identifier();
            if (currentToken.type() == TokenType.COMMA) {
                eat(TokenType.COMMA);
                return new ParametersNode(type, identifier, parameters());
            } else {
                return new ParametersNode(type, identifier);
            }
        }
    }

    // Grammar:
    // assignment: type identifier ["=" expression] | identifier "=" expression
    public AssignmentNode assignment() {
        if (currentToken.type() == TokenType.TYPE) {
            TypeNode type = type();
            IdentifierNode identifier = identifier();
            ExpressionNode expression = null;
            if (currentToken.type() == TokenType.ASSIGNMENT) {
                eat(TokenType.ASSIGNMENT);
                expression = expression();
            }
            return new AssignmentNode(type, identifier, expression);
        }
        IdentifierNode identifier = identifier();
        eat(TokenType.ASSIGNMENT);
        ExpressionNode expression = expression();
        return new AssignmentNode(identifier, expression);
    }

    // Grammar:
    // expression: term | expression "+" term | expression "-" term
    public ExpressionNode expression() {
        ExpressionNode term = term();
        while (currentToken.type() == TokenType.PLUS || currentToken.type() == TokenType.MINUS) {
            Token token = currentToken;
            eat(token.type());
            term = new ArithmeticNode(term, new OperatorNode(token.value()), term());
        }
        return term;
    }

    // Grammar:
    // term: factor | term "*" factor | term "/" factor | term "%" factor
    public ExpressionNode term() {
        ExpressionNode factor = factor();
        while (currentToken.type() == TokenType.MULTIPLY || currentToken.type() == TokenType.DIVIDE || currentToken.type() == TokenType.MODULO) {
            Token token = currentToken;
            eat(token.type());
            factor = new ArithmeticNode(factor, new OperatorNode(token.value()), factor());
        }
        return factor;
    }

    // Grammar:
    // factor: "+" factor | "-" factor | "++" factor | "--" factor | factor "++" | factor "--" | "!" factor | "(" expression ")" | number | identifier | function_call
    public ExpressionNode factor() {
        Token token = currentToken;
        ExpressionNode expression;
        if (token.type() == TokenType.PLUS) {
            eat(TokenType.PLUS);
            expression = new PositiveNode(factor());
        } else if (token.type() == TokenType.MINUS) {
            eat(TokenType.MINUS);
            expression = new NegativeNode(factor());
        }  else if (token.type() == TokenType.INCREMENT) {
            eat(TokenType.INCREMENT);
            expression = new IncrementNode(factor(), false);
        }  else if (token.type() == TokenType.DECREMENT) {
            eat(TokenType.DECREMENT);
            expression = new DecrementNode(factor(), false);
        } else if (token.type() == TokenType.EXCLAMATION) {
            eat(TokenType.EXCLAMATION);
            expression = new NegateNode(factor());
        } else if (token.type() == TokenType.LEFT_PAREN) {
            eat(TokenType.LEFT_PAREN);
            ParenthesizedExpressionNode node = new ParenthesizedExpressionNode(expression());
            eat(TokenType.RIGHT_PAREN);
            expression = node;
        } else if (numberChecker.check(token.type())) {
            eatNumber();
            expression = new LiteralNumberNode(switch (token.type()) {
                case INT -> NumberUtils.parseInt(token.value());
                case FLOAT -> Float.parseFloat(token.value());
                case DOUBLE -> Double.parseDouble(token.value());
                default -> null;
            });
        } else if (token.type() == TokenType.STRING) {
            eat(TokenType.STRING);
            expression = new LiteralStringNode(token.value());
        } else if (token.type() == TokenType.CHARACTER) {
            eat(TokenType.CHARACTER);
            expression = new LiteralCharacterNode(token.value().charAt(0));
        } else if (token.type() == TokenType.BOOLEAN) {
            eat(TokenType.BOOLEAN);
            expression = new LiteralBooleanNode(Boolean.parseBoolean(token.value()));
        } else if (token.type() == TokenType.IDENTIFIER) {
            if (peekTokenType() == TokenType.LEFT_PAREN) {
                expression = functionCall();
            } else {
                expression = new VariableNode(identifier());
            }
        } else {
            throw new ParseException("Unexpected token: " + token);
        }
        if (currentToken.type() == TokenType.INCREMENT) {
            eat(TokenType.INCREMENT);
            return new IncrementNode(expression, true);
        }
        if (currentToken.type() == TokenType.DECREMENT) {
            eat(TokenType.DECREMENT);
            return new DecrementNode(expression, true);
        }
        return expression;
    }

    // Grammar:
    // function_call: identifier "(" [arguments] ")"
    public FunctionCallNode functionCall() {
        IdentifierNode identifier = identifier();
        eat(TokenType.LEFT_PAREN);
        ArgumentsNode arguments = arguments();
        eat(TokenType.RIGHT_PAREN);
        return new FunctionCallNode(identifier, arguments);
    }

    // Grammar:
    // arguments: expression | expression "," arguments
    public ArgumentsNode arguments() {
        if (currentToken.type() == TokenType.RIGHT_PAREN) {
            return null;
        } else {
            ExpressionNode expression = expression();
            if (currentToken.type() == TokenType.COMMA) {
                eat(TokenType.COMMA);
                return new ArgumentsNode(expression, arguments());
            } else {
                return new ArgumentsNode(expression);
            }
        }
    }

    // Helper methods

    private TokenType peekTokenType() {
        Token token = tokens.peek();
        return token == null ? null : token.type();
    }

    private String peekTokenValue() {
        Token token = tokens.peek();
        return token == null ? null : token.value();
    }

    private TypeNode type() {
        Token token = currentToken;
        eat(TokenType.TYPE);
        return new TypeNode(Types.lookupType(token.value()));
    }

    private IdentifierNode identifier() {
        Token token = currentToken;
        eat(TokenType.IDENTIFIER);
        return new IdentifierNode(token.value());
    }

    private void eat(TokenType tokenType) {
        if (currentToken.type() == tokenType) {
            currentToken = tokens.pop();
        } else {
            throw new ParseException("Expected token type " + tokenType + ", got " + currentToken);
        }
    }

    private void eatNumber() {
        if (numberChecker.check(currentToken.type())) {
            currentToken = tokens.pop();
        } else {
            throw new ParseException("Expected token type NUMBER, got " + currentToken);
        }
    }

}
