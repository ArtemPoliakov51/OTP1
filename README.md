# OTP1

This is a Software Engineering Project for the Attendance Checker app.

## How to setup the app and run with Docker container (work-in-progress)

Create an empty database and a user in MariaDB with the [database_user.sql script](https://github.com/ArtemPoliakov51/OTP1/blob/Docker-Implementation/sql/database_user.sql) in the sql folder.
<br></br>
Build the Docker image:
```
docker build -t riikkakoo/attendance-checker:latest .
```
___
<b>OPTIONAL:</b></br>

Push the image to the Docker Hub if you want:
```
docker push riikkakoo/attendance-checker:latest
```
Pull the image from the Docker Hub if you want:
```
docker pull riikkakoo/attendance-checker:latest
```
___
<b>WINDOWS</b></br> 

To run the Docker container, make sure you have Xming installed and running.
<br></br>
Run the Docker container:
```
docker run --rm -e DISPLAY=host.docker.internal:0.0 riikkakoo/attendance-checker:latest
```
You should be able to login as these users (email and password combo):

- Email: freya.stephens@email.com, Password: salasana
- Email: ingram.martin@email.com, Password: verySecret
- Email: donelly123@email.com, Password: password
