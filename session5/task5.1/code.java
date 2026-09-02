class Solution {
    public String longestPalindrome(String s) {
        if (s == null || s.length() < 1) return "";
        
        int start = 0;
        int end = 0;
        
        for (int i = 0; i < s.length(); i++) {
            // Case 1: Odd length palindrome (centered at i)
            int len1 = expandAroundCenter(s, i, i);
            
            // Case 2: Even length palindrome (centered between i and i+1)
            int len2 = expandAroundCenter(s, i, i + 1);
            
            // Find the maximum length found at this center position
            int maxLen = Math.max(len1, len2);
            
            // If we found a longer palindrome, update the global start and end indices
            if (maxLen > (end - start + 1)) {
                start = i - (maxLen - 1) / 2;
                end = i + maxLen / 2;
            }
        }
        
        // Return the longest palindromic substring
        return s.substring(start, end + 1);
    }
    
    // Helper method to expand outwards and calculate palindrome length
    private int expandAroundCenter(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }
        // Subtract 1 because the loop terminates after left and right move one step too far
        return right - left - 1;
    }
}
