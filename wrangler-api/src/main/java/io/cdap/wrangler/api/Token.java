package io.cdap.wrangler.api.parser;

public enum TokenType {
    STRING,
    NUMBER,
    BOOLEAN,
    COLUMN,
    BYTE_SIZE,      // Add this
    TIME_DURATION;  // Add this
}