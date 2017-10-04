package sfriberg.matrixmultiplication;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.PrimitiveIterator.OfDouble;
import java.util.Random;
import java.util.function.BiFunction;

public class MatrixTest {

	@org.junit.Test
	public void testRows() {
		assertEquals(0, new Matrix(0, 0).rows());
		assertEquals(0, new Matrix(0, 1).rows());
		assertEquals(1, new Matrix(1, 0).rows());
		assertEquals(1, new Matrix(1, 1).rows());
		assertEquals(2, new Matrix(2, 3).rows());
		assertEquals(3, new Matrix(3, 2).rows());
	}

	@org.junit.Test
	public void testCols() {
		assertEquals(0, new Matrix(0, 0).cols());
		assertEquals(1, new Matrix(0, 1).cols());
		assertEquals(0, new Matrix(1, 0).cols());
		assertEquals(1, new Matrix(1, 1).cols());
		assertEquals(3, new Matrix(2, 3).cols());
		assertEquals(2, new Matrix(3, 2).cols());
	}

	@org.junit.Test
	public void testMultiply() {
		innerTestMultiply((a, b) -> a.multiply(b));
	}

	@org.junit.Test
	public void testMultiplyInlined() {
		innerTestMultiply((a, b) -> a.multiplyInlined(b));
	}

	@org.junit.Test
	public void testMultiplyReorderedInlined() {
		innerTestMultiply((a, b) -> a.multiplyReorderedInlined(b));
	}

	@org.junit.Test
	public void testMultiplyBlock() {
		innerTestMultiply((a, b) -> a.multiplyBlock(b));
	}

	@org.junit.Test
	public void testMultiplyBlockWithSize() {
		innerTestMultiply((a, b) -> a.multiplyBlock(b, 1));
		innerTestMultiply((a, b) -> a.multiplyBlock(b, 2));
		innerTestMultiply((a, b) -> a.multiplyBlock(b, 8));
		innerTestMultiply((a, b) -> a.multiplyBlock(b, 16));
		innerTestMultiply((a, b) -> a.multiplyBlock(b, 32));
	}

	public void innerTestMultiply(BiFunction<Matrix, Matrix, Matrix> multiplier) {
		try {
			multiplier.apply(new Matrix(2, 3), new Matrix(3, 2));
		} catch (IllegalArgumentException ex) {
			assertNotNull(ex);
		}

		{
			double[] numbers = {1, 2, 3, 4};
			OfDouble iterator = Arrays.stream(numbers).iterator();
			Matrix a = new Matrix(2, 2, iterator::next);
			iterator = Arrays.stream(numbers).iterator();
			Matrix b = new Matrix(2, 2, iterator::next);
			double[] rNumbers = {7, 10, 15, 22};
			iterator = Arrays.stream(rNumbers).iterator();
			assertEquals(new Matrix(2, 2, iterator::next), multiplier.apply(a, b));
		}
		{
			double[] numbers = {1, 2, 3, 4};
			OfDouble iterator = Arrays.stream(numbers).iterator();
			Matrix a = new Matrix(2, 2, iterator::next);
			double[] numbers2 = {4, 3, 2, 1};
			iterator = Arrays.stream(numbers2).iterator();
			Matrix b = new Matrix(2, 2, iterator::next);
			double[] rNumbers = {8, 5, 20, 13};
			iterator = Arrays.stream(rNumbers).iterator();
			assertEquals(new Matrix(2, 2, iterator::next), multiplier.apply(a, b));
		}
		{
			double[] numbers = {1, 2, 3, 4};
			OfDouble iterator = Arrays.stream(numbers).iterator();
			Matrix a = new Matrix(1, 4, iterator::next);
			iterator = Arrays.stream(numbers).iterator();
			Matrix b = new Matrix(4, 1, iterator::next);
			double[] rNumbers = {30};
			iterator = Arrays.stream(rNumbers).iterator();
			assertEquals(new Matrix(1, 1, iterator::next), multiplier.apply(a, b));
		}
		{
			double[] numbers = {1, 2, 3, 4};
			OfDouble iterator = Arrays.stream(numbers).iterator();
			Matrix a = new Matrix(4, 1, iterator::next);
			iterator = Arrays.stream(numbers).iterator();
			Matrix b = new Matrix(1, 4, iterator::next);
			double[] rNumbers = {1, 2, 3, 4, 2, 4, 6, 8, 3, 6, 9, 12, 4, 8, 12, 16};
			iterator = Arrays.stream(rNumbers).iterator();
			assertEquals(new Matrix(4, 4, iterator::next), multiplier.apply(a, b));
		}
		{
			// compare to basic impl.
			Matrix a = new Matrix(256, 128, new Random(13)::nextDouble);
			Matrix b = new Matrix(128, 256, new Random(23)::nextDouble);
			Matrix c = a.multiply(b);
			Matrix d = multiplier.apply(a, b);
			for (int row = 0; row < c.rows(); row++) {
				for (int col = 0; col < c.rows(); col++) {
					assertEquals(c.getValue(row, col), d.getValue(row, col), 0.0000001);
				}
			}
		}
	}
}
