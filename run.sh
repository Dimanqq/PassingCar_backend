#/bin/bash

mvn package
sudo docker build -t app_i --no-cache .
sudo docker rm -f app_w
sudo docker run -d --name app_w -p 80:8080 app_i
