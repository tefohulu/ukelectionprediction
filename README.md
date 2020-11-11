# Streaming Analytics

# Task

UK’s General Election is happening on 12th December. There are many ways to predict the result, one of them are by using twitter streaming, by analyzing the tweets that tweets their support to a certain party.

This report will explain how Streaming Analytics can predict the result of UK’s General Election, by analyzing tweets and count the occurrence of words that is related to the election.

# Middleware Configuration

Storm is a tool to process Big Data in real-time. This task using a Storm Local Mode in a single JVM with three key components : a nimbus node that acts as the main node that runs the program and distributes them to the clusters, a zookeeper that coordinates the Nimbus and Supervisor condition and manages the Storm Cluster, and Supervisor that does the task and communicate with zookeeper. This mode is chosen because local mode is faster to run and debug than distributed mode, and the data collection from streams did not require huge computational power.

Storm has four features : Spouts, Bolts, Streams, and Topologies. A spout is the source of the data (twitter streams), that taken by using Twitter API, created in Twitter Development.  It collects tweets as tuples and relays them to different Bolts. A bolt represents functions, such as counting, splitting, includes, and ignoring words. The overall schema of Spouts and Bolts are called a Topology. It represents the structure of the program.  In the topology, the parallelism required for each node can be specified and Storm will create threads to run the program.

The twitter analysis of election is useful to predict the winner of the election, and furthermore, to understand the political decisions that will be taken after this election. For example, if Conservatives is predicted to win, UK must be prepared for Brexit, and if Labour is predicted to win, UK must be prepared for a second referendum.

# Data Analytic Design

The topology is shown below. It contains one Spout and three Bolts, for counting, splitting, and ignoring words. 

![Topology](https://github.com/tefohulu/ukelectionprediction/blob/master/Appendix%20A%20-%20Topology.jpg)

In ignoring word bolt, there are one additional feature to include the election keywords. Word splitter splits the tweets into words, deletes all the punctuations, and change all the tweet letters to lower case. The ignoring words bolt will delete and add the words to be counted on word counting bolt. 

The keywords are the twitter names of parties and their leaders, shown below. These keywords represent hashtags (#Conservatives), mentions (@Conservatives), and occurrence in tweets (Conservatives). Hashtags, mentions, and occurrence are considered as a single word, because all of it considered as an interaction between twitter accounts and the Parties.

![Keywords](https://github.com/tefohulu/ukelectionprediction/blob/master/Appendix%20B.JPG)

# Results

This program begins the run on 11.00 pm on 8th of December and ends in the next 24 hours. The results are shown below, showing the results in Pie Chart and results from the terminal. It shows the results every 5 seconds. 

![Results](https://github.com/tefohulu/ukelectionprediction/blob/master/Appendix%20C%20-%20Prediction%20Result.jpg)

![Screenshot](https://github.com/tefohulu/ukelectionprediction/blob/master/Appendix%20D%20-%20Result%20Screenshot.jpg)

The analysis also only shows the results from the major parties from England, Scotland, and Wales because those parties have the most votes in last election.

The results shown that the Conservative Party will win the election with a huge gap from the second most popular party, Labour. The third-placed party is the Liberal Democrats, followed by Brexit Party. The regional parties are following the results, with Scottish National Party and Plaid Cymru on the 5th and 7th position, sandwiching The Green Party. Finally, Change Party is the last placed, with only 1 occurrence of tweet in 24 hours.

# Discussions of Results

The real results of the election successfully predict the top four parties that wins the election : Conservatives, Labour, SNP, and Libdems, with the position of SNP and Libdems swapped on the real result. However, the prediction results failed to predict votes percentage. Green Party and Brexit Party did not get a single vote. The real results of the election and the comparison shows below.

![RealResult](https://github.com/tefohulu/ukelectionprediction/blob/master/Appendix%20E%20-%20Real%20result%20of%20the%20Election.jpg)

![Comparison](https://github.com/tefohulu/ukelectionprediction/blob/master/Appendix%20F.JPG)

Although the twitter prediction successfully predicts Conservatives as the winner of the election, the rest of the prediction is quite far from the real results, as it did not accurately predict the percentage of votes for each party. The prediction result of the General Election of is won by the Conservative Party, followed by the Labour Party. The next-placed parties are Libdems, Brexit Party, SNP, Green, Cymru, and Change. 

# Conclusions and Recommendation

Although this result has already represented the GE results, this program can be improved using Storm in Distributed mode instead of local mode. This involves nimbus on a remote cluster. This improvement allows more tweets to be analyzed, improving the accuracy of the prediction. The accuracy of the analysis will also be improved if the program runs longer (3 months before the election). The result can also be put to a database instead of just showing in a terminal. 

Another important aspect to be considered is the sentiment of the tweets. A tweet that mentions a party name can leads to positive or negative sentiment to that party. Thus, a tweet that mentions a negative sentiment to a party should reduce the score, and the positive sentiment should add the score. This analysis is a part of Natural Language Processing, and successfully implemented in to predict election in other countries by using Machine Learning Algorithms. 

Twitter has another feature that should be considered, they are Retweets and Likes, that means a tweet that “agrees” another tweet. These features can make a twitter account tweets them without needing to tweet by themselves. A program should consider this number of Retweets and Likes, because a tweet that only have  a small number of retweets and likes is less important than tweets that have more retweets and likes.

Location can also become an important aspect to the analysis. If this election runs only in Scotland instead of Great Britain, the SNP will have more votes, as well as Plaid Cymru in Wales. This location-based prediction can help a voter to find out the most popular party in their location.
