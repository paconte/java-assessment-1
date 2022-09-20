package src;

import static src.Exercise1.isPalindrome;

public class TestExercise1 {

    /**
     * Raises an assertion error if the return value of isPalindrome(text) does not match the given boolean isPalindrome
     * This method also prints out whether the text is a palindrome or not.
     * @param text The eventual palindrome string
     * @param isPalindrome Whether text is a palindrome or not
     */
    public static void assertIsPalindrome(String text, boolean isPalindrome) {
        assert isPalindrome == isPalindrome(text);
        if (isPalindrome)
            System.out.println(text + " is a palindrome");
        else
            System.out.println(text + " is NOT a palindrome");
    }

    public static void main(String[] args) {
        System.out.println("-----------------------------------------");
        System.out.println("------------ Exercise 1 -----------------");
        System.out.println("-----------------------------------------");

        // palindromes
        assertIsPalindrome("rotator", true);
        assertIsPalindrome("mom", true);
        assertIsPalindrome("Atar a la rata", true);

        // no palindromes
        assertIsPalindrome("hello hello", false);

        // edge cases
        assertIsPalindrome("Isaac no ronca asi", true); // note the accent at the character i
        assertIsPalindrome("", true);
        try {
            assertIsPalindrome(null, false);
        } catch (NullPointerException npe) {}
    }
}
