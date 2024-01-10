package com.investasikita.news.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.investasikita.news.client.interfaces.ClientserviceProxy;
import com.investasikita.news.client.model.Investor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * Utility class for Spring Security.
 */
@Component
public final class SecurityUtils {

    static ClientserviceProxy clientService;

    @Autowired
    private SecurityUtils(ClientserviceProxy clientService) {
        SecurityUtils.clientService = clientService;
    }

    /**
     * Get the login of the current user.
     *
     * @return the login of the current user
     */
    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
            .map(authentication -> {
                if (authentication.getPrincipal() instanceof UserDetails) {
                    UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                    return springSecurityUser.getUsername();
                } else if (authentication.getPrincipal() instanceof String) {
                    return (String) authentication.getPrincipal();
                }
                return null;
            });
    }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise
     */
    public static boolean isAuthenticated() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
            .map(authentication -> authentication.getAuthorities().stream()
                .noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(AuthoritiesConstants.ANONYMOUS)))
            .orElse(false);
    }

    /**
     * If the current user has a specific authority (security role).
     * <p>
     * The name of this method comes from the isUserInRole() method in the Servlet API
     *
     * @param authority the authority to check
     * @return true if the current user has the authority, false otherwise
     */
    public static boolean isCurrentUserInRole(String authority) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
            .map(authentication -> authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority)))
            .orElse(false);
    }

    /**
     * Get the cif of the current user.
     *
     * @return the cif of the current user
     */
    public static Optional<String> getCurrentUserCif() {
        return retrieveUserDetailInfo("cif");
    }

    /**
     * Get the geraiId of the current user.
     *
     * @return the geraiId of the current user
     */
    public static Optional<String> getCurrentUserGeraiId() {
        return retrieveUserDetailInfo("geraiId");
    }

    /**
     * Get the Ifua of the current user.
     *
     * @return the Ifua of the current user
     */
    public static Optional<String> getCurrentUserIfua() {
        return retrieveUserDetailInfo("ifua");
    }

    private static Optional<String> retrieveUserDetailInfo(String infoType){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        ObjectMapper mapper = new ObjectMapper();
        return Optional.ofNullable(authentication.getDetails())
            .map(details -> {
                Map decodedDetails = (Map) ((OAuth2AuthenticationDetails) details).getDecodedDetails();
                if(decodedDetails == null) return null;
                return (String) decodedDetails.get(infoType);
            });
    }

    public static Optional<Investor> getInvestorDetail() {
        Optional<String> currentUserOpt = getCurrentUserLogin();
        if (currentUserOpt.isPresent()) {
            if (currentUserOpt.get().equals("anonymousUser")) {
                return Optional.empty();
            }

            try {
                return clientService.getInvestorDetail();
            }
            catch (Exception e){
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    public static String getInvestorCif() {
        String userCif = "";
        String userLogin = SecurityUtils.getCurrentUserLogin().get();
        if (userLogin.equals("anonymousUser")) {
            userCif = "ALL";
        } else {
            Optional<Investor> investor = SecurityUtils.getInvestorDetail();
            if (investor.isPresent())
                userCif = investor.get().clientEntity.cif;
        }
        return userCif;
    }
}
