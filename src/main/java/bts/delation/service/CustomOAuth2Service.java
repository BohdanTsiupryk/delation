package bts.delation.service;

import bts.delation.model.CustomOAuth2User;
import bts.delation.model.User;
import bts.delation.model.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2Service extends OidcUserService {

    private final UserService userService;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        System.out.println("__CustomOAuth2Service");
        String email = oidcUser.getEmail();

        User user = null;

        if (userService.isUserAuthorized(email)) {

            user = userService.getByEmail(email);

        } else {
            user = userService.save(new User(
                    oidcUser.getAttribute("sub"),
                    email,
                    UserRole.CLIENT
            ));
        }



        return new CustomOAuth2User(oidcUser, user.getUserRole());
    }
}
