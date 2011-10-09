package com.github.chids.httpresponsetest;

import java.io.IOException;
import java.lang.management.ManagementFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
        urlPatterns = { "/" },
        name = "HTTP response code servlet",
        description = "A servlet that simply reponds to any request with the same response code, the response can be changed via JMX")
public class Servlet extends HttpServlet
{
    private static final long serialVersionUID = 463794848312527144L;
    private ResponseStatus status;

    public Servlet() throws Exception
    {
        this.status = new ResponseStatus();
        this.status.register(ManagementFactory.getPlatformMBeanServer());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException
    {
        final Integer status = this.status.getStatus();
        response.addHeader("x-response-from", request.getLocalAddr() + ":" + request.getLocalPort());
        response.setContentType("text/plain; charset=\"utf-8\"");
        response.setStatus(status);
        response.getOutputStream().write(
                ("status: " + status + " from " + request.getLocalAddr() + ":" + request.getLocalPort() + "\n").getBytes());
    }

    @Override
    public void destroy()
    {
        try
        {
            this.status.unregister(ManagementFactory.getPlatformMBeanServer());
        }
        catch(final Exception e)
        {
            e.printStackTrace();
        }
        super.destroy();
    }
}