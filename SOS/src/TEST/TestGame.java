package TEST;

import SPRINT.*;
import org.junit.*;

import static org.junit.Assert.*;

public class TestGame {
    private static int count = 0;
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

    @Test
    public void testNullSquareValue() {
        Square square = new Square(Square.value.NULL);
        assertNotNull(square.getValue());
    }

    @BeforeClass
    public static void test() {
        System.out.println("Before Class");
    }

    @After
    public void testAfter() {
        count += 1;
        System.out.println("There have been " + count + " tests.");
    }
}
