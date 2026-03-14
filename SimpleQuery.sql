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

SELECT th.* FROM ThesisHistory th INNER JOIN Theses t on t.thesisId = th.thesisId INNER JOIN Topics topics on topics.topicId = t.topicId WHERE th.mssv = 'SV001' AND th.thesisId = '1'  ORDER BY createdAt DESC
select Count(thesisId) as numberOfReport from Theses where mscvHD = ;
select * from Appointment;
select s.fullName,th.reportFile,th.sourceCodeLink,th.similarityScore,th.plagiarismStatus,th.bestSource from Theses th join Students s on s.mssv = th.mssv where th.mscvHD='GV001';
select * from ResetTokens;

select a.appointmentId,a.mssv,a.mscv,tp.title,a.purpose,a.meetingDate,a.location,a.createdAt from Appointment a inner join Theses t on t.thesisId = a.thesisId inner join Topics tp on tp.topicId = t.topicId where mscv = 'GV001' and a.status ='PENDING'
select u.*,l.mscv,l.academicTitle,l.researchField,s.mssv,s.className,s.major,s.gpa,s.skills,s.phone from Users u left join Students s on u.id=s.userId left join Lecturers l on l.userId=u.id 