#!/bin/bash

# API 1
API_URL_1="http://localhost:8084/api/quality/generateRulesFile/DEF"
curl -L -o "rule.xls" "$API_URL_1"

# API 2
API_URL_2="http://localhost:8084/api/quality/generateConfigurationFile/"
curl -L -o "config.xls" "$API_URL_2"


