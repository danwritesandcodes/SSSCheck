package com.danwritesandcodes.app;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.*;

public class TestReadPDFHTMLText {

    /**
     * Test reading a basic text document
     */
    @Test
    public void test1() throws IOException {
        DocReader d;
        d = new DocReader("src/test/resources/doc.txt");
        assertEquals(9088, d.originalText.length());
    }

    /**
     * Test parsing the text file into tokens
     */
    @Test
    public void test2() throws IOException {
        DocReader d;
        d = new DocReader("src/test/resources/doc.txt");
        // TestUtils.writeTokensToFile(d.tokens,"doc.txt_tokens.txt");
        assertEquals(1269, d.tokens.size());
        for (Token t: d.tokens) {
            if (t.text.equals("examining,")) {
                assertEquals("examining",t.cleantext      );
                assertEquals(false      ,t.isLikelyCommand);
                assertEquals(false      ,t.isLikelyOption );
                continue;
            }
            if (t.text.equals("BuildConnectionAssessment")) {
                assertEquals("BuildConnectionAssessment",t.cleantext      );
                assertEquals(true                       ,t.isLikelyCommand);
                assertEquals(false                      ,t.isLikelyOption );
                continue;
            }
            if (t.text.equals("/genPaths")) {
                assertEquals("/genPaths",t.cleantext      );
                assertEquals(false      ,t.isLikelyCommand);
                assertEquals(true       ,t.isLikelyOption );
            }
        }
        long commandCount = d.tokens.stream()
                .filter(s -> s.isLikelyCommand)
                .count();
        assertEquals(36, commandCount);

        long optionCount = d.tokens.stream()
                .filter(s -> s.isLikelyOption)
                .count();
        assertEquals(43, optionCount);
    }

    /**
     * Test reading a basic PDF document
     */
    @Test
    public void test3() throws IOException {
        DocReader d;
        d = new DocReader("src/test/resources/doc.pdf");
        assertEquals(8923, d.originalText.length());
    }

    /**
     * Test parsing the PDF file into tokens
     */
    @Test
    public void test4() throws IOException {
        DocReader d;
        d = new DocReader("src/test/resources/doc.pdf");
        // TestUtils.writeTokensToFile(d.tokens,"doc.pdf_tokens.txt");

        assertEquals(1269, d.tokens.size());
        for (Token t: d.tokens) {
            if (t.text.equals("examining,")) {
                assertEquals("examining",t.cleantext      );
                assertEquals(false      ,t.isLikelyCommand);
                assertEquals(false      ,t.isLikelyOption );
                continue;
            }
            if (t.text.equals("BuildConnectionAssessment")) {
                assertEquals("BuildConnectionAssessment",t.cleantext      );
                assertEquals(true                       ,t.isLikelyCommand);
                assertEquals(false                      ,t.isLikelyOption );
                continue;
            }
            if (t.text.equals("/genPaths")) {
                assertEquals("/genPaths",t.cleantext      );
                assertEquals(false      ,t.isLikelyCommand);
                assertEquals(true       ,t.isLikelyOption );
            }
        }
        long commandCount = d.tokens.stream()
                .filter(s -> s.isLikelyCommand)
                .count();
        assertEquals(36, commandCount);

        long optionCount = d.tokens.stream()
                .filter(s -> s.isLikelyOption)
                .count();
        assertEquals(43, optionCount);
    }

    /**
     * Test reading a basic HTML document
     */
    @Test
    public void test5() throws IOException {
        DocReader d;
        d = new DocReader("src/test/resources/doc.html");
        assertEquals(8658, d.originalText.length());
    }

    /**
     * Test parsing the HTML file into tokens
     */
    @Test
    public void test6() throws IOException {
        DocReader d;
        d = new DocReader("src/test/resources/doc.html");
        // TestUtils.writeTokensToFile(d.tokens,"doc.html_tokens.txt");

        assertEquals(1234, d.tokens.size());
        for (Token t: d.tokens) {
            if (t.text.equals("examining,")) {
                assertEquals("examining",t.cleantext      );
                assertEquals(false      ,t.isLikelyCommand);
                assertEquals(false      ,t.isLikelyOption );
                continue;
            }
            if (t.text.equals("BuildConnectionAssessment")) {
                assertEquals("BuildConnectionAssessment",t.cleantext      );
                assertEquals(true                       ,t.isLikelyCommand);
                assertEquals(false                      ,t.isLikelyOption );
                continue;
            }
            if (t.text.equals("/genPaths")) {
                assertEquals("/genPaths",t.cleantext      );
                assertEquals(false      ,t.isLikelyCommand);
                assertEquals(true       ,t.isLikelyOption );
            }
        }
        long commandCount = d.tokens.stream()
                .filter(s -> s.isLikelyCommand)
                .count();
        assertEquals(36, commandCount);

        long optionCount = d.tokens.stream()
                .filter(s -> s.isLikelyOption)
                .count();
        assertEquals(43, optionCount);
    }

}
