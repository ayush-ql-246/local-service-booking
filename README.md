# Local Service Booking Platform (Backend)

A Spring Boot–based backend system for a local service booking platform where users can book services offered by providers, manage bookings, give ratings, and receive notifications.

---

## Features
- **User Management:** Register, login via otp, role-based access (User, Provider, Admin)
- **Provider Management:** Service details, availability, services offered
- **Booking System:** Book services, manage booking statuses
- **Ratings & Reviews:** Post ratings after completed bookings
- **Notifications:** notifications for bookings

---

## Tech Stack
- **Backend:** Spring Boot (Java)
- **Database:** PostgreSQL
- **ORM:** Hibernate/JPA
- **Security:** Spring Security, JWT Authentication
- **Build Tool:** Maven
- **API Style:** RESTful APIs

---

## Prerequisites
Before setting up the project, ensure you have:
- **Java 17** or above installed
- **Maven 3.8+** installed
- **PostgreSQL 14+** installed and running
- **Git** installed
- **IDE** like IntelliJ IDEA or VS Code

---

## Setup Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/ayush-ql-246/local-service-booking.git
cd local-service-booking-platform
```

### 2. Configure the Database

Create a PostgreSQL database:
CREATE DATABASE service_booking_db;

### 3. Update your application.properties file:
spring.datasource.url=<your_db_url>
spring.datasource.username=<your_db_username>
spring.datasource.password=<your_db_password>
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
jwt.secret=<your_jwt_secret_key>
jwt.expiration=<jwt_expiration_time_in_milliseconds>

### 4. Build and Run the Application
mvn clean install
mvn spring-boot:run

The application will start at: http://localhost:8080


### 5. API Endpoints

#### Common APIs
1) POST - /api/v1/auth/register :- Register User
2) POST - /api/v1/auth/request-otp :- Request OTP
3) POST - /api/v1/auth/login :- Login / verify OTP
4) GET - /api/v1/user/getUser :- Get logged in user data
5) PUT - /api/v1/user/update :- Update user data (any role)
6) GET - /api/v1/bookings/{bookingId} - Get Single booking detail by booking id (any role)

#### Provider APIs
7) PUT - /api/v1/providers/update :- Update provider profile
8) POST - /api/v1/providers/availability :- set weekly availability
9) GET - /api/v1/providers/availability :- Get weekly availability
10) POST - /api/v1/provider/services :- Add or update services
11) GET - /api/v1/provider/services :- Get All services of a Provider
12) DELETE - /api/v1/provider/services/{serviceId} :- Delete service
13) GET - /api/v1/bookings/provider?page={pageNo}&size={pageSize} :- Get all bookings of provider
14) PUT - /api/v1/bookings/{bookingId}/status :- Update booking status (For Provider)

#### User APIs
15) GET - /api/v1/services/catalog?category={category_type} :- Get services catalog for users
16) GET - /api/v1/services/catalog/available-services?category={category_type}&startTime={startTime}&endTime={endTime}&page={pageNo}&size={pageSize} :- Available services by category and time. (Note: start time and end time value can be in the range of 0-23.)
17) POST - /api/v1/bookings :- Create booking
18) GET - /api/v1/bookings/user?page={pageNo}&size={pageSize} :- Get all bookings of user
19) PUT - /api/v1/bookings/{bookingId}/cancel :- Cancel Booking (For User)
20) POST - /api/v1/bookings/{bookingId}/rating :- Add rating after service completion

#### Admin APIs
21) GET - /api/v1/admin/users?page={pageNo}&size={pageSize} :- Get all users
22) PUT - /api/v1/admin/users/1/status?status={status - ACTIVE/BLOCKED} :- Change User account status
23) GET - /api/v1/admin/bookings?page={pageNo}&size={pageSize} :- Get all bookings

### ER-Diagram
Link: https://quokkalabsllp-my.sharepoint.com/:i:/g/personal/ayush_joshi_quokkalabs_com/EQFFfLRsNdpPs2mqLa8RB0EBw6ZzAgxhYE4EIRcsJctyBQ?e=ptwK4K