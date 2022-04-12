# Running a JAR as Kubernetes Cron Job

## Build and Deploy

### Build

Go to the terminal and go the root folder of this project and run the following command:

```bash
make
```

### Deploy
1. you need to base64 encode all your secrets required in the secret-env.yaml file. Use the following command to get the base64 encoded string against your secret.
```bash
echo -n 'my-string' | base64
```
2. Put your base 64 encoded mail credentials into the `deployment/secret-env.yaml` file.
3. Run the following commands to deploy the CronJob.
```bash
kubectl create ns ct
kubectl apply -f deployment/secret-env.yaml -n ct
kubectl apply -f deployment/secret-cer.yaml -n ct
kubectl apply -f deployment/cronjob.yaml -n ct
```

will run the job periodically.


## Procedure
- create java project
- create a docker image out of the java project 
- push the docker image into any image registry. I am using DockerHub here. 
- use the docker image to run the kubernetes cron job