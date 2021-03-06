package edu.wvu.stat.rc2.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

/** RCWorkspace is mostly-immutable. All properties are immutable. The only thing that is mutable 
 	is the list of files. It is stored internally as an immutable copy of the argument to setFiles().
 	So the workspace itself is immutable, but the file list isn't.
 */

@AutoValue
@JsonIgnoreProperties(value={"files"}, allowGetters=true)
public abstract class RCWorkspace {
	
	@JsonCreator
	public static RCWorkspace create(
			@JsonProperty("id") int id, 
			@JsonProperty("version") int version, 
			@JsonProperty("projectId") int projectId,
			@JsonProperty("uniqueId") String uniqueId,
			@JsonProperty("name") String name) 
	{
		return new AutoValue_RCWorkspace(id, version, projectId, uniqueId, name);
	}
	
	public abstract @JsonProperty int getId();
	public abstract @JsonProperty int getVersion();
	public abstract @JsonProperty int getProjectId();
	public abstract @JsonProperty String getUniqueId();
	public abstract @JsonProperty String getName();

	static RCWorkspace createFromResultSet(ResultSet rs) throws SQLException {
		return new AutoValue_RCWorkspace(rs.getInt("id"), rs.getInt("version"), rs.getInt("projectId"), 
				rs.getString("uniqueId"), rs.getString("name"));
	}
	
	private List<RCFile> _files;
	public List<RCFile> getFiles() { return _files; }
	public void setFiles(List<RCFile> files) {
		_files = ImmutableList.copyOf(files);
	}
	
	private RCProject _project;
	@JsonIgnore public RCProject getProject() { return _project; }
	public void setProject(RCProject p) { _project = p; } 
	
	public Optional<RCFile> getFileWithId(int fileId) {
		return _files.stream().filter(f -> f.getId() == fileId).findFirst();
	}
	
	
	public static class RCWorkspaceMapper implements ResultSetMapper<RCWorkspace> {
		public RCWorkspaceMapper() {}
		public RCWorkspace map(int index, ResultSet rs, StatementContext ctx) throws SQLException {
			return RCWorkspace.createFromResultSet(rs);
		}
	}
	
	//queries in RCWorkspaceQueries
}
