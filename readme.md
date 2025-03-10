# Animal Movement Tracker


**I'm applying for the backend position.**


## 1. Project Overview

- Providing role-based authentication and authorization.
- Storing and organizing data within a **normalized database** structure.
- Offering a user-friendly interface to view, create, and update farms and movement records.
- Visualizing farms and movements on an integrated map.
- Enforcing robust validations at the database, backend, and frontend levels.
- Exposing a comprehensive REST API documented with **Swagger**.



## 2. Features

1. **Role-Based Authentication**  
   \- Supports **USER**, **ADMIN**, and **VIEWER** roles, enforced both in the frontend and backend.

2. **Normalized Database**  
   \- Clearly separated entities for Users, Roles, Farms, and Movements.

3. **Easy-to-Navigate UI**  
   \- A clean and intuitive interface to manage farms, movement records, and user roles.

4. **Map Visualization**  
   \- Interactive map to view farm locations and track animal movements between farms.

5. **Validations**  
   \- Comprehensive validation on forms, ensuring correct data entry and integrity at every layer.

6. **Swagger Documentation**  
   \- Quickly learn and test available API endpoints.


## 3. Technologies Used

- **Docker & Docker Compose** for containerization and environment setup.
- **Backend**  
  \- Spring Boot
- **Frontend**  
  \- Angular
- **Database**  
  \- PostgreSQL.



## 4. Prerequisites

Before running this project, ensure that you have installed the following tools:

1. **Git** – to clone the repository.  
2. **Docker** – to containerize and run the application.  



## 5. Installation and Setup

1. **Clone the Repository**  
   ```bash
   git clone -b master <repository-url>
   ```
2. **Run the application** 
    ```bash
       docker compose up --build
3. **Access the application**
    - Frontend: http://localhost:4200
    - Backend: http://localhost:8080
    - Swagger Docs: http://localhost:8080/swagger-ui/index.html#/


## 6. Authentication

### Admin Account
**Username**: admin 

**Password**: admin123

**Role**: ADMIN


### User Account

**Username**: user

**Password**: user123

**Role**: USER

### Viewer Account

**Username**: viewer

**Password**: viewer123

**Role**: VIEWER

## 7. Role Capabilities

### ADMIN

Full access to create, edit, delete users, farms and movements.

### USER

Can create and view movements but can only view farms

### VIEWER

Can only view farms and movements; no editing or creation privileges.

