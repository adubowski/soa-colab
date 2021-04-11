# SOA-COLAB
Repository contains code for all services developed for the project of Service Oriented Architecture code.

All Kubernetes configuration files can be found in the "kubernetes" folder.

In order to run the applications one needs a PostgreSQL instance running on port 5432 with 3 databases: 
- students, 
- projects
- meetings

A docker-compose file setting it up has been attached only for testing purposes, 
a production deployment with a standard PostgreSQL instance is recommended.

If you want to test the ingress functionality, please set up your /etc/hosts file with colab.nl as the host, 
and the ip address assigned to you by ingress.

The activemq needs to be set up. The following queues are required:
- meetingQueue
- joinLinkQueue