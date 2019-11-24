package com.danwritesandcodes.app;

import org.languagetool.JLanguageTool;
import org.languagetool.language.AmericanEnglish;
import org.languagetool.rules.Rule;
import org.languagetool.rules.RuleMatch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class SpellChecker {
    private final Map<String, Object> misspells = new TreeMap<>();
    public final ArrayList<String> errors = new ArrayList<>();
    private final ArrayList<String> ignoreWords = new ArrayList<>();

    public SpellChecker FindSpellingErrors(DocReader d) {
        List<RuleMatch> matches;
        loadIgnoredWords(d);

        JLanguageTool langTool = new JLanguageTool(new AmericanEnglish());
        for (Rule rule : langTool.getAllRules()) {
            if (!rule.isDictionaryBasedSpellingRule()) {
                langTool.disableRule(rule.getId());
            }
        }
        try {
            matches = langTool.check(d.textNoCommandsNoOptions);
        } catch (Exception e) {
            errors.add("Error: Failed to run spell checker, "+e);
            return this;
        }

        for (RuleMatch match : matches) {
            int startPos = match.getFromPos();
            int endPos = match.getToPos();
            String errorText = d.textNoCommandsNoOptions.substring(startPos, endPos);

            // If this misspell is in IGNORE.txt, don't report it
            if (ignoreWords.contains(errorText)) {
                continue;
            }
            if (misspells.containsKey(errorText)) {
                continue;
            }
            misspells.put(errorText,true);
        }

        if (misspells.size() == 0) {
            return this;
        }

        if (ignoreWords.size() == 0) {
            errors.add("Warning: Potential misspells:");
        } else {
            errors.add("Warning: Potential misspells ("+ignoreWords.size()+" ignored words):");
        }
        for (String s : misspells.keySet()) {
            errors.add("  "+s);
        }
        return this;
    }

    private void loadIgnoredWords(DocReader d) {
        File documentFile = new File(d.documentFileName);
        String ignoreTextFile = documentFile.getParent()+File.separator+"IGNORE.txt";
        File ignoreWordsFile = new File(ignoreTextFile);
        if (!ignoreWordsFile.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(ignoreTextFile))) {
            String line;
            // Process each line, skip comments (#) and blank likes
            while ((line = br.readLine()) != null) {
                ignoreWords.add(line);
            }
        } catch (Exception e) {
            errors.add("Error: Failed to read IGNORE.txt");
        }
    }
}
