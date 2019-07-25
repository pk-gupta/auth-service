package org.marlabs.consumer.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccessCtrlFilter implements Filter {

    private static final Logger LOG = LoggerFactory
	    .getLogger(AccessCtrlFilter.class);

    public void destroy() {
	// TODO Auto-generated method stub

    }

    public void doFilter(ServletRequest req, ServletResponse res,
	    FilterChain chain) throws IOException, ServletException {
	LOG.info("**************doFilter************************START");
	HttpServletRequest request = (HttpServletRequest) req;
	HttpServletResponse response = (HttpServletResponse) res;

	response.addHeader("Access-Control-Allow-Origin", "*");

	if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
	    response.setHeader("Access-Control-Allow-Methods",
		    "POST,GET,DELETE");
	    response.setHeader("Access-Control-Max-Age", "3600");
	    response.setHeader(
		    "Access-Control-Allow-Headers",
		    "content-type,access-control-request-headers,access-control-request-method,accept,origin,authorization,x-requested-with");
	    response.setStatus(HttpServletResponse.SC_OK);
	} else {
	    chain.doFilter(req, res);
	}
	LOG.info("**************doFilter************************END");
    }

    public void init(FilterConfig arg0) throws ServletException {
	// TODO Auto-generated method stub

    }

}
