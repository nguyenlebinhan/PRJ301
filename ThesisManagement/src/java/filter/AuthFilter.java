package filter;

import jakarta.servlet.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Filter để kiểm tra authentication và authorization
 */

public class AuthFilter implements Filter {
    private static final Logger LOGGER = Logger.getLogger(AuthFilter.class.getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("AuthFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        HttpSession session = httpRequest.getSession(false);
        String requestURI = httpRequest.getRequestURI();
        
        // Allow access to login page, some password recovery source and static resources

        if (requestURI.contains("/login") || 
            requestURI.contains("/forget-password") || // Trang nhập email
            requestURI.contains("/reset-password") ||  // Trang đặt lại mật khẩu mới
            requestURI.contains("/css/") || 
            requestURI.contains("/js/") || 
            requestURI.contains("/images/")) {

            chain.doFilter(request, response);
            return;
        }
        
        // Check if user is not logged in
        if (session == null || session.getAttribute("user") == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/auth/login");
            return;
        }
        
        // Check authorization based on role
        String role = (String) session.getAttribute("role");
        if (role != null) {
            if (requestURI.contains("/admin") && !role.equals("ADMIN")) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
                return;
            }
            if (requestURI.contains("/student") && !role.equals("STUDENT")) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
                return;
            }
            if (requestURI.contains("/lecturer") && !role.equals("LECTURER")) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
                return;
            }
        }
        
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        LOGGER.info("AuthFilter destroyed");
    }
}

