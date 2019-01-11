import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.math.MathContext;

public class AttackRSA {

	public static void main(String[] args) {
		String filename = "input.txt";
		BigInteger[] N = new BigInteger[3];
		BigInteger[] e = new BigInteger[3];
		BigInteger[] c = new BigInteger[3];
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			for (int i = 0; i < 3; i++) {
				String line = br.readLine();
				String[] elem = line.split(",");
				N[i] = new BigInteger(elem[0].split("=")[1]);
				e[i] = new BigInteger(elem[1].split("=")[1]);
				c[i] = new BigInteger(elem[2].split("=")[1]);
			}
			br.close();
		} catch (Exception err) {
			System.err.println("Error handling file.");
			err.printStackTrace();
		}
		BigInteger m = recoverMessage(N, e, c);
		System.out.println("Recovered message: " + m);
		System.out.println("Decoded text: " + decodeMessage(m));
	}

	public static String decodeMessage(BigInteger m) {
		return new String(m.toByteArray());
	}

	/**
	 * Tries to recover the message based on the three intercepted cipher texts.
	 * In each array the same index refers to same receiver. I.e. receiver 0 has
	 * modulus N[0], public key d[0] and received message c[0], etc.
	 * 
	 * @param N
	 *            The modulus of each receiver.
	 * @param e
	 *            The public key of each receiver (should all be 3).
	 * @param c
	 *            The cipher text received by each receiver.
	 * @return The same message that was sent to each receiver.
	 */
	private static BigInteger recoverMessage(BigInteger[] N, BigInteger[] e,
			BigInteger[] c) {
		// TODO Solve assignment.
		BigInteger bigN = N[0].multiply(N[1]).multiply(N[2]);
		BigInteger[] smallN = new BigInteger[3];
		BigInteger[] Y = new BigInteger[3];
		BigInteger res = BigInteger.ZERO;

		for (int i = 0;i<3 ;i++ ) {
			smallN[i]=bigN.divide(N[i]);
			Y[i]=smallN[i].modInverse(N[i]);
			res =res.add(c[i].multiply(smallN[i]).multiply(Y[i]));

		}

		return cbrt(res.mod(bigN));
	}
	static public BigInteger cbrt(BigInteger val) {
		return root(3, new BigDecimal(val)).toBigInteger();
	}
	static private BigDecimal root(final int n, final BigDecimal x) {
		if (x.compareTo(BigDecimal.ZERO) < 0) {
			throw new ArithmeticException("negative argument " + x.toString()
					+ " of root");
		}
		if (n <= 0) {
			throw new ArithmeticException("negative power " + n + " of root");
		}
		if (n == 1) {
			return x;
		}
		/* start the computation from a double precision estimate */
		BigDecimal s = new BigDecimal(Math.pow(x.doubleValue(), 1.0 / n));
		/*
		 * this creates nth with nominal precision of 1 digit
		 */
		final BigDecimal nth = new BigDecimal(n);
		/*
		 * Specify an internal accuracy within the loop which is slightly larger
		 * than what is demanded by ÂepsÂ below.
		 */
		final BigDecimal xhighpr = scalePrec(x, 2);
		MathContext mc = new MathContext(2 + x.precision());
		/*
		 * Relative accuracy of the result is eps.
		 */
		final double eps = x.ulp().doubleValue() / (2 * n * x.doubleValue());
		for (;;) {
			/*
			 * s = s -(s/n-x/n/s^(n-1)) = s-(s-x/s^(n-1))/n; test correction
			 * s/n-x/s for being smaller than the precision requested. The
			 * relative correction is (1-x/s^n)/n,
			 */
			BigDecimal c = xhighpr.divide(s.pow(n - 1), mc);
			c = s.subtract(c);
			MathContext locmc = new MathContext(c.precision());
			c = c.divide(nth, locmc);
			s = s.subtract(c);
			if (Math.abs(c.doubleValue() / s.doubleValue()) < eps) {
				break;
			}
		}
		return s.round(new MathContext(err2prec(eps)));
	} /* BigDecimalMath.root */

	/**
	 * Append decimal zeros to the value. This returns a value which appears to
	 * have a higher precision than the input.
	 * 
	 * @param x
	 *            The input value
	 * @param d
	 *            The (positive) value of zeros to be added as least significant
	 *            digits.
	 * @return The same value as the input but with increased (pseudo)
	 *         precision.
	 */
	static private BigDecimal scalePrec(final BigDecimal x, int d) {
		return x.setScale(d + x.scale());
	}

	/**
	 * Convert a relative error to a precision.
	 * 
	 * @param xerr
	 *            The relative error in the variable. The value returned depends
	 *            only on the absolute value, not on the sign.
	 * @return The number of valid digits in x. The value is rounded down, and
	 *         on the pessimistic side for that reason.
	 */
	static private int err2prec(double xerr) {
		/*
		 * Example: an error of xerr=+-0.5 a precision of 1 (digit), an error of
		 * +-0.05 a precision of 2 (digits)
		 */
		return 1 + (int) (Math.log10(Math.abs(0.5 / xerr)));
	}


}
