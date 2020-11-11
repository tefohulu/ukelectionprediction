package com.kaviddiss.storm;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IgnoreWordsBolt extends BaseRichBolt {
	
	private static final long serialVersionUID = 6069146554651714100L;
	private Set<String> IGNORE_LIST = new HashSet<String>(Arrays.asList(new String[] {
            "http", "https", "the", "you", "que", "and", "for", "that", "like", 
            "have", "this", "just", "with", "all", "get", "about", "can", "was", 
            "not", "your", "but", "are", "one", "what", "out", "when", "get", "lol", 
            "now", "para", "por", "want", "will", "know", "good", "from", "las", "don", 
            "people", "got", "why", "con", "time", "would",
    })); // Keywords that will be ignored
	private Set<String> PARTY_LIST = new HashSet<String>(Arrays.asList(new String[] {
            "conservatives", "uklabour", "thesnp", "libdems", 
            "forchange_now", "plaid_cymru", "thegreenparty", "brexitparty_uk",
            "borisjohnson", "jeremycorbyn", "nicolasturgeon", "joswinson", 
            "anna_soubry", "adamprice", "jon_bartley", "sianberry", "nigel_farage"
    })); // Keywords that searched : Party accounts and Leaders accounts
    private OutputCollector collector; 

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector collector) {
        this.collector = collector; // Declares a new collector 
    }

    @Override
    public void execute(Tuple input) {
        String lang = (String) input.getValueByField("lang");
        String word = (String) input.getValueByField("word");
        // The collector only emits the word that is on the list
        if ((!IGNORE_LIST.contains(word)) & PARTY_LIST.contains(word)) { 
            collector.emit(new Values(lang, word));
        }
    }

    @Override
    // Declares an output to be processed in WordCounter Bolt
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("lang", "word"));
    }
}