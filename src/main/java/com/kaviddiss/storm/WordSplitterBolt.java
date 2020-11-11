package com.kaviddiss.storm;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import twitter4j.Status;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WordSplitterBolt extends BaseRichBolt {
	
	private static final long serialVersionUID = 5151173513759399636L;
	private final int minWordLength;
    private OutputCollector collector;
    private Set<String> HASHTAGS_LIST = new HashSet<String>(Arrays.asList(new String[] {
            "#ge2019"})); // Keywords that will be ignored

    public WordSplitterBolt(int minWordLength) {
    	// Initialize the length of the words
        this.minWordLength = minWordLength; 
    }

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector collector) {
    	// Initialize the collector to collect the words
    	this.collector = collector; 
    }

    @Override
    public void execute(Tuple input) {
        Status tweet = (Status) input.getValueByField("tweet"); // Get tweets
        String lang = tweet.getUser().getLang(); // Get tweets lang
        String text = tweet.getText().replaceAll("[\\p{Punct}&&[^#]]", " ").replaceAll("\\r|\\n", "").toLowerCase(); 
        // Change all characters to lower case
        String[] words = text.split(" "); // Split tweets to words
        for (String word : words) {
        	if (HASHTAGS_LIST.contains((words))){
	            if (word.length() >= minWordLength) {
	            	// Enters words to collector if word length more or same of 5
	                collector.emit(new Values(lang, word));
            }
            }
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    	// Declare output to be processed to IgnoreWord Bolts
    	declarer.declare(new Fields("lang", "word")); 
    }
}
