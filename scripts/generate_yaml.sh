#!/bin/bash
docker cp "config.xls" "dataquality:/usr/src/app/config.xls"

docker cp "rule.xls" "dataquality:/usr/src/app/rule.xls"

json_data='{"configFilePath":"'"config.xls"'","ruleFilePath":"'"rule.xls"'"}'

# Step 2: Access the container and run curl
docker exec -it "dataquality" /bin/bash -c "curl -X POST -H 'Content-Type: application/json' -d '$json_data' http://localhost:8082/api/quality/generateConfigYaml/ -o config.zip"

docker cp "dataquality:/usr/src/app//config.zip" "."
