package com.danwritesandcodes.app;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

class TestUtils {

    /**
     * Writes the contents of the tokens ArrayList to the fileName test file.
     * This function is useful for debugging differences in parsing PDF, HTML, and
     * plain text documents, allowing you to compare the tokens saved for two
     * different files with the same expected content.
     *
     * @param tokens
     * @param fileName
     */
    public static void writeTokensToFile(ArrayList<Token> tokens, String fileName) {
        int tokenCount = 0;
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            for (Token t: tokens) {
                writer.write(t.text+"\n");
                tokenCount++;
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error reading file "+fileName+", "+e);
            System.exit(1);
        }
        System.out.println("Wrote "+tokenCount+" tokens to "+fileName);
    }
}
