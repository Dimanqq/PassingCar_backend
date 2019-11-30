#/bin/bash

cd "${0%/*}"

git checkout master
git pull

mvn package
if [ $? != 0 ]
then
	exit -1
fi

sudo docker build -t app_i --no-cache .
sudo docker rm -f app_w
sudo docker run -d --name app_w -p 80:8080 app_i
