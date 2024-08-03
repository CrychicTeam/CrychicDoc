package org.embeddedt.embeddium.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class StringUtils {

    public static int levenshteinDistance(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();
        int[][] dp = new int[m + 1][n + 1];
        int i = 0;
        while (i <= m) {
            dp[i][0] = i++;
        }
        i = 0;
        while (i <= n) {
            dp[0][i] = i++;
        }
        for (int ix = 1; ix <= m; ix++) {
            for (int j = 1; j <= n; j++) {
                int cost = s1.charAt(ix - 1) == s2.charAt(j - 1) ? 0 : 1;
                dp[ix][j] = Math.min(dp[ix - 1][j] + 1, Math.min(dp[ix][j - 1] + 1, dp[ix - 1][j - 1] + cost));
            }
        }
        return dp[m][n];
    }

    public static <T> List<T> fuzzySearch(Iterable<T> options, String userInput, int maxDistance, Function<T, String> toStringFn) {
        List<T> result = new ArrayList();
        String[] targetWords = userInput.toLowerCase().split("\\s+");
        for (T option : options) {
            String sentence = ((String) toStringFn.apply(option)).toLowerCase();
            boolean containsAllWords = true;
            for (String word : targetWords) {
                boolean containsWord = false;
                for (String sentenceWord : sentence.toLowerCase().split("\\s+")) {
                    int distance = levenshteinDistance(word, sentenceWord);
                    if (distance <= maxDistance) {
                        containsWord = true;
                        break;
                    }
                    if (sentenceWord.startsWith(word)) {
                        containsWord = true;
                        break;
                    }
                }
                if (!containsWord) {
                    containsAllWords = false;
                    break;
                }
            }
            if (containsAllWords) {
                result.add(option);
            }
        }
        return result;
    }
}