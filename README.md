📝 Task Management App

A secure and scalable Task Management Application built using Spring Boot, MongoDB, and Spring Security.
This app allows users to manage their tasks efficiently with authentication, authorization, and CRUD operations.

🚀 Features

🔐 User Authentication & Authorization (Spring Security)

🗂️ Create, Update, Delete, and View Tasks

📌 Task Status Management (Pending / Completed)

🧑‍💻 Role-based Access Control

🗃️ MongoDB for data persistence

📡 RESTful APIs

🛠️ Tech Stack

Backend: Spring Boot

Database: MongoDB

Security: Spring Security

Build Tool: Maven

Language: Java

📁 Project Structure
task-management-app
│
├── controller
├── service
├── repository
├── model
├── security
└── config

⚙️ Setup & Installation
1️⃣ Clone the Repository
git clone https://github.com/your-username/task-management-app.git
cd task-management-app

2️⃣ Configure MongoDB

Make sure MongoDB is running locally or update your application.properties:

spring.data.mongodb.uri=mongodb://localhost:27017/taskdb

3️⃣ Run the Application
mvn spring-boot:run


The app will start at:
👉 http://localhost:8080

🔐 Authentication

This project uses Spring Security for:

User login

Secure endpoints

Role-based access

Only authenticated users can manage tasks.
