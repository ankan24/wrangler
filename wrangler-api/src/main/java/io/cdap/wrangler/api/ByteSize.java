package io.cdap.wrangler.api.parser;

public class ByteSize extends Token {
    private final long bytes;

    public ByteSize(String value) {
        super(value);
        this.bytes = parseByteSize(value);
    }

    private long parseByteSize(String value) {
        if (value.endsWith("KB")) {
            return Long.parseLong(value.replace("KB", "")) * 1024;
        } else if (value.endsWith("MB")) {
            return Long.parseLong(value.replace("MB", "")) * 1024 * 1024;
        } else if (value.endsWith("GB")) {
            return Long.parseLong(value.replace("GB", "")) * 1024 * 1024 * 1024;
        } else if (value.endsWith("TB")) {
            return Long.parseLong(value.replace("TB", "")) * 1024L * 1024 * 1024 * 1024;
        } else if (value.endsWith("PB")) {
            return Long.parseLong(value.replace("PB", "")) * 1024L * 1024 * 1024 * 1024 * 1024;
        } else if (value.endsWith("B")) {
            return Long.parseLong(value.replace("B", ""));
        } else {
            throw new IllegalArgumentException("Invalid byte size format: " + value);
        }
    }

    public long getBytes() {
        return bytes;
    }
}