package lk.jiat.app.security.security;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import jakarta.security.enterprise.authentication.mechanism.http.AutoApplySession;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@AutoApplySession
@ApplicationScoped
public class AuthMechanism implements HttpAuthenticationMechanism {

    @Inject
    private AppIdentityStore identityStore;

    private static final Set<String> WHITE_LIST=  Set.of(
            "/login",
            "/register",
            "/auth/login",
            "/auth/register",
            "/public"
    );

    private boolean isWhitelisted(String path){
        return WHITE_LIST.stream().anyMatch(path::startsWith);
    }

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response, HttpMessageContext context) throws AuthenticationException {


        String path = request.getRequestURI();
        System.out.println("path :"+path);
//        if (isWhitelisted(path) ) {
//            return context.doNothing(); // no authentication needed
//        }
        System.out.println("AuthParameters ;"+context.getAuthParameters());

        AuthenticationParameters authParameters = context.getAuthParameters();
        System.out.println("credential  is :"+authParameters.getCredential());
        if (authParameters.getCredential() != null) {
            CredentialValidationResult result = identityStore.validate(authParameters.getCredential());
            System.out.println("get Result ");
            if (result.getStatus() == CredentialValidationResult.Status.VALID) {
                System.out.println("valid details");
                return context.notifyContainerAboutLogin(result);
            }else {
                return AuthenticationStatus.SEND_FAILURE;
            }


        }else{
            System.out.println("resut is null");
        }


        if (context.isProtected()) {
            try {
                response.sendRedirect(request.getContextPath()+"/login.jsp");
                return AuthenticationStatus.SEND_CONTINUE;
            } catch (IOException e) {
                throw new RuntimeException("Redirect to login failed", e);
            }
        }

        return context.doNothing();
    }
}
