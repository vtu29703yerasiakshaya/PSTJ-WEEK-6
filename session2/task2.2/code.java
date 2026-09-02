class Solution {
    public boolean rotateString(String s, String goal) {
        // Rotations are only possible if both strings
        // have the same length.
        if (s.length() != goal.length()) {
            return false;
        }

        // Every rotation of s will appear inside s + s.
        String doubled = s + s;

        return doubled.contains(goal);
    }
}
