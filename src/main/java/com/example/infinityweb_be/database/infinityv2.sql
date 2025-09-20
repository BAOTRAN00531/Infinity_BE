--------------------------------------------------------------------------------
-- 1. TẠO DATABASE VÀ CHUYỂN CONTEXT
--------------------------------------------------------------------------------
CREATE DATABASE InfinityWeb
GO
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

ALTER TABLE dbo.Users
    ADD is_vip BIT NOT NULL DEFAULT 0,
        vip_expiry_date DATETIME NULL;


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
    thumbnail   VARCHAR(255)   NULL,
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

ALTER TABLE dbo.Lessons
    ADD is_completed BIT NOT NULL DEFAULT 0;
GO

ALTER TABLE dbo.Lessons
    ADD video_url NVARCHAR(255) NULL;
GO

ALTER TABLE dbo.Lessons
    ADD icon NVARCHAR(255) NULL;
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
-- 2) Ràng buộc nghĩa (nếu chưa có)
ALTER TABLE Questions
    ADD sense_id BIGINT;

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
ALTER TABLE dbo.Verification_token
    ADD confirmed_at DATETIME2 NULL;
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
    ADD [type] NVARCHAR(50) NULL, -- loại từ (cụm, từ đơn, trợ từ,...)
        difficulty NVARCHAR(50) NULL; -- mức độ khó
GO
ALTER TABLE dbo.Lexicon_Units
    DROP COLUMN meaning_vi;
GO

ALTER TABLE dbo.Lexicon_Units
    ADD meaning_eng NVARCHAR(255);
GO
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

-- Bảng Orders (không có course_id)
CREATE TABLE dbo.Orders
(
    id             INT IDENTITY (1,1) PRIMARY KEY,
    user_id        INT            NOT NULL,
    order_code     VARCHAR(50)    NOT NULL UNIQUE,
    order_date     DATETIME       NOT NULL DEFAULT GETDATE(),
    payment_method VARCHAR(20)    NOT NULL,
    status         VARCHAR(20)    NOT NULL DEFAULT 'PENDING',
    total_amount   DECIMAL(18, 2) NOT NULL DEFAULT 0,
    expiry_date    DATE           NOT NULL
);
GO

-- Bảng Order_Details (có course_id)
CREATE TABLE dbo.Order_Details
(
    id           INT IDENTITY (1,1) PRIMARY KEY,
    order_id     INT            NOT NULL,
    course_id    INT            NOT NULL,
    service_name NVARCHAR(255)  NOT NULL,
    service_desc NVARCHAR(500)  NULL,
    price        DECIMAL(18, 2) NOT NULL
);

ALTER TABLE dbo.Order_Details
    ALTER COLUMN course_id INT NULL;
GO

CREATE TABLE user_question_progress
(
    id           bigint IDENTITY (1, 1) NOT NULL,
    user_id      int                    NOT NULL,
    lesson_id    int                    NOT NULL,
    question_id  int                    NOT NULL,
    is_completed bit                    NOT NULL,
    completed_at datetime,
    CONSTRAINT pk_user_question_progress PRIMARY KEY (id)
)
GO

ALTER TABLE user_question_progress
    ADD CONSTRAINT FK_USER_QUESTION_PROGRESS_ON_LESSON FOREIGN KEY (lesson_id) REFERENCES lessons (id)
GO

ALTER TABLE user_question_progress
    ADD CONSTRAINT FK_USER_QUESTION_PROGRESS_ON_QUESTION FOREIGN KEY (question_id) REFERENCES questions (id)
GO

ALTER TABLE user_question_progress
    ADD CONSTRAINT FK_USER_QUESTION_PROGRESS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id)
GO

-- Bảng Practice_schedule
IF OBJECT_ID(N'dbo.practice_schedule', N'U') IS NULL
    BEGIN
        CREATE TABLE dbo.practice_schedule
        (
            id             BIGINT IDENTITY (1,1) NOT NULL PRIMARY KEY,
            user_id        BIGINT                NOT NULL,
            question_id    BIGINT                NOT NULL,
            efactor        DECIMAL(4, 2)
                CONSTRAINT DF_practice_schedule_efactor DEFAULT (2.5),
            interval_days  INT
                CONSTRAINT DF_practice_schedule_interval DEFAULT (0),
            -- Lưu ý: trong SQL Server, TIMESTAMP là ROWVERSION. Dùng DATETIME2 thay thế.
            next_due       DATETIME2(0)
                CONSTRAINT DF_practice_schedule_next_due DEFAULT (SYSUTCDATETIME()),
            success_streak INT
                CONSTRAINT DF_practice_schedule_streak DEFAULT (0),
            last_result    BIT                   NULL,
            last_updated   DATETIME2(0)
                CONSTRAINT DF_practice_schedule_last_updated DEFAULT (SYSUTCDATETIME()),
            CONSTRAINT UQ_practice_schedule_user_question UNIQUE (user_id, question_id)
        );
    END
GO
--Bảng writing_rubic
IF OBJECT_ID(N'dbo.writing_rubric', N'U') IS NULL
    BEGIN
        CREATE TABLE dbo.writing_rubric
        (
            id         BIGINT IDENTITY (1,1) NOT NULL PRIMARY KEY,
            name       NVARCHAR(100)         NOT NULL,
            lang       NVARCHAR(10)          NOT NULL, -- "en", "vi", ...
            type       NVARCHAR(30)          NOT NULL, -- "composite" (mặc định)
            config     NVARCHAR(MAX)         NOT NULL, -- JSON cấu hình
            created_at DATETIME2(0)
                CONSTRAINT DF_writing_rubric_created DEFAULT SYSUTCDATETIME()
        );
    END
GO
IF OBJECT_ID(N'dbo.lexicon_sense', N'U') IS NULL
    BEGIN
        CREATE TABLE dbo.lexicon_sense
        (
            id                INT IDENTITY (1,1) PRIMARY KEY,
            lexicon_unit_id   INT            NOT NULL, -- INT (khớp Lexicon_Units.id)
            pos               NVARCHAR(64)   NULL,
            ipa               NVARCHAR(128)  NULL,
            gloss_vi          NVARCHAR(512)  NOT NULL, -- nghĩa tiếng Việt
            gloss_en          NVARCHAR(512)  NULL,
            examples_json     NVARCHAR(MAX)  NULL,
            collocations_json NVARCHAR(MAX)  NULL,
            audio_url         NVARCHAR(1024) NULL,
            confidence        DECIMAL(4, 3)  NULL,     -- 0..1
            status            NVARCHAR(32)   NOT NULL DEFAULT 'proposed',
            created_at        DATETIME2(0)   NOT NULL DEFAULT SYSUTCDATETIME(),

            CONSTRAINT fk_lexicon_sense_unit
                FOREIGN KEY (lexicon_unit_id) REFERENCES dbo.Lexicon_Units (id)
        );
    END
GO
IF OBJECT_ID(N'dbo.phrase_token_map', N'U') IS NULL
    BEGIN
        CREATE TABLE dbo.phrase_token_map
        (
            id              INT IDENTITY (1,1) PRIMARY KEY,
            phrase_id       INT            NOT NULL, -- INT (khớp Phrases.id)
            token_start     INT            NOT NULL,
            token_end       INT            NOT NULL,
            lexicon_unit_id INT            NULL,     -- INT (khớp Lexicon_Units.id)
            sense_id        INT            NULL,     -- INT (khớp lexicon_sense.id)
            gloss_vi        NVARCHAR(512)  NULL,
            ipa             NVARCHAR(128)  NULL,
            audio_url       NVARCHAR(1024) NULL,
            created_at      DATETIME2(0)   NOT NULL DEFAULT SYSUTCDATETIME(),

            CONSTRAINT fk_ptm_phrase
                FOREIGN KEY (phrase_id) REFERENCES dbo.Phrases (id),
            CONSTRAINT fk_ptm_unit
                FOREIGN KEY (lexicon_unit_id) REFERENCES dbo.Lexicon_Units (id),
            CONSTRAINT fk_ptm_sense
                FOREIGN KEY (sense_id) REFERENCES dbo.lexicon_sense (id)
        );
    END
GO

-- Bước 5: Thêm lại các ràng buộc khóa ngoại (gộp chung)
ALTER TABLE dbo.Orders
    ADD CONSTRAINT FK_Orders_User FOREIGN KEY (user_id) REFERENCES dbo.Users (id);
GO

ALTER TABLE dbo.Order_Details
    ADD CONSTRAINT FK_OrderDetails_Order FOREIGN KEY (order_id) REFERENCES dbo.Orders (id),
        CONSTRAINT FK_OrderDetails_Course FOREIGN KEY (course_id) REFERENCES dbo.Courses (id);
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
        '$2a$12$InSkNTTMKpbjnGK6K2YgduCKv/up.ylLgEnJrOoXgWnh3SFUECUiG', 1)
GO

INSERT INTO dbo.Users
    (username, email, full_name, role, password, is_active)
VALUES ('admin1', 'admin1email1@example.com', N'Tên nào cũng được', 'admin',
        '$2a$12$kUg548eke5DJx1mj5GdByOn3KFIjqOvD8xGmwoPpI9owL3BnUV2JS', 1)
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
INSERT INTO dbo.Languages (id, code, name)
VALUES (1, 'vi', N'Tiếng Việt'),
       (2, 'en', N'English'),
       (3, 'zh', N'Chinese'),
       (4, 'ja', N'Japanese'),
       (5, 'ko', N'Korean')
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

-- 4.5.1. English for Beginners Course
INSERT INTO dbo.Courses (name, description, language_id, created_by, level, duration, status, price, thumbnail)
VALUES (N'English for Beginners',
        N'Complete beginner English course with 10 modules covering essential communication skills', 2, 1,
        N'Beginner (A1)', N'25-30 weeks', N'active', 199.99, N'english-beginner-thumb.jpg')
GO

-- 4.6. Sample Module
INSERT INTO dbo.Modules (course_id, name, created_by)
VALUES (1, N'Module 1', 1)
GO

-- 4.6.1. English Course Modules
INSERT INTO dbo.Modules (course_id, name, description, created_by, [order], duration, status)
VALUES (2, N'Getting Started', N'Basic communication fundamentals', 1, 1, N'2-3 weeks', N'active'),
       (2, N'Personal Information', N'Self-introduction and basic personal details', 1, 2, N'2-3 weeks', N'active'),
       (2, N'Family and People', N'Family relationships and describing people', 1, 3, N'3 weeks', N'active'),
       (2, N'Daily Life and Routines', N'Daily activities and time expressions', 1, 4, N'3 weeks', N'active'),
       (2, N'Home and Environment', N'Home vocabulary and location expressions', 1, 5, N'3 weeks', N'active'),
       (2, N'Food and Drinks', N'Food vocabulary and expressing preferences', 1, 6, N'3 weeks', N'active'),
       (2, N'Shopping and Money', N'Shopping vocabulary and transactions', 1, 7, N'3 weeks', N'active'),
       (2, N'Work and Activities', N'Jobs, hobbies, and question formation', 1, 8, N'3 weeks', N'active'),
       (2, N'Transportation and Directions', N'Transportation and navigation', 1, 9, N'3 weeks', N'active'),
       (2, N'Review and Communication', N'Integration and past tense introduction', 1, 10, N'3-4 weeks', N'active')
GO

-- 4.7.1. English Course Lessons
-- Module 1: Getting Started (module_id = 2)
INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url,
                         icon)
VALUES (2, N'English Alphabet and Sounds', N'26 letters and their pronunciation',
        N'Learn the English alphabet and basic phonetic sounds. Practice letter recognition and pronunciation.',
        'video', 1, N'15 minutes', N'active', 1, N'https://example.com/lessons/alphabet', N'type'),
       (2, N'Numbers 1-20', N'Counting from 1 to 20',
        N'Master numbers 1-20 with pronunciation practice and basic math vocabulary.', 'video', 2, N'12 minutes',
        N'active', 1, N'https://example.com/lessons/numbers', N'hash'),
       (2, N'Basic Greetings', N'Common greeting expressions',
        N'Learn essential greetings for formal and informal situations.', 'video', 3, N'10 minutes', N'active', 1,
        N'https://example.com/lessons/greetings', N'hand-heart'),
       (2, N'Courtesy Words', N'Please, thank you, excuse me', N'Master polite expressions for everyday interactions.',
        'video', 4, N'8 minutes', N'active', 1, N'https://example.com/lessons/courtesy', N'heart-handshake'),
       (2, N'First Conversations', N'Combining basic elements',
        N'Practice simple dialogues using greetings and courtesy words.', 'video', 5, N'18 minutes', N'active', 1,
        N'https://example.com/lessons/conversations1', N'messages-square'),

-- Module 2: Personal Information (module_id = 3)
       (3, N'What''s Your Name?', N'Asking and giving names',
        N'Learn to introduce yourself and ask others'' names politely.', 'video', 1, N'10 minutes', N'active', 1,
        N'https://example.com/lessons/names', N'user'),
       (3, N'Where Are You From?', N'Countries and nationalities',
        N'Express your origin and learn about different countries and nationalities.', 'video', 2, N'15 minutes',
        N'active', 1, N'https://example.com/lessons/countries', N'map-pin'),
       (3, N'How Old Are You?', N'Ages and numbers 20-100', N'Ask and answer about age using larger numbers.', 'video',
        3, N'12 minutes', N'active', 1, N'https://example.com/lessons/age', N'calendar'),
       (3, N'Personal Pronouns', N'I, you, he, she, we, they', N'Master subject pronouns and their usage in sentences.',
        'video', 4, N'14 minutes', N'active', 1, N'https://example.com/lessons/pronouns', N'users'),
       (3, N'The Verb ''To Be''', N'Am, is, are forms',
        N'Learn the most important verb in English and its present tense forms.', 'video', 5, N'20 minutes', N'active',
        1, N'https://example.com/lessons/tobe', N'equal'),

-- Module 3: Family and People (module_id = 4)
       (4, N'Family Members', N'Parents, siblings, relatives',
        N'Learn vocabulary for family relationships and family tree.', 'video', 1, N'16 minutes', N'active', 1,
        N'https://example.com/lessons/family', N'users-round'),
       (4, N'Describing Appearance', N'Hair, eyes, height', N'Describe physical characteristics of people.', 'video', 2,
        N'18 minutes', N'active', 1, N'https://example.com/lessons/appearance', N'eye'),
       (4, N'Age and Personality', N'Character traits and age groups',
        N'Express personality traits and different life stages.', 'video', 3, N'15 minutes', N'active', 1,
        N'https://example.com/lessons/personality', N'smile'),
       (4, N'Possessive Adjectives', N'My, your, his, her, our, their',
        N'Show ownership and relationships using possessive adjectives.', 'video', 4, N'17 minutes', N'active', 1,
        N'https://example.com/lessons/possessive', N'hand'),
       (4, N'Questions About People', N'Who, what, how questions',
        N'Ask and answer questions about people and their characteristics.', 'video', 5, N'19 minutes', N'active', 1,
        N'https://example.com/lessons/people-questions', N'help-circle'),

-- Module 4: Daily Life and Routines (module_id = 5)
       (5, N'Daily Activities', N'Morning and evening routines',
        N'Learn vocabulary for common daily activities and routines.', 'video', 1, N'14 minutes', N'active', 1,
        N'https://example.com/lessons/daily-activities', N'sun'),
       (5, N'Time and Clock', N'Telling time and time expressions',
        N'Master time expressions and how to read analog and digital clocks.', 'video', 2, N'22 minutes', N'active', 1,
        N'https://example.com/lessons/time', N'clock'),
       (5, N'Days of the Week', N'Monday through Sunday', N'Learn days of the week and weekly routines.', 'video', 3,
        N'11 minutes', N'active', 1, N'https://example.com/lessons/days', N'calendar-days'),
       (5, N'Present Simple Tense', N'Regular verb conjugation',
        N'Master the present simple tense for describing habits and facts.', 'video', 4, N'25 minutes', N'active', 1,
        N'https://example.com/lessons/present-simple', N'repeat'),
       (5, N'Frequency Adverbs', N'Always, usually, sometimes, never', N'Express how often you do activities.', 'video',
        5, N'16 minutes', N'active', 1, N'https://example.com/lessons/frequency', N'timer'),

-- Module 5: Home and Environment (module_id = 6)
       (6, N'Rooms in the House', N'Kitchen, bedroom, bathroom, living room',
        N'Learn names of rooms and their functions.', 'video', 1, N'13 minutes', N'active', 1,
        N'https://example.com/lessons/rooms', N'home'),
       (6, N'Furniture and Objects', N'Common household items', N'Vocabulary for furniture and household objects.',
        'video', 2, N'17 minutes', N'active', 1, N'https://example.com/lessons/furniture', N'armchair'),
       (6, N'Prepositions of Place', N'In, on, under, next to, behind', N'Describe location and position of objects.',
        'video', 3, N'19 minutes', N'active', 1, N'https://example.com/lessons/prepositions', N'move-3d'),
       (6, N'There is/There are', N'Describing what exists', N'Express existence and quantity of things in places.',
        'video', 4, N'21 minutes', N'active', 1, N'https://example.com/lessons/there-is-are', N'search'),
       (6, N'Describing Your Home', N'Combining vocabulary and grammar',
        N'Practice describing your living situation using new vocabulary.', 'video', 5, N'18 minutes', N'active', 1,
        N'https://example.com/lessons/describe-home', N'house'),

-- Module 6: Food and Drinks (module_id = 7)
       (7, N'Common Foods', N'Fruits, vegetables, basic food groups',
        N'Learn essential food vocabulary and categories.', 'video', 1, N'15 minutes', N'active', 1,
        N'https://example.com/lessons/foods', N'apple'),
       (7, N'Drinks and Beverages', N'Hot and cold drinks', N'Vocabulary for different types of beverages.', 'video', 2,
        N'12 minutes', N'active', 1, N'https://example.com/lessons/drinks', N'coffee'),
       (7, N'Meals of the Day', N'Breakfast, lunch, dinner', N'Learn about meal times and typical foods for each meal.',
        'video', 3, N'14 minutes', N'active', 1, N'https://example.com/lessons/meals', N'utensils'),
       (7, N'Likes and Dislikes', N'Expressing food preferences',
        N'Express what you like and don''t like to eat and drink.', 'video', 4, N'16 minutes', N'active', 1,
        N'https://example.com/lessons/preferences', N'thumbs-up'),
       (7, N'At the Restaurant', N'Ordering food and restaurant vocabulary',
        N'Practice ordering food and interacting in restaurant situations.', 'video', 5, N'20 minutes', N'active', 1,
        N'https://example.com/lessons/restaurant', N'chef-hat'),

-- Module 7: Shopping and Money (module_id = 8)
       (8, N'Numbers and Prices', N'Large numbers and currency',
        N'Learn numbers 100-1000 and how to talk about prices.', 'video', 1, N'17 minutes', N'active', 1,
        N'https://example.com/lessons/prices', N'dollar-sign'),
       (8, N'Clothing Items', N'Basic wardrobe vocabulary', N'Learn names of clothing items and accessories.', 'video',
        2, N'15 minutes', N'active', 1, N'https://example.com/lessons/clothing', N'shirt'),
       (8, N'At the Store', N'Shopping locations and interactions',
        N'Navigate different types of stores and shopping situations.', 'video', 3, N'18 minutes', N'active', 1,
        N'https://example.com/lessons/stores', N'store'),
       (8, N'Making Purchases', N'Buying and payment vocabulary',
        N'Practice polite purchasing interactions and payment methods.', 'video', 4, N'19 minutes', N'active', 1,
        N'https://example.com/lessons/purchases', N'credit-card'),
       (8, N'Quantities and Containers', N'Some, any, much, many',
        N'Learn to express quantities and use appropriate containers.', 'video', 5, N'21 minutes', N'active', 1,
        N'https://example.com/lessons/quantities', N'package'),

-- Module 8: Work and Activities (module_id = 9)
       (9, N'Jobs and Occupations', N'Common professions', N'Learn vocabulary for different jobs and professions.',
        'video', 1, N'16 minutes', N'active', 1, N'https://example.com/lessons/jobs', N'briefcase'),
       (9, N'Workplace Vocabulary', N'Office and work-related terms',
        N'Vocabulary for workplace environments and activities.', 'video', 2, N'14 minutes', N'active', 1,
        N'https://example.com/lessons/workplace', N'building'),
       (9, N'Hobbies and Free Time', N'Leisure activities', N'Express what you do for fun and relaxation.', 'video', 3,
        N'17 minutes', N'active', 1, N'https://example.com/lessons/hobbies', N'gamepad-2'),
       (9, N'Present Simple Questions', N'Do/Does question formation',
        N'Learn to ask yes/no questions in present simple tense.', 'video', 4, N'23 minutes', N'active', 1,
        N'https://example.com/lessons/questions', N'message-circle-question'),
       (9, N'WH- Questions', N'What, where, when, who, why, how',
        N'Master information questions for getting specific details.', 'video', 5, N'25 minutes', N'active', 1,
        N'https://example.com/lessons/wh-questions', N'circle-help'),

-- Module 9: Transportation and Directions (module_id = 10)
       (10, N'Modes of Transportation', N'Cars, buses, trains, planes',
        N'Learn vocabulary for different ways to travel.', 'video', 1, N'15 minutes', N'active', 1,
        N'https://example.com/lessons/transportation', N'car'),
       (10, N'Places in the City', N'Public buildings and locations', N'Navigate city locations and public places.',
        'video', 2, N'18 minutes', N'active', 1, N'https://example.com/lessons/city-places', N'map'),
       (10, N'Asking for Directions', N'Polite ways to ask for help',
        N'Learn to ask for directions politely and clearly.', 'video', 3, N'16 minutes', N'active', 1,
        N'https://example.com/lessons/ask-directions', N'navigation'),
       (10, N'Giving Directions', N'Left, right, straight, landmarks',
        N'Give clear directions using basic directional vocabulary.', 'video', 4, N'20 minutes', N'active', 1,
        N'https://example.com/lessons/give-directions', N'signpost'),
       (10, N'Prepositions of Movement', N'To, from, into, out of, through',
        N'Express movement and direction with prepositions.', 'video', 5, N'22 minutes', N'active', 1,
        N'https://example.com/lessons/movement', N'move'),

-- Module 10: Review and Communication (module_id = 11)
       (11, N'Past Tense - To Be', N'Was, were forms', N'Introduction to past tense using the verb "to be".', 'video',
        1, N'19 minutes', N'active', 1, N'https://example.com/lessons/past-tobe', N'history'),
       (11, N'Making Plans', N'Going to + verb future', N'Express future plans and intentions.', 'video', 2,
        N'21 minutes', N'active', 1, N'https://example.com/lessons/future-plans', N'calendar-plus'),
       (11, N'Weather and Seasons', N'Weather descriptions and seasons',
        N'Talk about weather conditions and seasonal activities.', 'video', 3, N'17 minutes', N'active', 1,
        N'https://example.com/lessons/weather', N'cloud-sun'),
       (11, N'Health and Body', N'Body parts and health expressions',
        N'Basic health vocabulary and expressing how you feel.', 'video', 4, N'18 minutes', N'active', 1,
        N'https://example.com/lessons/health', N'heart-pulse'),
       (11, N'Putting It All Together', N'Comprehensive review and practice',
        N'Review all grammar points and practice real-life conversations.', 'video', 5, N'30 minutes', N'active', 1,
        N'https://example.com/lessons/final-review', N'graduation-cap')
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
USE InfinityWeb;

------------------------------------------------------------
-- R1: general_en_simple  (đã dùng cho “I have a dog”)
------------------------------------------------------------
IF NOT EXISTS (SELECT 1
               FROM dbo.writing_rubric
               WHERE name = N'general_en_simple')
    BEGIN
        INSERT dbo.writing_rubric(name, lang, type, config)
        VALUES (N'general_en_simple', N'en', N'composite', N'{
    "jwWeight": 0.4,
    "tokenWeight": 0.6,
    "semWeight": 0.0,
    "passThreshold": 70,
    "minLen": 3,
    "mustContainAny": [],
    "mustContainAll": [],
    "forbid": [],
    "regexMust": [],
    "regexForbid": [
      "\\\\bi\\\\s+has\\\\b",
      "\\\\b(you|we|they)\\\\s+has\\\\b",
      "\\\\b(he|she|it)\\\\s+have\\\\b(?!\\\\s+got\\\\b)"
    ],
    "penalties": [
      {"pattern":"\\\\bi\\\\s+has\\\\b","value":20},
      {"pattern":"\\\\b(you|we|they)\\\\s+has\\\\b","value":15},
      {"pattern":"\\\\b(he|she|it)\\\\s+have\\\\b(?!\\\\s+got\\\\b)","value":12}
    ],
    "hardZeroRules": [
      {"pattern":"\\\\b(i|you|we|they)\\\\s+has\\\\b","message":"Sai chia động từ: với I/you/we/they dùng “have”, không phải “has”."},
      {"pattern":"\\\\b(he|she|it)\\\\s+have\\\\b(?!\\\\s+got\\\\b)","message":"Sai chia động từ: với he/she/it dùng “has”, không dùng “have”."}
    ],
    "altTargets": []
  }');
    END;

------------------------------------------------------------
-- R2: en_to_be_basic  (is/are với chủ ngữ)
-- 0 điểm nếu: I/you/we/they + is  hoặc  he/she/it + are
------------------------------------------------------------
IF NOT EXISTS (SELECT 1
               FROM dbo.writing_rubric
               WHERE name = N'en_to_be_basic')
    BEGIN
        INSERT dbo.writing_rubric(name, lang, type, config)
        VALUES (N'en_to_be_basic', N'en', N'composite', N'{
    "jwWeight": 0.4, "tokenWeight": 0.6, "semWeight": 0.0, "passThreshold": 70, "minLen": 2,
    "regexForbid": [],
    "penalties": [],
    "hardZeroRules": [
      {"pattern":"\\\\b(i|you|we|they)\\\\s+is\\\\b","message":"Sai chia “to be”: với I/you/we/they dùng “am/are”, không dùng “is”."},
      {"pattern":"\\\\b(he|she|it)\\\\s+are\\\\b","message":"Sai chia “to be”: với he/she/it dùng “is”, không dùng “are”."}
    ],
    "altTargets": []
  }');
    END;

------------------------------------------------------------
-- R3: en_do_does_present_simple (phủ định/câu hỏi hiện tại đơn)
-- 0 điểm nếu: he/she/it + do|don't   hoặc   I/you/we/they + does|doesn't
------------------------------------------------------------
IF NOT EXISTS (SELECT 1
               FROM dbo.writing_rubric
               WHERE name = N'en_do_does_present_simple')
    BEGIN
        INSERT dbo.writing_rubric(name, lang, type, config)
        VALUES (N'en_do_does_present_simple', N'en', N'composite', N'{
    "jwWeight": 0.4, "tokenWeight": 0.6, "semWeight": 0.0, "passThreshold": 70, "minLen": 3,
    "regexForbid": [],
    "penalties": [
      {"pattern":"\\b(he|she|it)\\s+do\\b","value":15},
      {"pattern":"\\b(he|she|it)\\s+don''t\\b","value":15},
      {"pattern":"\\b(i|you|we|they)\\s+does\\b","value":15},
      {"pattern":"\\b(i|you|we|they)\\s+doesn''t\\b","value":15}
    ],
    "hardZeroRules": [
      {"pattern":"\\b(he|she|it)\\s+do(n''t)?\\b","message":"Hiện tại đơn: với he/she/it dùng “does/doesn''t”."},
      {"pattern":"\\b(i|you|we|they)\\s+does(n''t)?\\b","message":"Hiện tại đơn: với I/you/we/they dùng “do/don''t”."}
    ],
    "altTargets": []
  }');
    END;


------------------------------------------------------------
-- R4: en_present_continuous  (am/is/are + V-ing)
-- Yêu cầu có to be + động từ -ing ; phạt nặng nếu thiếu -ing
------------------------------------------------------------
IF NOT EXISTS (SELECT 1
               FROM dbo.writing_rubric
               WHERE name = N'en_present_continuous')
    BEGIN
        INSERT dbo.writing_rubric(name, lang, type, config)
        VALUES (N'en_present_continuous', N'en', N'composite', N'{
    "jwWeight": 0.3, "tokenWeight": 0.7, "semWeight": 0.0, "passThreshold": 70, "minLen": 3,
    "regexMust": ["\\\\b(am|is|are)\\\\b", "\\\\b\\w+ing\\\\b"],
    "regexForbid": ["\\\\b(always)\\\\b"],       // tránh lạm dụng always ở thì tiếp diễn
    "penalties": [
      {"pattern":"\\\\b(am|is|are)\\\\s+\\w+\\\\b(?!\\s*ing)","value":15} // to be + base không -ing
    ],
    "hardZeroRules": [],
    "altTargets": []
  }');
    END;

------------------------------------------------------------
-- R5: en_present_perfect_simple  (have/has + V3)
-- Yêu cầu have/has + (ed|một số V3 phổ biến); cấm dùng "ago"
------------------------------------------------------------
IF NOT EXISTS (SELECT 1
               FROM dbo.writing_rubric
               WHERE name = N'en_present_perfect_simple')
    BEGIN
        INSERT dbo.writing_rubric(name, lang, type, config)
        VALUES (N'en_present_perfect_simple', N'en', N'composite', N'{
    "jwWeight": 0.3, "tokenWeight": 0.7, "semWeight": 0.0, "passThreshold": 70, "minLen": 3,
    "regexMust": ["\\\\b(have|has)\\\\b"],
    "regexForbid": ["\\\\bago\\\\b"],
    "penalties": [
      {"pattern":"\\\\b(have|has)\\\\b(?!.*\\\\b(\\\w+ed|gone|been|done|seen|made|had|taken|written|eaten|known|thought)\\\\b)","value":18}
    ],
    "hardZeroRules": [],
    "altTargets": []
  }');
    END;

-- 2) Thêm cột liên kết rubric vào questions (nếu chưa có)
IF COL_LENGTH('dbo.questions', 'writing_rubric_id') IS NULL
ALTER TABLE dbo.questions
    ADD writing_rubric_id BIGINT NULL;
GO

-- 3) (Tuỳ) thêm nơi lưu đáp án mở + đáp án thay thế
IF COL_LENGTH('dbo.questions', 'correct_text') IS NULL
ALTER TABLE dbo.questions
    ADD correct_text NVARCHAR(4000) NULL;
IF COL_LENGTH('dbo.questions', 'alt_answers_json') IS NULL
ALTER TABLE dbo.questions
    ADD alt_answers_json NVARCHAR(MAX) NULL;
GO
-- 4.11. Sample Order

-- INSERT INTO dbo.Order_Details (order_id, course_id ,service_name, service_desc, price)
-- VALUES (1, 1, N'Unlock full khóa học 1 năm', N'Truy cập khoá học trong 1 năm', 1000000)
-- GO

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

-- DROP DATABASE InfinityWeb