package io.cdap.wrangler.api.parser;

import java.util.ArrayList;
import java.util.List;

public class UsageDefinition {
    private final List<TokenType> tokenTypes;

    public UsageDefinition() {
        this.tokenTypes = new ArrayList<>();
    }

    public UsageDefinition add(TokenType tokenType) {
        tokenTypes.add(tokenType);
        return this;
    }

    public List<TokenType> getTokenTypes() {
        return tokenTypes;
    }
}

// Example usage:
// UsageDefinition usage = new UsageDefinition()
//     .add(TokenType.BYTE_SIZE)
//     .add(TokenType.TIME_DURATION);