package dk.kvalitetsit.regsj.testkomponent.session;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserContextInterceptor implements HandlerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(UserContextInterceptor.class);
    private final UserContextService userContextService;

    public UserContextInterceptor(UserContextService userContextService) {
        this.userContextService = userContextService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        var handlerMethod = (HandlerMethod)handler;

        var annotation = handlerMethod.getMethod().getAnnotation(UserContext.class);
        if(annotation == null) {
            return true;
        }

        if(!userContextService.isPresentAndValid()) {
            logger.warn("User context is not present.");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);

            return false;
        }

        return true;
    }
}
