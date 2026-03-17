# Hướng Dẫn Deploy Ứng Dụng Thesis Management Lên AWS

## Tổng Quan

Ứng dụng này là một Java Web Application (JSP/Servlet) sử dụng SQL Server database. Có 2 phương án deploy chính:

1. **AWS Elastic Beanstalk** (Khuyến nghị - Dễ nhất)
2. **AWS EC2 với Tomcat** (Nhiều quyền kiểm soát hơn)

---

## Phương Án 1: AWS Elastic Beanstalk (Khuyến nghị)

### Yêu Cầu
- AWS Account
- AWS CLI đã cài đặt và cấu hình
- EB CLI (Elastic Beanstalk CLI) - Tùy chọn
- WAR file đã build sẵn (`dist/ThesisManagement.war`)

### Bước 1: Chuẩn Bị Database (AWS RDS SQL Server)

1. **Tạo RDS SQL Server Instance:**
   - Vào AWS Console → RDS → Create Database
   - Chọn **SQL Server Express Edition** (miễn phí) hoặc Standard Edition
   - Cấu hình:
     - DB Instance Identifier: `thesis-management-db`
     - Master Username: `admin` (hoặc tên bạn muốn)
     - Master Password: (đặt mật khẩu mạnh)
     - DB Instance Class: `db.t3.micro` (free tier) hoặc lớn hơn
     - Storage: 20 GB (tối thiểu)
     - VPC: Chọn VPC mặc định hoặc tạo mới
     - Public Access: **Yes** (để Elastic Beanstalk có thể kết nối)
     - Security Group: Tạo mới hoặc chọn existing, mở port 1433

2. **Ghi Lại Thông Tin:**
   - Endpoint: `thesis-management-db.xxxxx.us-east-1.rds.amazonaws.com`
   - Port: `1433`
   - Database Name: Tạo database mới tên `ThesisManagement`

3. **Tạo Database:**
   - Kết nối đến RDS instance bằng SQL Server Management Studio hoặc Azure Data Studio
   - Chạy lệnh: `CREATE DATABASE ThesisManagement;`

### Bước 2: Cấu Hình Email (AWS SES - Tùy chọn)

Nếu ứng dụng cần gửi email:

1. Vào AWS SES → Verified identities
2. Verify email domain hoặc email address
3. Tạo SMTP credentials trong SES → SMTP settings
4. Ghi lại: SMTP endpoint, username, password

### Bước 3: Deploy Lên Elastic Beanstalk

#### Cách 1: Sử dụng AWS Console (Dễ nhất)

1. **Tạo Application:**
   - Vào AWS Console → Elastic Beanstalk → Create Application
   - Application name: `ThesisManagement`
   - Platform: **Java**
   - Platform version: **Java 17 running on 64bit Amazon Linux 2** (hoặc Java 11)
   - Application code: **Upload your code** → Chọn file `dist/ThesisManagement.war`

2. **Cấu Hình Environment:**
   - Environment name: `thesis-management-env`
   - Domain: (để trống hoặc nhập subdomain)
   - Description: (tùy chọn)

3. **Cấu Hình Environment Variables:**
   - Trong phần Configuration → Software → Environment properties, thêm:
     ```
     DB_URL=jdbc:sqlserver://your-rds-endpoint:1433;databaseName=ThesisManagement;encrypt=true;trustServerCertificate=true
     DB_USERNAME=admin
     DB_PASSWORD=your_password
     ```
   - Nếu có email:
     ```
     EMAIL_HOST=email-smtp.us-east-1.amazonaws.com
     EMAIL_PORT=587
     EMAIL_USERNAME=your_ses_username
     EMAIL_PASSWORD=your_ses_password
     EMAIL_FROM=noreply@yourdomain.com
     ```

4. **Cấu Hình Security Group:**
   - Vào Configuration → Security → Edit
   - Thêm inbound rule cho port 80, 443 (HTTP/HTTPS)
   - Đảm bảo có thể kết nối đến RDS (port 1433)

5. **Deploy:**
   - Click **Create environment**
   - Đợi 5-10 phút để AWS tạo environment và deploy

6. **Kiểm Tra:**
   - Sau khi deploy xong, bạn sẽ có URL: `http://thesis-management-env.xxxxx.us-east-1.elasticbeanstalk.com`
   - Truy cập URL để kiểm tra ứng dụng

#### Cách 2: Sử dụng EB CLI (Nâng cao)

1. **Cài đặt EB CLI:**
   ```bash
   pip install awsebcli
   ```

2. **Khởi tạo EB:**
   ```bash
   cd "C:\Users\ADMIN\Desktop\PRJ Final\ThesisManagement"
   eb init -p "java-17" thesis-management
   ```

3. **Tạo environment:**
   ```bash
   eb create thesis-management-env
   ```

4. **Set environment variables:**
   ```bash
   eb setenv DB_URL="jdbc:sqlserver://your-rds-endpoint:1433;databaseName=ThesisManagement;encrypt=true;trustServerCertificate=true" \
            DB_USERNAME="admin" \
            DB_PASSWORD="your_password"
   ```

5. **Deploy:**
   ```bash
   eb deploy
   ```

6. **Mở ứng dụng:**
   ```bash
   eb open
   ```

### Bước 4: Cập Nhật Deployment

Khi có thay đổi code:

1. **Build lại WAR file:**
   - Sử dụng IDE (NetBeans) hoặc Ant build script
   - WAR file sẽ ở `dist/ThesisManagement.war`

2. **Deploy lại:**
   - **Console:** Vào Elastic Beanstalk → Upload and deploy
   - **EB CLI:** `eb deploy`

---

## Phương Án 2: AWS EC2 với Tomcat

### Bước 1: Tạo EC2 Instance

1. Launch EC2 instance:
   - AMI: Amazon Linux 2
   - Instance type: t2.micro (free tier) hoặc t3.small
   - Security Group: Mở port 22 (SSH), 80, 443, 8080 (Tomcat)
   - Key Pair: Tạo hoặc chọn key pair để SSH

### Bước 2: Cài Đặt Java và Tomcat

SSH vào EC2 instance:

```bash
# Cập nhật hệ thống
sudo yum update -y

# Cài đặt Java 17
sudo yum install java-17-amazon-corretto -y

# Tải Tomcat 10
cd /opt
sudo wget https://archive.apache.org/dist/tomcat/tomcat-10/v10.1.20/bin/apache-tomcat-10.1.20.tar.gz
sudo tar -xzf apache-tomcat-10.1.20.tar.gz
sudo mv apache-tomcat-10.1.20 tomcat
sudo chown -R ec2-user:ec2-user /opt/tomcat

# Tạo systemd service
sudo nano /etc/systemd/system/tomcat.service
```

Nội dung file `tomcat.service`:
```ini
[Unit]
Description=Apache Tomcat
After=network.target

[Service]
Type=forking
User=ec2-user
Group=ec2-user
Environment="JAVA_HOME=/usr/lib/jvm/java-17-amazon-corretto"
Environment="CATALINA_HOME=/opt/tomcat"
Environment="CATALINA_BASE=/opt/tomcat"
ExecStart=/opt/tomcat/bin/startup.sh
ExecStop=/opt/tomcat/bin/shutdown.sh
Restart=on-failure

[Install]
WantedBy=multi-user.target
```

```bash
# Khởi động Tomcat
sudo systemctl daemon-reload
sudo systemctl enable tomcat
sudo systemctl start tomcat
```

### Bước 3: Deploy WAR File

```bash
# Upload WAR file lên EC2 (từ máy local)
scp -i your-key.pem dist/ThesisManagement.war ec2-user@your-ec2-ip:/tmp/

# SSH vào EC2
ssh -i your-key.pem ec2-user@your-ec2-ip

# Copy WAR vào webapps
sudo cp /tmp/ThesisManagement.war /opt/tomcat/webapps/ROOT.war
sudo chown ec2-user:ec2-user /opt/tomcat/webapps/ROOT.war

# Restart Tomcat
sudo systemctl restart tomcat
```

### Bước 4: Cấu Hình Environment Variables

```bash
# Tạo file environment
sudo nano /opt/tomcat/bin/setenv.sh
```

Nội dung:
```bash
#!/bin/bash
export DB_URL="jdbc:sqlserver://your-rds-endpoint:1433;databaseName=ThesisManagement;encrypt=true;trustServerCertificate=true"
export DB_USERNAME="admin"
export DB_PASSWORD="your_password"
export JAVA_OPTS="-Xmx512m -Xms256m"
```

```bash
sudo chmod +x /opt/tomcat/bin/setenv.sh
sudo systemctl restart tomcat
```

### Bước 5: Cấu Hình Nginx (Reverse Proxy - Tùy chọn)

```bash
# Cài đặt Nginx
sudo yum install nginx -y

# Cấu hình
sudo nano /etc/nginx/conf.d/tomcat.conf
```

Nội dung:
```nginx
server {
    listen 80;
    server_name your-domain.com;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```

```bash
sudo systemctl enable nginx
sudo systemctl start nginx
```

---

## Cấu Hình Bảo Mật

### 1. HTTPS với AWS Certificate Manager

1. Request certificate trong ACM
2. Cấu hình Load Balancer trong Elastic Beanstalk để sử dụng certificate
3. Redirect HTTP → HTTPS

### 2. Security Groups

- Chỉ mở port cần thiết
- RDS Security Group: Chỉ cho phép kết nối từ Elastic Beanstalk/EC2 security group

### 3. Database Backup

- Bật automated backups trong RDS
- Retention period: 7 ngày (tối thiểu)

---

## Monitoring và Logs

### Elastic Beanstalk:
- Xem logs: `eb logs` hoặc trong Console → Logs
- Health monitoring tự động

### EC2:
```bash
# Xem Tomcat logs
tail -f /opt/tomcat/logs/catalina.out
tail -f /opt/tomcat/logs/localhost.log
```

---

## Troubleshooting

### Lỗi kết nối Database:
- Kiểm tra Security Group của RDS
- Kiểm tra endpoint và credentials
- Test kết nối từ EC2: `telnet rds-endpoint 1433`

### Ứng dụng không chạy:
- Kiểm tra logs: `eb logs` hoặc `/opt/tomcat/logs/`
- Kiểm tra environment variables
- Kiểm tra WAR file có đúng không

### Out of Memory:
- Tăng heap size trong `.ebextensions/01-environment.config`
- Hoặc trong `setenv.sh`: `-Xmx1024m`

---

## Chi Phí Ước Tính (Free Tier)

- **RDS SQL Server Express**: Miễn phí (750 giờ/tháng, 20GB storage)
- **Elastic Beanstalk**: Miễn phí (chỉ tính phí EC2)
- **EC2 t2.micro**: Miễn phí (750 giờ/tháng)
- **SES**: 62,000 emails/tháng miễn phí

**Tổng:** ~$0/tháng nếu ở trong free tier limits

---

## Liên Kết Hữu Ích

- [AWS Elastic Beanstalk Java Guide](https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/java-se-platform.html)
- [AWS RDS SQL Server](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/CHAP_SQLServer.html)
- [AWS SES Setup](https://docs.aws.amazon.com/ses/latest/dg/send-email-smtp-java.html)

