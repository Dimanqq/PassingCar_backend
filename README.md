# PassingCar_backend

Run database

`sudo docker run -d --name postgres_pc -p 5432:5432 -e POSTGRES_PASSWORD=qp~pq234 postgres`

`sudo docker exec -it postgres psql -U postgres -c "CREATE DATABASE passing_car;"`
