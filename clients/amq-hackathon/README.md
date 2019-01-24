# amq-hackathon

## Requirements

- [Apache Maven 3.x](http://maven.apache.org)

## Preparing

```
cd $PROJECT_ROOT
mvn clean install
```

## Running the example standalone

```
cd $PROJECT_ROOT
mvn spring-boot:run
```

## Running the example in OpenShift

```
oc new-project jack

cd input-online-processor
oc create -f src/main/kube/serviceaccount.yml
oc create -f src/main/kube/configmap.yml
oc create -f src/main/kube/secret.yml
oc secrets add sa/input-online-processor-sa secret/input-online-processor-secret
oc policy add-role-to-user view system:serviceaccount:jack:input-online-processor-sa
mvn -P openshift clean install fabric8:deploy

cd input-batch-processor
oc create -f src/main/kube/serviceaccount.yml
oc create -f src/main/kube/configmap.yml
oc create -f src/main/kube/secret.yml
oc secrets add sa/input-batch-processor-sa secret/input-batch-processor-secret
oc policy add-role-to-user view system:serviceaccount:jack:input-batch-processor-sa
mvn -P openshift clean install fabric8:deploy
```

## Testing the code



## Notes

```
cat broker.crt.b64 | base64 -D > broker.crt

keytool -keystore "$PWD/client.ts" -storepass 'r3dh4t1!' -noprompt -alias broker -import -file "$PWD/broker.crt" -storetype PKCS12


cp client.ts /tmp/client.ts
```

Input Online Sender
```
mvn spring-boot:run '-Damqp.ssl-truststore-path=/tmp/client.ts' '-Damqp.ssl-truststore-password=r3dh4t1!' '-Damqp.client-id=1' '-Damqphub.amqp10jms.remote-url=amqps://messaging-qvekprmj3e-amq-online-infra.apps.amqhackfest4.openshift.opentlc.com:443' '-Damqphub.amqp10jms.username=inputsender' '-Damqphub.amqp10jms.password=hackfest' '-Damqp.producer-queue=inputonline' '-Dserver.port=9090' '-Dmanagement.port=9091'
```

Input Batch Sender
```
mvn spring-boot:run '-Damqp.ssl-truststore-path=/tmp/client.ts' '-Damqp.ssl-truststore-password=r3dh4t1!' '-Damqp.client-id=2' '-Damqphub.amqp10jms.remote-url=amqps://messaging-qvekprmj3e-amq-online-infra.apps.amqhackfest4.openshift.opentlc.com:443' '-Damqphub.amqp10jms.username=batchsender' '-Damqphub.amqp10jms.password=hackfest' '-Damqp.producer-queue=inputbatch' '-Damqp.min-message-size=20000' '-Damqp.max-message-size=1000000' '-Damqp.timer-period=60000' '-Dserver.port=7070' '-Dmanagement.port=7071'
```

Results Consumer (Client 1)
```
mvn spring-boot:run '-Damqp.ssl-truststore-path=/tmp/client.ts' '-Damqp.ssl-truststore-password=r3dh4t1!' '-Damqphub.amqp10jms.remote-url=amqps://messaging-qvekprmj3e-amq-online-infra.apps.amqhackfest4.openshift.opentlc.com:443' '-Damqphub.amqp10jms.username=resultreceiver' '-Damqphub.amqp10jms.password=hackfest' '-Damqp.consumer-queue=result1'
```

Results Consumer (Client 2)
```
mvn spring-boot:run '-Damqp.ssl-truststore-path=/tmp/client.ts' '-Damqp.ssl-truststore-password=r3dh4t1!' '-Damqphub.amqp10jms.remote-url=amqps://messaging-qvekprmj3e-amq-online-infra.apps.amqhackfest4.openshift.opentlc.com:443' '-Damqphub.amqp10jms.username=resultreceiver' '-Damqphub.amqp10jms.password=hackfest' '-Damqp.consumer-queue=result2' '-Dserver.port=6060' '-Dmanagement.port=6061'
```
