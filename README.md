# AdaptiveTesting
Project for performing adaptive testing for different loaded tests

The aim of the project is to provide the system, which allows you to _create_, _configure_ and _conduct_ testing on different topics using modern **adaptive testing** approach.
It means in contradiction to the traditional approach, when there is some predefined set of questions, which are asked to the on who try to pass the test, _the adaptive_** way offer the next method:
1. The complexity of the next question is based on the amount of correct and wrong answers the user made before, i.e.: if recepient answered correctly several times for simple questions more probably he knew that answers pretty well at all, so it is time to increase complexity or test the other sides of the topic;
2. The amount of questions proposed depends on the number of correctly answered questions (such implication can be set up using special **rules** (*question_definition_rule*), it will be covered a bit later;
3. The result grade is awarded based on the amount of _marks_ receipent earned for each grade. A rule is simple: the grade with the most mark amount is chosen.

For now the structure of the system is not exposed here, however, you can discover it yourself using Swagger docs injected into the project.

### How run the application

#### Requirements
To run this app you should have several dependencies installed on your computer:
1. Java JRE or JDK (version 1.8 or higher);
2. JavaFX 8+ (version 8 is installed with JDK/JRE 1.8 but be aware that starting from Java 11 JavaFX packages was removed from general Java libraries);
3. Docker installed on your computer (to run the image of MySQL database that is used within project)

#### Follow the next steps
1. Download the source code from the last pre-release (tag name is _v.1.0.0-alpha_);
2. Unzip the package;
3. Start the docker Daemon (On windows you can achieve that by just launching the docker doolbox console);
4. Open the terminal in the root of the unzipped project;
5. Start the docker container using Docker compose (docker-compose file is present): _docker-compose up -d_;
6. Start the application using _mvn spring-boot:run_ if you have Maven installed, otherwise run _mvnw spring-boot:run_ from the same directory (taking an advantage from existed Maven wrapper in the project);
7. On the opened JavaFX gui window hit the button **Initialize database** and wait for several seconds till it is done;
8. Start the test using corresponding button & enjoy!

_Hint_** : you can also communicate with the app using REST API following the next path in your browser: http://localhost:8080/swagger-ui.html
P.S. After you close the app, please, make sure your docker container is also down invoking _docker-compose down_ from the root folder.
