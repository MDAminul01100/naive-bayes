package spam;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class TrainDataForSpamDetection {
	
	public ArrayList<ArrayList<Double>> takeInput(String inputFILE) throws IOException
	{
		ArrayList<ArrayList<Double>> tempInputArrayList = new ArrayList<ArrayList<Double>>();
		BufferedReader br = new BufferedReader(new FileReader(inputFILE));
		
		while(true)
		{			
			String tempStr = br.readLine();
			if(tempStr == null)
				break;
			
			String[] tempStrArray = tempStr.split(",");
			ArrayList<Double> tempArrayList = new ArrayList<Double>(); 
			
			for(String s : tempStrArray)
				tempArrayList.add(Double.parseDouble(s));
			
			tempInputArrayList.add(tempArrayList);
		}	
		br.close();
		return tempInputArrayList;
	}
	
	public String makeTarinedFile(ArrayList<ArrayList<Double>> inputArrayList) throws IOException
	{
		String trainedFile = "trainedFile.txt";
		BufferedWriter wr = new BufferedWriter(new FileWriter(trainedFile));
	
		int numberOfLines = inputArrayList.size() ;
		int numberOfDataInALine = inputArrayList.get(0).size() - 4;
		
		for(int i = 0; i < numberOfDataInALine; i++)
		{
			double tempSpamProbability = 0;
			double tempHamProbability = 0;
			double tempHamFrequency = 0;
			double tempSpamFrequency = 0;
			double count = 0;
			for(int j = 0; j < numberOfLines; j++)
			{
				count += inputArrayList.get(j).get(i);

				if(inputArrayList.get(j).get(numberOfDataInALine + 3) == 1.0)
					tempSpamFrequency += inputArrayList.get(j).get(i);
				else if(inputArrayList.get(j).get(numberOfDataInALine + 3) == 0.0)
					tempHamFrequency += inputArrayList.get(j).get(i);
			}
			if(count != 0)
			{
				tempSpamProbability = tempSpamFrequency / count;
				tempHamProbability = tempHamFrequency / count;
			}
			String toString = tempHamProbability +"," + tempSpamProbability + System.lineSeparator();
			
			wr.write(toString);
			
		}	
		wr.close();
		return ("C:\\\\Users\\\\aminul\\\\eclipse-workspace\\\\dbms\\\\"+ trainedFile);
	}	
	public double getPrediction(ArrayList<ArrayList<Double>> tempArrayList,ArrayList<ArrayList<Double>> probArrayList)
	{
		
		double predictionAccuracy = 0.0;
		int numberOfDataInALine = tempArrayList.get(0).size()-4;
		int correctPrediction = 0;
		for(int i = 0; i < tempArrayList.size(); i++)
		{
			double hamProb = 0.0;
			double spamProb = 0.0;
			
			for(int j = 0; j < numberOfDataInALine; j++)
			{
				if(tempArrayList.get(i).get(j) == 0.0)
				{
					continue;
				}
				hamProb += java.lang.Math.log(probArrayList.get(j).get(0)+1);
				spamProb += java.lang.Math.log(probArrayList.get(j).get(1)+1);
			}
			
			if((hamProb > spamProb) && (tempArrayList.get(i).get(numberOfDataInALine + 3) == 0.0))
				correctPrediction++;
			else if((hamProb < spamProb) && (tempArrayList.get(i).get(numberOfDataInALine + 3) == 1.0))
				correctPrediction++;
		}
		
		int size = tempArrayList.size();
		predictionAccuracy = (double)correctPrediction / size;
		return predictionAccuracy;
	}
	
	
	public double getAccuracy(String dataFile,int seedValue) throws IOException
	{
		ArrayList<ArrayList<Double>> inputArrayList = new ArrayList<ArrayList<Double>>(); 
		ArrayList<ArrayList<Double>> probArrayList = new ArrayList<ArrayList<Double>>(); 
		ArrayList<ArrayList<Double>> arrayListToTrainData = new ArrayList<ArrayList<Double>>();
		
		inputArrayList = takeInput(dataFile);
		
		int count = inputArrayList.size();
		Random rand = new Random(seedValue);
		int temp = count/10;
		
		boolean []flag = new boolean[count];
		for(int i = 0; i < count; i++)
			flag[i] = false;
		
		ArrayList<ArrayList<Double>> tempArrayList = new ArrayList<ArrayList<Double>>(); 
		while(temp > 0)
		{
			int temp1 = rand.nextInt(inputArrayList.size());
			if(flag[temp1] == true) {continue;}
			
			tempArrayList.add(inputArrayList.get(temp1));
			temp--;
			flag[temp1] = true;
		}
		
		for(int i = 0; i < inputArrayList.size(); i++)
		{
			if(flag[i] == false)
				arrayListToTrainData.add(inputArrayList.get(i));
		}
		String probabilityFile = makeTarinedFile(arrayListToTrainData);
		probArrayList = takeInput(probabilityFile);
		
		return getPrediction(tempArrayList,probArrayList);
	}

	public void doCrossValidation() throws IOException
	{
		double accuracy = 0;
		for(int i= 0; i< 10; i++)
		{
			double temp = getAccuracy("C:\\\\Users\\\\Aminul\\\\eclipse-workspace\\\\dbms\\\\src\\\\spam\\\\input.txt",i);
			accuracy += temp;
		
		}
		System.out.println(accuracy*10 + "%");
	}		
}
