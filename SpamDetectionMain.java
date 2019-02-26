package spam;

import java.io.IOException;

public class SpamDetectionMain {
	public static void main(String[] args) throws IOException
	{
		TrainDataForSpamDetection obj = new TrainDataForSpamDetection("C:\\Users\\Aminul\\eclipse-workspace\\dbms\\src\\spam\\data.txt");
		//String probabilityFile = obj.makeTarinedFile();
		obj.doCrossValidation("C:\\Users\\Aminul\\eclipse-workspace\\dbms\\File.txt");
	}
}
