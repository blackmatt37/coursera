package edu.illinois.vlsicad.test;

import edu.illinois.vlsicad.COOMatrix;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class COOMatrixTest {
    static final double DELTA = 0.01;
    COOMatrix testMatrix;
    COOMatrix medMatrix;
    COOMatrix hugeMatrix;

    @Before
    public void setUp() throws Exception {
        // This is what the actual mat_test 3x3 matrix looks like
        // 4.0 -1.0 0.0
        // -1.0 4.0 -1.0
        // 0.0 -1.0 4.0

        // This is how you would read it in from file
        // testMatrix = COOMatrix.readCOOMatrix("mat_test.txt");

        // This is how you might construct it programmatically
        int[] rows = new int[]{0, 0, 1, 1, 1, 2, 2};
        int[] cols = new int[]{0, 1, 0, 1, 2, 1, 2};
        double[] data = new double[]{4.0, -1.0, -1.0, 4.0, -1.0, -1.0, 4.0};
        testMatrix = new COOMatrix(3, data.length);
        testMatrix.setRow(rows);
        testMatrix.setCol(cols);
        testMatrix.setData(data);


        medMatrix = COOMatrix.readCOOMatrix("psd.txt");
        hugeMatrix = COOMatrix.readCOOMatrix("mat_helmholtz.txt");
    }

    @Test
    public void testReadTestCOOMatrix() throws Exception {
        assertEquals("n doesn't match", 3, testMatrix.getDim());
        assertEquals("nnz doesn't match", 7, testMatrix.getNnz());

        // Quick-n-dirty comparison
        String expected = "3 7\n" +
                "0 0 4.00\n" +
                "0 1 -1.00\n" +
                "1 0 -1.00\n" +
                "1 1 4.00\n" +
                "1 2 -1.00\n" +
                "2 1 -1.00\n" +
                "2 2 4.00\n";

        assertEquals("Contents don't match", expected, testMatrix.toString());
    }

    @Test
    public void testReadHugeCOOMatrix() throws Exception {
        assertEquals("n doesn't match", 400, hugeMatrix.getDim());
        assertEquals("nnz doesn't match", 1920, hugeMatrix.getNnz());

        // Check some values
        double[] data = hugeMatrix.getData();
        assertEquals(-1.0, data[100], DELTA);
        assertEquals(-1.0, data[200], DELTA);
        assertEquals(5.0, data[300], DELTA);
        assertEquals(-1.0, data[400], DELTA);
        assertEquals(-1.0, data[500], DELTA);
        assertEquals(-1.0, data[600], DELTA);
    }

    @Test
    public void testMultiplyWithVector() {
        double[] result = new double[testMatrix.getDim()];
        result = testMatrix.multiplyWithVector(new double[]{1, 2, 3}, result);
        double[] expected = {2.0, 4.0, 10.0};
        assertTrue("Result of matrix vector multiplication not as expected", Arrays.equals(expected, result));
    }

    @Test
    public void testSolve1() {
        double[] b = new double[]{1.0, 2.0, 3.0};
        double[] x = new double[testMatrix.getDim()];
        testMatrix.solve(b, x);
        double[] expected = new double[]{0.4642857139733551, 0.8571428557253853, 0.9642857139733623};

        assertArrayEquals("Solution for x is not as expected", expected, x, DELTA);
    }

    @Test
    public void testSolve2() {
        double[] b = new double[]{-1.0, -2.0, -3.0};
        double[] x = new double[testMatrix.getDim()];
        testMatrix.solve(b, x);
        double[] expected = new double[]{-0.4642857139733551, -0.8571428557253853, -0.9642857139733623};

        assertArrayEquals("Solution for x is not as expected", expected, x, DELTA);
    }

    @Test
    public void testSolveMed() {
        double[] b = new double[]{
                3.23,
                28.53,
                28.15,
                79.51,
                27.84,
                42.26,
                38.75,
                54.68,
                62.47,
                14.31,
                56.43,
                18.38,
                46.48,
                15.20,
                70.18,
                65.64,
                43.22,
                97.30,
                57.36,
                71.74
        };

        double[] x = new double[medMatrix.getDim()];
        medMatrix.solve(b, x);

        double[] expected = new double[]{
                24.927,
                37.688,
                37.160,
                76.675,
                36.682,
                48.829,
                42.369,
                43.422,
                44.282,
                37.542,
                53.145,
                31.743,
                43.985,
                36.658,
                49.680,
                53.243,
                41.794,
                67.126,
                50.374,
                62.277
        };

        assertArrayEquals("Solution for x is not as expected", expected, x, DELTA);
    }

    @Test
    public void testSolveHuge() {
        double[] b = new double[hugeMatrix.getDim()];
        double[] x = new double[hugeMatrix.getDim()];
        Arrays.fill(b, 1.0); // Fill all b's with 1.0

        hugeMatrix.solve(b, x);

        // Comparing it to the values from the C++ version

        double[] expectedPartial = new double[]{
                0.421187,
                0.552967,
                0.595933,
                0.610376,
                0.61534,
                0.617076,
                0.61769,
                0.617909,
                0.617987,
                0.618011,
                0.618011,
                0.617987,
                0.617909,
                0.61769,
                0.617076,
                0.61534,
                0.610376,
                0.595933,
                0.552967,
                0.421187,
                0.552967,
                0.747716,
                0.816322,
                0.840604,
                0.849251
        };

        for (int i = 0; i < expectedPartial.length; i++) {
            assertEquals(String.format("%d value doesn't match!", i), expectedPartial[i], x[i], DELTA);
        }
    }

    @Test
    public void testSubtract() {
        double[] vector1 = new double[]{4, 5, 6};
        double[] vector2 = new double[]{2, 4, 8};
        double[] result = COOMatrix.subtract(vector1, vector2);
        double[] expected = new double[]{2.0, 1.0, -2.0};

        assertArrayEquals("Result of vector subtraction not as expected", expected, result, DELTA);
    }

    @Test
    public void testMultiplyWith() {
        double[] vector = new double[]{3, 7, 11};
        double[] result = COOMatrix.multiplyWith(vector, 4.5);

        // Assert that we are not creating any new array
        assertEquals("Variables do not reference the same object", vector, result);

        assertArrayEquals("Multiplied value is not as expected", result, new double[]{3 * 4.5, 7 * 4.5,
                11 * 4.5}, DELTA);
    }

    @Test
    public void addTo() {
        double[] vector1 = new double[]{3, 7, 11};
        double[] vector2 = new double[]{11, 7, 3};
        double[] result = COOMatrix.addTo(vector1, vector2);

        // Assert that we are not creating any new array
        assertEquals("Variables do not reference the same object", result, vector1);

        assertArrayEquals("Added value is not as expected", result, new double[]{14, 14, 14}, DELTA);
    }

    @Test
    public void testSubtractFrom() {
        double[] vector1 = new double[]{3, 7, 11};
        double[] vector2 = new double[]{11, 7, 3};
        double[] result = COOMatrix.subtractFrom(vector1, vector2);

        // Assert that we are not creating any new array
        assertEquals("Variables do not reference the same object", result, vector1);

        assertArrayEquals("Subtracted value is not as expected", result, new double[]{-8, 0, 8}, DELTA);
    }

    @Test
    public void testDotProduct() {
        double[] vector1 = new double[]{3, 2, 3};
        double[] vector2 = new double[]{-1, 4, 8};
        double result = COOMatrix.dot(vector1, vector2);
        double expected = 29;

        assertEquals("Result of dot product not as expected", expected, result, DELTA);
    }

}
