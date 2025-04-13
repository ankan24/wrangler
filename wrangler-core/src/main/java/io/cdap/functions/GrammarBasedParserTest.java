package io.cdap.wrangler.parser;

import io.cdap.wrangler.api.parser.TokenGroup;
import org.junit.Assert;
import org.junit.Test;

public class GrammarBasedParserTest {
    @Test
    public void testValidByteSizeAndTimeDurationParsing() throws Exception {
        String recipe = "aggregate sizeColumn timeColumn totalSizeColumn totalTimeColumn sizeUnit=MB timeUnit=seconds";

        RecipeCompiler compiler = new RecipeCompiler();
        TokenGroup tokenGroup = compiler.compile(recipe);

        Assert.assertNotNull(tokenGroup);
        Assert.assertEquals("MB", tokenGroup.getTokens().get(4).value());
        Assert.assertEquals("seconds", tokenGroup.getTokens().get(5).value());
    }

    @Test(expected = RecipeCompilerException.class)
    public void testInvalidSyntax() throws Exception {
        String recipe = "aggregate sizeColumn timeColumn totalSizeColumn"; // Missing arguments

        RecipeCompiler compiler = new RecipeCompiler();
        compiler.compile(recipe); // Should throw an exception
    }
}