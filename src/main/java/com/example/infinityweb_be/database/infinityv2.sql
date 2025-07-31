--------------------------------------------------------------------------------
-- 1. TẠO DATABASE VÀ CHUYỂN CONTEXT
--------------------------------------------------------------------------------
-- CREATE DATABASE InfinityWeb
-- GO
USE InfinityWeb
GO

--------------------------------------------------------------------------------
-- 2. TẠO CÁC BẢNG CHÍNH
--------------------------------------------------------------------------------
-- 2.1. Users
CREATE TABLE dbo.Users
(
    id         INT IDENTITY (1,1) PRIMARY KEY,
    username   VARCHAR(50) UNIQUE  NOT NULL,
    email      VARCHAR(100) UNIQUE NOT NULL,
    full_name  NVARCHAR(100)       NULL,
    password   VARCHAR(255)        NOT NULL,
    avatar     VARCHAR(255)        NULL,
    role       VARCHAR(20)         NOT NULL CHECK (role IN ('student', 'admin')),
    is_active  BIT                 NOT NULL DEFAULT 0, -- 0=chưa kích hoạt,1=đã kích hoạt
    created_at DATETIME            NOT NULL DEFAULT GETDATE(),
    updated_at DATETIME            NOT NULL DEFAULT GETDATE()
)
GO

-- 2.2. Refresh Tokens
CREATE TABLE dbo.Refresh_Tokens
(
    id         INT IDENTITY (1,1) PRIMARY KEY,
    user_id    INT          NOT NULL,
    token      VARCHAR(255) NOT NULL,
    expires_at DATETIME     NOT NULL,
    created_at DATETIME     NOT NULL DEFAULT GETDATE(),
    FOREIGN KEY (user_id) REFERENCES dbo.Users (id)
)
GO

-- 2.4. Languages
CREATE TABLE dbo.Languages
(
    id         INT IDENTITY (1,1) PRIMARY KEY,
    code       VARCHAR(10) UNIQUE NOT NULL,
    name       NVARCHAR(50)       NOT NULL,
    flag       VARCHAR(255)       NOT NULL DEFAULT '',
    difficulty NVARCHAR(50)       NULL,
    popularity NVARCHAR(50)       NULL
)
GO

CREATE TABLE LanguageTemplates
(
    id   INT IDENTITY (1,1) PRIMARY KEY,
    code VARCHAR(10) UNIQUE NOT NULL,
    name NVARCHAR(50)       NOT NULL
)
GO


-- 2.4. Courses
CREATE TABLE dbo.Courses
(
    id          INT IDENTITY (1,1) PRIMARY KEY,
    name        NVARCHAR(100)  NOT NULL,
    description NVARCHAR(255)  NULL,
    language_id INT            NULL,
    created_by  INT            NOT NULL,
    created_at  DATETIME       NOT NULL DEFAULT GETDATE(),
    updated_by  INT            NULL,
    updated_at  DATETIME       NULL,
    level       NVARCHAR(50)   NULL,
    duration    NVARCHAR(50)   NULL,
    status      NVARCHAR(20)   NULL,
    price       DECIMAL(18, 4) NULL,
    FOREIGN KEY (language_id) REFERENCES dbo.Languages (id),
    FOREIGN KEY (created_by) REFERENCES dbo.Users (id),
    FOREIGN KEY (updated_by) REFERENCES dbo.Users (id)
)
GO

-- 2.5. Modules
CREATE TABLE dbo.Modules
(
    id          INT IDENTITY (1,1) PRIMARY KEY,
    course_id   INT           NOT NULL,
    name        NVARCHAR(100) NOT NULL,
    description NVARCHAR(255) NULL,
    created_by  INT           NOT NULL,
    created_at  DATETIME      NOT NULL DEFAULT GETDATE(),
    updated_by  INT           NULL,
    updated_at  DATETIME      NULL,
    [order]     INT           NULL,
    duration    NVARCHAR(50)  NULL,
    status      NVARCHAR(20)  NULL,
    FOREIGN KEY (course_id) REFERENCES dbo.Courses (id),
    FOREIGN KEY (created_by) REFERENCES dbo.Users (id),
    FOREIGN KEY (updated_by) REFERENCES dbo.Users (id)
)
GO

-- 2.6. Lessons
CREATE TABLE dbo.Lessons
(
    id          INT IDENTITY (1,1) PRIMARY KEY,
    module_id   INT           NOT NULL,
    name        NVARCHAR(100) NOT NULL,
    description NVARCHAR(255) NULL,
    content     NVARCHAR(MAX) NULL,
    type        NVARCHAR(20)  NOT NULL DEFAULT 'video',
    [order]     INT           NOT NULL DEFAULT 1,
    duration    NVARCHAR(50)  NULL,
    status      NVARCHAR(20)  NOT NULL DEFAULT 'active',
    created_by  INT           NOT NULL,
    created_at  DATETIME      NOT NULL DEFAULT GETDATE(),
    updated_by  INT           NULL,
    updated_at  DATETIME      NULL,
    FOREIGN KEY (module_id) REFERENCES dbo.Modules (id),
    FOREIGN KEY (created_by) REFERENCES dbo.Users (id),
    FOREIGN KEY (updated_by) REFERENCES dbo.Users (id),
    CONSTRAINT UQ_Lessons_Module_Order UNIQUE (module_id, [order])
)
GO

-- 2.7. Enrollment
CREATE TABLE dbo.Enrollment
(
    id          INT IDENTITY (1,1) PRIMARY KEY,
    user_id     INT      NULL,
    course_id   INT      NULL,
    enrolled_at DATETIME NOT NULL DEFAULT GETDATE(),
    FOREIGN KEY (user_id) REFERENCES dbo.Users (id),
    FOREIGN KEY (course_id) REFERENCES dbo.Courses (id)
)
GO

-- 2.8. Question Types
CREATE TABLE dbo.Question_Types
(
    id          INT IDENTITY (1,1) PRIMARY KEY,
    code        VARCHAR(50) UNIQUE NOT NULL,
    description NVARCHAR(255)      NULL,
    min_options INT                NOT NULL DEFAULT 0,
    min_correct INT                NOT NULL DEFAULT 0,
    max_correct INT                NULL
)
GO

-- 2.9. Questions
CREATE TABLE dbo.Questions
(
    id               INT IDENTITY (1,1) PRIMARY KEY,
    course_id        INT           NULL,
    lesson_id        INT           NULL,
    question_text    NVARCHAR(500) NOT NULL,
    question_type_id INT           NOT NULL,
    media_url        VARCHAR(255)  NULL,
    audio_url        VARCHAR(255)  NULL,
    video_url        VARCHAR(255)  NULL,
    created_by       INT           NOT NULL,
    created_at       DATETIME      NOT NULL DEFAULT GETDATE(),
    updated_by       INT           NULL,
    updated_at       DATETIME      NULL,
    FOREIGN KEY (course_id) REFERENCES dbo.Courses (id),
    FOREIGN KEY (lesson_id) REFERENCES dbo.Lessons (id),
    FOREIGN KEY (question_type_id) REFERENCES dbo.Question_Types (id),
    FOREIGN KEY (created_by) REFERENCES dbo.Users (id),
    FOREIGN KEY (updated_by) REFERENCES dbo.Users (id)
)
GO

ALTER TABLE dbo.Questions
    ADD difficulty VARCHAR(10) NOT NULL DEFAULT 'easy',
        points INT NOT NULL DEFAULT 0
GO


-- 2.10. Question Options
CREATE TABLE dbo.Question_Options
(
    id          INT IDENTITY (1,1) PRIMARY KEY,
    question_id INT           NULL,
    option_text NVARCHAR(255) NULL,
    is_correct  BIT           NOT NULL DEFAULT 0,
    position    INT           NULL,
    image_url   VARCHAR(255)  NULL,
    FOREIGN KEY (question_id) REFERENCES dbo.Questions (id)
)
GO

-- 2.11. Question Answers
CREATE TABLE dbo.Question_Answers
(
    id                INT IDENTITY (1,1) PRIMARY KEY,
    question_id       INT           NOT NULL,
    answer_text       NVARCHAR(500) NOT NULL,
    position          INT           NULL,
    is_case_sensitive BIT           NOT NULL DEFAULT 0,
    FOREIGN KEY (question_id) REFERENCES dbo.Questions (id)
)
GO

-- 2.12. User Progress
CREATE TABLE dbo.User_Progress
(
    id                  INT IDENTITY (1,1) PRIMARY KEY,
    user_id             INT         NOT NULL,
    entity_type         VARCHAR(20) NOT NULL CHECK (entity_type IN ('course', 'module', 'lesson')),
    entity_id           INT         NOT NULL,
    progress_percentage DECIMAL(5, 2) CHECK (progress_percentage BETWEEN 0 AND 100),
    last_updated        DATETIME    NOT NULL DEFAULT GETDATE(),
    FOREIGN KEY (user_id) REFERENCES dbo.Users (id)
)
GO

-- 2.14. Quiz Results
CREATE TABLE dbo.Quiz_Results
(
    id                 INT IDENTITY (1,1) PRIMARY KEY,
    user_id            INT           NOT NULL,
    question_id        INT           NOT NULL,
    selected_option_id INT           NULL,
    answer_text        NVARCHAR(500) NULL,
    is_correct         BIT           NULL,
    answered_at        DATETIME      NOT NULL DEFAULT GETDATE(),
    FOREIGN KEY (user_id) REFERENCES dbo.Users (id),
    FOREIGN KEY (question_id) REFERENCES dbo.Questions (id),
    FOREIGN KEY (selected_option_id) REFERENCES dbo.Question_Options (id)
)
GO

-- 2.14. Translations
CREATE TABLE dbo.Translations
(
    id              INT IDENTITY (1,1) PRIMARY KEY,
    entity_type     VARCHAR(20)   NOT NULL CHECK (entity_type IN ('course', 'module', 'lesson', 'question', 'option')),
    entity_id       INT           NOT NULL,
    language_id     INT           NOT NULL,
    field_name      VARCHAR(50)   NOT NULL,
    translated_text NVARCHAR(MAX) NULL,
    FOREIGN KEY (language_id) REFERENCES dbo.Languages (id)
)
GO

-- 2.15. Admin Audit Log
CREATE TABLE dbo.Admin_Audit_Log
(
    id           INT IDENTITY (1,1) PRIMARY KEY,
    admin_id     INT           NOT NULL,
    action       NVARCHAR(255) NOT NULL,
    target_table NVARCHAR(50)  NOT NULL,
    target_id    INT           NOT NULL,
    details      NVARCHAR(MAX) NULL,
    action_time  DATETIME      NOT NULL DEFAULT GETDATE(),
    FOREIGN KEY (admin_id) REFERENCES dbo.Users (id)
)
GO

-- 2.16. User Streaks
CREATE TABLE dbo.User_Streaks
(
    user_id          INT PRIMARY KEY,
    current_streak   INT  NOT NULL DEFAULT 0,
    last_active_date DATE NULL,
    longest_streak   INT  NOT NULL DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES dbo.Users (id)
)
GO

-- 2.17. Question Layout Configs
CREATE TABLE dbo.Question_Layout_Configs
(
    id               INT IDENTITY (1,1) PRIMARY KEY,
    question_type_id INT           NULL,
    layout_code      VARCHAR(50)   NULL,
    config_json      NVARCHAR(MAX) NULL,
    FOREIGN KEY (question_type_id) REFERENCES dbo.Question_Types (id)
)
GO

-- 2.18. Verification Token
CREATE TABLE dbo.Verification_token
(
    id         BIGINT IDENTITY (1,1) PRIMARY KEY,
    token      VARCHAR(1024) NOT NULL,
    created_at DATETIME2     NOT NULL,
    expires_at DATETIME2     NOT NULL,
    confirmed  BIT           NOT NULL DEFAULT 0,
    type       VARCHAR(50)   NOT NULL,
    user_id    INT           NOT NULL,
    CONSTRAINT FK_VerificationToken_User FOREIGN KEY (user_id)
        REFERENCES dbo.Users (id) ON DELETE CASCADE
)
GO

-- 2.19 Lexicon Units
CREATE TABLE dbo.Lexicon_Units
(
    id             INT IDENTITY (1,1) PRIMARY KEY,
    text           NVARCHAR(100) NOT NULL, -- ví dụ: お
    language_id    INT           NOT NULL, -- foreign key
    ipa            NVARCHAR(100),          -- phiên âm IPA
    audio_url      VARCHAR(255),
    image_url      VARCHAR(255),
    meaning_vi     NVARCHAR(255),
    part_of_speech NVARCHAR(50),           -- noun, verb...
    created_at     DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (language_id) REFERENCES dbo.Languages (id)
)
GO
ALTER TABLE dbo.Lexicon_Units
    ADD
    [type]         NVARCHAR(50) NULL, -- loại từ (cụm, từ đơn, trợ từ,...)
    difficulty     NVARCHAR(50) NULL; -- mức độ khó
ALTER TABLE dbo.Lexicon_Units
DROP COLUMN meaning_vi;
ALTER TABLE dbo.Lexicon_Units
    ADD meaning_eng NVARCHAR(255);
--2.20 Phrases
CREATE TABLE dbo.Phrases
(
    id          INT IDENTITY (1,1) PRIMARY KEY,
    text        NVARCHAR(255) NOT NULL, -- ví dụ: お茶をください
    language_id INT           NOT NULL,
    ipa         NVARCHAR(255),
    audio_url   VARCHAR(255),
    image_url   VARCHAR(255),
    meaning_vi  NVARCHAR(255),
    created_at  DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (language_id) REFERENCES dbo.Languages (id)
)
GO
--2.21 Phrase_Lexicon_Map
CREATE TABLE dbo.Phrase_Lexicon_Map
(
    phrase_id  INT NOT NULL,
    lexicon_id INT NOT NULL,
    [order]    INT NOT NULL, -- vị trí trong cụm
    FOREIGN KEY (phrase_id) REFERENCES dbo.Phrases (id),
    FOREIGN KEY (lexicon_id) REFERENCES dbo.Lexicon_Units (id)
)
GO

-- 2.22. Orders
CREATE TABLE dbo.Orders
(
    id             INT IDENTITY (1,1) PRIMARY KEY,
    user_id        INT            NOT NULL,
    course_id      INT            NOT NULL,
    order_code     VARCHAR(50)    NOT NULL UNIQUE,
    order_date     DATETIME       NOT NULL DEFAULT GETDATE(),
    payment_method VARCHAR(20)    NOT NULL,
    status         VARCHAR(20)    NOT NULL DEFAULT 'PENDING',
    total_amount   DECIMAL(18, 2) NOT NULL DEFAULT 0,
    expiry_date    DATE           NOT NULL,
    FOREIGN KEY (user_id) REFERENCES dbo.Users (id),
    FOREIGN KEY (course_id) REFERENCES dbo.Courses (id)
)
GO

-- 2.23. Order Details
CREATE TABLE dbo.Order_Details
(
    id           INT IDENTITY (1,1) PRIMARY KEY,
    order_id     INT            NOT NULL,
    service_name NVARCHAR(255)  NOT NULL,
    service_desc NVARCHAR(500)  NULL,
    price        DECIMAL(18, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES dbo.Orders (id)
)
GO

ALTER TABLE orders
    ADD CONSTRAINT uc_orders_order_code UNIQUE (order_code)
GO

ALTER TABLE orders
    ADD CONSTRAINT FK_ORDERS_ON_COURSE FOREIGN KEY (course_id) REFERENCES courses (id)
GO

ALTER TABLE orders
    ADD CONSTRAINT FK_ORDERS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id)
GO

--------------------------------------------------------------------------------
-- 4. TRIGGERS
--------------------------------------------------------------------------------
-- Trigger: trg_UpdateLessons
CREATE OR ALTER TRIGGER dbo.trg_UpdateLessons
    ON dbo.Lessons
    AFTER INSERT, UPDATE
    AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @admin_id INT;
    SELECT TOP (1) @admin_id = COALESCE(i.updated_by, i.created_by)
    FROM inserted i;

    -- 1) Update Module
    UPDATE m
    SET m.updated_at = GETDATE(),
        m.updated_by = @admin_id
    FROM dbo.Modules m
             INNER JOIN inserted i ON i.module_id = m.id;

    -- 2) Audit Log
    INSERT INTO dbo.Admin_Audit_Log (admin_id, action, target_table, target_id, details, action_time)
    SELECT @admin_id,
           CASE WHEN EXISTS(SELECT * FROM deleted) THEN 'UPDATE' ELSE 'INSERT' END,
           'Lessons',
           i.id,
           'Name: ' + COALESCE(i.name, ''),
           GETDATE()
    FROM inserted i;

    -- 4) Update Course
    UPDATE c
    SET c.updated_at = GETDATE(),
        c.updated_by = @admin_id
    FROM dbo.Courses c
             INNER JOIN dbo.Modules m ON m.course_id = c.id
             INNER JOIN inserted i ON i.module_id = m.id;
END
GO

--------------------------------------------------------------------------------
-- 4. DỮ LIỆU MẪU (SAMPLE DATA)
--------------------------------------------------------------------------------

-- 4.1. Sample Users
INSERT INTO dbo.Users
    (username, email, full_name, role, password, is_active)
VALUES ('admin', 'adminemail@example.com', N'Tên nào cũng được', 'admin',
        '$2a$12$gSBFLoRiPCU5vtJSVKx9qej0wqfhVvgsfpZRiB/yoMR94aBtzf4Bu', 1)
GO

INSERT INTO dbo.Users
    (username, email, full_name, role, password, is_active)
VALUES ('admin1', 'admin1email1@example.com', N'Tên nào cũng được', 'admin',
        '$2a$12$7qNKP7SVXS0.fddyBLqeKuyjU6IS/zv/DFY8nLnciMo/LAzZ9HqJi', 1)
GO

INSERT INTO dbo.Users
    (username, email, full_name, role, password, is_active)
VALUES ('nguyenuser', 'user@example.com', N'Nguyễn User', 'admin',
        '$2a$10$nQMrCsEcqFuihjTwYb9IWuXWIcCbFWUyBObZxgn9rdzIwV8yDlDZS', 1)
GO

-- 4.2. Sample Question Types
INSERT INTO dbo.Question_Types
    (code, description, min_options, min_correct, max_correct)
VALUES ('multiple_choice_single', N'Câu hỏi trắc nghiệm - 1 đáp án đúng', 2, 1, 1),
       ('multiple_choice_multi', N'Câu hỏi trắc nghiệm - nhiều đáp án đúng', 2, 1, NULL),
       ('reorder_words', N'Sắp xếp từ thành câu đúng', 4, 0, 0),
       ('text_input', N'Nhập câu trả lời từ bàn phím', 0, 0, 0)
GO

-- 4.4. Languages
INSERT INTO dbo.Languages (code, name)
VALUES ('vi', N'Tiếng Việt'),
       ('en', N'English'),
       ('zh', N'Chinese'),
       ('ja', N'Japanese'),
       ('ko', N'Korean')
GO

-- 4.4. Language Templates
INSERT INTO LanguageTemplates (code, name)
VALUES ('vi', 'Vietnamese'),
       ('en', 'English'),
       ('jp', 'Japanese'),
       ('cn', 'Chinese')
GO

-- 4.5. Sample Course
INSERT INTO dbo.Courses (name, language_id, created_by)
VALUES (N'Khoá học mẫu', 1, 1)
GO

-- 4.6. Sample Module
INSERT INTO dbo.Modules (course_id, name, created_by)
VALUES (1, N'Module 1', 1)
GO

-- 4.7. Sample Lesson
INSERT INTO dbo.Lessons (module_id, name, created_by)
VALUES (1, N'Bài học 1', 1)
GO

-- 4.8. Sample Question
INSERT INTO dbo.Questions (course_id,
                           lesson_id,
                           question_text,
                           question_type_id,
                           media_url,
                           audio_url,
                           video_url,
                           created_by,
                           difficulty,
                           points)
VALUES (1,
        1,
        N'Câu hỏi mẫu: Thủ đô của Việt Nam là gì?',
        1,
        NULL, NULL, NULL,
        1,
        'easy',
        10)
GO

-- 4.9. Sample Options
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (1, N'Hà Nội', 1, 1),
       (1, N'Hồ Chí Minh', 0, 2),
       (1, N'Đà Nẵng', 0, 4)
GO

-- 4.10. Sample Answers
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (1, N'Hà Nội'),
       (1, N'Ha Noi')
GO

-- 4.11. Sample Order
INSERT INTO dbo.Orders (user_id, course_id, order_code, payment_method, status, expiry_date, total_amount)
VALUES (1, 1, 'ORD202507230001', 'VNPAY', 'PAID', DATEADD(YEAR, 1, GETDATE()), 1000000)
GO

INSERT INTO dbo.Order_Details (order_id, service_name, service_desc, price)
VALUES (1, N'Unlock full khóa học 1 năm', N'Truy cập khoá học trong 1 năm', 1000000)
GO

--------------------------------------------------------------------------------
-- 5. CHỈ MỤC TỐI ƯU HIỆU SUẤT
--------------------------------------------------------------------------------
CREATE INDEX IX_Users_Email ON dbo.Users (email)
GO
CREATE INDEX IX_Enrollment_UserCourse ON dbo.Enrollment (user_id, course_id)
GO
CREATE INDEX IX_UserProgress ON dbo.User_Progress (user_id, entity_type, entity_id)
GO
CREATE INDEX IX_Translations ON dbo.Translations (entity_type, entity_id, language_id)
GO
CREATE INDEX IX_QuestionAnswers ON dbo.Question_Answers (question_id)
GO
CREATE INDEX IX_QuizResults ON dbo.Quiz_Results (user_id, question_id)
GO
CREATE INDEX IX_Lessons_ModuleId ON dbo.Lessons (module_id)
GO
CREATE INDEX IX_Lessons_Status ON dbo.Lessons (status)
GO
