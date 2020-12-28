import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Driver {

	public static int items = 1682;
	public static int users = 943;
	static int data[][] = new int [items][users];
	
	public static void main(String[] args) {
		if(args.length != 2){
			System.out.println("Please check your inputs.");
		}
		
		String inputFileName = args[0];
		String outputFileName = args[1];
		
		int data[][];
		try {
			data = GenerateMatrix(inputFileName);
			CollabrativeFilteringAlgorithm sm = new CollabrativeFilteringAlgorithm(data);
			sm.calculateCosineSimilarity(data);
			writeOutput(outputFileName);
			 
			System.out.println("Writing an output to the file is done");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//generating the matrix from the given input after reading and performing the split.
	public static int[][] GenerateMatrix(String inputFileName) throws FileNotFoundException
	{
		String temp[] = null; 
		File file = new File(inputFileName);
		
		if (!file.exists()){
			System.out.println("The file does not exist.");
			System.exit(0);
		}
		
		if(file.length() != 0)
		{	
			System.out.println("File is getting processed into matix...");
			FileReader reader =  null;
			Scanner sc = null;
			
			reader = new FileReader(file);
			sc = new Scanner(reader);
			
			while(sc.hasNextLine()){
				String textLine = sc.nextLine();
				temp = textLine.split(" ");
				
				int itemIndex = Integer.parseInt(temp[1]);
				int userIndex = Integer.parseInt(temp[0]);
				int rating = Integer.parseInt(temp[2]);
				data[itemIndex-1][userIndex-1] = rating;
			}
			sc.close();
		}
		else
		{
			System.out.println("Please check the file if it is Empty");
			System.exit(0);
		}
		return data;
	}
	
	//Writting the output to output file.
	@SuppressWarnings("resource")
	public static void writeOutput(String outputFileName) throws IOException{
		BufferedWriter output = new BufferedWriter(new FileWriter(outputFileName));
		
			for(int u=0;u<Driver.users;u++){
				for(int i = 0;i<Driver.items;i++){
					String user = Integer.toString(u+1);
					String item = Integer.toString(i+1);
					if(data[i][u] >= 1  && data[i][u] <= 5 )
					{
						data[i][u] = data[i][u];
					}
					else if(data[i][u] > 5)
					{
						data[i][u] = 5;
					}if(data[i][u] == 0){
						data[i][u] = 1;
					}
					String weight= Integer.toString(data[i][u]);
					output.write(user+" "+item+" "+weight);
					output.newLine();
					output.flush();	
			}
		}

	}
}