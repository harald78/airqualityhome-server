# Server project setup
## Getting started
Make sure you have access to your home server by using [SSH](https://www.digitalocean.com/community/tutorials/how-to-use-ssh-to-connect-to-a-remote-server).
Open a terminal session and copy the content of this directory to your home server. For this create a
directory e. g. `mkdir /home/server` directly on your server.

Then copy the content of this directory to the created directory:

`scp ./ <username>@<host>:/home/server`

## Requirements on your server
Please make sure you use at least a RaspberryPi 3B+ or better a RaspberryPi4 model with 4GB of RAM.
Install the latest RaspberryPi [OS](https://www.raspberrypi.com/software/) (64 bit version). Thereafter
install the following software if not already installed:

Docker:
```
sudo apt update
sudo apt install docker.io
```

Docker Compose
```
sudo apt-get install docker-compose
```

## Setting the database URL
Enter the Gateway IP which you've should note from the database setup directly into
the compose.app.yaml file or replace the entry by pointing to your environment variable 
and enter the correct value within the env_vars.sh file.

`jdbc:mariadb://<maria-db-gateway>:3306/airqualityhome`

## Create VAPID keys for push notification
To create the VAPID keys for push notification you need a installation of [node.js](https://nodejs.org/en) on your computer.
If you have a node.js installation you can install the required [web-push](https://www.npmjs.com/package/web-push) package and
follow the instructions to generate the key pairs.

<span style="color:orange">NOTE: The server will not run without VAPID keys set!</span>

## Setting up the env_vars.sh
Please setup the env_vars.sh files with your secrets. Therefore replace all < > - Entries with suitable values.
You can choose the secrets freely. Just make sure you use the same secrets for the server setup you've used
for the database setup previously.
You can use the editor <span style="color:green">**nano**</span> for setting up this file directly on your server.

<span style="color:orange">**Never store the secrets directly in a repository!!!**</span>

## Expose the variables
You can expose the variables with the following command:

`source env_vars.sh`

## Building the project and copy the app
Before starting the server build the project from the root directory with the following command:
`mvn clean package`

Then copy the created jar-File from the target folder to the server:
`scp ./target/airqualityhome-server-0.0.1-SNAPSHOT.jar <user>@<host>:/home/server/app.jar`

## Starting the server
To start the database server please use the following command:
`docker-compose -f compose.app.yaml up -d --build`

The -d flag option starts the server as a deamon (background) task. To see if your server has
been started correctly please use the command `docker ps -a`

