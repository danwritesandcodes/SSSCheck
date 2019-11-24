package com.danwritesandcodes.app;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
  * The StyleChecker class applies a set of style rule checks to the document. If a
  * violation is found, the context in which the violation occurred and a message
  * about how to correct the violation are returned.
 */
public class StyleChecker {
    public final ArrayList<String> errors = new ArrayList<>();

    /**
     * Find style errors in the current document. The errors are based on the
     * Microsoft Style guide (https://docs.microsoft.com/en-us/style-guide/welcome/)
     *
     * @param d  Document that contains the document text
     */
    public StyleChecker FindStyleErrors(DocReader d) {
        String normalizedText = d.originalText.replaceAll("[\n\r ]+", " ");
        ArrayList<StylePatternExplanation> patterns = new ArrayList<>();

        // Define six style guide rule patterns and explanation messages
        patterns.add(new StylePatternExplanation(Pattern.compile("(?i)actionable"),
                "Do not use actionable, use \"you can act on\" or similar"));
        patterns.add(new StylePatternExplanation(Pattern.compile("(?i)afterwards"),
                "Do not use afterwards, use \"afterward\""));
        patterns.add(new StylePatternExplanation(Pattern.compile("(?i)and/or"),
                "Avoid and/or, prefer \"and\" or \"or\" instead"));
        patterns.add(new StylePatternExplanation(Pattern.compile("(?i)anti-aliasing"),
                "Do not hyphenate, use \"antialiasing\" instead"));
        patterns.add(new StylePatternExplanation(Pattern.compile("(?i)back space"),
                "Do not use back space, use \"backspace\" instead"));
        patterns.add(new StylePatternExplanation(Pattern.compile("(?i)black box"),
                "Do not use black box, use another term"));

        // Apply each rule to the document text
        // to identify style violations
        for (StylePatternExplanation sp: patterns) {
            Matcher m = sp.pattern.matcher(normalizedText);
            while (m.find()) {
                try {
                    errors.add(CreateContextString(normalizedText,m.start(),m.end())+ " ["+sp.message+"]");
                } catch (Exception e) {
                    errors.add("Error processing text for rule check \""+sp.message+"\"");
                }
            }
        }
        return this;
    }

    /**
     * CreateContextString build a string that includes 20 characters before and
     * 20 characters after the style error. The function moves the start and end points
     * to word boundaries to clean up the message.
     *
     * @param docText Original source document text
     * @param matchBegins Point where rule match begins. Context will include about 20 characters
     *                    before this point.
     * @param matchEnds Point where rule match ends. Context will include about 20 characters
     *                  after this point.
     * @return Context string from the doc that includes the rule violation.
     */

    private String CreateContextString(String docText, int matchBegins, int matchEnds) {
        int startLocation = matchBegins - 20;
        int endLocation = matchEnds + 20;
        String contextString;

        // Match occurs near the beginning of the string,
        // set the start point for the context string
        // to the start of the input string
        if (startLocation < 0) {
            startLocation = 0;
        }

        // Match occurs near the end of the string,
        // set the end point for the context string
        // to the end of the input string
        if (endLocation > docText.length()) {
            endLocation = docText.length() - 1;
        }

        // Move the start point for the context string
        // toward the end so the context begins at a word boundary
        for (int i = startLocation; i < endLocation; i++) {
            if (docText.charAt(i) == ' ') {
                startLocation = i;
                break;
            }
        }

        // Move the end point for the context string
        // toward the start so the context ends at a word boundary
        for (int i = endLocation; i > startLocation; i--) {
            if (docText.charAt(i) == ' ') {
                endLocation = i;
                break;
            }
        }

        // Add dot-dot-dot (...) to the beginning and ending of the string
        // and return the string
        contextString = "..." + docText.substring(startLocation, endLocation) + " ...";
        return contextString;
    }

    /**
     * StylePatternExplanation contains a rule for matching text
     * in the document and an explanation of the style guide rule
     */
    static class StylePatternExplanation {
        final java.util.regex.Pattern pattern;
        final String message;

        StylePatternExplanation(java.util.regex.Pattern pattern, String message) {
            this.pattern = pattern;
            this.message = message;
        }
    }
}
