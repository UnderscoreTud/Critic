package me.tud.critic.lexer.token;

public record Token(TokenType type, String value, char previousChar, int line, int pos) {

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Token{");
        sb.append("type=").append(type);
        sb.append(", value='").append(value).append('\'');
        sb.append(", previousChar='").append(previousChar).append('\'');
        sb.append(", pos=").append(line).append(":").append(pos);
        sb.append('}');
        return sb.toString();
    }
}
