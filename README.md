# app-sample
Sample Application based on https://github.com/spring-cloud-samples/fortune-teller

How to push Application sample to PCFdev

    cf cs p-mysql 512mb fortunes-db
    cf cs p-config-server standard config-server -c '{"git": { "uri": "https://github.com/sergiubodiu/app-sample", "searchPaths": "configuration" } }'