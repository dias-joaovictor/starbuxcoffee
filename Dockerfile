FROM openjdk:11
COPY /target/starbuxcoffee-0.0.1-SNAPSHOT.jar /usr/src/myapp/app.jar
COPY /helpers/runapplication.sh /usr/src/myapp/runapplication.sh
WORKDIR /usr/src/myapp
RUN chmod +x .
ENTRYPOINT ["./runapplication.sh"]