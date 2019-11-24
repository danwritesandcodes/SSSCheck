package com.danwritesandcodes.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

class CommandChecker {
    public final HashMap<String,ArrayList<String>> goldenCommands = new HashMap<>();
    public final Map<String, Object> goldenOptions = new TreeMap<>();
    public final ArrayList<String> errors = new ArrayList<>();

    /**
     * Read the optional GOLDEN.txt file from the same directory as the PDF, HTML, or
     * text document. If this file is not provided, no command checking is performed.
     *
     * @param d Document
     */
    public void LoadDefaultCommandFile(DocReader d) {
        File documentFile = new File(d.documentFileName);
        String goldenCommandTextFile = documentFile.getParent()+ File.separator+"GOLDEN.txt";
        File goldenCommandFile = new File(goldenCommandTextFile);
        if (!goldenCommandFile.exists()) {
            return;
        }
        ReadGoldenCommandFile(goldenCommandTextFile);
    }

    /**
     * Read and process the golden command file. The file contains
     * all the commands and options supported by the tool. If a command
     * in the document text doesn't match a golden command or option,
     * the program reports an error.
     *
     */
    public void ReadGoldenCommandFile(String fileName) {
        String [] parts;

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            // Process each line, skip comments (#) and blank likes
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#")) {
                    continue;
                }
                if (line.matches("^\\s*$")) {
                    continue;
                }
                parts = line.split("[ \t]+");
                if (!goldenCommands.containsKey(parts[0])) {
                    goldenCommands.put(parts[0],new ArrayList<>());
                }
                if (!parts[0].matches("^[A-Z][a-zA-Z]+$"))
                {
                    System.out.println("Error: The following command line does not begin with a valid command name:");
                    System.out.println(line);
                    continue;
                }
                for (String p: parts) {
                    if (p.startsWith("/")) {
                        goldenCommands.get(parts[0]).add(p);
                        goldenOptions.put(p,true);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading golden command file ("+fileName+"), "+e);
            System.exit(1);
        }
    }

    /**
     * Compare the likely commands in the document
     * against the list of golden commands.
     *
     */
    public int CompareDocVSGoldenData(DocReader d) {
        ArrayList<String> suggestions;
        String lastCommand = "";
        int errorCount = 0;
        for (Token t: d.tokens) {
            // This word looks like a command, check if it is in the golden list
            if (t.isLikelyCommand) {
                if (goldenCommands.containsKey(t.cleantext)){
                    lastCommand = t.cleantext;
                } else {
                    errors.add("Error: No match for likely command "+t.cleantext);
                    suggestions = getSuggestions(t.cleantext);
                    if (suggestions.size() > 0) {
                        String allSuggestions = String.join(", ", suggestions);
                        errors.add("  Did you mean any of the following: "+allSuggestions);
                    }
                    errorCount++;
                }
                continue;
            }

            if (t.isLikelyOption) {
                if ((lastCommand.length() > 0) && !goldenCommands.get(lastCommand).contains(t.cleantext)) {
                    errors.add("Error: Option "+t.cleantext+" is not an option for the previous command ("+lastCommand+")");
                    errorCount++;
                }
                if (!goldenOptions.containsKey(t.cleantext)) {
                    errors.add("Error: Option " + t.cleantext + " is not an option for any command");
                }
            }
        }
        return errorCount;
    }

    /**
     * Generate an ArrayList that contains command names which closely
     * match the problem command name. Matching is determined by the
     * Levenshtein distance between the problem command name and the
     * candidate from the golden command file.
     *
     * @param commandName
     * @return
     */
    private ArrayList<String> getSuggestions(String commandName) {
        ArrayList<String> suggestions = new ArrayList<>();
        for (String s: goldenCommands.keySet()) {
            // if ((s.substring(0, 5).equals(commandName.substring(0, 5))) &&
            //         (Math.abs(commandName.length() - s.length()) < 4)) {
            if (Math.abs(commandName.length() - s.length()) < 4) {
                int distance = levenshteinDistance(commandName, s);
                if (distance < 4) {
                    suggestions.add(s);
                }
            }
        }
        return suggestions;
    }

    // Levenshtein distance is a measure of the degree of difference between two strings
    // This implementation from
    // https://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Java
    private int levenshteinDistance (String lhs, String rhs) {
        int len0 = lhs.length() + 1;
        int len1 = rhs.length() + 1;

        // the array of distances
        int[] cost = new int[len0];
        int[] newcost = new int[len0];

        // initial cost of skipping prefix in String s0
        for (int i = 0; i < len0; i++) cost[i] = i;

        // dynamically computing the array of distances

        // transformation cost for each letter in s1
        for (int j = 1; j < len1; j++) {
            // initial cost of skipping prefix in String s1
            newcost[0] = j;

            // transformation cost for each letter in s0
            for(int i = 1; i < len0; i++) {
                // matching current letters in both strings
                int match = (lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1;

                // computing cost for each transformation
                int cost_replace = cost[i - 1] + match;
                int cost_insert  = cost[i] + 1;
                int cost_delete  = newcost[i - 1] + 1;

                // keep minimum cost
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
            }

            // swap cost/newcost arrays
            int[] swap = cost; cost = newcost; newcost = swap;
        }

        // the distance is the cost for transforming all letters in both strings
        return cost[len0 - 1];
    }
}
