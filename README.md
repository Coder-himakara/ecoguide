# ECO-GUIDE
### Overview
The application is built around the concept of the development of an Animal/plant Details and Location Prediction System to provide comprehensive animal and plant information resources. 
### Target Users
Organizations and national institutions dedicated to protecting and managing forests and wildlife can install this application on their systems to keep track of 
the locations of animals and plants in the forests they oversee. Other users who are involved in wildlife activities like Wildlife Photographers, Wildlife Biologists, Park Rangers, etc can use this application to get information.

## Getting Started
### Prerequisites

* Java Development Kit (JDK) 21:
  - Ensure that JDK 21 is installed on your system.
* JavaFX SDK:
  - Download and install the JavaFX SDK compatible with JDK 21.
* IntelliJ IDEA:
  - Install IntelliJ IDEA (Community or Ultimate edition).
* Maven:
  - Ensure Maven is installed and configured on your system.
* Scene Builder:
  - Download and install Scene Builder for designing your JavaFX interfaces.
* MySQL Database:
  - Install MySQL Server and MySQL Workbench for database management. Or you can use the WAMP server or XAMPP.
* MySQL Connector/J:
  - Ensure you have the MySQL Connector/J library added to your Maven dependencies. This is required for connecting your JavaFX application to the MySQL database
    
### Installation
1. Clone the repo
   ```
   https://github.com/Coder-himakara/ecoguide.git
   ```
2. Open project using IntelliJ IDEA
3. Go to `Project Struture` in `File` tab and ensure SDK is set to 21.
4. Ensure you have the MySQL Connector library added to the `pom.xml` file
   ```
    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>8.0.33</version>
    </dependency>
   ```
5. Execute following Maven Goals
   ```
   mvn install
   ```
   ```
   mvn compile
   ```
6. Run `HelloApplication` class  to start the application
  #### Note
  The database and the relevant tables  will be created automatically once you run the application for the first time. Otherwise, you can use the scripts included in the
  `DbConfig` class file for creating the database and tables manually.

### Create an Admin Account
- Only the users mentioned in the `approved_admin` table can sign up for the application.
- First you have to add authorized users for the `approved_admin` table manually using your Database management system.
- After that use the `admin_pid` related to a user in `approved_admin` table to sign up for the application.
- Only one admin account can be created for one `admin_pid`.

#### Note
To delete an admin account, you have to manullay delete the record from the `approved_admin` table using your Database management system.
