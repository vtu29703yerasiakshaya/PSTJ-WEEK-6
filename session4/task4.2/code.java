class Solution {
    public int strStr(String haystack, String needle) {
        int hLen = haystack.length();
        int nLen = needle.length();
        
        // If needle is longer than haystack, it cannot be a substring
        if (hLen < nLen) {
            return -1;
        }
        
        // Loop through haystack only up to where needle can still fit
        for (int i = 0; i <= hLen - nLen; i++) {
            // Check if the substring starting at index i matches needle
            if (haystack.substring(i, i + nLen).equals(needle)) {
                return i;
            }
        }
        
        return -1;
    }
}
