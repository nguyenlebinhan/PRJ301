# 🎓 PRJ301 - Thesis Management System

<div align="center">

<!-- TODO: Add project logo -->

[![GitHub stars](https://img.shields.io/github/stars/nguyenlebinhan/PRJ301?style=for-the-badge)](https://github.com/nguyenlebinhan/PRJ301/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/nguyenlebinhan/PRJ301?style=for-the-badge)](https://github.com/nguyenlebinhan/PRJ301/network)
[![GitHub issues](https://img.shields.io/github/issues/nguyenlebinhan/PRJ301?style=for-the-badge)](https://github.com/nguyenlebinhan/PRJ301/issues)
[![GitHub license](https://img.shields.io/github/license/nguyenlebinhan/PRJ301?style=for-the-badge)](LICENSE)

**A comprehensive web application to streamline the thesis management process for students and faculty.**

</div>

## 📖 Overview

The PRJ301 Thesis Management System is a web-based application designed to facilitate the lifecycle of academic theses within an educational institution. It provides functionalities for students to submit their theses, for supervisors to review and manage student progress, and for administrators to oversee the entire process. Built with Java Servlet and JSP, it offers a traditional, robust backend with dynamic web pages for user interaction, backed by a relational database for data storage.

## ✨ Features

-   🎯 **User Authentication & Authorization**: Secure login system with distinct roles for Students, Supervisors, and Administrators.
-   📝 **Thesis Submission**: Students can submit thesis proposals and final documents.
-   ✅ **Thesis Review & Approval**: Supervisors can review submitted theses, provide feedback, and manage approval status.
-   👤 **User Management**: Administrators can manage student and supervisor accounts.
-   🗃️ **Database Integration**: Persistent storage of thesis details, user information, and administrative data.
-   🌐 **Dynamic Web Interface**: Interactive user experience powered by JSP.

## 🖥️ Screenshots


## 🛠️ Tech Stack

**Backend:**
![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white)
![Servlet](https://img.shields.io/badge/Servlet-F05032?style=for-the-badge&logo=apache-tomcat&logoColor=white)
![JSP](https://img.shields.io/badge/JSP-E6B221?style=for-the-badge)
![JDBC](https://img.shields.io/badge/JDBC-blue?style=for-the-badge)

**Frontend:**
![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)

**Database:**
![SQL](https://img.shields.io/badge/SQL-4479A1?style=for-the-badge&logo=microsoft-sql-server&logoColor=white) <!-- Assumed SQL Server based on common Java setups, can be adapted -->

**Runtime/Server:**
![Apache Tomcat](https://img.shields.io/badge/Apache_Tomcat-F8DC75?style=for-the-badge&logo=apache-tomcat&logoColor=black)

## 🚀 Quick Start

Follow these steps to get your development environment set up.

### Prerequisites

Before you begin, ensure you have the following installed:

-   **Java Development Kit (JDK)**: Version 8 or higher.
-   **Apache Tomcat**: Version 9.x or higher (or another Servlet Container).
-   **SQL Database**: SQL Server (or a compatible relational database like MySQL/PostgreSQL).
-   **An IDE for Java Development**: Apache NetBeans, IntelliJ IDEA, or Eclipse are recommended.

### Installation

1.  **Clone the repository**
    ```bash
    git clone https://github.com/nguyenlebinhan/PRJ301.git
    cd PRJ301
    ```

2.  **Database Setup**
    *   **Create Database**: Create a new database in your SQL Server instance (e.g., `ThesisDB`).
    *   **Run SQL Script**: Execute the `SimpleQuery.sql` script located in the repository root against your newly created database. This will create the necessary tables and populate initial data.
    ```sql
    -- Example for SQL Server Management Studio or command line
    -- USE [ThesisDB];
    -- GO
    -- [Copy and paste contents of SimpleQuery.sql here]
    ```

3.  **Configure Database Connection**
    *   Navigate to the `ThesisManagement` project directory.
    *   Locate the database connection configuration within the Java source files (typically in a utility class or properties file, e.g., `src/java/utils/DBUtils.java` or similar).
    *   Update the connection parameters (database URL, username, password) to match your local database setup.

4.  **Build the Project**
    *   Open the `ThesisManagement` project in your preferred Java IDE (e.g., NetBeans, IntelliJ IDEA).
    *   Ensure all necessary `.jar` dependencies from the `lib/` directory are included in the project's build path.
    *   Build the project to compile Java sources and generate a `.war` file.
    ```bash
    # This process is typically handled by your IDE.
    # For NetBeans, right-click project -> Clean and Build.
    # For IntelliJ/Eclipse, use their respective build commands.
    ```

5.  **Deploy to Apache Tomcat**
    *   Copy the generated `.war` file (e.g., `ThesisManagement.war`) from your project's `dist/` or `target/` directory into Tomcat's `webapps/` directory.
    *   Start or restart your Apache Tomcat server. Tomcat will automatically deploy the application.

6.  **Open your browser**
    *   Visit `http://localhost:8080/ThesisManagement` (or the appropriate URL if you've configured Tomcat differently or renamed the `.war` file).

## 📁 Project Structure

```
PRJ301/
├── AI plag/              # Possibly related to plagiarism detection, documentation, or research.
├── SimpleQuery.sql       # SQL script for database schema creation and initial data.
├── ThesisManagement/     # Main Java Web Application project.
│   ├── build/            # (IDE-generated) Build artifacts.
│   ├── nbproject/        # (NetBeans-specific) Project configuration files.
│   ├── src/
│   │   ├── java/         # Java source code (Servlets, DAO, Models, Utility classes).
│   │   └── java.java     # (Placeholder/example, likely actual java packages/files are here)
│   ├── web/              # Web content root for JSP files, HTML, CSS, JS.
│   │   ├── WEB-INF/      # Web application deployment descriptor (web.xml), libraries, classes.
│   │   ├── index.jsp     # Main entry point/welcome file.
│   │   └── [other JSP/HTML/CSS/JS files]
│   └── pom.xml           # (If Maven is used, otherwise build is IDE-driven)
├── lib/                  # External Java libraries (.jar files) required by the application.
└── [other detected files or directories]
```

## ⚙️ Configuration

### Database Connection
The application requires specific database connection details. These are typically configured within a Java class (e.g., `DBUtils.java`, `DataSource.java`) inside the `ThesisManagement/src/java` directory. You will need to modify the following:

-   `DB_URL`: The JDBC URL for your database.
-   `DB_USERNAME`: Your database username.
-   `DB_PASSWORD`: Your database password.

### Web Deployment Descriptor
The `ThesisManagement/web/WEB-INF/web.xml` file defines the servlets, servlet mappings, filters, listeners, and other deployment configurations for the web application. Review this file for specific servlet URLs or security constraints.

## 🔧 Development

### Recommended IDE
We recommend using an IDE like Apache NetBeans, IntelliJ IDEA, or Eclipse for Java web development. These IDEs provide excellent support for JSP, Servlets, and project building/deployment to Tomcat.

### Building
The project is typically built directly through the IDE, which compiles Java source files and packages the web resources into a `.war` file.

### Running
Once deployed to Apache Tomcat, the application runs directly on the server. No separate "start" command is typically needed beyond starting Tomcat itself.

## 🧪 Testing

This project does not include explicit unit or integration test frameworks in the provided structure. Testing is expected to be manual through the web interface.

## 🚀 Deployment

The `ThesisManagement` project can be deployed to any Servlet container compliant with Servlet API 3.0 or higher (e.g., Apache Tomcat 9+). The standard method involves placing the generated `.war` file into the server's `webapps/` directory.

## 📚 API Reference (Internal)

The application uses Java Servlets to handle requests and interact with the database via JDBC. JSPs are used for view rendering.

### Key Servlets
(Based on typical Thesis Management System functionality, look for `.java` files extending `HttpServlet`)

-   `LoginServlet`: Handles user authentication.
-   `RegisterServlet`: For new user registration (if implemented).
-   `ThesisControllerServlet`: Manages thesis-related operations (list, add, edit, view).
-   `UserControllerServlet`: Manages user accounts (for admin).
-   `LogoutServlet`: Handles user logout.

## 🤝 Contributing

We welcome contributions to enhance this Thesis Management System! Please refer to our [Contributing Guide](CONTRIBUTING.md) <!-- TODO: Create CONTRIBUTING.md --> for details on how to get started.

### Development Setup for Contributors
Ensure you have the prerequisites listed above and are familiar with Java Servlet/JSP development within an IDE.

## 📄 License

This project is licensed under the [LICENSE_NAME](LICENSE) - see the LICENSE file for details. <!-- TODO: Specify a license like MIT, Apache 2.0, etc., and add a LICENSE file if not present -->

## 🙏 Acknowledgments

-   **Java Platform**: The core technology powering this application.
-   **Apache Tomcat**: For providing the robust servlet container.
-   **SQL Database**: For reliable data persistence.

## 📞 Support & Contact

-   🐛 Issues: [GitHub Issues](https://github.com/nguyenlebinhan/PRJ301/issues)
-   👤 Author: [nguyenlebinhan](https://github.com/nguyenlebinhan)

---

<div align="center">

**⭐ Star this repo if you find it helpful!**

Made with ❤️ by nguyenlebinhan <!-- TODO: Confirm author name if different -->

</div>
