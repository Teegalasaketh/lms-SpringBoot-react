<div style="background-color:black; color:white; padding:20px; font-family:Arial, sans-serif;">

# 📚 Library Management System (LMS)

A full-stack **Library Management System** built using **React.js** for the frontend and **Spring Boot** for the backend.  
This system allows **Admins** to manage books and **Students** to view, issue, and return books.

---

## 🧑‍💻 Tech Stack

### Frontend
- React.js  
- JavaScript (ES6)  
- HTML5, CSS3  
- Axios  
- React Router DOM  

### Backend
- Spring Boot  
- Java  
- Maven  
- RESTful APIs  

### Database
- MySQL

---

## 📁 Project Structure

<pre style="background-color:#111; color:#fff; padding:15px;">
SUB1_2025/
│
├── lms-frontend/
│   ├── public/
│   ├── src/
│   │   ├── admin/
│   │   ├── student/
│   │   ├── auth/
│   │   ├── api/
│   │   ├── components/
│   │   ├── App.js
│   │   ├── App.css
│   │   └── index.js
│   ├── package.json
│   └── package-lock.json
│
├── lms-springboot-backend/
│   ├── src/main/java/com/example/demo
│   │   ├── controller
│   │   ├── dao
│   │   ├── model
│   │   ├── config
│   │   └── LmsSpringbootApplication.java
│   ├── src/main/resources
│   │   └── application.properties
│   ├── pom.xml
│   └── mvnw
│
├── .gitignore
└── README.md
</pre>

---

## 👥 User Roles

### 👨‍💼 Admin
- Add new books  
- View all books  
- Issue books  
- View issued / reserved / returned books  
- Admin dashboard  

### 👨‍🎓 Student
- View available books  
- View issued books  
- View reserved books  
- Return books  
- Student dashboard  

---

## 🔐 Authentication & Security
- Login & Signup  
- Role-based access (Admin / Student)  
- Protected routes  
- Spring Security  

---

## 🚀 How to Run the Project

### Prerequisites
- Node.js (v16+)  
- Java JDK 17+  
- Maven  
- MySQL  
- Git  

---

### Clone Repository
<pre style="background-color:#111; color:#fff; padding:10px;">
git clone https://github.com/&lt;your-username&gt;/SUB1_2025.git
cd SUB1_2025
</pre>

---

### Backend Setup
<pre style="background-color:#111; color:#fff; padding:10px;">
cd lms-springboot-backend
mvn clean install
mvn spring-boot:run
</pre>

Runs on: **http://localhost:8080**

---

### Frontend Setup
<pre style="background-color:#111; color:#fff; padding:10px;">
cd lms-frontend
npm install
npm start
</pre>

Runs on: **http://localhost:3000**

---

## ⚙️ Database Configuration
Edit:
`src/main/resources/application.properties`

<pre style="background-color:#111; color:#fff; padding:10px;">
spring.datasource.url=jdbc:mysql://localhost:3306/lms
spring.datasource.username=root
spring.datasource.password=yourpassword

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
</pre>

---

## 📌 Future Enhancements
- JWT Authentication  
- Email Notifications  
- Book Search & Filters  
- Fine Calculation  
- Docker / AWS Deployment  
- Responsive UI  

---

## 📄 License
Educational use only.

</div>
