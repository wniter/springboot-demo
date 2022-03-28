package com.example.filter.interceptor.filter;

import javax.servlet.*;
import java.io.IOException;
import java.util.Date;

/**
 *过滤器
 *定义一个TimeFilter类，实现javax.servlet.Filter：
 * TimeFilter重写了Filter的三个方法，方法名称已经很直白的描述了其作用，这里不再赘述。
 * 要使该过滤器在Spring Boot中生效，还需要一些配置。这里主要有两种配置方式。
 */

/**
 * 配置方式一
 * @Component注解让TimeFilter成为Spring上下文中的一个Bean，
 * @WebFilter注解的urlPatterns属性配置了哪些请求可以进入该过滤器，/*表示所有请求。
 * // @Component
 * // @WebFilter(urlPatterns = {"/*"})
 */


public class TimeFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("过滤器初始化");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("开始执行过滤器");
        Long start = new Date().getTime();
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("【过滤器】耗时 " + (new Date().getTime() - start));
        System.out.println("结束执行过滤器");
    }

    @Override
    public void destroy() {
        System.out.println("过滤器销毁");
    }
}
