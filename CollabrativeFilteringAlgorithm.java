import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

public class CollabrativeFilteringAlgorithm {
	
	public int[][] data;
	private Map<Integer,ArrayList<Integer>> userItemList;
	int neighbours = 30;
	double calculateCosineDistance[][];
	private Map<Integer,ArrayList<User>> UserItemListNeighbours;

	public CollabrativeFilteringAlgorithm(int data[][]){
		this.data = data;
		userItemList = new LinkedHashMap<Integer,ArrayList<Integer>>();
		
		calculateCosineDistance = new double[Driver.items][Driver.users];
		for(int i=0;i<Driver.users;i++){
			for(int j=0;j<Driver.users;j++){
				calculateCosineDistance[i][j] = -1;
			}
		}
		
		UserItemListNeighbours = new LinkedHashMap<Integer,ArrayList<User>>();
	}
	
	
	public void calculateCosineSimilarity(int data[][]){
		
		double normalizedData[] = new double[Driver.users];  //contains average rating given by user
		
		for(int j=0;j<Driver.users;j++){
			for(int i=0;i<Driver.items;i++)
			{
				//my list has entries for item for which uesr has not rated
				if(data[i][j] == 0)
				{
					if(!userItemList.containsKey(j+1)){
						userItemList.put(j+1, new ArrayList<Integer>());
					}
					userItemList.get(j+1).add(i+1);
				}
				
				normalizedData[j] += data[i][j] * data[i][j];
				normalizedData[j] = Math.sqrt(normalizedData[j]);
			}
		}

	
		//Calculations for final cosine distance and the devisor.
		double dot_product=0;
		for(int u=0;u<Driver.users;u++)
		{
			for(int us=u+1;us<Driver.users;us++)
			{
				dot_product  = 0;
				for(int i=0;i<Driver.items;i++)
				{
					if(calculateCosineDistance[u][us] == -1)
						dot_product += data[i][u] * data[i][us]; 
				}
				double value = (normalizedData[u] * normalizedData[us]);
				calculateCosineDistance[u][us] = dot_product/value;
				calculateCosineDistance[us][u] = dot_product/value;
			}
		}
	
		

		//for finding the K nearest neighbours
		for(int i=0;i<Driver.users;i++)
		{
			for(int j=0;j<Driver.users;j++)
			{
				User n = new User(j, calculateCosineDistance[i][j]);
				if(!UserItemListNeighbours.containsKey(i)){
					UserItemListNeighbours.put(i,new ArrayList<User>());
				}
				UserItemListNeighbours.get(i).add(n);
			}
		}
		
		Set<Map.Entry<Integer, ArrayList<User>>> entryKey = UserItemListNeighbours.entrySet();
		for (Map.Entry<Integer, ArrayList<User>> entry : entryKey) {
			Integer key = entry.getKey();
			ArrayList<User> value = entry.getValue();
			Collections.sort(value);
			UserItemListNeighbours.put(key, new ArrayList<>(value.subList(0, neighbours)));
		}
		
		/*
		 *Calculation for predicting the user-item pair after performing the weighted average of deviations from the neighbor's mean 
		 */
		
		//calculating Average rating for each user
		double averageRating[] = new double[Driver.users];
		int numberOfNonZeros=0;
		
		for(int i=0;i<Driver.users;i++)
		{
			int sum =0;
			numberOfNonZeros=0;
			
			for(int j=0;j<Driver.items;j++)
			{
				if(data[j][i] != 0){
					sum = sum + data[j][i];
					numberOfNonZeros++;
				}
			}
			averageRating[i] = sum/numberOfNonZeros;
		}
		
		//calculation for weighted formulae for the predictio of ratings.
		for (Map.Entry<Integer, ArrayList<Integer>> entry : userItemList.entrySet()) {
			
			Integer key = entry.getKey();
			ArrayList<Integer> value = entry.getValue(); //products which user hasn't rated
			for(Integer temp : value){
				
				double itemRatings=0;
				double numerator =0;
				double WeightedSummation = 0;
				double predictedRatings = 0;
				double ratingMultiplier=0;
				
				ArrayList<User> numberOfNeighOfAKey = new ArrayList<User>();
				numberOfNeighOfAKey  = UserItemListNeighbours.get(key-1); //neighbors for the user
				for(User tempNeighForKey : numberOfNeighOfAKey){

					int tempNeigh = tempNeighForKey.position;
					if(data[temp-1][tempNeigh]!=0){
						itemRatings = Math.abs(data[temp-1][tempNeigh] - averageRating[tempNeigh]);
						ratingMultiplier += itemRatings * calculateCosineDistance[key-1][tempNeigh];
						WeightedSummation += calculateCosineDistance[key-1][tempNeigh];
					}
					
				}
				numerator = ratingMultiplier;
				predictedRatings  =  averageRating[key-1] + numerator/WeightedSummation;
				data[temp-1][key-1] = (int) Math.round(predictedRatings);
			}
		}
		
	}

	private class User implements Comparable<User>{
		
		double value=0;
		int position=0;
		
		public User (int position,double value){
			this.position = position;
			this.value= value;
		}
		
		@Override
		public int compareTo(User o) {
			User n = (User) o;
			double comparevalue = ((User)n).value;
			double res =  comparevalue - this.value ;
			
			return (res > 0) ? 1 : (res < 0) ? -1 : 0;
		}
		
		
	}
}