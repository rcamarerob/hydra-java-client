package com.rcb.poc.hydra.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HydraClientResponse {

    private String owner;

    private String[] audience;

    private String client_uri;

    private String[] grant_types;

    private String subject_type;

    private String logo_uri;

    private String created_at;

    private String[] redirect_uris;

    private String[] allowed_cors_origins;

    private String client_id;

    private String token_endpoint_auth_method;

    private String userinfo_signed_response_alg;

    private String updated_at;

    private String client_secret_expires_at;

    private String scope;

    private String tos_uri;

    private String client_name;

    private String[] contacts;

    private String[] response_types;

    private String policy_uri;
}
