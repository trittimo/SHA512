package trittimo;

import java.math.BigInteger;

// Provides the bitwise logical functions used in SHA512
// The mathematical definition of these functions were taken from http://www.iwar.org.uk/comsec/resources/cipher/sha256-384-512.pdf
public class Logic {

	// Used in the compression function
	public static long Ch(long x, long y, long z) {
		return (x & y) ^ (~x & z);
	}
	
	// Used in the compression function
	public static long Maj(long x, long y, long z) {
		return (x & y) ^ (x & z) ^ (y & z);
	}
	
	// Used in the compression function
	public static long rotate(long x, int l) {
		return (x >>> l) | (x << (Long.SIZE - l));
	}
	

	// Used in the compression function
	// Sn = right rotate by n bits
	// Rn = right shift by n bits
	public static long Sigma0(long x) {
		// S28(x) ^ S34(x) ^ S39(x)
		return rotate(x, 28) ^ rotate(x, 34) ^ rotate(x, 39); 
	}
	
	// Used in the compression function
	public static long Sigma1(long x) {
		// S14(x) ^ S18(x) ^ S41(x)
		return rotate(x, 14) ^ rotate(x, 18) ^ rotate(x, 41);
	}
	
	// Used in the message schedule
	public static long _Sigma0(long x) {
		// S1(x) ^ S8(x) ^ R7(x)
		return rotate(x, 1) ^ rotate(x, 8) ^ (x >>> 7);
	}
	
	// Used in the message schedule
	public static long _Sigma1(long x) {
		// S19(x) ^ S61(x) ^ R6(x)
		return rotate(x, 19) ^ rotate(x, 61) ^ (x >>> 6);
	}
	
	// Pads the input byte array
	public static byte[] pad(byte[] input) {
		// Need to append at least 17 bytes (16 for length of the message, and 1 for the '1' bit)
		// then fill with 0s until multiple of 128 bytes
		int size = input.length + 17;
		while (size % 128 != 0) {
			size += 1;
		}
		
		// The padded byte array
		byte[] out = new byte[size];

		// Copy over the old stuff
		for (int i = 0; i < input.length; i++) {
			out[i] = input[i];
		}
		
		// Add the '1' bit
		out[input.length] = (byte) 0x80;
		
		// Somewhat legacy code, was using BigInteger before converting to longs, but it works
		// so why change it
		// Convert the original length of the input to a byte array
		byte[] lenInBytes = BigInteger.valueOf(input.length * 8).toByteArray();

		// And put it at the end of our padded input
		for (int i = lenInBytes.length; i > 0; i--) {
			out[size - i] = lenInBytes[lenInBytes.length - i];
		}
		
		// Print out the total message bits before/after if debug >= 1
		if (SHA512.DEBUG >= 1) {
			System.out.printf("Total message length in bits before padding: %d\n", input.length * 8);
			System.out.printf("Total message length in bits after padding: %d\n", out.length * 8);
		}
		
		return out;
	}
	
	//Converts the byte array input starting at index j into a long
	public static long arrToLong(byte[] input, int j) {
		long v = 0;
		for (int i = 0; i < 8; i++) {
			v = (v << 8) + (input[i + j] & 0xff);
		}
		return v;
	}
	
	// Converts the byte array input into blocks of longs
	public static long[][] toBlocks(byte[] input) {

		// a block has: 1024 bits = 128 bytes = 16 longs
		long[][] blocks = new long[input.length / 128][16];
		
		// For every block
		for (int i = 0; i < input.length / 128; i++) {
			// For each long in a block
			for (int j = 0; j < 16; j++) {
				// Set the block value to the correct one
				blocks[i][j] = arrToLong(input, i * 128 + j * 8);
			}
		}
		return blocks;
	}
	
	
	// Calculates the expanded message blocks W0-W79
	public static long[][] Message(long[][] M) {
		long[][] W = new long[M.length][80];

		// For each block in the input
		for (int i = 0; i < M.length; i++) {
			if (SHA512.DEBUG >= 2)
				System.out.printf("W for block %d\n", i);

			// For each long in the block
			for (int j = 0; j < 16; j++) {
				// Set the initial values of W to be the value of the input directly
				W[i][j] = M[i][j];
				if (SHA512.DEBUG >= 2) {
					System.out.printf("W(%d): %016x\n", j, W[i][j]);
				}
			}

			// For the rest of the values
			for (int j = 16; j < 80; j++) {
				// Do some math from the SHA512 algorithm
				W[i][j] = _Sigma1(W[i][j-2]) + W[i][j-7] + _Sigma0(W[i][j-15]) + W[i][j-16];
				if (SHA512.DEBUG >= 2) {
					System.out.printf("W(%d): %016x\n", j, W[i][j]);
				}
			}
			if (SHA512.DEBUG >= 2)
				System.out.println("=====================================");
		}
		
		return W;
	}
}
