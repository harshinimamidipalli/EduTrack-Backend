# EduTrack Backend

EduTrack is an academic assignment management system designed to simplify how teachers distribute assignments and how students access and submit them.

This repository contains the **Spring Boot backend** of the EduTrack application. It handles user authentication, assignment management, file uploads (images & PDFs), student submissions, and database operations.

---

## 🔗 Related Repositories

- **Frontend Repository:**  
  https://github.com/harshinimamidipalli/EduTrack-Frontend

- **Backend Repository:**  
  https://github.com/harshinimamidipalli/EduTrack-Backend

---

## 🚀 Features

- User authentication with role-based access
  - Teacher
  - Student
- Assignment management
  - Create assignments
  - Set deadlines
  - Add descriptions
  - Upload images
  - Upload PDFs
- Student features
  - View assignments
  - Submit assignments
  - Re-submit assignments before deadline
- File handling
  - Image uploads
  - PDF uploads
  - Static file serving using Spring configuration
- Database integration using PostgreSQL
- RESTful APIs for frontend communication
- Global CORS configuration for frontend connectivity

---

## 🛠 Tech Stack

- **Java:** 17.0.11  
- **Spring Boot:** 3.5.7  
- **Spring Data JPA**
- **Hibernate**
- **PostgreSQL**
- **Maven**
- **REST APIs**

---

## 📂 Project Structure

src/main/java/com/edutrack
├── controller # REST controllers
├── model # Entity classes
├── repository # JPA repositories
├── service # Business logic
└── config # WebConfig & CORS configuration

## ⚙️ Database Configuration

- **Database Name:** `edutrack_db`
- **Database User:** `postgres`


## 📡 API Endpoints

Some important endpoints used by the frontend:

GET    /assignments
POST   /assignments/upload

GET    /submissions
POST   /submissions/upload

GET    /users
POST   /login

## 🔮 Future Enhancements

Role-based dashboards with permissions

Notifications for new assignments

Assignment grading analytics

Admin dashboard

Cloud-based file storage

Deployment to production environment

## 👩‍💻 Author

Author: Harshini M

## 📄 License

This project is developed for academic and learning purposes.
