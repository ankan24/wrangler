package io.cdap.wrangler.api.parser;

import java.util.ArrayList;
import java.util.List;

public class TokenGroup {
    private final List<Token> tokens;

    public TokenGroup() {
        this.tokens = new ArrayList<>();
    }

    public void add(Token token) {
        tokens.add(token);
    }

    public List<Token> getTokens() {
        return tokens;
    }
}