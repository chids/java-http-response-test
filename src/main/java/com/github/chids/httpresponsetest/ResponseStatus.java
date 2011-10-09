package com.github.chids.httpresponsetest;

import java.util.concurrent.atomic.AtomicInteger;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.servlet.http.HttpServletResponse;

public class ResponseStatus implements ResponseStatusMBean
{
    private AtomicInteger status = new AtomicInteger(HttpServletResponse.SC_OK);
    private ObjectName objectName;

    public ResponseStatus() throws Exception
    {
        this.objectName = new ObjectName(getClass().getPackage().getName() + ":type=" + ResponseStatusMBean.class.getSimpleName());
    }

    @Override
    public void setStatus(Integer status)
    {
        this.status.set(status);
    }

    @Override
    public Integer getStatus()
    {
        return this.status.get();
    }

    public void register(final MBeanServer server) throws Exception
    {
        server.registerMBean(this, this.objectName);
    }

    public void unregister(final MBeanServer server) throws Exception
    {
        server.unregisterMBean(this.objectName);
    }
}