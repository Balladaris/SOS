package TEST;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestGame {
    public int findMax(int[] arr) {
        int max = 0;
        for (int i = 1; i < arr.length; i++) {
            if (max < arr[i]) {
                max = arr[i];
            }
        }
        return max;
    }

    @Test
    public void testMax() {
        assertEquals(4, findMax(new int[] {1, 3, 2, 4}));
        assertEquals(4, findMax(new int[] {1, 2, 3, -1}));
    }

    @BeforeClass
    public static void test() {
        System.out.println("Before Class");
    }
}
