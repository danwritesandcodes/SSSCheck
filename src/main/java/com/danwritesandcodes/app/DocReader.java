package com.danwritesandcodes.app;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import org.jsoup.Jsoup;

class DocReader {
    public String documentFileName;
    public String originalText;
    public String textNoCommandsNoOptions;
    public ArrayList<Token> tokens = new ArrayList<>();
    private int indexCount = 1;

    private void AddToken(String word) {
        boolean isPossibleCommand = false;
        boolean isPossibleOption = false;
        String wordStripped = "";

        // Strip punctuation, etc for later comparison
        Pattern wordStrippedPattern = Pattern.compile("([a-zA-Z0-9-/]+)");
        Matcher wordStrippedMatcher = wordStrippedPattern.matcher(word);
        if (wordStrippedMatcher.find()) {
            wordStripped = wordStrippedMatcher.group(1);
        }

        // Possible commands begin with a capital letter and contain
        // a second capital letter. This ignores commands with only a
        // single capital letter
        Pattern possibleCommand = Pattern.compile("^[A-Z][a-z]+[A-Z][A-Za-z]+$");
        Pattern possibleOption = Pattern.compile("^/[a-z]+[A-Za-z]*$");
        Matcher possibleCommandMatcher = possibleCommand.matcher(wordStripped);
        if (possibleCommandMatcher.find()) {
            isPossibleCommand = true;
        }
        if (wordStripped.startsWith("/")) {
            Matcher possibleOptionMatcher = possibleOption.matcher(wordStripped);
            if (possibleOptionMatcher.find()) {
                isPossibleOption = true;
            }
        }
        Token token = new Token(word, wordStripped, indexCount, isPossibleCommand, isPossibleOption);
        tokens.add(token);
        indexCount++;
    }

    /**
     * ReadTextDocument reads a plain text document and tokenizes the content.
     *
     * @param fileName Name of the input text document
     */
    private void ReadTextDocument(String fileName) {
        documentFileName = fileName;
        File file = new File(fileName);
        try {
            originalText = new String (Files.readAllBytes(file.toPath()), Charset.forName("UTF-8"));
        }
        catch (IOException e) {
            System.out.println("Error: Cannot read file ("+fileName+"), "+e);
            return;
        }
        StringTokenizer st = new StringTokenizer(originalText, " \t\n\r");
        while (st.hasMoreElements()) {
            String tok = st.nextToken();
            AddToken(tok);
        }

        // Build a string that omits likely commands and options
        writeTextNoCommandsNoOptions();
    }

    /**
     * ReadHTMLDocument reads an HTML-formatted document and tokenizes the content.
     *
     * @param fileName Name of the input HTML file
     */
    private void ReadHTMLDocument(String fileName) {
        File htmlFile = new File(fileName);
        try {
            originalText = Jsoup.parse(htmlFile,"UTF-8","").text();
        }
        catch (IOException e) {
            System.out.println("Error: Cannot read file ("+fileName+"), "+e);
            return;
        }
        StringTokenizer st = new StringTokenizer(originalText, " \t\n\r");
        while (st.hasMoreElements()) {
            String tok = st.nextToken();
            AddToken(tok);
        }

        // Build a string that omits likely commands and options
        writeTextNoCommandsNoOptions();
    }

    /**
     * readPDFDocument opens the PDF file and converts the content
     * to plain text.
     *
     * @param fileName Name of the PDF input file
     */
    private void readPDFDocument(String fileName) {
        PdfReader reader;
        StringBuilder textFromPage = new StringBuilder("");
        try {
            reader = new PdfReader(fileName);
            for (int pageCnt = 1; pageCnt <= reader.getNumberOfPages(); pageCnt++)
            {
                textFromPage.append(PdfTextExtractor.getTextFromPage(reader, pageCnt));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        originalText = textFromPage.toString();
        StringTokenizer st = new StringTokenizer(originalText, " \t\n\r");
        while (st.hasMoreElements()) {
            String tok = st.nextToken();
            AddToken(tok);
        }

        // Build a string that omits likely commands and options
        writeTextNoCommandsNoOptions();
    }

    /**
     * writeTextNoCommandsNoOptions creates a single string that
     * contains the document text without likely command and option
     * names. The string is used for the spelling check.
     */

    private void writeTextNoCommandsNoOptions() {
        // Build a string that omits likely commands and options
        StringBuilder sb = new StringBuilder("");
        for (Token t: tokens) {
            if (t.isLikelyOption || t.isLikelyCommand) {
                continue;
            }
            sb.append(t.cleantext);
            sb.append(" ");
        }
        textNoCommandsNoOptions = sb.toString();
        // writeStringToFile(textNoCommandsNoOptions,"sb.txt");
    }

    /**
     * writeStringToFile writes out docText to the file fileName.
     * This is used for debugging.
     *
     * @param docText Text string to be written to the file
     * @param fileName File name of the output file
     */

    public static void writeStringToFile(String docText, String fileName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(docText);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error reading file "+fileName+", "+e);
            System.exit(1);
        }
        System.out.println("Wrote "+fileName);
    }
    /**
     * DocReader constructor
     *
     * @param fileName Input text or PDF file name
     * @throws IOException Exception that occurs when fileName does not exist
     */
    public DocReader(String fileName) throws IOException {
        this.documentFileName = fileName;
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("Error: file ("+fileName+") does not exist, exiting");
            throw new IOException("Error: file ("+fileName+") does not exist, exiting");
        }
        if (fileName.toLowerCase().endsWith(".txt")) {
            ReadTextDocument(fileName);
            return;
        }
        if (fileName.toLowerCase().endsWith(".pdf")) {
            readPDFDocument(fileName);
            return;
        }
        if (fileName.toLowerCase().endsWith(".html")) {
            ReadHTMLDocument(fileName);
            return;
        }
        throw new IOException("Error: file ("+fileName+") has an unknown extension, exiting");
    }

    public DocReader() {
        this.documentFileName = "";
        this.originalText = "";
    }

    public void setTextNoCommandsNoOptions(String textNoCommandsNoOptions) {
        this.textNoCommandsNoOptions = textNoCommandsNoOptions;
    }

}
