package com.rcb.poc.hydra.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HydraGetResponse {

    private String[] requested_access_token_audience;

    private String acr;

    private String login_challenge;

    private String subject;

    private OidcContext oidc_context;

    private String challenge;

    private HydraClientResponse client;

    private String skip;

    private String request_url;

    private String login_session_id;

    private String[] requested_scope;
}
