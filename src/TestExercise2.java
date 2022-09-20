package src;

import java.util.Set;

import static src.Exercise2.findKPairs;

public class TestExercise2 {

    /**
     * Auxiliary method to test findKPairs(int[], int)
     * Raises an assertion error if the size of the returned value of findKPairs(int[], int) does not match count
     *
     * @param count The number of k-complementary pairs
     * @param array The array of integers
     * @param k The K value
     */
    public static void assertFindKPairs(int count, int[] array, int k) {
        Set<Exercise2.IntPair> result = findKPairs(array, k);
        assert count == result.size();
        System.out.println(result);
    }

    public static void main(String[] args) {
        System.out.println("-----------------------------------------");
        System.out.println("------------ Exercise 2 -----------------");
        System.out.println("-----------------------------------------");

        int[] input1 = {1, 2, 3, 4, 5};
        int[] input2 = {5, 4, 3, 2, 1};
        int[] input3 = {2, 2, 2, 2, 2, 2};
        int[] input4 = {1, 3, 5, 7, 9, 11};
        int[] input5 = {1, 3, 5, 7, 9, 11, 9, 7, 5, 3, 1};
        int[] input6 = {1, 8, -3, 0, 1, 3, -2, 4, 5};
        int[] input7 = {};
        int[] input8 = {Integer.MAX_VALUE / 2, Integer.MAX_VALUE / 2 + 1};

        assertFindKPairs(2, input1, 5);
        assertFindKPairs(2, input2, 5);
        assertFindKPairs(1, input3, 4);
        assertFindKPairs(2, input4, 10);
        assertFindKPairs(3, input5, 10);
        assertFindKPairs(2, input6, 6);
        assertFindKPairs(0, input7, 6);
        assertFindKPairs(1, input8, Integer.MAX_VALUE);
    }
}