package sfriberg.matrixmultiplication;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class MatrixBenchmark {

	@Param({"16", "64", "128", "256", "512", "1024", "2048", "3000"})
	private int size;

	private Matrix a;
	private Matrix b;

	@Setup
	final public void setup() {
		Random srand = new Random(123456789L);
		a = new Matrix(size, size, srand::nextDouble);
		b = new Matrix(size, size, srand::nextDouble);
	}

	@Benchmark
	public Matrix testMultiply() {
		return a.multiply(b);
	}

	@Benchmark
	public Matrix testMultiplyInlined() {
		return a.multiplyInlined(b);
	}

	@Benchmark
	public Matrix testMultiplyBlock() {
		return a.multiplyBlock(b);
	}

	@Benchmark
	public Matrix testMultiplyReordedInlined() {
		return a.multiplyReorderedInlined(b);
	}
}
