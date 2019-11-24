package com.danwritesandcodes.app;

import org.junit.Test;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class TestCommandChecker {

    /**
     * Test reading the golden command file
     */
    @Test
    public void test1() throws IOException {

        // Load the document

        DocReader d;
        d = new DocReader("src/test/resources/doc.txt");

        // Load the golden command file
        CommandChecker c = new CommandChecker();
        c.LoadDefaultCommandFile(d);
//        c.AddGoldenCommandFile("src/test/resources/GOLDEN.txt");
        assertEquals(9,c.goldenCommands.size());

        // Size of goldenCommands shouldn't change when reading the same golden command file
        c.ReadGoldenCommandFile("src/test/resources/GOLDEN.txt");
        assertEquals(9,c.goldenCommands.size());

        // DUMMY.txt contains one new additional command
        c.ReadGoldenCommandFile("src/test/resources/DUMMY.txt");
        assertEquals(10,c.goldenCommands.size());
    }

    /**
     * Test reading the golden command file and use it
     * to compare against a document
     */
    @Test
    public void test2() throws IOException {

        // Load the document and golden command file
        DocReader d;
        d = new DocReader("src/test/resources/doc_w_errors.txt");
        CommandChecker c = new CommandChecker();
        c.LoadDefaultCommandFile(d);
        assertEquals(9,c.goldenCommands.size());

        // Check the commands and options in the document against the golden file
        int commandErrorCount = c.CompareDocVSGoldenData(d);
        assertEquals(13,commandErrorCount);

        // commandErrorCount should equal the number of error
        // strings that start with "Error:"
        long errorStringCount =
                c.errors
                        .stream()
                        .filter(s -> s.startsWith("Error:"))
                        .count();
        assertEquals(17,errorStringCount);
    }

    /**
     * Test reading the golden command file and use it
     * to compare against a short document
     */
    @Test
    public void test3() throws IOException {

        // Load the document and golden command file
        DocReader d;
        d = new DocReader("src/test/resources/para.txt");
        CommandChecker c = new CommandChecker();
        c.LoadDefaultCommandFile(d);
//        assertEquals(9,c.goldenCommands.size());

        // Check the commands and options in the document against the golden file
        int commandErrorCount = c.CompareDocVSGoldenData(d);
//        assertEquals(13,commandErrorCount);

        // commandErrorCount should equal the number of error
        // strings that start with "Error:"
        long errorStringCount =
                c.errors
                        .stream()
                        .filter(s -> s.startsWith("Error:"))
                        .count();
        assertEquals(3,errorStringCount);
    }

    /**
     * Test reading the golden command file and use it
     * to compare against a short document
     */
    @Test
    public void test4() throws IOException {

        // Load the document and golden command file
        DocReader d;
        d = new DocReader("src/test/resources/no_command.txt");
        CommandChecker c = new CommandChecker();
        c.LoadDefaultCommandFile(d);
//        assertEquals(9,c.goldenCommands.size());

        // Check the commands and options in the document against the golden file
        int commandErrorCount = c.CompareDocVSGoldenData(d);
//        assertEquals(13,commandErrorCount);

        // commandErrorCount should equal the number of error
        // strings that start with "Error:"
        long errorStringCount =
                c.errors
                        .stream()
                        .filter(s -> s.startsWith("Error:"))
                        .count();
        assertEquals(2,errorStringCount);
    }
}
