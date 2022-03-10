# Starbux Coffee Shop
# Topics
1. [About](#about)
2. [Reference](#reference)
3. [Prerequisite](#prerequisite)
4. [Running](#running)
5. [Checking the Application](#checking)
6. [API Documentation](#docs)
7. [Postman Collection](#postman)
8. [Basic Flow Explanation](#flow)



## About
Development of Coffee Shop application to make orders and manage products. 

This application is developed using Springboot 2.6 and mysql.


Developed by: João Victor Dias

## Reference
#### [GitHub](https://github.com/silverfoxjv/starbuxcoffee) 
#### [Docker Hub](https://hub.docker.com/r/silverfoxjv/starbuxcoffee) 

## Prerequisite
_Mandatory_
- Docker
- Docker Compose

_Optional_
- Java 8 or Java 11
- Maven
- Mysql

## Running
### **Hard Way**
Building the project:
> mvn clean install

Starting database:
> docker-compose -f "docker-compose-infra.yaml" up -d --build

The command above will start a mysql instance on port **3307** on a docker and execute the script in /database/script.sql. This script will create all database structure we need.\
mysql info:
* Host: localhost
* Port: 3307
* Main database: starbux-db
* Root password: rootPass
* User: starbux_user
* Password: starbux_pass

Running the project:
> java -jar -DMYSQL_HOST=localhost -DMYSQL_PORT=3307 target/starbuxcoffee-0.0.1-SNAPSHOT.jar


### **Easy Way**
Running via Docker compose:
> docker-compose -f "docker-compose-remote.yaml" up -d --build

The execution above will run [this](https://hub.docker.com/r/silverfoxjv/starbuxcoffee) image, present on docker hub.




## Checking

Checking the application - Healthcheck endpoint:
> curl --location --request GET 'http://localhost:8080/api/actuator/health'

Expected Return:
> {"status":"UP"}

Checking the application - Products endpoint:
> curl --location --request GET 'http://localhost:8080/api/products'

Expected Return:
> {"products":[]}


## Docs
After those steps above, access the following link:
> http://localhost:8080/api/swagger-ui/index.html


## Postman 
The collection and the environment are present on postman directory.
* Collection: postman/starbux.postman_collection.json
* Environment: postman/starbux.postman_environment.json

The collection already has env variable to store bearer token and use it wherever it's necessary. The same for the cartid.


## Flow
**Admin flows**: 
<p>To any kind of products and prices management or issue a report, the user must be authenticated as ADMIN.<p>
<br>
Registered admin credentials:

### _João Dias Admin_
<p>User: joao.dias@bestseller.com<p>
<p>Pass: diasadmin<p>

### _Tech Lead Admin_
<p>User: tl@bestseller.com<p>
<p>Pass: techlead<p>

### _Admin Admin_
<p>User: admin@bestseller.com<p>
<p>Pass: adminbestseller<p>
<br>

**Customer flows**:
<p>To start de cart and make order, the user must to open a new cart passing to it the customer id.
<p> Customer ID represents any document the customers has.
<p> With the cart opened and with the cartid, the user can do any combination wanted.
<p> To finished the order, the customer must to checkout the cart. After that, the customers can register and pay(both flows, payment and customer register, are not implemented. I concluded they are out of scope).