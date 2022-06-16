![image](https://user-images.githubusercontent.com/9162650/163873043-8f64b0b8-c783-4c8f-a39d-9cba417f07fe.png)

# [Spring Boot - Sample Pilet Feed](https://piral.io/)
A simple Spring Boot sample pilet feed service for use with Piral. If you don't want to play around with a feed service locally then use the (free) community edition of the [official Piral Feed Service](https://feed.piral.cloud/). Alternatively, if you are interested in more beef such as running the official Piral Feed Service in your environment or accessing premium features in the online edition then have a look at [Piral Cloud website](https://www.piral.cloud/).

**The code in this project is based on the Pilet Specification V2 and uses a local MySQL database to save the pilets and related files.**

## Getting Started
The following are the steps needed to start the feed service:

- Install a version of MySQL 8 locally. Alternatively use the docker-compose found within the following project:
```
cd sample-pilet-service/docker/mysql
docker-compose up -d
```
- Import the spring boot application inside an IDE (e.g. IntelliJ Idea) and start the application. Alternatively, you can launch the application via maven from the root of the project:
```
cd sample-pilet-service
mvn spring-boot:run
```

## API

The APIs exposed by the application are:
- GET http://localhost:9000/api/v1/pilets/{appshell}

The {appshell} parameter allows to retrieve only the pilets published for a specific application shell.
```
Example: http://localhost:9000/api/v1/pilets/appshell1
Return:
{
	"items": [
		{
			"name": "test-pilet-1",
			"version": "1.0.0",
			"link": "http://localhost:9000/api/v1/pilets/files/appshell1/test-pilet-1/1.0.0/index.js",
			"requireRef": "wp4Chunkpr_testpilet1",
			"integrity": "sha256-lYHPk+RLXbvzXH7iVYodWwQEGW9Xa4s0/dZHXb1kbSA=",
			"spec": "v2"
		}
	]
}
```

- GET http://localhost:9000/api/v1/pilets/files/{appshell}/{pilet_name}/{package_version}/index.js
The first endpoint returns the files related to the returned pilets indicating the path of this endpoint, which allows to return the content of js/css files.
```
Example: http://localhost:9000/api/v1/pilets/files/appshell1/test-pilet-1/1.0.0/index.js
Return: 
//@pilet v:2(wp4Chunkpr_testpilet1,{})
System.register(["react"],(function(e){var...
...
...
```

- POST http://localhost:9000/api/v1/pilets/{appshell}
This endpoint allows you to upload the output of a pilet build. It accepts a .tgz file as input and stores the pilet metadata and associated files in the MySQL database.

This endpoint is protected by basic authentication. The credentials are contained within the Spring properties in the application.yml file and are the following:
```
username: piralfeed_piletpublish_user
password: piralfeed_piletpublish_password
```

## Usage within the application shell code

- Publish the pilet developed using the following command: npx pilet publish --fresh --url http://localhost:9000/api/v1/pilets/{appshell} --api-key cGlyYWxmZWVkX3BpbGV0cHVibGlzaF91c2VyOnBpcmFsZmVlZF9waWxldHB1Ymxpc2hfcGFzc3dvcmQ=
```
Example: 
npx pilet publish --fresh --url http://localhost:9000/api/v1/pilets/appshell1 --api-key cGlyYWxmZWVkX3BpbGV0cHVibGlzaF91c2VyOnBpcmFsZmVlZF9waWxldHB1Ymxpc2hfcGFzc3dvcmQ=
```
- Update the shell app code specifying the pilets retrieval endpoint: http://localhost:9000/api/v1/pilets/{appshell}
```
Example: 
const feedUrl = 'http://localhost:9000/api/v1/pilets/appshell1';
```

## Swagger

http://localhost:9000/swagger-ui/index.html