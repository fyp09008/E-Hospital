package secretsharing;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class WriteTextFile {
	public static void write(String inLine){
		try {
			String outputFileName = "recombined_secret.txt";
			FileWriter outputFileReader = new FileWriter(outputFileName);
			PrintWriter outputStream = new PrintWriter(outputFileReader);
			outputStream.println(inLine);
			outputStream.close();
		} catch (IOException e) {
			System.out.println("IOException:");
			e.printStackTrace();
		}
	}
}
