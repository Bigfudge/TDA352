// Compilation (CryptoLibTest contains the main-method):
//   javac CryptoLibTest.java
// Running:
//   java CryptoLibTest

public class CryptoLib {

	/**
	 * Returns an array "result" with the values "result[0] = gcd",
	 * "result[1] = s" and "result[2] = t" such that "gcd" is the greatest
	 * common divisor of "a" and "b", and "gcd = a * s + b * t".
	 **/
	public static int[] EEA(int a, int b) {
		// Note: as you can see in the test suite,
		// your function should work for any (positive) value of a and b.
		int t =1;
		int oldT=0;

		int s = 0;
		int oldS=1;

		int r=b;
		int oldR=a;

		int q;

		while(r!=0) {

			q= oldR  / r;

			int tmp = r;
			r= oldR-q*tmp;
			oldR=tmp;

			tmp = s;
			s= oldS-q*tmp;
			oldS=tmp;

			tmp = t;
			t= oldT-q*tmp;
			oldT=tmp;
		}

		int[] result = new int[3];
		result[0] = oldR;
		result[1] = oldS;
		result[2] = oldT;
		return result;
	}


	/**
	 * Returns Euler's Totient for value "n".
	 **/
	public static int EulerPhi(int n) {
		int result =0;

		for(int i =0; i<n;i++) {
			if(gcd(i,n)==1)
				result++;
		}
		return result;
	}

	public static int gcd(int a, int b) {
		while(b!=0) {
			int tmp = a;
			a=b;
			b=tmp % b;
		}

		return a;
	}

	/**
	 * Returns the value "v" such that "n*v = 1 (mod m)". Returns 0 if the
	 * modular inverse does not exist.
	 **/
	public static int ModInv(int n, int m) {
		n=n%m;
		if(n<0) {
			n=m+n;
		}
		for(int x=0;x<m;x++) {
			if((n*x)%m ==1) {
				return x;
			}
		}
		return 0;
	}

	/**
	 * Returns 0 if "n" is a Fermat Prime, otherwise it returns the lowest
	 * Fermat Witness. Tests values from 2 (inclusive) to "n/3" (exclusive).
	 **/
	public static int FermatPT(int n) {

		for(int i =2; i<n/3; i++) {
			if(EEA(i,n)[0]!=1) {
				return 0;
			}
			if(bigMod(i,n-1,n) != 1) {
				return i;
			}
		}

		return 0;
	}
	public static int bigMod(int a, int b, int m) {
		if(b==0) {
			return 0;
		}
		return ((a%m)*(bigMod(a,b-1,m)%m))%m;

	}
	/**
	 * Returns the probability that calling a perfect hash function with
	 * "n_samples" (uniformly distributed) will give one collision (i.e. that
	 * two samples result in the same hash) -- where "size" is the number of
	 * different output values the hash function can produce.
	 **/
	public static double HashCP(double n_samples, double size) {
		return -1;
	}

}
