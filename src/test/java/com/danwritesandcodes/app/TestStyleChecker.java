package com.danwritesandcodes.app;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

public class TestStyleChecker {

    /**
     * Test reading the golden command file
     */
    @Test
    public void test1() throws IOException {
        DocReader d;
        d = new DocReader("src/test/resources/style_errors.txt");
        StyleChecker s = new StyleChecker();
        s.FindStyleErrors(d);
        assertEquals(6, s.errors.size());
    }


    /**
     * Test individual phrases
     */
    @Test
    public void test2() {
        DocReader d = new DocReader();
        d.originalText = "No problem here";
        StyleChecker s = new StyleChecker();
        s.FindStyleErrors(d);
        assertEquals(0, s.errors.size());

        d.originalText = "The word actionable should cause an error";
        s = new StyleChecker();
        s.FindStyleErrors(d);
        assertEquals(1, s.errors.size());

        d.originalText = "The words black box and/or back space should cause errors";
        s = new StyleChecker().FindStyleErrors(d);
        assertEquals(3, s.errors.size());
    }

    /**
     * Test single paragraph
     */
    @Test
    public void test3() throws IOException {
        DocReader d = new DocReader("src/test/resources/para.txt");
        StyleChecker s = new StyleChecker();
        s.FindStyleErrors(d);
        assertEquals(1, s.errors.size());
    }
}
