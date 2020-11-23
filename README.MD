###### note on the soap client

it is necessary to run cxf-codegen:wsdl2java, so the java classes get generated from the wsdl file.

###### useful commands

// to generate a project from the archetypes
mvn archetype:generate -DarchetypeGroupId=br.com.vivo.rpa -DarchetypeVersion=1.0.0-SNAPSHOT -DarchetypeArtifactId=resource-manager-db-mvna
mvn archetype:generate -DarchetypeGroupId=br.com.vivo.rpa -DarchetypeVersion=1.0.0-SNAPSHOT -DarchetypeArtifactId=resource-manager-soap-mvna

// to run the applications simultaneously, changing the ports (default are 8080 and 5005)
mvnw quarkus:dev -Dquarkus.http.port=?? -Ddebug=??

// to run the demo, in the test folder, run the docker-compose file and create a request.json file