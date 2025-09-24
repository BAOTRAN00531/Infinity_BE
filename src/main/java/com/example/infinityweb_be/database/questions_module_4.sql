-- Module 4: Daily Life and Routines
INSERT INTO dbo.Modules (course_id, name, description, created_by, [order], duration, status)
VALUES (2, N'Daily Life and Routines', N'Describing daily activities and time expressions', 1, 4, N'2-3 weeks', N'active');
DECLARE @Module4_Id INT = SCOPE_IDENTITY();

-- Lesson 1: Daily Activities
INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module4_Id, N'Daily Activities', N'Common activities we do every day', N'Daily verbs: wake up, get up, brush teeth, take a shower, eat breakfast, go to work/school, have lunch, come home, have dinner, go to bed', N'vocabulary', 1, 45, N'active', 1, NULL, N'⏰');
DECLARE @Lesson1_Id INT = SCOPE_IDENTITY();

-- Lesson 2: Time Expressions
INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module4_Id, N'Time Expressions', N'Telling time and time-related phrases', N'Time vocabulary: in the morning, in the afternoon, in the evening, at night, early, late, on time, at + time, o''clock', N'vocabulary', 2, 45, N'active', 1, NULL, N'🕐');
DECLARE @Lesson2_Id INT = SCOPE_IDENTITY();

-- Lesson 3: Present Simple Tense
INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module4_Id, N'Present Simple Tense', N'Describing routines and habits', N'Present simple for daily routines: I/You/We/They + verb, He/She/It + verb+s, Do/Does questions, Don''t/Doesn''t negatives', N'grammar', 3, 45, N'active', 1, NULL, N'📝');
DECLARE @Lesson3_Id INT = SCOPE_IDENTITY();

-- Lesson 4: Frequency Adverbs
INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module4_Id, N'Frequency Adverbs', N'How often we do things', N'Frequency words: always, usually, often, sometimes, rarely, never, every day/week/month, once/twice a week', N'grammar', 4, 45, N'active', 1, NULL, N'🔄');
DECLARE @Lesson4_Id INT = SCOPE_IDENTITY();

-- Lesson 5: Talking About Schedules
INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module4_Id, N'Talking About Schedules', N'Discussing daily and weekly schedules', N'Schedule language: What time do you...?, I start work at..., My schedule is..., I''m free/busy at...', N'conversation', 5, 45, N'active', 1, NULL, N'📅');
DECLARE @Lesson5_Id INT = SCOPE_IDENTITY();

-- Questions for Lesson 1: Daily Activities

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'What is the first thing you do when you wake up?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 10, 1);
DECLARE @Q1_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q1_Id, N'Get up', 1, 1),
       (@Q1_Id, N'Go to sleep', 0, 2),
       (@Q1_Id, N'Have dinner', 0, 3);
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q1_Id, N'get up');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Complete: I _______ my teeth every morning.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 10, 1);
DECLARE @Q2_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q2_Id, N'brush');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'What do you do before going to work?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 10, 1);
DECLARE @Q3_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q3_Id, N'eat breakfast');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Match the daily activities',
        (SELECT id FROM dbo.Question_Types WHERE code = 'matching'), 'medium', 15, 1);
DECLARE @Q4_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q4_Id, N'Wake up|Morning', 1, 1),
       (@Q4_Id, N'Have lunch|Afternoon', 1, 2),
       (@Q4_Id, N'Go to bed|Night', 1, 3);

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Say "take a shower"',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'easy', 10, 1);
DECLARE @Q5_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q5_Id, N'take a shower');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Arrange: to / I / go / work / morning / the / in',
        (SELECT id FROM dbo.Question_Types WHERE code = 'reorder_words'), 'medium', 15, 1);
DECLARE @Q6_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q6_Id, N'I', 1, 1),
       (@Q6_Id, N'go', 1, 2),
       (@Q6_Id, N'to', 1, 3),
       (@Q6_Id, N'work', 1, 4),
       (@Q6_Id, N'in', 1, 5),
       (@Q6_Id, N'the', 1, 6),
       (@Q6_Id, N'morning', 1, 7);

-- Questions for Lesson 2: Time Expressions

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'When do we say "in the morning"?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 10, 1);
DECLARE @Q7_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q7_Id, N'6:00 AM to 12:00 PM', 1, 1),
       (@Q7_Id, N'12:00 PM to 6:00 PM', 0, 2),
       (@Q7_Id, N'6:00 PM to 12:00 AM', 0, 3);
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q7_Id, N'6:00 am to 12:00 pm');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Complete: I wake up _____ 7 o''clock.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 10, 1);
DECLARE @Q8_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q8_Id, N'at');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'How do you say 3:00?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 10, 1);
DECLARE @Q9_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q9_Id, N'three o''clock');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Choose the image that shows "evening"',
        (SELECT id FROM dbo.Question_Types WHERE code = 'single_choice_image'), 'easy', 10, 1);
DECLARE @Q10_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q10_Id, N'image_evening.jpg', 1, 1),
       (@Q10_Id, N'image_morning.jpg', 0, 2),
       (@Q10_Id, N'image_noon.jpg', 0, 3);

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'What time do you usually eat breakfast?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 10, 1);
DECLARE @Q11_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q11_Id, N'at 7 o''clock');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Say "in the afternoon"',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'easy', 10, 1);
DECLARE @Q12_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q12_Id, N'in the afternoon');

-- Questions for Lesson 3: Present Simple Tense

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Which is correct for "she"?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 10, 1);
DECLARE @Q13_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q13_Id, N'She works', 1, 1),
       (@Q13_Id, N'She work', 0, 2),
       (@Q13_Id, N'She working', 0, 3);
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q13_Id, N'she works');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Complete: _____ you like coffee?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 10, 1);
DECLARE @Q14_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q14_Id, N'do');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Make negative: He eats breakfast.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 10, 1);
DECLARE @Q15_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q15_Id, N'he doesn''t eat breakfast');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Select all correct present simple forms',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_multi'), 'medium', 15, 1);
DECLARE @Q16_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q16_Id, N'I work every day', 1, 1),
       (@Q16_Id, N'She goes to school', 1, 2),
       (@Q16_Id, N'They doesn''t like it', 0, 3),
       (@Q16_Id, N'We have lunch at 12', 1, 4);

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Arrange: doesn''t / she / breakfast / eat',
        (SELECT id FROM dbo.Question_Types WHERE code = 'reorder_words'), 'medium', 15, 1);
DECLARE @Q17_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q17_Id, N'She', 1, 1),
       (@Q17_Id, N'doesn''t', 1, 2),
       (@Q17_Id, N'eat', 1, 3),
       (@Q17_Id, N'breakfast', 1, 4);

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Say "Do you work on weekends?"',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'easy', 10, 1);
DECLARE @Q18_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q18_Id, N'do you work on weekends');

-- Questions for Lesson 4: Frequency Adverbs

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Which means 100% of the time?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 10, 1);
DECLARE @Q19_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q19_Id, N'Always', 1, 1),
       (@Q19_Id, N'Usually', 0, 2),
       (@Q19_Id, N'Sometimes', 0, 3);
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q19_Id, N'always');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Complete: I _______ drink coffee in the morning.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 10, 1);
DECLARE @Q20_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q20_Id, N'always');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'What''s the opposite of "always"?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 10, 1);
DECLARE @Q21_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q21_Id, N'never');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Match frequency adverbs with percentages',
        (SELECT id FROM dbo.Question_Types WHERE code = 'matching'), 'medium', 15, 1);
DECLARE @Q22_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q22_Id, N'Always|100%', 1, 1),
       (@Q22_Id, N'Often|70%', 1, 2),
       (@Q22_Id, N'Never|0%', 1, 3);

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Arrange: sometimes / I / late / am',
        (SELECT id FROM dbo.Question_Types WHERE code = 'reorder_words'), 'medium', 15, 1);
DECLARE @Q23_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q23_Id, N'I', 1, 1),
       (@Q23_Id, N'am', 1, 2),
       (@Q23_Id, N'sometimes', 1, 3),
       (@Q23_Id, N'late', 1, 4);

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Say "I usually go to bed at 10 PM"',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'easy', 10, 1);
DECLARE @Q24_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q24_Id, N'i usually go to bed at 10 pm');

-- Questions for Lesson 5: Talking About Schedules

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'How do you ask about someone''s work schedule?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 10, 1);
DECLARE @Q25_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q25_Id, N'What time do you start work?', 1, 1),
       (@Q25_Id, N'When is your work?', 0, 2),
       (@Q25_Id, N'How is your work?', 0, 3);
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q25_Id, N'what time do you start work');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Complete: I''m _______ on Friday afternoon.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 10, 1);
DECLARE @Q26_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q26_Id, N'free');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Answer: When do you have lunch?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 10, 1);
DECLARE @Q27_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q27_Id, N'at 12 o''clock');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Listen and answer: What time does the meeting start? (Audio: "The meeting starts at 2 PM")',
        (SELECT id FROM dbo.Question_Types WHERE code = 'listening'), 'easy', 10, 1);
DECLARE @Q28_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q28_Id, N'2 PM', 1, 1),
       (@Q28_Id, N'12 PM', 0, 2),
       (@Q28_Id, N'3 PM', 0, 3);
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q28_Id, N'2 pm');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Arrange: start / I / work / at / 9 / o''clock',
        (SELECT id FROM dbo.Question_Types WHERE code = 'reorder_words'), 'medium', 15, 1);
DECLARE @Q29_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q29_Id, N'I', 1, 1),
       (@Q29_Id, N'start', 1, 2),
       (@Q29_Id, N'work', 1, 3),
       (@Q29_Id, N'at', 1, 4),
       (@Q29_Id, N'9', 1, 5),
       (@Q29_Id, N'o''clock', 1, 6);

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Say "My schedule is very busy"',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'easy', 10, 1);
DECLARE @Q30_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q30_Id, N'my schedule is very busy');