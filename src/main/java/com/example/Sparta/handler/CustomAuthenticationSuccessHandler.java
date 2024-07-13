package com.example.Sparta.handler;

import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    public CustomAuthenticationSuccessHandler() {
        super();
        setDefaultTargetUrl("/");
    }
}