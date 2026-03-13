## 🎓 Hệ Thống Quản Lý Khóa Luận (Java Servlet + JSP) / Thesis Management System

Ứng dụng web quản lý khóa luận tốt nghiệp cho sinh viên, giảng viên và quản trị viên, xây dựng bằng **Java Servlet + JSP** và cơ sở dữ liệu quan hệ.

Web-based thesis management system for students, lecturers, and administrators, built with **Java Servlet + JSP** and a relational database.

---

## 📖 Giới thiệu / Overview

**Tiếng Việt**

Hệ thống hỗ trợ toàn bộ quy trình khóa luận:

- Quản lý tài khoản và phân quyền (Sinh viên, Giảng viên, Admin).
- Đăng ký đề tài, phê duyệt đề tài, theo dõi tiến độ.
- Quản lý lịch hẹn, lịch gặp giảng viên hướng dẫn.
- Lưu trữ thông tin khóa luận và lịch sử thay đổi.

**English**

The system supports the full thesis lifecycle:

- User management and role-based access (Student, Lecturer, Admin).
- Topic registration, approval workflow, and progress tracking.
- Appointment scheduling between students and supervisors.
- Persistent storage of thesis information and history.

---

## ✨ Chức năng chính / Main Features

- **Xác thực & phân quyền / Authentication & Authorization**: Đăng nhập, đăng xuất, phân quyền theo vai trò thông qua `AuthController` và `AuthFilter`.
- **Quản lý sinh viên / Student Management**: Chức năng cho sinh viên đăng ký đề tài, xem trạng thái, lịch sử khóa luận (`StudentController`, JSP trong `web/jsp/student`).
- **Quản lý giảng viên / Lecturer Management**: Giảng viên xem danh sách sinh viên hướng dẫn, duyệt đề tài, quản lý lịch hẹn (`LecturerController`, JSP trong `web/jsp/lecturer`).
- **Quản trị hệ thống / Administration**: Admin quản lý tài khoản, đề tài, cấu hình chung (`AdminController`, JSP trong `web/jsp/admin`).
- **Giao diện động / Dynamic UI**: Xây dựng bằng JSP, JSTL và HTML/CSS/JS.

---

## 🛠 Công nghệ / Tech Stack

**Tiếng Việt**

- **Ngôn ngữ & Backend**: Java, Servlet API, JSP, JSTL, JDBC.
- **Cơ sở dữ liệu**: SQL Server (hoặc hệ quản trị CSDL quan hệ tương đương), script trong `SimpleQuery.sql`.
- **Server**: Apache Tomcat (Servlet container).
- **Frontend**: JSP, HTML5, CSS3, JavaScript.
- **Thư viện ngoài (.jar)**: JDBC driver cho SQL Server, Jakarta Mail/JSTL, v.v. (thư mục `lib/` và `ThesisManagement/web/WEB-INF/lib/`).

**English**

- **Language & Backend**: Java, Servlet API, JSP, JSTL, JDBC.
- **Database**: SQL Server (or compatible RDBMS), initialized via `SimpleQuery.sql`.
- **Server**: Apache Tomcat (Servlet container).
- **Frontend**: JSP, HTML5, CSS3, JavaScript.
- **External libraries (.jar)**: SQL Server JDBC driver, Jakarta Mail/JSTL, etc. (in `lib/` and `ThesisManagement/web/WEB-INF/lib/`).

---

## 📁 Cấu trúc thư mục / Folder Structure

```text
PRJ Final/
├── AI plag/                 # Công cụ Python (AI/plagiarism, tài liệu phụ trợ)
│   ├── main.py              # Script Python chính
│   ├── README.md            # README riêng cho phần AI
│   └── venv/                # Virtual env Python (có thể bỏ qua khi làm Java)
├── lib/                     # Thư viện Java .jar dùng chung (JSTL, mail, JDBC, ...)
├── SimpleQuery.sql          # Script SQL khởi tạo CSDL cho hệ thống khóa luận
├── ThesisManagement/        # Dự án web Java Servlet + JSP (NetBeans/Ant)
│   ├── build.xml            # Script Ant build project
│   ├── dist/
│   │   └── ThesisManagement.war   # File .war đã build sẵn
│   ├── nbproject/           # Cấu hình NetBeans
│   ├── src/
│   │   ├── conf/
│   │   │   └── MANIFEST.MF
│   │   └── java/
│   │       ├── config/      # Listener cấu hình ứng dụng (vd: AppListener)
│   │       ├── controller/  # Các Servlet controller (Admin, Auth, Lecturer, Student)
│   │       ├── dal/         # Kết nối DB, khởi tạo DB (DBContext, DBInitializer)
│   │       ├── dao/         # Data Access Object cho User, Thesis, Topic, ...
│   │       ├── dto/         # DTO/transfer objects
│   │       ├── filter/      # AuthFilter cho phân quyền truy cập
│   │       ├── model/       # Model/domain classes
│   │       ├── service/     # Business services (nếu có)
│   │       ├── servlet/     # Servlet phụ (nếu có)
│   │       └── utils/       # Helper/utility classes
│   └── web/
│       ├── index.html       # Trang vào hệ thống (welcome)
│       ├── META-INF/        # File context cấu hình cho server
│       └── WEB-INF/
│           ├── web.xml      # Deployment descriptor, mapping servlet/filter
│           ├── lib/         # .jar cần cho ứng dụng web
│           └── jsp/
│               ├── auth/    # JSP đăng nhập/đăng ký, quên mật khẩu, ...
│               ├── admin/   # JSP cho admin
│               ├── lecturer/# JSP cho giảng viên
│               ├── student/ # JSP cho sinh viên
│               └── error/   # Trang lỗi 403, 404, ...
└── README.md                # (File này) Mô tả tổng quan dự án
```

**Tóm tắt (VN)**: Làm việc với Java web thì bạn chủ yếu mở dự án `ThesisManagement` trong NetBeans/IDE, còn thư mục `AI plag` là phần Python tách riêng.

**Summary (EN)**: For Java web development you mainly open the `ThesisManagement` project in your IDE; the `AI plag` folder is a separate Python helper project.

---

## 🚀 Cài đặt & Chạy / Setup & Run

### 1. Yêu cầu môi trường / Prerequisites

**Tiếng Việt**

- **JDK**: Java 8 trở lên (JDK 11 khuyến nghị).
- **Apache Tomcat**: Phiên bản 9.x hoặc tương đương hỗ trợ Servlet 4+.
- **CSDL**: SQL Server (hoặc RDBMS tương thích, cần tự chỉnh JDBC URL).
- **IDE Java**: NetBeans (phù hợp nhất với cấu trúc hiện tại), IntelliJ IDEA hoặc Eclipse.

**English**

- **JDK**: Java 8+ (JDK 11 recommended).
- **Apache Tomcat**: Version 9.x or any Servlet 4+ compatible container.
- **Database**: SQL Server (or another RDBMS, adjust JDBC URL accordingly).
- **Java IDE**: NetBeans (best fit for this project), IntelliJ IDEA, or Eclipse.

### 2. Khởi tạo cơ sở dữ liệu / Database Setup

**Tiếng Việt**

1. Tạo một database mới trong SQL Server, ví dụ: `ThesisManagementDB`.
2. Mở file `SimpleQuery.sql` và chạy toàn bộ script lên database vừa tạo để tạo bảng và dữ liệu mẫu (nếu có).

**English**

1. Create a new database in SQL Server, e.g., `ThesisManagementDB`.
2. Open `SimpleQuery.sql` and execute the script against that database to create tables and seed data (if included).

### 3. Cấu hình kết nối DB / Configure Database Connection

**Tiếng Việt**

- Mở project `ThesisManagement` trong IDE.
- Tìm đến các class trong package `dal` (ví dụ: `DBContext`, `DBInitializer`) trong `ThesisManagement/src/java/dal/`.
- Cập nhật các thông tin:
  - URL kết nối JDBC (database name, host, port).
  - Tên đăng nhập và mật khẩu của SQL Server.

**English**

- Open the `ThesisManagement` project in your IDE.
- Locate the classes in the `dal` package (e.g., `DBContext`, `DBInitializer`) under `ThesisManagement/src/java/dal/`.
- Update:
  - JDBC URL (database name, host, port).
  - Database username and password.

### 4. Build dự án / Build the Project

**Tiếng Việt**

- Trong NetBeans: chuột phải vào project `ThesisManagement` → **Clean and Build**.
- Đảm bảo tất cả `.jar` trong `lib/` và `ThesisManagement/web/WEB-INF/lib/` nằm trong classpath của project.
- Sau khi build, file `.war` sẽ được tạo tại `ThesisManagement/dist/ThesisManagement.war`.

**English**

- In NetBeans: right-click the `ThesisManagement` project → **Clean and Build**.
- Ensure all necessary `.jar` files from `lib/` and `ThesisManagement/web/WEB-INF/lib/` are on the project classpath.
- After building, a `.war` file will be created at `ThesisManagement/dist/ThesisManagement.war`.

### 5. Triển khai lên Tomcat / Deploy to Tomcat

**Tiếng Việt**

Bạn có hai cách phổ biến:

1. **Chạy trực tiếp từ IDE**: Cấu hình Tomcat trong NetBeans, chọn project `ThesisManagement` → Run/Debug.
2. **Triển khai thủ công**:
   - Sao chép `ThesisManagement/dist/ThesisManagement.war` vào thư mục `webapps/` của Tomcat.
   - Khởi động hoặc khởi động lại Tomcat, Tomcat sẽ tự giải nén và deploy ứng dụng.
   - Truy cập: `http://localhost:8080/ThesisManagement` (hoặc context path bạn cấu hình).

**English**

You can use either approach:

1. **Run from IDE**: Configure Tomcat in NetBeans and Run/Debug the `ThesisManagement` project.
2. **Manual deployment**:
   - Copy `ThesisManagement/dist/ThesisManagement.war` into Tomcat’s `webapps/` folder.
   - Start or restart Tomcat to deploy the app.
   - Open `http://localhost:8080/ThesisManagement` (or your configured context path).

---

## 📌 Cách sử dụng & các module chính / Usage & Main Modules

**Tiếng Việt**

- **Đăng nhập**: Người dùng truy cập trang đăng nhập (JSP trong `web/WEB-INF/jsp/auth/`), xử lý bởi `AuthController` và `AuthFilter`.
- **Sinh viên** (`/student/*`):
  - Xem danh sách đề tài khả dụng.
  - Đăng ký/huỷ đăng ký đề tài.
  - Xem tiến độ và lịch sử chỉnh sửa khóa luận.
- **Giảng viên** (`/lecturer/*`):
  - Xem danh sách sinh viên hướng dẫn.
  - Duyệt/ từ chối đề tài, cập nhật trạng thái.
  - Quản lý lịch hẹn, nhận xét.
- **Admin** (`/admin/*`):
  - Quản lý tài khoản sinh viên/giảng viên.
  - Quản lý danh sách đề tài, cấu hình hệ thống.

**English**

- **Login**: Users visit the login page (JSP in `web/WEB-INF/jsp/auth/`), handled by `AuthController` and `AuthFilter`.
- **Student** (`/student/*`):
  - View available thesis topics.
  - Register/unregister for topics.
  - View progress and history of thesis changes.
- **Lecturer** (`/lecturer/*`):
  - See supervised students.
  - Approve/reject topics and update thesis status.
  - Manage appointments and feedback.
- **Admin** (`/admin/*`):
  - Manage student/lecturer accounts.
  - Manage topics and global system settings.

Các controller chính nằm trong `src/java/controller`, các lớp truy cập dữ liệu (DAO) trong `src/java/dao`, và giao diện JSP được tổ chức trong `web/WEB-INF/jsp/...`.

The main controllers are under `src/java/controller`, data access (DAO) classes under `src/java/dao`, and JSP views are organized under `web/WEB-INF/jsp/...`.

---

## ⚙️ Cấu hình & Tùy biến / Configuration & Customization

**Tiếng Việt**

- **URL & context path**: Được cấu hình trong `ThesisManagement/web/WEB-INF/web.xml` (các `<servlet-mapping>`, `<filter-mapping>`). Bạn có thể thay đổi `url-pattern` để chỉnh lại đường dẫn.
- **Phiên làm việc (session)**: Thời gian timeout phiên được thiết lập trong `web.xml` (thẻ `<session-config>`).
- **Email hoặc dịch vụ ngoài**: Nếu sử dụng thư viện mail trong `lib/`, cấu hình thường nằm trong package `config` hoặc `utils` (hãy kiểm tra các class tương ứng để chỉnh SMTP, tài khoản gửi mail, v.v.).

**English**

- **URL & context path**: Defined in `ThesisManagement/web/WEB-INF/web.xml` (via `<servlet-mapping>` and `<filter-mapping>`). You can adjust `url-pattern` values to change endpoint paths.
- **Session settings**: Session timeout is configured in `web.xml` under `<session-config>`.
- **Email & external services**: If mail libraries in `lib/` are used, configuration will typically live in `config` or `utils` packages (check those classes to adjust SMTP, sender account, etc.).

---

## 👤 Tác giả & Bản quyền / Author & License

**Tiếng Việt**

- **Tác giả**: Nguyễn Lê Binh An - Quach Hoàng Hải - Nguyễn Hữu Thành Vinh.
- **Giấy phép (License)**: Dự án dùng cho học tập/thực hành. Nếu cần phát hành công khai, hãy thêm file `LICENSE` (ví dụ: MIT, Apache 2.0) 

**English**

- **Author**: Update this section with your name/team/supervisor information as needed.
- **License**: Currently intended for learning/academic purposes. For public release, add a `LICENSE` file (e.g., MIT, Apache 2.0) 
---

## 📩 Liên hệ / Contact

Nếu bạn cần hỗ trợ cấu hình hoặc chạy dự án, hãy liên hệ trực tiếp với nhóm phát triển.

For support or questions about configuration and running the project, please contact the project team.
