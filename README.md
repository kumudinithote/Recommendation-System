# Recommendation-System
Recommender system to predict future user ratings of unrated items based on previous ratings of user and his neighbors



The RMSE of the algorithm comes around 1.0

Steps to Run - 

Requirements - System with JAVA installed on it & input file should be present in the same folder and will have to give absolute path.

Development is done on Eclipse IDE
Steps : you can run directly on eclipse

For Linux:
Step 1 - javac Driver.java CollabrativeFilteringAlgorithm.java
Step 2 - java Driver absolutePath/InputFileName outputFileName
Step 3 - Check output.txt file for output.


Steps of the Algorithm Implemented: -

•	Firstly, user-item matrix construction: - Convert and input a data into user-item rating matrix.

•	Secondly, Similarity computation: - To find the similarity between the user first calculated the cosined distance between the user and then implemented cosined similarity Algorithm.

•	Thirdly, neighborhood selection: - according to the result from the similarity ranking between users, the optimal k-nearest neighbors are selected to implement the predicted set.

•	For the prediction, find K -nearest neighbors, here I have taken 30 neighbors by sorting the vales of cosined distance in the decreasing order.

•	Finally, rating prediction and item recommendation: - after obtaining the nearest neighbors set of the target user, we use the similarity as a weight to get the target user's forecast of the unrated item and form a Top-N list to recommend to the user.

•	With the help of K -nearest neighbors, calculated the weighted average of deviations from neighbors mean to find the final prediction for the item rating from the user using the following formulae.

Reference:

https://www.researchgate.net/publication/331397327_User-based_collaborative_filtering_approach_for_content_recommendation_in_OpenCourseWare_platforms
https://journals.plos.org/plosone/article?id=10.1371/journal.pone.0204003
http://files.grouplens.org/papers/algs.pdf
