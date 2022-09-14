# barebone

This project has been configured as a template you can use to create your new MS.
What this project does is:
- Exposing a GET dummy endpoint that return a string.
- Listening to a topic from staging kafka instance
- Writing into a topic in staging kafka instance.

Once we have the code we can just replace every "barebone" and "Barebone" with the name of the new service (In IntelliJ: control+shift+r, check case sensitive | In Eclipse: Search > File... (check case sensitive) > Replace):

* Refactor the package es.fujitsu.barebone from src/main/java and src/test/java to es.fujitsu.your-service-name
* Refactor the file BareboneApplication file to YourServiceNameApplication
* Refactor the file BareboneController file to YourServiceNameController
* Refactor the file KafkaBareboneProducerEventListener to KafkaYourServiceNameProducerEventListener
* ...in the end rafactor everything Prefix*Barebone*Sufix to Prefix*YourServiceName*Sufix taking into account uppercase.

# Environment configuration

## Azure environments

Adding environment variables to Azure Devops Pipelines: 
- As we are using ConfigMaps and secrets into Kubernetes for storing some sensible information, it is required to declare some values inside the pipeline we are going to use.
- For this step, you will need to access the pipeline itself, click on the top right button 'variables' and setting here the value you are going to declare (pair key,value)

## Local environments

We need to add the variables that otherwise should be in ConfigMap as "Environment" variables for the execution of our microservice locally (i.e for Eclipse IDE in Boot Dashboard right click in the microservice > Open Config > Environment)

## Variables

Minimum config:

```
SERVER_PORT=8080;
LOGGING_LEVEL_ES=TRACE;
SPRING_PROFILES_ACTIVE=dev;
ENVIRONMENT_SUFFIX=dev;
```

In addition with kafka disabled:

```
KAFKA_ENABLED=FALSE
```

or with Kafka enabled and its minimum config if we are going to integrate with that service:

```
KAFKA_ENABLED=TRUE
KAFKA_NUMPARTITIONS_BAREBONE=1
KAFKA_REPLICATIONFACTOR_BAREBONE=1
KAFKA_TOPIC_BAREBONE=barebone
KAFKA_TOPIC_BAREBONE-WRITTING=barebone-local-writting
SPRING_KAFKA_CONSUMER_GROUP_BAREBONE=group-barebone

```

If the Kafka instance uses SASL add:

```
SPRING_KAFKA_BOOTSTRAP_SERVERS=<server_url>:9094
SPRING_KAFKA_SECURITY_PROTOCOL=SASL_SSL
SPRING_KAFKA_JAAS_ENABLED=true
SPRING_KAFKA_PROPERTIES_SASL_JAAS_CONFIG=org.apache.kafka.common.security.scram.ScramLoginModule required username="kafka-scram-client-credentials" password="<password>";
SPRING_KAFKA_PROPERTIES_SASL_MECHANISM=SCRAM-SHA-512
SPRING_KAFKA_SSL_PROTOCOL=SSL
SPRING_KAFKA_SSL_TRUST_STORE_LOCATION=file:client.truststore.strimzi.sasl.jks
SPRING_KAFKA_SSL_TRUST_STORE_PASSWORD=<kafka-ssl-trust-store-password>
```

If we want to connect with a Kafka without SASL (only SSL), replace Kafka config with:

```
SPRING_KAFKA_BOOTSTRAP_SERVERS=<server_url>:9094
SPRING_KAFKA_SECURITY_PROTOCOL=SSL
SPRING_KAFKA_SSL_PROTOCOL=SSL
SPRING_KAFKA_SSL_TRUST_STORE_LOCATION=file:client.truststore.strimzi.jks
SPRING_KAFKA_SSL_TRUST_STORE_PASSWORD=<kafka-ssl-trust-store-password>
```

If the service integrates with ElasticSearch add:

```
ELASTICSEARCH_SSL=true;
ELASTICSEARCH_HOST=*askforit*;
ELASTICSEARCH_PORT=443;
```

If we want to validate how endpoints are exposed in the actuator add:

```
MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE: env,health,prometheus,metrics
```

Or any other endpoints from actuator that you want to expose.



# Deployment instructions

- Launch the prompt at level of your DockerFile in the project folder.

- Login in AWS ECR:

```bash
aws ecr get-login-password --region eu-west-1 | docker login --username AWS --password-stdin 818418065468.dkr.ecr.eu-west-1.amazonaws.com
```

- If not exist, create a repository where store our images.

- Generating the jar file: mvnw clean package

- Build the docker image with the version proper version number: docker build . -t 818418065468.dkr.ecr.eu-west-1.amazonaws.com/your-service:0.0.1

- Tag the image (si es la última versión poner en target latest-snapshot como en el siguiente ejemplo, sino poner la misma version que en source): 

```bash
docker tag 818418065468.dkr.ecr.eu-west-1.amazonaws.com/your-service:0.0.2 818418065468.dkr.ecr.eu-west-1.amazonaws.com/your-service:latest-snapshot
```

- Push it (si es la última versión subimos como latest-snapshot, sino con la versión que corresponda): 

```bash
docker push 818418065468.dkr.ecr.eu-west-1.amazonaws.com/your-service:latest-snapshot
```

- At ArgosCD project, create the following files at the proper environment folder(staging in this case):

_your-service-config.yaml_ -> define the properties that your project needs
_your-service-service.yaml_ -> define service metadata and nodePort(8080)
_your-service-es-mongodb.yml_ -> file that point to the secret managen by AWS Secret, make sure that you are referencing the correct secret.
_your-service-deployment.yaml_ -> file that includes all instructions that Argos need to deploy de application including reference to the container and image
(make sure that your docker image here is the image you previously have generated and tagged as lastest-snapshot), other conf files, external secrets etc.

Commits in this repo into master branch will be detected by Argos Deamon who will deploy the service. If not, you can trigger manually just deleting the existing pod.

# Input for Kafka topic barebone-local

Look for the topic barebone-local in the topics list on Kadeck
Press Ingest button (rigth corner on the window)
Introduce the following data:

Key (**WITH quotes**): "whatever"

Value (**NO quotes**): 

```json
{
    "metadata": {
      "interchangeId": "6f49c4a9-ad94-465a-8bda-b1874d774d98",
      "timeStamp": "2021-01-05T15:50:20.244+01:00",
      "source": "urn:com:fujitsu:interchange:source:sgc",
      "dataType": "urn:com:fujitsu:interchange:data-type:pda-couriers-stop-confirmation-event",
      "sagaExtendedInfo" : []
    },
    "name": "John",
    "surname": "Doe",
    "address": "742 Evergreen Terrace, Springfield",
    "phone": "612 345 678"
}
```

**Headers**: 

Key (**No quotes and DOUBLE underscore**):__TypeId__  

Value (**WITH quotes**): "es.fujitsu.barebone.dto.BareboneDto"

Encoder for Key: string

Encoder for Value: Json

# Testing

## Manual

1. Kafka data reading (consumer) can be tested by manually inserting an entry with Kadeck as described in the previous point and observing how KafkaListeners.bareboneTopicListener captures it from Kafka and raises an application event which is in turn captured by BareboneListener.
2. Data writing (producer) to Kafka can be tested in two ways:

..* Following the process (1) since BareboneListener raises a BareboneEvent event which in turn is captured by KafkaBareboneProducerEventListener.onApplicationEvent (method implemented in its parent class KafkaProducerEventListener) that ends up writing in the other topic

..* Attacking "manually" the /onDemandPublishToKafka endpoint that directly calls the service that raises the BareboneEvent event, repeating the process described above

## Automated

Unit tests can be executed with Junit, bear in mind that the same environment variables must be included as in Spring Boot for their execution or failures will occur. 

