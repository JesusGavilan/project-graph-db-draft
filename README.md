project-graph-db-draft
======================

Draft graph DB of the project topology 

I installed the lastest stable version of neo4j (1.9.4).
In order to have a common installation, I followed the next documentation to install Neo4j on Debian/Ubuntu --->http://www.neo4j.org/develop/ec2_manual

To avoid problems related to permison, I've changed at /var/lib/neo4j/conf/neo4j-server.properties the next line:
org.neo4j.server.database.location= path
Being path, place where you define at programm, in my case:
/home/jesus/github/project-graph-db-draft/graphdb/target/graph-network

In order to avoid permission problems:
  
  chmod -R 777 graph-network
