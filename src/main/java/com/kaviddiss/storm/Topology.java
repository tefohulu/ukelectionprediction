package com.kaviddiss.storm;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;

public class Topology {
	static final String TOPOLOGY_NAME = "storm-twitter-word-count";

	public static void main(String[] args) {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("twitter4j.properties");
			prop.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		}// Implements the Twitter properties to the program
		
		Config config = new Config(); // Set a new configurations
		config.setMessageTimeoutSecs(120); // Set a timeout for 120 seconds
		TopologyBuilder b = new TopologyBuilder(); // Set a new topology
		
		// Spout the tweets to a stream to be processed to the first bolt
		b.setSpout("TwitterSampleSpout", new TwitterSampleSpout()); 
		// The first bolt that splits tweets into words, with a minimal length of 5
        b.setBolt("WordSplitterBolt", new WordSplitterBolt(5)).shuffleGrouping("TwitterSampleSpout"); 
        // The second bolt that ignores some words and take words in the list
        b.setBolt("IgnoreWordsBolt", new IgnoreWordsBolt()).shuffleGrouping("WordSplitterBolt");
        // The third bolt that counts the occurrence of a word
        // This program runs for 12 hours (1440 minutes), shows top 50 words every 5 seconds
        b.setBolt("WordCounterBolt", new WordCounterBolt(5, 1440 * 60, 50)).shuffleGrouping("IgnoreWordsBolt");

		final LocalCluster cluster = new LocalCluster();
		cluster.submitTopology(TOPOLOGY_NAME, config, b.createTopology());
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			// Runs the program
			public void run() {
				cluster.killTopology(TOPOLOGY_NAME);
				cluster.shutdown();
			}
		});
	}
}