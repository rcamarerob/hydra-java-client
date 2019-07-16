package com.rcb.poc.hydra.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class OidcContext {

    private List<String> acr_values;

    private String display;

    private Map<String, String> id_token_hint_claims;

    private String login_hint;

    private List<String> ui_locales;
}
