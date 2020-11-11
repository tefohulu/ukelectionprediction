package com.kaviddiss.storm;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class WordCounterBolt extends BaseRichBolt {

	private static final long serialVersionUID = 2706047697068872387L;
	private static final Logger logger = LoggerFactory.getLogger(WordCounterBolt.class);
	private final long logIntervalSec;
    private final long clearIntervalSec;
    private final int topListSize;
    private Map<String, Long> counter;
    private long lastLogTime;
    private long lastClearTime;

    public WordCounterBolt(long logIntervalSec, long clearIntervalSec, int topListSize) {
        this.logIntervalSec = logIntervalSec;
        this.clearIntervalSec = clearIntervalSec;
        this.topListSize = topListSize;
    }// Gets Parameter to run the program

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector collector) {
        counter = new HashMap<String, Long>(); // Hashmap that contains the words and the occurrence
        lastLogTime = System.currentTimeMillis(); // Determine the last time program runs
        lastClearTime = System.currentTimeMillis(); // Determine the last time application clears
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
    	// TODO Auto-generated method stub
    }

    @Override
    public void execute(Tuple input) {
        // Count occurrence of each words on the previous bolt
    	String word = (String) input.getValueByField("word");
        Long count = counter.get(word);
        count = count == null ? 1L : count + 1;
        counter.put(word, count);
        
        long now = System.currentTimeMillis();
        long logPeriodSec = (now - lastLogTime) / 1000; // Runs the program for a period of time
        if (logPeriodSec > logIntervalSec) {
        	logger.info("\n\n");
        	logger.info("Word count: "+counter.size());
            publishTopList();
            lastLogTime = now;
        }
    }

    private void publishTopList() {
        // calculate top list:
        SortedMap<Long, String> top = new TreeMap<Long, String>();
        for (Map.Entry<String, Long> entry : counter.entrySet()) {
            long count = entry.getValue();
            String word = entry.getKey();
            // Change the hashmap sequence based on its occurrence
            top.put(count, word);
            if (top.size() > topListSize) {
                top.remove(top.firstKey());
            }
        }

        // Output top list:
        for (Map.Entry<Long, String> entry : top.entrySet()) {
            logger.info(new StringBuilder("top - ").append(entry.getValue()).append('|').append(entry.getKey()).toString());
        }

        // Clear top list
        long now = System.currentTimeMillis();
        if (now - lastClearTime > clearIntervalSec * 1000) {
            counter.clear();
            lastClearTime = now;
        }
    }
}
