package io.cdap.wrangler.api.parser;

import org.junit.Assert;
import org.junit.Test;

public class ByteSizeTest {
    @Test
    public void testByteSizeParsing() {
        ByteSize byteSize1 = new ByteSize("10KB");
        Assert.assertEquals(10240, byteSize1.getBytes());

        ByteSize byteSize2 = new ByteSize("1.5MB");
        Assert.assertEquals(1572864, byteSize2.getBytes());

        ByteSize byteSize3 = new ByteSize("5GB");
        Assert.assertEquals(5368709120L, byteSize3.getBytes());

        ByteSize byteSize4 = new ByteSize("2TB");
        Assert.assertEquals(2199023255552L, byteSize4.getBytes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidByteSize() {
        new ByteSize("10XYZ"); // Invalid unit
    }
}