#!/usr/bin/env bash

#builds hydra client image from the last sources
gradle buildDocker

#runs hydra, postgres and hydra client containers
docker-compose -f quickstart.yml up --build