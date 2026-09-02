import java.util.*;

class Solution {
    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> result = new ArrayList<>();

        if (s.length() < p.length()) {
            return result;
        }

        int[] pCount = new int[26];
        int[] windowCount = new int[26];

        // Count characters in p
        for (char c : p.toCharArray()) {
            pCount[c - 'a']++;
        }

        int windowSize = p.length();

        // Build the first window
        for (int i = 0; i < windowSize; i++) {
            windowCount[s.charAt(i) - 'a']++;
        }

        // Check each window
        for (int i = 0; i <= s.length() - windowSize; i++) {

            if (Arrays.equals(pCount, windowCount)) {
                result.add(i);
            }

            // Remove the leftmost character
            if (i + windowSize < s.length()) {
                windowCount[s.charAt(i) - 'a']--;

                // Add the next character
                windowCount[s.charAt(i + windowSize) - 'a']++;
            }
        }

        return result;
    }
}
