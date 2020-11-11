package com.kaviddiss.storm;

import backtype.storm.Config;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;
import twitter4j.*;

import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
// This spout collects tweets to be pushed to Word Splitter Bolt
@SuppressWarnings({ "rawtypes", "serial" })
public class TwitterSampleSpout extends BaseRichSpout {
	private SpoutOutputCollector collector; // Creates a tweet collector for the linked list
	private LinkedBlockingQueue<Status> queue; // Creates a new linked list of tweets
	private TwitterStream twitterStream; // Creates a new TwitterStream

	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		// Create a default queue of 50000 tweets
		queue = new LinkedBlockingQueue<Status>(50000);
		this.collector = collector;

		StatusListener listener = new StatusListener() {
			@Override
			public void onStatus(Status status) {
				queue.offer(status);
			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice sdn) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTrackLimitationNotice(int i) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onScrubGeo(long l, long l1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStallWarning(StallWarning stallWarning) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onException(Exception e) {
				// TODO Auto-generated method stub
			}
		};
		// Generates new twitter stream
		TwitterStreamFactory factory = new TwitterStreamFactory();
		twitterStream = factory.getInstance();
		twitterStream.addListener(listener);
		twitterStream.sample();
	}

	@Override
	// Collects next tuple of the stream
	public void nextTuple() {
		Status ret = queue.poll();
		if (ret == null) {
			Utils.sleep(50); // If there is no tweets, the queue sleeps and try again
		} else {
			collector.emit(new Values(ret)); // If there is tweets, it emits as ret
		}
	}

	@Override
	public void close() {
		twitterStream.shutdown();
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		Config ret = new Config();
		ret.setMaxTaskParallelism(1);
		return ret;
	}

	@Override
	public void ack(Object id) {
		// TODO Auto-generated method stub
	}

	@Override
	public void fail(Object id) {
		// TODO Auto-generated method stub
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("tweet")); 
		// Connects spout to Word Splitter Bolt
	}
}
