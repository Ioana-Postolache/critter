# Critter Chronologer Project

Critter Chronologer a Software as a Service application that provides a scheduling interface for a small business that takes care of animals. This Spring Boot project will allow users to create pets, owners, and employees, and then schedule events for employees to provide services for pets.

## Getting Started

### Dependencies

* [IntelliJ IDEA Community Edition](https://www.jetbrains.com/idea/download) (or Ultimate) recommended 
* [Java SE Development Kit 8+](https://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Maven](https://maven.apache.org/download.cgi)
* [MySQL Server 8](https://dev.mysql.com/downloads/mysql/) (or another standalone SQL instance)
* [Postman](https://www.getpostman.com/downloads/)

Here are [instructions for installing MySQL 8](https://dev.mysql.com/doc/refman/8.0/en/installing.html).

You should install the Server and Connector/J, but it is also convenient to install the Documentation and Workbench.

### Instructions on how to run MySQL in a docker container
Run MySQL in a docker container
```
docker pull mysql
```

Create bridge network:
```
docker network create mysql-net
```
Starting a MySQL instance:
```
docker run --network mysql-net --name primary-mysql-container -e MYSQL_ROOT_PASSWORD=my-secret-pw -p 6603:3306 -d mysql:8.0
```

... where:
 - mysql-net is the docker network
 - primary-mysql-container is the name you want to assign to your container
 - my-secret-pw is the password to be set for the MySQL root user
 - 6603:3306 - port mapping
 - 8.0 is the tag specifying the MySQL version you want. 
 
 Unless you specify the port to use from outside the container, the ports are not exposed. You can't contact a port inside a docker container by default.
 This is why the -p option is important. It tells docker you want it to allow connections from outside the container, and it tells docker what port it should listen on, to proxy to the mysql process inside the container. [Source](https://stackoverflow.com/questions/60156809/how-to-connect-to-mysql-instance-running-in-container-on-local-machine)

 Connect to MySQL from the MySQL command line client
 
 The following command starts another mysql container instance and runs the mysql command line client against your original mysql container, allowing you to execute SQL statements against your database instance:
 
`docker run -it --network mysql-net --rm mysql mysql -hprimary-mysql-container -uroot -p`
 
 ... where:
 - primary-mysql-container is the name of your original mysql container (connected to the mysql-net Docker network).
 - root is the user
 
You'll need to add the password you've added in the previous step (i.e., `my-secret-pw`)
 
Create database named critter:
```
CREATE DATABASE IF NOT EXISTS critter;
```
 
Add a user:
 ```
CREATE USER 'ioana'@'%'
    IDENTIFIED BY 'superpassword';
GRANT ALL
    ON *.*
    TO 'ioana'@'%'
    WITH GRANT OPTION;
```
Note: we used '%' instead of 'localhost' in order to match the connecting to MySQL from different IP addresses
Check that user was added:
```
SELECT user FROM mysql.user;
```

Afterwards, you can start the stopped container: 
```
docker start /primary-mysql-container
```

### Installation

1. Clone or download this repository.
2. Open IntelliJ IDEA.
3. In IDEA, select `File` -> `Open` and navigate to the `critter` directory within this repository. Select that directory to open.
4. The project should open in IDEA. In the project structure, navigate to `src/main/java/com.udacity.jdnd.course3.critter`. 
5. Within that directory, click on CritterApplication.java and select `Run` -> `Debug CritterApplication`. 
6. Open a browser and navigate to the url: [http://localhost:8082/test](http://localhost:8082/test)

You should see the message "Critter Starter installed successfully" in your browser.

## Testing

Once you have completed the above installation, you should also be able to run the included unit tests to verify basic functionality as you complete it. To run unit tests:

1. Within your project in IDEA, Navigate to `src/test/java/com.udacity.jdnd.course3.critter`.
2. Within that directory, click on `CritterFunctionalTest.java` and select `Run` -> `Run CritterFunctionalTest`.

A window should open showing you the test executions. All 9 tests should fail and if you click on them they will show `java.lang.UnsupportedOperationeException` as the cause.

As you complete the objectives of this project, you will be able to verify progress by re-running these tests.

### Tested Conditions
Tests will pass under the following conditions:

* `testCreateCustomer` - **UserController.saveCustomer** returns a saved customer matching the request
* `testCreateEmployee` - **UserController.saveEmployee** returns a saved employee matching the request
* `testAddPetsToCustomer` - **PetController.getPetsByOwner** returns a saved pet with the same id and name as the one saved with **UserController.savePet** for a given owner
* `testFindPetsByOwner` - **PetController.getPetsByOwner** returns all pets saved for that owner.
* `testFindOwnerByPet` - **UserController.getOwnerByPet** returns the saved owner used to create the pet.
* `testChangeEmployeeAvailability` - **UserController.getEmployee** returns an employee with the same availability as set for that employee by **UserControler.setAvailability**
* `testFindEmployeesByServiceAndTime` - **UserController.findEmployeesForService** returns all saved employees that have the requested availability and skills and none that do not
* `testSchedulePetsForServiceWithEmployee` - **ScheduleController.createSchedule** returns a saved schedule matching the requested activities, pets, employees, and date
* `testFindScheduleByEntities` - **ScheduleController.getScheduleForEmployee** returns all saved schedules containing that employee. **ScheduleController.getScheduleForPet** returns all saved schedules for that pet. **ScheduleController.getScheduleForCustomer** returns all saved schedules for any pets belonging to that owner.

### Postman
In addition to the included unit tests, a Postman collection has been provided. 

1. Open Postman.
2. Select the `Import` button.
3. Import the file found in this repository under `src/main/resource/Udacity.postman_collection.json`
4. Expand the Udacity folder in postman.

Each entry in this collection contains information in its `Body` tab if necessary and all requests should function for a completed project. Depending on your key generation strategy, you may need to edit the specific ids in these requests for your particular project.

## Built With

* [Spring Boot](https://spring.io/projects/spring-boot) - Framework providing dependency injection, web framework, data binding, resource management, transaction management, and more.
* [Google Guava](https://github.com/google/guava) - A set of core libraries used in this project for their collections utilities.
* [H2 Database Engine](https://www.h2database.com/html/main.html) - An in-memory database used in this project to run unit tests.
* [MySQL Connector/J](https://www.mysql.com/products/connector/) - JDBC Drivers to allow Java to connect to MySQL Server
