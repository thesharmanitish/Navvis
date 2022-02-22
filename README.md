# Getting Started

### Reference Documentation
Perform the following steps to build the project 

Repo Link : https://github.com/thesharmanitish/phone-services

* Checkout the latest code from phone-services on local system.
>   git clone https://github.com/thesharmanitish/phone-services 

* Prerequisite : Java and maven installed, if not install java 1.8, set JAVA_HOME to installed java path

* change directory to phone-services/ and run below command to build 
 >  ./mvnw -U -e --settings settings.xml clean install

* Generated artifact version would be as 0.0.X-SNAPSHOT
   For more specific version check pom.xml for Ganymede-Asciidoctorj

* Once build successful, run the jar via below command.
>  java -jar CodingTest-0.0.1-SNAPSHOT.jar

* If want to start as container, we need to build the Dockerfile.
> docker build -t phone-service .
> docker run -itd -p 8091:8080 phone-service

* run above command to start phone service as container.

### Testing Instructions
* Swagger API can be access at 
> http://host:port/swagger-ui/index.html
> by default host is localhost and port is 8080 until specified

* Currently 4 operations are supported
    * Post Phone Numbers
    * Get Phone Numbers via taskID
    * Get All the TaskIDs
    * delete phone numbers associated with given TaskID
* we will discuss testing instructions for each one of them in order.

* Post Phone Number
    * Operation expects the request to be sent at /api/process/file
    * the body of request expects the sourceFile as location to phone record file in json format  
    * Operation responses with the taskID for the process
    * Operation saves the processed phone number and persist them in database
    * The taskID can be referenced back to next API for fetching details of valid phones numbers
    * response is  404 in case No Phones found
* Get Phone Numbers via taskID
    * The taskID provided from above operation is passed as parameter in this operation.
    * Operation expects requests to be sent at /api/process/file/{taskID}, where taskID is parameter,discussed above.
    * Operation responses with the valid phone numbers from database in json format.
    * response is  404 in case No Phones found
* Get All the TaskIDs
    * The Operation expects request to be sent at /api/task
    * Operations responses with all the taskIDs in json format
    * response is  404 in case No Phones found    
* Delete phone numbers associated with given TaskID
    * Operation expects requests to be sent at /api/process/file/{taskID}
    * Here taskID is the parameter from first operation.
    * Operation deletes the record from database
    * Operation response with the delted taskID in case of success.
    * response is  404 in case No Phones found    


### Test Cases
Currently we have 5 test cases to test out the basic functionality.
we can run below command to execute the test cases.
> mvn clean test

### Deployment on AWS / Cloud
We can deploy phone-service as container or run directly on ec2 node.
we configure build pipeline on gitlab/jenkins, build the project 
and push the generated image to image repsitory (internal or external)
once build is completed, we can start deployment on aws ECS or cloud service.

A sample of steps are mentioned in deployToAws.sh to get started with local setup itself.

