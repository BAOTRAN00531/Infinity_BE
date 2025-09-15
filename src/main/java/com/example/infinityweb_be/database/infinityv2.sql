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


ALTER TABLE dbo.Lessons
    ADD video_url NVARCHAR(255) NULL;



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
    language_id    INT NOT NULL,           -- foreign key
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
    ADD [type]         NVARCHAR(50) NULL, -- loại từ (cụm, từ đơn, trợ từ,...)
    difficulty     NVARCHAR(50) NULL; -- mức độ khó
ALTER TABLE dbo.Lexicon_Units
DROP
COLUMN meaning_vi;
ALTER TABLE dbo.Lexicon_Units
    ADD meaning_eng NVARCHAR(255);
--2.20 Phrases
CREATE TABLE dbo.Phrases
(
    id          INT IDENTITY (1,1) PRIMARY KEY,
    text        NVARCHAR(255) NOT NULL, -- ví dụ: お茶をください
    language_id INT NOT NULL,
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
    lexicon_id INT NOT NULL, [
    order]
    INT
    NOT
    NULL, -- vị trí trong cụm
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
    course_id      INT            NOT NULL,
    service_name NVARCHAR(255)  NOT NULL,
    service_desc NVARCHAR(500)  NULL,
    price        DECIMAL(18, 2) NOT NULL
);

ALTER TABLE dbo.Order_Details
    ALTER COLUMN course_id INT NULL;


GO

-- Bảng Practice_schedule
IF OBJECT_ID(N'dbo.practice_schedule', N'U') IS NULL
BEGIN
  CREATE TABLE dbo.practice_schedule (
    id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
    efactor DECIMAL(4,2) CONSTRAINT DF_practice_schedule_efactor DEFAULT (2.5),
    interval_days INT CONSTRAINT DF_practice_schedule_interval DEFAULT (0),
    -- Lưu ý: trong SQL Server, TIMESTAMP là ROWVERSION. Dùng DATETIME2 thay thế.
    next_due DATETIME2(0) CONSTRAINT DF_practice_schedule_next_due DEFAULT (SYSUTCDATETIME()),
    success_streak INT CONSTRAINT DF_practice_schedule_streak DEFAULT (0),
    last_result BIT NULL,
    last_updated DATETIME2(0) CONSTRAINT DF_practice_schedule_last_updated DEFAULT (SYSUTCDATETIME()),
    CONSTRAINT UQ_practice_schedule_user_question UNIQUE (user_id, question_id)
  );
END
GO
--Bảng writing_rubic
IF OBJECT_ID(N'dbo.writing_rubric', N'U') IS NULL
BEGIN
CREATE TABLE dbo.writing_rubric (
                                    id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
                                    name NVARCHAR(100) NOT NULL,
                                    lang NVARCHAR(10) NOT NULL,            -- "en", "vi", ...
                                    type NVARCHAR(30) NOT NULL,            -- "composite" (mặc định)
                                    config NVARCHAR(MAX) NOT NULL,         -- JSON cấu hình
                                    created_at DATETIME2(0) CONSTRAINT DF_writing_rubric_created DEFAULT SYSUTCDATETIME()
);
END
GO
IF OBJECT_ID(N'dbo.lexicon_sense', N'U') IS NULL
BEGIN
  CREATE TABLE dbo.lexicon_sense
  (
      id                INT IDENTITY(1,1) PRIMARY KEY,
      lexicon_unit_id   INT           NOT NULL,        -- INT (khớp Lexicon_Units.id)
      pos               NVARCHAR(64)  NULL,
      ipa               NVARCHAR(128) NULL,
      gloss_vi          NVARCHAR(512) NOT NULL,        -- nghĩa tiếng Việt
      gloss_en          NVARCHAR(512) NULL,
      examples_json     NVARCHAR(MAX) NULL,
      collocations_json NVARCHAR(MAX) NULL,
      audio_url         NVARCHAR(1024) NULL,
      confidence        DECIMAL(4,3)  NULL,            -- 0..1
      status            NVARCHAR(32)  NOT NULL DEFAULT 'proposed',
      created_at        DATETIME2(0)  NOT NULL DEFAULT SYSUTCDATETIME(),

      CONSTRAINT fk_lexicon_sense_unit
        FOREIGN KEY (lexicon_unit_id) REFERENCES dbo.Lexicon_Units (id)
  );
END
GO
IF OBJECT_ID(N'dbo.phrase_token_map', N'U') IS NULL
BEGIN
  CREATE TABLE dbo.phrase_token_map
  (
      id              INT IDENTITY(1,1) PRIMARY KEY,
      phrase_id       INT           NOT NULL,           -- INT (khớp Phrases.id)
      token_start     INT           NOT NULL,
      token_end       INT           NOT NULL,
      lexicon_unit_id INT           NULL,               -- INT (khớp Lexicon_Units.id)
      sense_id        INT           NULL,               -- INT (khớp lexicon_sense.id)
      gloss_vi        NVARCHAR(512) NULL,
      ipa             NVARCHAR(128) NULL,
      audio_url       NVARCHAR(1024) NULL,
      created_at      DATETIME2(0)  NOT NULL DEFAULT SYSUTCDATETIME(),

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
USE InfinityWeb;

------------------------------------------------------------
-- R1: general_en_simple  (đã dùng cho “I have a dog”)
------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM dbo.writing_rubric WHERE name=N'general_en_simple')
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
IF NOT EXISTS (SELECT 1 FROM dbo.writing_rubric WHERE name=N'en_to_be_basic')
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
IF NOT EXISTS (SELECT 1 FROM dbo.writing_rubric WHERE name=N'en_do_does_present_simple')
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
IF NOT EXISTS (SELECT 1 FROM dbo.writing_rubric WHERE name=N'en_present_continuous')
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
IF NOT EXISTS (SELECT 1 FROM dbo.writing_rubric WHERE name=N'en_present_perfect_simple')
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
ALTER TABLE dbo.questions ADD writing_rubric_id BIGINT NULL;
GO

-- 3) (Tuỳ) thêm nơi lưu đáp án mở + đáp án thay thế
IF COL_LENGTH('dbo.questions', 'correct_text') IS NULL
ALTER TABLE dbo.questions ADD correct_text NVARCHAR(4000) NULL;
IF COL_LENGTH('dbo.questions', 'alt_answers_json') IS NULL
ALTER TABLE dbo.questions ADD alt_answers_json NVARCHAR(MAX) NULL;
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