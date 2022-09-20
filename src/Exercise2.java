package src;

import java.util.*;

public class Exercise2 {

    /**
     * Auxiliary class to store k-complementary pairs
     *
     * @param x
     * @param y
     */
    public record IntPair(int x, int y) {
    }

    /**
     * Returns the k-complementary pairs of an array. Where given an Array A, a pair (i, j) is K-complementary
     * if K = A[i] + A[j]
     * <p>
     * Assumptions:
     * - The sum of all the possible array pairs A[i] + A[j] must be in the range [INTEGER.MIN_VALUE, INTEGER.MAX_VALUE]
     * otherwise the method won't work.
     * - Positive and negative integers are allowed.
     * - Repetition of integers is allowed, e.g. f([1,2,2,3,3], k)
     * - Returned pairs won't be repeated; the returned set will eliminate the repeated sets.
     * - Returned pairs will be ordered, the left side will be the lower int,
     * e.g. f([3,4,4,3], 7) will return [(3,4)] and not [(3,4), (4,3)]
     * <p>
     * Complexity:
     * - Time complexity is given by the sorting algorithm: O(n log(n)). Assuming the given array is sorted,
     * then the time complexity is O(n).
     * - Space complexity is given by the sorting algorithm: O(n)
     *
     * @param intArray The array of integers
     * @param k        K-complementary value
     * @return A set of K-complementary pairs
     */
    static Set<IntPair> findKPairs(int intArray[], int k) {
        Set<IntPair> result = new HashSet<>();
        if (intArray.length == 0)
            return result;

        Arrays.sort(intArray); // O(n log(n)), see java.util.Arrays Dual-Pivot Quicksort
        int l = 0; // left pointer
        int r = intArray.length - 1; // right pointer

        while (l < r) {
            if (intArray[r] + intArray[l] == k) {
                result.add(new IntPair(intArray[r], intArray[l]));
                l++;
            } else if (intArray[r] + intArray[l] > k) {
                r--;
            } else
                l++;
        }

        return result;
    }

    /**
     * Entry method to call findKPairs(). The first argument is the K value, the second and later arguments are the
     * integer values of the expected array. Following the below format:
     * java Exercise2 k arr[0] arr[0] ... arr[n-1] arr[n]
     * Please note the is no error handling, and it is expected correct values.
     *
     * Examples:
     *  java Exercise2 5 1 2 3 4 5
     *  java Exercise2 4 2 2 2
     *
     * @param args The program arguments
     */
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        int[] array = new int[args.length-1];
        for (int i = 1; i < args.length; i++) {
            array[i-1] = Integer.parseInt(args[i]);
        }
        Set<IntPair> result = findKPairs(array, k);
        for (IntPair pair: result) {
            System.out.println(pair);
        }
    }
}
