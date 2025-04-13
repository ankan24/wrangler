package io.cdap.wrangler.api.parser;

import org.junit.Assert;
import org.junit.Test;

public class TimeDurationTest {
    @Test
    public void testTimeDurationParsing() {
        TimeDuration timeDuration1 = new TimeDuration("5ms");
        Assert.assertEquals(5, timeDuration1.getMilliseconds());

        TimeDuration timeDuration2 = new TimeDuration("2.1s");
        Assert.assertEquals(2100, timeDuration2.getMilliseconds());

        TimeDuration timeDuration3 = new TimeDuration("3m");
        Assert.assertEquals(180000, timeDuration3.getMilliseconds());

        TimeDuration timeDuration4 = new TimeDuration("1h");
        Assert.assertEquals(3600000, timeDuration4.getMilliseconds());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTimeDuration() {
        new TimeDuration("10XYZ"); // Invalid unit
    }
}