package me.tud.critic;

import me.tud.critic.lexer.LexicalAnalyser;
import me.tud.critic.parser.AbstractSyntaxTree;
import me.tud.critic.parser.node.ASTNode;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

public class Script {

    @NotNull
    private final String fileName;
    private final String data;
    private LexicalAnalyser lexer;

    public Script(@NotNull String fileName, InputStream inputStream) throws IOException {
        this.fileName = fileName;
        this.data = new String(inputStream.readAllBytes());
    }

    public boolean init() {
        this.lexer = new LexicalAnalyser(data);
        AbstractSyntaxTree ast = new AbstractSyntaxTree(lexer.lex());
        ASTNode rootNode = ast.program();
        rootNode.init();
        rootNode.evaluate();
//        rootNode.visualize();
        return true;
    }

    public @NotNull String getFileName() {
        return fileName;
    }

    public LexicalAnalyser getLexer() {
        return lexer;
    }

    public String getData() {
        return lexer.getData();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Script script))
            return false;

        return fileName.equals(script.fileName);
    }

    @Override
    public int hashCode() {
        return fileName.hashCode();
    }
}
