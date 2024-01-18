#!/bin/bash

# Read the file line by line
while IFS= read -r line; do
 # Extract the key and path
  key=$(echo "$line" | cut -d ':' -f 1)
  path=$(echo "$line" | cut -d ':' -f 2)


  # Use the paths in a curl POST request
  if [ $key == "CONFIG_PATH" ]; then
    CONFIG_PATH=$path
  elif [ $key == "RULE_PATH" ]; then
    RULE_PATH=$path
  fi



done < path.txt

# Construct JSON data
json_data='{"configFilePath":"'"$CONFIG_PATH"'","ruleFilePath":"'"$RULE_PATH"'"}'


curl -X POST -H "Content-Type: application/json" -d "$json_data" http://localhost:8082/api/quality/generateConfigYaml/ -o config.zip

# Use the paths in a curl POST request with JSON data

