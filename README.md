# Running a JAR as Kubernetes Cron Job

```bash
kubectl apply -f cronjob.yaml
```

will run the job periodically.


## Procedure
- create java project
- create a docker image out of the java project 
- push the docker image into any docker registry
- use the docker image to run the kubernetes cron job


## Future improvements
- passing arguments from the kubernetes cron job to the java application
- saving any file to persistent storage form the java application 
