package bts.delation.handlers;

import bts.delation.model.CustomOAuth2User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;

@RequiredArgsConstructor
public class SecurityInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
         if (Objects.nonNull(modelAndView)) {
             SecurityContext context = SecurityContextHolder.getContext();

             if (context.getAuthentication().isAuthenticated()) {
                 return;
             }

             CustomOAuth2User principal = (CustomOAuth2User) context.getAuthentication().getPrincipal();

             modelAndView.addObject("principal", principal);
         }
    }
}
