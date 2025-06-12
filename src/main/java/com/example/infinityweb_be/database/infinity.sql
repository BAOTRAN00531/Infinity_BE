CREATE DATABASE InfinityWeb;
GO
USE InfinityWeb;
GO

-- 1. Bảng Users
CREATE TABLE Users (
    id INT PRIMARY KEY IDENTITY(1,1),
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    full_name NVARCHAR(100),
    role VARCHAR(20) CHECK (role IN ('student', 'admin')) NOT NULL,
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
);

-- 2. Bảng Refresh_Tokens
CREATE TABLE Refresh_Tokens (
    id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT NOT NULL,
    token VARCHAR(255) NOT NULL,
    expires_at DATETIME NOT NULL,
    created_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (user_id) REFERENCES Users(id)
);

-- 3. Bảng Languages
CREATE TABLE Languages (
    id INT PRIMARY KEY IDENTITY(1,1),
    code VARCHAR(10) UNIQUE NOT NULL,
    name NVARCHAR(50) NOT NULL
);

-- 4. Bảng Courses
CREATE TABLE Courses (
    id INT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(100) NOT NULL,
    description NVARCHAR(255),
    language_id INT,
    created_by INT NOT NULL,
    created_at DATETIME DEFAULT GETDATE(),
    updated_by INT,
    updated_at DATETIME,
    FOREIGN KEY (language_id) REFERENCES Languages(id),
    FOREIGN KEY (created_by) REFERENCES Users(id),
    FOREIGN KEY (updated_by) REFERENCES Users(id)
);

-- 5. Bảng Modules
CREATE TABLE Modules (
    id INT PRIMARY KEY IDENTITY(1,1),
    course_id INT NOT NULL,
    name NVARCHAR(100) NOT NULL,
    description NVARCHAR(255),
    created_by INT NOT NULL,
    created_at DATETIME DEFAULT GETDATE(),
    updated_by INT,
    updated_at DATETIME,
    FOREIGN KEY (course_id) REFERENCES Courses(id),
    FOREIGN KEY (created_by) REFERENCES Users(id),
    FOREIGN KEY (updated_by) REFERENCES Users(id)
);

-- 6. Bảng Lessons
CREATE TABLE Lessons (
    id INT PRIMARY KEY IDENTITY(1,1),
    module_id INT NOT NULL,
    name NVARCHAR(100) NOT NULL,
    description NVARCHAR(255),
    content NVARCHAR(MAX),
    created_by INT NOT NULL,
    created_at DATETIME DEFAULT GETDATE(),
    updated_by INT,
    updated_at DATETIME,
    FOREIGN KEY (module_id) REFERENCES Modules(id),
    FOREIGN KEY (created_by) REFERENCES Users(id),
    FOREIGN KEY (updated_by) REFERENCES Users(id)
);

-- 7. Bảng Enrollment
CREATE TABLE Enrollment (
    id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT,
    course_id INT,
    enrolled_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (user_id) REFERENCES Users(id),
    FOREIGN KEY (course_id) REFERENCES Courses(id)
);

-- 8. Bảng Question_Types
CREATE TABLE Question_Types (
    id INT PRIMARY KEY IDENTITY(1,1),
    code VARCHAR(50) UNIQUE NOT NULL,
    description NVARCHAR(255),
    min_options INT DEFAULT 0,
    min_correct INT DEFAULT 0,
    max_correct INT NULL
);

-- Dữ liệu mẫu cho Question_Types
INSERT INTO Question_Types (code, description, min_options, min_correct, max_correct) VALUES
('multiple_choice_single', N'Câu hỏi trắc nghiệm - 1 đáp án đúng', 2, 1, 1),
('multiple_choice_multi', N'Câu hỏi trắc nghiệm - nhiều đáp án đúng', 2, 1, NULL),
('reorder_words', N'Sắp xếp từ thành câu đúng', 3, 0, 0),
('text_input', N'Nhập câu trả lời từ bàn phím', 0, 0, 0);

-- 9. Bảng Questions
CREATE TABLE Questions (
    id INT PRIMARY KEY IDENTITY(1,1),
    course_id INT,
    lesson_id INT,
    question_text NVARCHAR(500) NOT NULL,
    question_type_id INT NOT NULL,
    created_by INT NOT NULL,
    created_at DATETIME DEFAULT GETDATE(),
    updated_by INT,
    updated_at DATETIME,
    FOREIGN KEY (course_id) REFERENCES Courses(id),
    FOREIGN KEY (lesson_id) REFERENCES Lessons(id),
    FOREIGN KEY (question_type_id) REFERENCES Question_Types(id),
    FOREIGN KEY (created_by) REFERENCES Users(id),
    FOREIGN KEY (updated_by) REFERENCES Users(id)
);

-- 10. Bảng Question_Options
CREATE TABLE Question_Options (
    id INT PRIMARY KEY IDENTITY(1,1),
    question_id INT,
    option_text NVARCHAR(255),
    is_correct BIT DEFAULT 0,
    position INT NULL,
    FOREIGN KEY (question_id) REFERENCES Questions(id)
);

-- 11. Bảng Question_Answers
CREATE TABLE Question_Answers (
    id INT PRIMARY KEY IDENTITY(1,1),
    question_id INT NOT NULL,
    answer_text NVARCHAR(500) NOT NULL,
    position INT NULL,
    FOREIGN KEY (question_id) REFERENCES Questions(id)
);

-- 12. Bảng User_Progress
CREATE TABLE User_Progress (
    id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT NOT NULL,
    entity_type VARCHAR(20) CHECK (entity_type IN ('course', 'module', 'lesson')) NOT NULL,
    entity_id INT NOT NULL,
    progress_percentage DECIMAL(5,2) CHECK (progress_percentage BETWEEN 0 AND 100),
    last_updated DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (user_id) REFERENCES Users(id)
);

-- 13. Bảng Quiz_Results
CREATE TABLE Quiz_Results (
    id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT NOT NULL,
    question_id INT NOT NULL,
    selected_option_id INT,
    answer_text NVARCHAR(500),
    is_correct BIT,
    answered_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (user_id) REFERENCES Users(id),
    FOREIGN KEY (question_id) REFERENCES Questions(id),
    FOREIGN KEY (selected_option_id) REFERENCES Question_Options(id)
);

-- 14. Bảng Translations
CREATE TABLE Translations (
    id INT PRIMARY KEY IDENTITY(1,1),
    entity_type VARCHAR(20) CHECK (entity_type IN ('course', 'module', 'lesson', 'question', 'option')) NOT NULL,
    entity_id INT NOT NULL,
    language_id INT NOT NULL,
    field_name VARCHAR(50) NOT NULL,
    translated_text NVARCHAR(MAX),
    FOREIGN KEY (language_id) REFERENCES Languages(id)
);

-- 15. Bảng Admin_Audit_Log
CREATE TABLE Admin_Audit_Log (
    id INT PRIMARY KEY IDENTITY(1,1),
    admin_id INT NOT NULL,
    action NVARCHAR(255) NOT NULL,
    target_table NVARCHAR(50) NOT NULL,
    target_id INT NOT NULL,
    details NVARCHAR(MAX),
    action_time DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (admin_id) REFERENCES Users(id)
);

-- 16. Trigger kiểm tra Question_Options
CREATE TRIGGER trg_Validate_QuestionOptions
ON Question_Options
AFTER INSERT, UPDATE
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @question_id INT, @question_type_id INT, @min_options INT, @min_correct INT, @max_correct INT;

    -- Tạo bảng tạm để lưu cấu hình của các câu hỏi
    SELECT 
        q.id AS question_id,
        qt.id AS question_type_id,
        qt.min_options,
        qt.min_correct,
        qt.max_correct
    INTO #tmp_config
    FROM Questions q
    JOIN Question_Types qt ON q.question_type_id = qt.id
    WHERE q.id IN (SELECT DISTINCT question_id FROM inserted);

    DECLARE question_cursor CURSOR FOR 
    SELECT question_id, question_type_id, min_options, min_correct, max_correct 
    FROM #tmp_config;

    OPEN question_cursor;
    FETCH NEXT FROM question_cursor INTO @question_id, @question_type_id, @min_options, @min_correct, @max_correct;

    WHILE @@FETCH_STATUS = 0
    BEGIN
        DECLARE @option_count INT = (SELECT COUNT(*) FROM Question_Options WHERE question_id = @question_id);
        DECLARE @correct_count INT = (SELECT COUNT(*) FROM Question_Options WHERE question_id = @question_id AND is_correct = 1);

        IF (@min_options > 0 AND @option_count < @min_options)
        BEGIN
            RAISERROR(N'Câu hỏi %d phải có ít nhất %d lựa chọn.', 16, 1, @question_id, @min_options);
            ROLLBACK;
            RETURN;
        END

        IF (@min_correct > 0 AND @correct_count < @min_correct)
        BEGIN
            RAISERROR(N'Câu hỏi %d cần ít nhất %d đáp án đúng.', 16, 1, @question_id, @min_correct);
            ROLLBACK;
            RETURN;
        END

        IF (@max_correct IS NOT NULL AND @correct_count > @max_correct)
        BEGIN
            RAISERROR(N'Câu hỏi %d chỉ được phép có tối đa %d đáp án đúng.', 16, 1, @question_id, @max_correct);
            ROLLBACK;
            RETURN;
        END

        FETCH NEXT FROM question_cursor INTO @question_id, @question_type_id, @min_options, @min_correct, @max_correct;
    END

    CLOSE question_cursor;
    DEALLOCATE question_cursor;

    DROP TABLE #tmp_config;
END;
GO

-- 17. Trigger cập nhật chuỗi phân cấp
CREATE TRIGGER trg_UpdateQuestionOptions
ON Question_Options
AFTER UPDATE, INSERT
AS
BEGIN
    SET NOCOUNT ON;
    DECLARE @current_user_id INT = CAST(SESSION_CONTEXT(N'user_id') AS INT); -- Giả sử user_id được lưu trong SESSION_CONTEXT

    -- Cập nhật Questions
    UPDATE q
    SET updated_at = GETDATE(),
        updated_by = @current_user_id
    FROM Questions q
    INNER JOIN inserted i ON q.id = i.question_id;

    -- Ghi log hành động
    INSERT INTO Admin_Audit_Log (admin_id, action, target_table, target_id, details)
    SELECT @current_user_id, 
           CASE WHEN EXISTS (SELECT * FROM deleted) THEN 'UPDATE' ELSE 'INSERT' END,
           'Question_Options',
           id,
           'Updated option_text: ' + COALESCE(option_text, '')
    FROM inserted;

    -- Cập nhật Lessons
    UPDATE l
    SET updated_at = GETDATE(),
        updated_by = @current_user_id
    FROM Lessons l
    INNER JOIN Questions q ON q.lesson_id = l.id
    INNER JOIN inserted i ON i.question_id = q.id;

    -- Cập nhật Modules
    UPDATE m
    SET updated_at = GETDATE(),
        updated_by = @current_user_id
    FROM Modules m
    INNER JOIN Lessons l ON l.module_id = m.id
    INNER JOIN Questions q ON q.lesson_id = l.id
    INNER JOIN inserted i ON i.question_id = q.id;

    -- Cập nhật Courses
    UPDATE c
    SET updated_at = GETDATE(),
        updated_by = @current_user_id
    FROM Courses c
    INNER JOIN Modules m ON m.course_id = c.id
    INNER JOIN Lessons l ON l.module_id = m.id
    INNER JOIN Questions q ON q.lesson_id = l.id
    INNER JOIN inserted i ON i.question_id = q.id;
END;
GO

CREATE TRIGGER trg_UpdateQuestions
ON Questions
AFTER UPDATE, INSERT
AS
BEGIN
    SET NOCOUNT ON;
    DECLARE @current_user_id INT = CAST(SESSION_CONTEXT(N'user_id') AS INT);

    -- Cập nhật Lessons
    UPDATE l
    SET updated_at = GETDATE(),
        updated_by = @current_user_id
    FROM Lessons l
    INNER JOIN inserted i ON i.lesson_id = l.id;

    -- Ghi log hành động
    INSERT INTO Admin_Audit_Log (admin_id, action, target_table, target_id, details)
    SELECT @current_user_id, 
           CASE WHEN EXISTS (SELECT * FROM deleted) THEN 'UPDATE' ELSE 'INSERT' END,
           'Questions',
           id,
           'Updated question_text: ' + COALESCE(question_text, '')
    FROM inserted;

    -- Cập nhật Modules
    UPDATE m
    SET updated_at = GETDATE(),
        updated_by = @current_user_id
    FROM Modules m
    INNER JOIN Lessons l ON l.module_id = m.id
    INNER JOIN inserted i ON i.lesson_id = l.id;

    -- Cập nhật Courses
    UPDATE c
    SET updated_at = GETDATE(),
        updated_by = @current_user_id
    FROM Courses c
    INNER JOIN Modules m ON m.course_id = c.id
    INNER JOIN Lessons l ON l.module_id = m.id
    INNER JOIN inserted i ON i.lesson_id = l.id;
END;
GO

CREATE TRIGGER trg_UpdateLessons
ON Lessons
AFTER UPDATE, INSERT
AS
BEGIN
    SET NOCOUNT ON;
    DECLARE @current_user_id INT = CAST(SESSION_CONTEXT(N'user_id') AS INT);

    -- Cập nhật Modules
    UPDATE m
    SET updated_at = GETDATE(),
        updated_by = @current_user_id
    FROM Modules m
    INNER JOIN inserted i ON i.module_id = m.id;

    -- Ghi log hành động
    INSERT INTO Admin_Audit_Log (admin_id, action, target_table, target_id, details)
    SELECT @current_user_id, 
           CASE WHEN EXISTS (SELECT * FROM deleted) THEN 'UPDATE' ELSE 'INSERT' END,
           'Lessons',
           id,
           'Updated name: ' + COALESCE(name, '')
    FROM inserted;

    -- Cập nhật Courses
    UPDATE c
    SET updated_at = GETDATE(),
        updated_by = @current_user_id
    FROM Courses c
    INNER JOIN Modules m ON m.course_id = c.id
    INNER JOIN inserted i ON i.module_id = m.id;
END;
GO

CREATE TRIGGER trg_UpdateModules
ON Modules
AFTER UPDATE, INSERT
AS
BEGIN
    SET NOCOUNT ON;
    DECLARE @current_user_id INT = CAST(SESSION_CONTEXT(N'user_id') AS INT);

    -- Cập nhật Courses
    UPDATE c
    SET updated_at = GETDATE(),
        updated_by = @current_user_id
    FROM Courses c
    INNER JOIN inserted i ON i.course_id = c.id;

    -- Ghi log hành động
    INSERT INTO Admin_Audit_Log (admin_id, action, target_table, target_id, details)
    SELECT @current_user_id, 
           CASE WHEN EXISTS (SELECT * FROM deleted) THEN 'UPDATE' ELSE 'INSERT' END,
           'Modules',
           id,
           'Updated name: ' + COALESCE(name, '')
    FROM inserted;
END;
GO

CREATE TRIGGER trg_UpdateCourses
ON Courses
AFTER UPDATE, INSERT
AS
BEGIN
    SET NOCOUNT ON;
    DECLARE @current_user_id INT = CAST(SESSION_CONTEXT(N'user_id') AS INT);

    -- Ghi log hành động
    INSERT INTO Admin_Audit_Log (admin_id, action, target_table, target_id, details)
    SELECT @current_user_id, 
           CASE WHEN EXISTS (SELECT * FROM deleted) THEN 'UPDATE' ELSE 'INSERT' END,
           'Courses',
           id,
           'Updated name: ' + COALESCE(name, '')
    FROM inserted;
END;
GO

-- 18. Chỉ mục để tối ưu hiệu suất
CREATE INDEX idx_users_email ON Users(email);
CREATE INDEX idx_enrollment_user_course ON Enrollment(user_id, course_id);
CREATE INDEX idx_user_progress ON User_Progress(user_id, entity_type, entity_id);
CREATE INDEX idx_translations ON Translations(entity_type, entity_id, language_id);
CREATE INDEX idx_question_answers ON Question_Answers(question_id);
CREATE INDEX idx_quiz_results ON Quiz_Results(user_id, question_id);
GO
ALTER TABLE Users
ADD avatar VARCHAR(255);

-- Cập nhật bảng Questions: Thêm cột media_url
ALTER TABLE Questions
ADD media_url VARCHAR(255);

-- Cập nhật bảng Question_Options: Thêm cột image_url
ALTER TABLE Question_Options
ADD image_url VARCHAR(255);
SELECT 'sqlserver' dbms,t.TABLE_CATALOG,t.TABLE_SCHEMA,t.TABLE_NAME,c.COLUMN_NAME,c.ORDINAL_POSITION,c.DATA_TYPE,c.CHARACTER_MAXIMUM_LENGTH,n.CONSTRAINT_TYPE,k2.TABLE_SCHEMA,k2.TABLE_NAME,k2.COLUMN_NAME FROM INFORMATION_SCHEMA.TABLES t LEFT JOIN INFORMATION_SCHEMA.COLUMNS c ON t.TABLE_CATALOG=c.TABLE_CATALOG AND t.TABLE_SCHEMA=c.TABLE_SCHEMA AND t.TABLE_NAME=c.TABLE_NAME LEFT JOIN(INFORMATION_SCHEMA.KEY_COLUMN_USAGE k JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS n ON k.CONSTRAINT_CATALOG=n.CONSTRAINT_CATALOG AND k.CONSTRAINT_SCHEMA=n.CONSTRAINT_SCHEMA AND k.CONSTRAINT_NAME=n.CONSTRAINT_NAME LEFT JOIN INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS r ON k.CONSTRAINT_CATALOG=r.CONSTRAINT_CATALOG AND k.CONSTRAINT_SCHEMA=r.CONSTRAINT_SCHEMA AND k.CONSTRAINT_NAME=r.CONSTRAINT_NAME)ON c.TABLE_CATALOG=k.TABLE_CATALOG AND c.TABLE_SCHEMA=k.TABLE_SCHEMA AND c.TABLE_NAME=k.TABLE_NAME AND c.COLUMN_NAME=k.COLUMN_NAME LEFT JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE k2 ON k.ORDINAL_POSITION=k2.ORDINAL_POSITION AND r.UNIQUE_CONSTRAINT_CATALOG=k2.CONSTRAINT_CATALOG AND r.UNIQUE_CONSTRAINT_SCHEMA=k2.CONSTRAINT_SCHEMA AND r.UNIQUE_CONSTRAINT_NAME=k2.CONSTRAINT_NAME WHERE t.TABLE_TYPE='BASE TABLE';
ALTER TABLE Questions ADD audio_url VARCHAR(255), video_url VARCHAR(255);
CREATE TABLE User_Streaks (
    user_id INT PRIMARY KEY,
    current_streak INT DEFAULT 0,
    last_active_date DATE,
    longest_streak INT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES Users(id)
);
CREATE TABLE Question_Layout_Configs (
    id INT PRIMARY KEY IDENTITY(1,1),
    question_type_id INT,
    layout_code VARCHAR(50),
    config_json NVARCHAR(MAX),
    FOREIGN KEY (question_type_id) REFERENCES Question_Types(id)
);
ALTER TABLE Users
ADD is_active BIT DEFAULT 0;  -- 0 = chưa kích hoạt, 1 = đã kích hoạt
USE [InfinityWeb];

ALTER TABLE Users
ADD password VARCHAR(255);  -- 0 = chưa kích hoạt, 1 = đã kích hoạt
USE [InfinityWeb];
GO
GO

-- ✅ DROP bảng Refresh_Tokens vì đã gộp vào verification_token
IF OBJECT_ID('dbo.Refresh_Tokens', 'U') IS NOT NULL DROP TABLE dbo.Refresh_Tokens;
GO

-- ✅ Cập nhật bảng verification_token: tăng token lên 512 ký tự
IF COL_LENGTH('verification_token', 'token') < 512
BEGIN
    ALTER TABLE verification_token ALTER COLUMN token VARCHAR(512);
END
GO

-- ✅ Giữ nguyên các bảng, trigger, constraint cũ (không tạo lại ở đây)

-- ⚠️ Nếu verification_token chưa tồn tại thì tạo (đề phòng)
IF OBJECT_ID('dbo.verification_token', 'U') IS NULL
BEGIN
    CREATE TABLE [dbo].[verification_token](
        [id] [bigint] IDENTITY(1,1) NOT NULL,
        [confirmed] [bit] NOT NULL,
        [created_at] [datetime2](6) NULL,
        [expires_at] [datetime2](6) NULL,
        [token] [varchar](512) NULL,
        [user_id] [int] NULL,
        [type] [varchar](255) NULL,
    PRIMARY KEY CLUSTERED ([id] ASC)
    ) ON [PRIMARY];
END
GO

-- ✅ Đảm bảo foreign key tồn tại (nếu chưa có)
IF NOT EXISTS (
    SELECT * FROM sys.foreign_keys WHERE parent_object_id = OBJECT_ID('verification_token') AND name = 'FK_verification_token_user'
)
BEGIN
    ALTER TABLE [dbo].[verification_token]  WITH CHECK 
    ADD CONSTRAINT [FK_verification_token_user] FOREIGN KEY([user_id])
    REFERENCES [dbo].[Users] ([id]);
END
GO

-- ✅ Tạo chỉ mục nếu cần
IF NOT EXISTS (
    SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID('verification_token') AND name = 'idx_verification_token_user_type'
)
BEGIN
    CREATE INDEX idx_verification_token_user_type ON verification_token(user_id, type);
END
GO
ALTER TABLE verification_token
ALTER COLUMN token VARCHAR(MAX);   -- hoặc VARCHAR(MAX)
-- Chỉ còn index hỗ trợ truy vấn, không phải UNIQUE
IF EXISTS (
    SELECT * FROM sys.indexes 
    WHERE object_id = OBJECT_ID('verification_token') 
      AND is_unique = 1 AND name = 'UQ_verification_token_user_id'
)
    DROP INDEX UQ_verification_token_user_id ON verification_token;
