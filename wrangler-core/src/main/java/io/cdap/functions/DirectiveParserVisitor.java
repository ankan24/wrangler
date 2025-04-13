package io.cdap.wrangler.parser;

import io.cdap.wrangler.api.parser.ByteSize;
import io.cdap.wrangler.api.parser.TimeDuration;
import io.cdap.wrangler.api.parser.TokenGroup;

public class DirectiveParserVisitor extends DirectivesBaseVisitor<Object> {
    private final TokenGroup tokenGroup;

    public DirectiveParserVisitor(TokenGroup tokenGroup) {
        this.tokenGroup = tokenGroup;
    }

    @Override
    public Object visitByteSizeArg(DirectivesParser.ByteSizeArgContext ctx) {
        String text = ctx.getText(); // Get the token text (e.g., "10KB")
        ByteSize byteSize = new ByteSize(text); // Create a ByteSize instance
        tokenGroup.add(byteSize); // Add to the TokenGroup
        return byteSize;
    }

    @Override
    public Object visitTimeDurationArg(DirectivesParser.TimeDurationArgContext ctx) {
        String text = ctx.getText(); // Get the token text (e.g., "150ms")
        TimeDuration timeDuration = new TimeDuration(text); // Create a TimeDuration instance
        tokenGroup.add(timeDuration); // Add to the TokenGroup
        return timeDuration;
    }

    @Override
    public Object visitValue(DirectivesParser.ValueContext ctx) {
        String text = ctx.getText();
        if (text.matches("\\d+(B|KB|MB|GB|TB|PB)")) {
            ByteSize byteSize = new ByteSize(text);
            tokenGroup.add(byteSize);
            return byteSize;
        } else if (text.matches("\\d+(ms|s|m|h|d)")) {
            TimeDuration timeDuration = new TimeDuration(text);
            tokenGroup.add(timeDuration);
            return timeDuration;
        }
        return super.visitValue(ctx); // Fallback to the default behavior
    }
}