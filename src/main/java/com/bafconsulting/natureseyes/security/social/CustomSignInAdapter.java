package com.bafconsulting.natureseyes.security.social;

import com.bafconsulting.natureseyes.config.JHipsterProperties;
import com.bafconsulting.natureseyes.security.SecurityUtils;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.google.api.Google;
import org.springframework.web.context.request.NativeWebRequest;

import javax.inject.Inject;

public class CustomSignInAdapter implements SignInAdapter {

    @Inject
    private UserDetailsService userDetailsService;

    @Inject
    private JHipsterProperties jHipsterProperties;

    @Override
    public String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
        UserDetails user = userDetailsService.loadUserByUsername(userId);
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
            user,
            null,
            user.getAuthorities());
        
        SecurityContextHolder.getContext().setAuthentication(newAuth);
        SecurityUtils.connection = (Connection<Google>) connection;
        return jHipsterProperties.getSocial().getRedirectAfterSignIn();
    }
}
