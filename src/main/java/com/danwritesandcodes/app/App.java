package com.danwritesandcodes.app;

import java.io.IOException;

/**
 * This application checks a PDF or plain text document for
 * the following types of errors:
 * - Commands which do not match the golden command reference
 * - Six style errors
 * - Spelling errors
 *
 */
class App
{
    public static void main( String[] args )
    {
        if (args.length == 0) {
            System.out.println("Error: Command is ssscheck file_name");
            System.out.println("  ssscheck document.txt");
            System.out.println("  ssscheck document.html");
            System.out.println("  ssscheck document.pdf");
            return;
        }

        // Load the PDF or TXT document
        System.out.println("Reading document file "+args[0]+" ...");
        DocReader d;
        try {
            d = new DocReader(args[0]);
        } catch (IOException e) {
            return;
        }

        // Check the likely commands and options in the document
        // against the golden command file
        CommandChecker c = new CommandChecker();
        c.LoadDefaultCommandFile(d);
        c.CompareDocVSGoldenData(d);
        if (c.errors.size() > 0) {
            System.out.println();
            for (String errString: c.errors) {
                System.out.println(errString);
            }
        }

        // Check the document for style errors
        StyleChecker s = new StyleChecker();
        s.FindStyleErrors(d);
        if (s.errors.size() > 0) {
            System.out.println("\nError: Document contains the following style errors:");
            for (String errString: s.errors) {
                System.out.println(errString);
            }
        }

        // Check the document for spelling errors
        SpellChecker sp = new SpellChecker();
        sp.FindSpellingErrors(d);
        if (sp.errors.size() > 0) {
            System.out.println();
            for (String errString: sp.errors) {
                System.out.println(errString);
            }
        }
    }
}
