package spam;

import java.io.IOException;

public class SpamDetectionMain {
	public static void main(String[] args) throws IOException
	{
		TrainDataForSpamDetection obj = new TrainDataForSpamDetection();		
		obj.doCrossValidation();
	}
}
