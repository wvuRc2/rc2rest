package edu.wvu.stat.rc2.rworker.message;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;

public class HelpMessage extends BaseMessage {
	private final long _startTime;
	private final String _topic;
	private List<String> _paths;
	
	@JsonCreator
	public HelpMessage(
			@JsonProperty("msg") String msg,
			@JsonProperty("startTime") long startTime,
			@JsonProperty("helpTopic") String topic,
			@JsonProperty("helpPath") List<String> paths
		)
	{
		super(msg);
		_startTime = startTime;
		_topic = topic;
		_paths = ImmutableList.copyOf(paths);
	}
	
	public long getStartTime() { return _startTime; }
	public String getTopic() { return _topic; }
	public List<String> getPaths() { return _paths; }
}
