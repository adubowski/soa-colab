# SOA-COLAB
Repository contains code for all services developed for the project of Service Oriented Architecture code.

All Kubernetes configuration files can be found in the "kubernetes" folder.

In order to run the applications one needs a PostgreSQL instance running on port 5432 with 3 databases: 
- students, 
- projects
- meetings

A docker-compose file setting it up has been attached only for testing purposes, 
a production deployment with a standard PostgreSQL instance is recommended.