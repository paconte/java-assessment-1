package src;

import java.util.Objects;

class Exercise1 {
    /**
     * Returns true if the given string is a palindrome, otherwise return false.
     * Raises a NullPointerException if text is null.
     *
     * Assumptions:
     * Spaces are allowed, e.g. f("Evil olive") will return true
     * Case is ignored, e.g. f("Anna") will return true
     * Two characters are equals if java considers it, e.g. f("Isaac ronca as") will return true, note i ==
     *
     * @param text The eventual palindrome
     * @return a boolean indicating if the text is a palindrome or not
     */
    public static boolean isPalindrome(String text) {
        //precondition
        Objects.requireNonNull(text, "text must be not null");
        //the below regex could be declared static to avoid compilation time
        String cleanText = text.replaceAll("\\s+", "").toLowerCase();

        int i = 0;
        int j = cleanText.length() - 1;

        while (i < j) {
            if (cleanText.charAt(i) != cleanText.charAt(j)) {
                return false;
            }
            i++;
            j--;
        }

        return true;
    }

    /**
     * Entry method to call isPalindrome().
     * Examples:
     *  java Exercise1 Anna
     *  java Exercise1 "Some sentence"
     *
     */
    public static void main (String[] args) {
        String word = args[0];
        String print;
        if (isPalindrome(word))
            print = "\"" + word + "\" is a palindrome.";
        else
            print = "\"" + word + "\" is not a palindrome.";

        System.out.println(print);
    }
}