package com.rcb.poc.hydra.web;

import com.github.ory.hydra.api.AdminApi;
import com.github.ory.hydra.model.AcceptConsentRequest;
import com.github.ory.hydra.model.AcceptLoginRequest;
import com.github.ory.hydra.model.CompletedRequest;
import com.rcb.poc.hydra.HydraClientProperties;
import com.rcb.poc.hydra.model.HydraGetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Arrays;

@Slf4j
@Controller
public class HydraClientController {

    private String consentPostRedirect;

    private final HydraClientProperties properties;

    @Autowired
    public HydraClientController(HydraClientProperties properties) {
        this.properties = properties;
    }

    @GetMapping("/login")
    public String getLogin(@RequestParam("login_challenge") String loginChallenge) {

        log.debug("Login challenge {}", loginChallenge);

        RestTemplate rt = new RestTemplate();
        String loginUrl = properties.getBasePath()+properties.getOauthPath()+"login?login_challenge="+loginChallenge;
        //hydra checks if it has already validated the login
        ResponseEntity<HydraGetResponse>  response = rt.getForEntity(loginUrl, HydraGetResponse.class);
        //we don't treat the response
        log.info("Login response {}", response.toString());

        if(response.getBody().getSkip().equals("true")) {
            CompletedRequest result = acceptLoginRequest(loginChallenge);
            //not showing ui login page, redirect to consent
            return "redirect:/"+result.getRedirectTo();
        }

        //show login page,
        log.info("Show login page");
        return "login";
        //return "login";
    }

    @PostMapping("/login")
    public RedirectView postLogin(@RequestParam("login_challenge") String loginChallenge) {

        //we do nothing ... login always success

        CompletedRequest result = acceptLoginRequest(loginChallenge);
        //if login SUCCESS, then redirect to hydra and it will show consent page
        log.debug("redirecting .... {}", result.getRedirectTo());
        return new RedirectView(result.getRedirectTo());
    }

    @GetMapping("/consent")
    public String getConsent(@RequestParam("consent_challenge") String challenge) {

        RestTemplate rt = new RestTemplate();
        String consentUrl = properties.getBasePath()+properties.getOauthPath()+"consent?consent_challenge="+challenge;
        ResponseEntity<HydraGetResponse> response = rt.getForEntity(consentUrl, HydraGetResponse.class);
        log.info("Consent response {}", response.toString());

        AdminApi apiInstance = getAdminApi();
        AcceptConsentRequest body = new AcceptConsentRequest(); // AcceptLoginRequest |
        try {
            CompletedRequest result = apiInstance.acceptConsentRequest(challenge, body.grantScope(Arrays.asList(response.getBody().getRequested_scope()))
                .grantAccessTokenAudience(Arrays.asList(response.getBody().getRequested_access_token_audience())));
            consentPostRedirect = result.getRedirectTo();
            log.debug("CompletedRequest Consent {}", result);
            System.out.println(result);
        } catch (Exception e) {
            log.error("Exception when calling AdminApi#acceptLoginRequest {}", e);
        }

        log.debug("consent {} ", challenge);
        return "consent";
    }


    @PostMapping("/consent")
    public RedirectView consent(@RequestParam("consent_challenge") String challenge) {

        log.debug("redirecting consent to .... {}", consentPostRedirect);
        return new RedirectView(consentPostRedirect);
    }


    private CompletedRequest acceptLoginRequest(@RequestParam("login_challenge") String loginChallenge) {
        AdminApi apiInstance = getAdminApi();
        AcceptLoginRequest body = new AcceptLoginRequest(); // AcceptLoginRequest |
        CompletedRequest result = null;
        try {
            //tells hydra user is authenticated, //grant the login
            result = apiInstance.acceptLoginRequest(loginChallenge, body.rememberFor(Long.valueOf(3600)).subject("any_user_name"));

            log.debug("CompletedRequest Login {}", result);
        } catch (Exception e) {
            log.error("Exception when calling AdminApi#acceptLoginRequest {}", e);
        }
        return result;
    }

    private AdminApi getAdminApi() {
        AdminApi apiInstance = new AdminApi();
        apiInstance.getApiClient().setBasePath(properties.getBasePath());
        return apiInstance;
    }
}
