package spam;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class TrainDataForSpamDetection {
	String inputFile;
	
	
	public TrainDataForSpamDetection(String inputFile)
	{
		this.inputFile = inputFile;
	}
	
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
	
	public String makeTarinedFile() throws IOException
	{
		//BufferedWriter wr = new BufferedWriter(new FileWriter("trainedFile.txt",true));
		FileWriter wr = new FileWriter("File.txt",true);
		ArrayList<ArrayList<Double>> inputArrayList = new ArrayList<ArrayList<Double>>(); 
		inputArrayList = takeInput(inputFile);
		
		
		int numberOfLines = inputArrayList.size() ;
		int numberOfDataInALine = inputArrayList.get(0).size() - 4;
		
		for(int i = 0; i < numberOfDataInALine; i++)
		{
			double tempSpamProbability = 0;
			double tempHamProbability = 0;
			double tempHam = 0;
			double tempSpam = 0;
			double count = 0;
			for(int j = 0; j < numberOfLines; j++)
			{
				if(inputArrayList.get(j).get(numberOfDataInALine + 3) == 1)
					tempSpam += inputArrayList.get(j).get(i);
				else
					tempHam += inputArrayList.get(j).get(i);
				count += inputArrayList.get(j).get(i);
			}
			if(count != 0)
			{
				tempSpamProbability = tempSpam / count;
				tempHamProbability = tempHam / count;
			}
			String toString = tempHamProbability +"," + tempSpamProbability + System.lineSeparator();
			
			wr.write(toString);
			
		}	
		wr.close();
		return "File.txt";
	}	
	public double getPrediction(ArrayList<ArrayList<Double>> tempArrayList,ArrayList<ArrayList<Double>> inputArrayList,ArrayList<ArrayList<Double>> probArrayList)
	{
		double predictionAccuracy;
		int numberOfDataInALine = tempArrayList.get(0).size()-4;
		int correctPrediction = 0;
		for(int i = 0; i < tempArrayList.size(); i++)
		{
			double hamProb = 1.0;
			double spamProb = 1.0;
			
			for(int j = 0; j < numberOfDataInALine; j++)
			{
				if(tempArrayList.get(i).get(j) != 0.0)
				{
					hamProb += java.lang.Math.log(probArrayList.get(j).get(0)+1);
					spamProb += java.lang.Math.log(probArrayList.get(j).get(1)+1);
				}
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
	
	
	public double getAccuracy(String probabilityFile,String dataFile,int seedValue) throws IOException
	{
		ArrayList<ArrayList<Double>> inputArrayList = new ArrayList<ArrayList<Double>>(); 
		ArrayList<ArrayList<Double>> probArrayList = new ArrayList<ArrayList<Double>>(); 
		
		inputArrayList = takeInput(dataFile);
		probArrayList = takeInput(probabilityFile);
		
		int count = inputArrayList.size()/10;
		Random rand = new Random(seedValue);
		int temp = count;
		
		boolean []flag = new boolean[inputArrayList.size()];
		for(int i = 0; i < count; i++)
			flag[i] = false;
		
		ArrayList<ArrayList<Double>> tempArrayList = new ArrayList<ArrayList<Double>>(); 
		while(temp > 0)
		{
			int temp1 = rand.nextInt(inputArrayList.size());
			if(flag[temp1]) 
				continue;
			tempArrayList.add(inputArrayList.get(temp1));
			temp--;
		}
		return getPrediction(tempArrayList,inputArrayList,probArrayList);
	}
	public void doCrossValidation(String probabilityFile) throws IOException
	{
		double accuracy = 0;
		for(int i= 0; i< 10; i++)
			accuracy += getAccuracy(probabilityFile,"C:\\\\Users\\\\Aminul\\\\eclipse-workspace\\\\dbms\\\\src\\\\spam\\\\data.txt",i);
		System.out.println(accuracy*10 + "%");
	}		
}
