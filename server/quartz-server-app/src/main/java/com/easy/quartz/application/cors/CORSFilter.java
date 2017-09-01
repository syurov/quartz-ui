package com.easy.quartz.application.cors;


import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CORSFilter implements Filter {
  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    final HttpServletResponse response = (HttpServletResponse) res;
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
    response.setHeader("Access-Control-Max-Age", "3600");
    response.setHeader("Access-Control-Allow-Headers", "x-requested-with, Content-Type");
    if (req instanceof HttpServletRequest && ((HttpServletRequest) req).getMethod().equals("OPTIONS")) {
      ((HttpServletResponse) res).setStatus(HttpStatus.OK.value());

    } else
      chain.doFilter(req, res);

  }

  @Override
  public void destroy() {

  }
}

