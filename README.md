# Healthy Endpoints Dinit Project

### Project Description:
This repository contains the backend part of a Spring Boot application developed for the summer camp at DInit d.o.o. 

The application is designed to monitor the health of URLs. The application follows a three-tier architecture, using REST APIs and DTOs to efficiently manage data across a presentation, business logic, and data access layers.

### Key features:
- Background Health Check Service: Continuosly monitors all of the URLs at a regular intervals using a scheduled timer.
- Health Check: Check the health of the URL. If its healthy it will return true, if its not healthy it will return false. This is done in one of two ways:
 - Reachability check: A URL is considered healthy if it returns an HTTTP status code between 200 and 300.
 - Status Check: If the URL returns status code between 200 and 300, the service inspects the JSON response's status property and classifies the URL as: "Healthy" when the status property indicates a healthy state, and "Unhealthy" when the status propert indicates an unhealthy state.

----

### Tech Stack: 
- Java - Spring Boot
- Angular 
- PostgreSQL
- Docker
- Azure

----

### Important Links:
GitHub repository for the frontend: [Healthy Endpoints Frontend](https://github.com/Paveljolak/healthy-endpoints-dinitproject-frontend)

----

#### NOTE:
This project is publicly shared with the permission of DInit d.o.o. However, please note that I do not have permission to share any logos or branding associated with the company.

If you encounter any logos or branding in this repository, please submit an issue so I can remove it immediately, as it was not intended to be included.



