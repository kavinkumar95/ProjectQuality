#!/bin/bash

# API 1
API_URL_1="http://localhost:8082/api/quality/generateRulesFile/"
curl -L -o "rule.xls" "$API_URL_1"

# API 2
API_URL_2="http://localhost:8082/api/quality/generateConfigurationFile/"
curl -L -o "config.xls" "$API_URL_2"


