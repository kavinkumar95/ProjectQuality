#!/bin/bash

github_token="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"
# Set the GitHub raw URL for the zip file
github_raw_url="https://raw.githubusercontent.com/kavinkumar95/ProjectQuality/main/scripts.zip"

# Set the destination file path where you want to save the downloaded zip file
destination_path="scripts.zip"

# Use curl to download the zip file
curl -k -H -LJO "$github_raw_url" -o "$destination_path"


# Check if the download was successful
if [ $? -eq 0 ]; then
    echo "Download successful!"

    # Extract the contents of the zip file
     unzip "$destination_path" -d "$(dirname "$destination_path")"

    # Check if the extraction was successful
    if [ $? -eq 0 ]; then
        echo "Extraction successful!"
	cd scripts/
	chmod +x *.sh
	bash start_service.sh
    else
        echo "Extraction failed. Please check the zip file and try again."
    fi
else
    echo "Download failed. Please check the URL and try again."
fi
