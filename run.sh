#/bin/bash

cd "${0%/*}"

git checkout master
git pull

mvn sonar:sonar \
	-Dsonar.projectKey=PA \
	-Dsonar.host.url=http://84.237.50.237:9000 \
	-Dsonar.login=44e6579881d8e05a512f899fc911d5937d7e2fe6 # under Chernik

mvn package
if [ $? != 0 ]
then
	exit -1
fi

sudo docker build -t app_i --no-cache .
sudo docker rm -f app_w
sudo docker run -d --name app_w -p 80:8080 app_i
