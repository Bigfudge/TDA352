import java.io.BufferedReader;
import java.io.FileReader;

import javax.xml.bind.DatatypeConverter;

public class CBCXor {

	public static void main(String[] args) {
		String filename = "input.txt";
		byte[] first_block = null;
		byte[] encrypted = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			first_block = br.readLine().getBytes();
			encrypted = DatatypeConverter.parseHexBinary(br.readLine());
			br.close();
		} catch (Exception err) {
			System.err.println("Error handling file.");
			err.printStackTrace();
			System.exit(1);
		}
		String m = recoverMessage(first_block, encrypted);
		System.out.println("Recovered message: " + m);
	}

	/**
	 * Recover the encrypted message (CBC encrypted with XOR, block size = 12).
	 * 
	 * @param first_block
	 *            We know that this is the value of the first block of plain
	 *            text.
	 * @param encrypted
	 *            The encrypted text, of the form IV | C0 | C1 | ... where each
	 *            block is 12 bytes long.
	 */
	private static String recoverMessage(byte[] first_block, byte[] encrypted) {
		int block_size = 12;
		byte[] k = new byte[12];
		byte[] plain = new byte[encrypted.length];
		for (int i = 0;i<block_size ;i++ ) {
			k[i]= (byte)((int)encrypted[block_size+i]^(int)first_block[i]^(int)encrypted[i]);
		}
		for (int i = 12;i < encrypted.length ;i++ ) {
		 	plain[i-12]=(byte)((int)k[i%block_size]^(int)encrypted[i-block_size]^(int)encrypted[i]);
		 } 
		
		 
		return new String(plain);
	}
}
