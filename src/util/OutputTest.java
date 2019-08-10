/**
 * 
 */
package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author yzc
 *
 */
public class OutputTest {

	/**
	 * Output testString into file test.txt
	 * Note: bufferedWriter will close in the end of the method, so do not use this method in a loop
	 * @param testString String to be output
	 */
	public static void output(String testString) {
		output(testString, "test.txt");
	}

	/**
	 * Output testString into file
	 * Note: bufferedWriter will close in the end of the method, so do not use this method in a loop
	 * @param testString String to be output
	 * @param fileName file name with path
	 */
	public static void output(String testString, String fileName) {
		output(testString, fileName, false);
	}

	/**
	 * Output testString into file
	 * Note: bufferedWriter will close in the end of the method, so do not use this method in a loop
	 * @param testString String to be output
	 * @param fileName file name with path
	 * @param append boolean value specifying file writing way, append or not
	 */
	public static void output(String testString, String fileName, boolean append) {
		try {
			FileWriter fileWriter = new FileWriter(fileName, append);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(testString);				
			bufferedWriter.flush();
			bufferedWriter.close();	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
