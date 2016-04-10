package edu.wvu.stat.rc2;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import edu.wvu.stat.rc2.config.DatabaseConfig;
import edu.wvu.stat.rc2.config.DatabaseConfigImpl;
import io.dropwizard.Configuration;

public class Rc2AppConfiguration extends Configuration {
	public static final String UserSessionKey = "rc2user";
	public static final String LoginTokenKey = "rc2token";
	
	private boolean _prettyPrint;
	private boolean _enableTracing;
	private DatabaseConfig _dbConfig;
	
	public boolean getPrettyPrint() { return _prettyPrint; }
	public void setPrettyPrint(boolean p) { _prettyPrint = p; }
	
	public boolean getEnableTracing() { return _enableTracing; }
	public void setEnableTracing(boolean t) { _enableTracing = t; }
	
	@JsonDeserialize(as=DatabaseConfigImpl.class)
	public DatabaseConfig getDatabaseConfig() { return _dbConfig; }
	public void setDatabaseConfig(DatabaseConfig dbConfig) { _dbConfig = dbConfig; }
	
}
