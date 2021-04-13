# SOA-COLAB
Repository contains code for all services developed for the project of Service Oriented Architecture code.

Before you start the application, ensure you have your database running.
In order to run the applications one needs a PostgreSQL instance running with 3 databases:
- students,
- projects
- meetings

A docker-compose file setting it up has been attached only for testing purposes,
a production deployment with a standard PostgreSQL instance is recommended.

Once the database is up and running, check the IP address at which it can be reached 
and update the POSTGRES_DB_URL in the group-service.yml, project-service.yml and meeting-service.yml.
Since host.docker.internal does not run on Linux, we have found this to be the best solution how to point to a database.

All Kubernetes configuration files can be found in the "kubernetes" folder.
To avoid any issues, it is recommended to start the Apache ActiveMQ first, 
by applying the activemq.yml configuration.
Then, the services should be started in the following order: group-service, 
project-service, meeting-service, join-link-service. Ingress can be started at the end to ensure all services are found.

If you want to test the ingress functionality, please set up your /etc/hosts file with colab.nl as the host, 
and the ip address assigned to you by ingress. Otherwise, you need to check the IP address of each of the services.

The Frontend service should be reachable at the host colab.nl path 
while other services are available through their respective API endpoints, e.g. /api/groups.
Check the documentation in the report for guidelines or find it in the respective Controller of each service.

The following message queues are used in the project:
- meetingQueue
- joinLinkQueue
It is not required to set them up since ActiveMQ silently configures required queues when needed.
Inspecting the message queues is possible if you run `minikube service activemq-service`  