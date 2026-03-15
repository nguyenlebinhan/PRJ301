select t.topicId,t.topicCode,t.description,t.technicalRequirements,t.difficultyScore from TopicRegistrations tr INNER JOIN Topics t on tr.topicId = t.topicId WHERE tr.mssv = 'SV001' AND tr.status = 'ACCEPTED';
select * from TopicRegistrations where status = 'ACCEPTED' AND mssv='SV001';
select* from Topics;
select * from Students;
Select * from Users;
select * from Lecturers;
SELECT DISTINCT tr.mssv, s.fullName, s.gpa, s.major, s.className,s.email FROM TopicRegistrations tr INNER JOIN Students s ON s.mssv = tr.mssv WHERE tr.mscvHD = 'GV001' AND tr.status = 'ACCEPTED';
Select l.fullName from TopicRegistrations tr inner join Lecturers l on l.mscv=tr.mscvHD where tr.mssv = 'SV001' and tr.status='ACCEPTED';
Select * from Progress;
select * from Theses
select * from Users
SELECT u.* FROM Users u ORDER BY u.createdAt DESC;
SELECT t.*, l.fullName as lecturerName, l.researchField  FROM Topics t LEFT JOIN Lecturers l ON t.createdBy = l.mscv ORDER BY t.createdAt DESC
SELECT t.* from Topics t WHERE t.title LIKE '%B%' 
Select l.* from TopicRegistrations tr inner join Lecturers l on l.mscv=tr.mscvHD where tr.mssv = 'SV001' and tr.status='ACCEPTED'

DELETE FROM Topics 
                     WHERE topicId = 3 AND createdBy = 'GV001'

select t.topicId,t.title,t.topicCode,tr.mssv,t.description,t.technicalRequirements,t.difficultyScore,th.thesisId,th.reportFile,th.sourceCodeLink from TopicRegistrations tr INNER JOIN Topics t on tr.topicId = t.topicId left join Theses th on th.topicId=t.topicId WHERE tr.mssv = 'SV001' AND tr.status = 'ACCEPTED'
select * from ThesisHistory ;

CREATE TRIGGER trg_AfterInsertThesis
ON Theses
AFTER INSERT,UPDATE
AS
BEGIN
    SET NOCOUNT ON;

    INSERT INTO ThesisHistory (
        thesisId, 
        mssv, 
        reportFile, 
        sourceCodeLink, 
        createdAt,
        similarityScore,
        plagiarismStatus,
        bestSource,
        plagiarism_analysis,
        relevantTopicScore,
        relevantTopicStatus,
        relevance_analysis

    )
    SELECT 
        i.thesisId, 
        i.mssv, 
        i.reportFile, 
        i.sourceCodeLink, 
        GETDATE(),
        i.similarityScore,
        i.plagiarismStatus,
        i.bestSource,
        i.plagiarism_analysis,
        i.relevantTopicScore,
        i.relevantTopicStatus,
        i.relevance_analysis
    FROM 
        inserted i;
END;
GO

CREATE TRIGGER trg_CleanupResetTokens
ON ResetTokens 
AFTER INSERT 
AS
BEGIN
    SET NOCOUNT ON;

   
    DELETE FROM ResetTokens 
    WHERE expiresAt < GETDATE() 
       OR used = 1;
END;
GO

CREATE PROCEDURE sp_DeleteUser (@UserId INT)
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRANSACTION
    BEGIN TRY
        
        DECLARE @Mssv VARCHAR(20) = (SELECT mssv FROM Students WHERE userId = @UserId);
        DECLARE @Mscv VARCHAR(20) = (SELECT mscv FROM Lecturers WHERE userId = @UserId);

       
        IF @Mssv IS NOT NULL
        BEGIN
            DELETE FROM ThesisHistory WHERE mssv = @Mssv;
            DELETE FROM TopicRegistrations WHERE mssv = @Mssv;
            DELETE FROM Theses WHERE mssv = @Mssv;
            DELETE FROM Students WHERE userId = @UserId;
        END

        
        IF @Mscv IS NOT NULL
        BEGIN
           
            DELETE FROM ThesisHistory WHERE thesisId IN (SELECT thesisId FROM Theses WHERE mscvHD = @Mscv);
            
           
            DELETE FROM TopicRegistrations WHERE topicId IN (SELECT topicId FROM Topics WHERE createdBy = @Mscv);
            
           
            DELETE FROM Theses WHERE mscvHD = @Mscv;
            
            
            DELETE FROM Topics WHERE createdBy = @Mscv;
            
            DELETE FROM Lecturers WHERE userId = @UserId;
        END

        
        DELETE FROM ResetTokens WHERE userId = @UserId;

        
        DELETE FROM Users WHERE id = @UserId;

        COMMIT TRANSACTION
        PRINT 'Đã xóa sạch toàn bộ dữ liệu liên quan đến User ID: ' + CAST(@UserId AS VARCHAR);
    END TRY
    BEGIN CATCH
        ROLLBACK TRANSACTION;
        DECLARE @Err NVARCHAR(MAX) = ERROR_MESSAGE();
        RAISERROR(@Err, 16, 1);
    END CATCH
END

CREATE PROCEDURE sp_DeleteTopic (@TopicId INT, @ExecutedByUserId INT)
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRANSACTION
    BEGIN TRY
        
        DECLARE @UserRole NVARCHAR(100) = (SELECT role FROM Users WHERE id = @ExecutedByUserId);
        DECLARE @LecturerMscv VARCHAR(20) = (SELECT mscv FROM Lecturers WHERE userId = @ExecutedByUserId);
        DECLARE @OwnerMscv VARCHAR(20) = (SELECT createdBy FROM Topics WHERE topicId = @TopicId);

        
        IF @UserRole <> 'ADMIN' AND (@LecturerMscv IS NULL OR @LecturerMscv <> @OwnerMscv)
        BEGIN
            RAISERROR('Bạn không có quyền xóa đề tài này!', 16, 1);
            RETURN;
        END

       
        DELETE FROM ThesisHistory WHERE thesisId IN (SELECT thesisId FROM Theses WHERE topicId = @TopicId);

        
        DELETE FROM Theses WHERE topicId = @TopicId;

        
        DELETE FROM TopicRegistrations WHERE topicId = @TopicId;

        
        DELETE FROM Topics WHERE topicId = @TopicId;

        COMMIT TRANSACTION
        PRINT 'Đã xóa sạch đề tài và các dữ liệu liên quan.';
    END TRY
    BEGIN CATCH
        ROLLBACK TRANSACTION;
        DECLARE @ErrMsg NVARCHAR(MAX) = ERROR_MESSAGE();
        RAISERROR(@ErrMsg, 16, 1);
    END CATCH
END

SELECT th.* FROM ThesisHistory th INNER JOIN Theses t on t.thesisId = th.thesisId INNER JOIN Topics topics on topics.topicId = t.topicId WHERE th.mssv = 'SV001' AND th.thesisId = '1'  ORDER BY createdAt DESC
select Count(thesisId) as numberOfReport from Theses where mscvHD = ;
select * from Appointment;
select s.fullName,th.reportFile,th.sourceCodeLink,th.similarityScore,th.plagiarismStatus,th.bestSource from Theses th join Students s on s.mssv = th.mssv where th.mscvHD='GV001';
select * from ResetTokens;
select s.fullName,t.mssv,s.className,th.title,l.fullName,t.reportFile,t.sourceCodeLink,t.similarityScore,t.plagiarismStatus,t.bestSource,t.relevantTopicScore,t.relevantTopicStatus from Theses t inner join Students s on s.mssv=t.mssv inner join Lecturers l on l.mscv=t.mscvHD inner join Topics th on t.topicId = th.topicId 
select a.appointmentId,a.mssv,a.mscv,tp.title,a.purpose,a.meetingDate,a.location,a.createdAt from Appointment a inner join Theses t on t.thesisId = a.thesisId inner join Topics tp on tp.topicId = t.topicId where mscv = 'GV001' and a.status ='PENDING'
select u.*,l.mscv,l.academicTitle,l.researchField,s.mssv,s.className,s.major,s.gpa,s.skills,s.phone from Users u left join Students s on u.id=s.userId left join Lecturers l on l.userId=u.id 