package com.danwritesandcodes.app;

import org.junit.Test;

import java.io.IOException;
import static org.junit.Assert.assertEquals;

public class TestSpellChecker {

    /**
     * Test reading the golden command file
     */
    @Test
    public void test1() throws IOException {
        DocReader d;
        d = new DocReader("src/test/resources/doc_w_errors.txt");
        SpellChecker s = new SpellChecker();
        s.FindSpellingErrors(d);
        assertEquals(9, s.errors.size());
    }

    /**
     * Test individual errors
     */
    @Test
    public void test2() {
        DocReader d = new DocReader();
        d.setTextNoCommandsNoOptions("This sentence is clear and spelled perfectly with zero errors.");
        SpellChecker s = new SpellChecker().FindSpellingErrors(d);
        try {
            assertEquals(0, s.errors.size());
        } catch (AssertionError e) {
            for (String err : s.errors) {
                System.out.println("TEST2 MESSAGE" + err);
            }
        }
    }

    /**
     * Test individual errors
     */
    @Test
    public void test3() {
        DocReader d = new DocReader();
        d.setTextNoCommandsNoOptions("This sentance is a spellin nightmere with three errors plus one for the header.");
        SpellChecker s = new SpellChecker().FindSpellingErrors(d);
        try {
            assertEquals(4,s.errors.size());
        } catch (AssertionError e) {
            for (String err: s.errors) {
                System.out.println("TEST3 MESSAGE"+err);
            }
        }
    }
}
