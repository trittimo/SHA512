package trittimo;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

// This class is where the main body of the algorithm is defined
public class SHA512 {
	public static int DEBUG = 0;
	
	public static void main(String[] args) throws IOException {
		// If the user isn't using the program correctly, or they want help
		if (args.length < 1 || args[0].equals("help")) {
			System.err.println("Usage: SHA512 <input file> <debug level>");
			System.err.println("       Debug level description:");
			System.err.println("         0: No debugging - only the final hash is printed");
			System.err.println("         1: Minimal debugging - only the block hashes are printed");
			System.err.println("         2: Maximum debugging - W values and abcdefg for each round are printed");
			System.exit(-1);
		}
		
		// If they provide a debug value, set it
		if (args.length > 1) {
			DEBUG = Integer.parseInt(args[1]);
		}
		
		File inputFile = new File(args[0]);
		
		if (!inputFile.isFile()) {
			System.err.println("Input file '" + args[0] + "' does not exist!");
			System.exit(-2);
		}
		
		// Read the entire input file as UTF-8
		String input = new String(Files.readAllBytes(inputFile.toPath()), StandardCharsets.UTF_8);

		// A strange bug occurs on Windows since it adds a carriage return as well as a newline
		// Thus we have to get rid of all carriage returns in the read input
		input = input.replaceAll("\r\n", "\n");

		// Do the hash
		String hashed = SHA512.hash(input.getBytes());
		
		// Print it out
		System.out.println(hashed);
	}
	
	
	// Does the actual hash
	public static String hash(byte[] input) {
		// First pad the input to the correct length, adding the bits specified in the SHA algorithm
		input = Logic.pad(input);

		// Break the padded input up into blocks
		long[][] blocks = Logic.toBlocks(input);
		
		// And get the expanded message blocks
		long[][] W = Logic.Message(blocks);
		
		// Set up the buffer which will eventually contain the final hash
		// Initially, it's set to the constants provided as part of the algorithm
		long[] buffer = Constants.IV.clone();

		// For every block
		for (int i = 0; i < blocks.length; i++) {
			// a-h is set to the buffer initially
			long a = buffer[0];
			long b = buffer[1];
			long c = buffer[2];
			long d = buffer[3];
			long e = buffer[4];
			long f = buffer[5];
			long g = buffer[6];
			long h = buffer[7];
			
			// Run 80 rounds of the SHA-512 compression function on a-h
			for (int j = 0; j < 80; j++) {
				long t1 = h + Logic.Sigma1(e) + Logic.Ch(e, f, g) + Constants.K[j] + W[i][j];
				long t2 = Logic.Sigma0(a) + Logic.Maj(a, b, c);
				h = g;
				g = f;
				f = e;
				e = d + t1;
				d = c;
				c = b;
				b = a;
				a = t1 + t2;
				
				// Print out a-h if the debug mode is >= 2
				if (DEBUG >= 2) {
					System.out.printf("R%d abcdefgh: %016x %016x %016x %016x %016x %016x %016x %016x\n", j, a, b, c, d, e, f, g, h);
				}
			}

			// After finishing the compression, save the state to the buffer
			buffer[0] = a + buffer[0];
			buffer[1] = b + buffer[1];
			buffer[2] = c + buffer[2];
			buffer[3] = d + buffer[3];
			buffer[4] = e + buffer[4];
			buffer[5] = f + buffer[5];
			buffer[6] = g + buffer[6];
			buffer[7] = h + buffer[7];

			// If the debug mode is >= 1, print out the block hash
			if (DEBUG >= 1) {
				System.out.printf("Hash for block %d: %016x %016x %016x %016x %016x %016x %016x %016x\n", i, buffer[0], buffer[1], buffer[2], buffer[3], buffer[4], buffer[5], buffer[6], buffer[7]);
			}
		}
		
		// After everything is done, return the final hash as a string
		String result = "";
		for (int i = 0; i < 8; i++) {
			result += String.format("%016x", buffer[i]);
		}
		
		return result;
	}
}
