# hydra-java-client

Java client for ORY Hydra Oauth2 server.

##Compile and run

Running the _buildAndRun.sh_ script, you will build the hydra java client docker image and all the need containers will start:
- A postgress database, persistence for Hydra server
- ORY Hydra server (OAuth 2 and OpenID Connect server)
- Our Java Hydra client :)

##Configure environment

To test the OpenID layer over the OAuth protocol, we need to store in Hydra Server (postgress) the client app. We can do this, as follows:

```console
Creates a new OAuth 2.0 client

$ docker-compose -f quickstart.yml exec hydra \
    hydra clients create \
    --endpoint http://127.0.0.1:4445/ \
    --id my-client \
    --secret secret \
    -g client_credentials

OAuth2 client my-client
OAuth2 client secret: secret

# Let's perform the client credentials grant.
$ docker-compose -f quickstart.yml exec hydra \
    hydra token client \
    --endpoint http://127.0.0.1:4444/ \
    --client-id my-client \
    --client-secret secret

6l56Koy4F_gzgnY7uUb7j-vIE2x_6yyBZuC6qDZjOQ8.WLvbGBIHwetfIX4_AfWs-8-xgZ698g0FMc4inZfYhow

# Let's perform token introspection on that token. Make sure to copy the token you just got and not the dummy value.
$ docker-compose -f quickstart.yml exec hydra \
    hydra token introspect \
    --endpoint http://127.0.0.1:4445/ \
    --client-id my-client \
    --client-secret secret \
    6l56Koy4F_gzgnY7uUb7j-vIE2x_6yyBZuC6qDZjOQ8.WLvbGBIHwetfIX4_AfWs-8-xgZ698g0FMc4inZfYhow
{
        "active": true,
        "client_id": "my-client",
        "exp": 1527078658,
        "iat": 1527075058,
        "iss": "http://127.0.0.1:4444/",
        "sub": "my-client",
        "token_type": "access_token"
}
```
Next, we will perform the OAuth 2.0 Authorization Code Grant. For that, we must first create a client that is capable of performing that grant:
```console
docker-compose -f quickstart.yml exec hydra \
    hydra clients create \
    --endpoint http://127.0.0.1:4445 \
    --id auth-code-client \
    --secret secret \
    --grant-types authorization_code,refresh_token \
    --response-types code,id_token \
    --scope openid,offline \
    --callbacks http://127.0.0.1:5555/callback
```

Starts a server that serves an example web application. The application will perform the OAuth 2.0 Authorization Code Flow using ORY Hydra. The web server runs on http://127.0.0.1:5555.
```console
$ docker-compose -f quickstart.yml exec hydra \
    hydra token user \
    --client-id auth-code-client \
    --client-secret secret \
    --endpoint http://127.0.0.1:4444/ \
    --port 5555 \
    --scope openid,offline

Setting up home route on http://127.0.0.1:4445/
Setting up callback listener on http://127.0.0.1:4445/callback
Press ctrl + c on Linux / Windows or cmd + c on OSX to end the process.
If your browser does not open automatically, navigate to:

        http://127.0.0.1:5555/
```


Further information: 
- [ORY Hydra Introduction](https://www.ory.sh/docs/deprecated/latest/hydra/)
- [5 minutes tutorial](https://www.ory.sh/docs/next/hydra/5min-tutorial)
