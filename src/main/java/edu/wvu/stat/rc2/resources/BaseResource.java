package edu.wvu.stat.rc2.resources;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.wvu.stat.rc2.PermissionChecker;
import edu.wvu.stat.rc2.RCCustomError;
import edu.wvu.stat.rc2.RCError;
import edu.wvu.stat.rc2.persistence.RCLoginToken;
import edu.wvu.stat.rc2.persistence.RCUser;
import edu.wvu.stat.rc2.rs.RCUserPrincipal;
import edu.wvu.stat.rc2.rs.Rc2DBInject;
import edu.wvu.stat.rc2.rs.Rc2SecurityContext;

public abstract class BaseResource {
	final static Logger log= LoggerFactory.getLogger(BaseResource.class);
	
	@Context  HttpServletRequest _servletRequest;
	@Context SecurityContext _securityContext;
	@Rc2DBInject DBI _dbi;
	private RCUser _testUser;
	private PermissionChecker _permChecker;

	public BaseResource() {}

	/** constructor for unit tests to bypass injection */
	BaseResource(DBI dbi, RCUser user) {
		_dbi = dbi;
		_testUser = user;
	}

	protected PermissionChecker getPermChecker() {
		if (null == _permChecker)
			_permChecker = new PermissionChecker(_dbi, getUser());
		return _permChecker;
	}
	
	protected RCUser getUser() {
		Principal p = _securityContext.getUserPrincipal();
		if (p instanceof RCUserPrincipal)
			return ((RCUserPrincipal)p).getUser();
		log.warn("getUser() called on BaseResource subclass while not logged in");
		return _testUser;
	}
	
	protected RCLoginToken getLoginToken() {
		if (_securityContext instanceof Rc2SecurityContext) {
			Rc2SecurityContext ctx = (Rc2SecurityContext)_securityContext;
			return ctx.getToken();
		}
		return (RCLoginToken) _servletRequest.getAttribute("loginToken");
	}
	
	public void throwRestError(RCRestError error) throws WebApplicationException {
		Response rsp = Response.status(error.getHttpCode())
							.entity(Arrays.asList(error))
							.build();
		throw new WebApplicationException(rsp);
		
	}

	public void throwCustomRestError(RCRestError error, String details) throws WebApplicationException {
		RCCustomError cerr = new RCCustomError(error, details);
		Response rsp = Response.status(error.getHttpCode())
							.entity(Arrays.asList(cerr))
							.build();
		throw new WebApplicationException(rsp);
		
	}

	public List<RCError> formatErrorResponse(RCRestError error) {
		return Arrays.asList(error);
	}

	public List<RCError> formatErrorResponse(List<RCError> errors) {
		return errors;
	}
	
	public Map<String,Object> formatSingleResponse(String name, Object obj) {
		Map<String,Object> map = new HashMap<>();
		map.put("status", 0);
		map.put(name, obj);
		return map;
	}
}
