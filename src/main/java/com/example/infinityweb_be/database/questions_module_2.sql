-- =====================================================
-- MODULE 2: PERSONAL INFORMATION
-- =====================================================

-- Insert Module 2
INSERT INTO dbo.Modules (course_id, name, description, created_by, [order], duration, status)
VALUES (2, N'Personal Information', N'Self-introduction and basic personal details', 1, 2, N'2-3 weeks', N'active');

DECLARE @Module2_Id INT = SCOPE_IDENTITY();

-- Insert Lessons for Module 2
INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module2_Id, N'What''s Your Name?', N'Asking and giving names',
        N'Learn to introduce yourself and ask others'' names politely.', 'video', 1, N'10 minutes', N'active', 1,
        N'https://example.com/lessons/names', N'user');

DECLARE @Lesson1_Id INT = SCOPE_IDENTITY();

INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module2_Id, N'Where Are You From?', N'Countries and nationalities',
        N'Express your origin and learn about different countries and nationalities.', 'video', 2, N'15 minutes',
        N'active', 1, N'https://example.com/lessons/countries', N'map-pin');

DECLARE @Lesson2_Id INT = SCOPE_IDENTITY();

INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module2_Id, N'How Old Are You?', N'Ages and numbers 20-100', N'Ask and answer about age using larger numbers.', 'video',
        3, N'12 minutes', N'active', 1, N'https://example.com/lessons/age', N'calendar');

DECLARE @Lesson3_Id INT = SCOPE_IDENTITY();

INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module2_Id, N'Personal Pronouns', N'I, you, he, she, we, they', N'Master subject pronouns and their usage in sentences.',
        'video', 4, N'14 minutes', N'active', 1, N'https://example.com/lessons/pronouns', N'users');

DECLARE @Lesson4_Id INT = SCOPE_IDENTITY();

INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module2_Id, N'The Verb ''To Be''', N'Am, is, are forms',
        N'Learn the most important verb in English and its present tense forms.', 'video', 5, N'20 minutes', N'active',
        1, N'https://example.com/lessons/tobe', N'equal');

DECLARE @Lesson5_Id INT = SCOPE_IDENTITY();

-- =====================================================
-- QUESTIONS FOR MODULE 2
-- =====================================================

-- LESSON 1: What's Your Name?
-- Question 1: How do you ask someone's name?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'How do you ask someone''s name?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q1_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q1_Id, N'What is your name?', 1, 1),
       (@Q1_Id, N'How are you?', 0, 2),
       (@Q1_Id, N'Where are you?', 0, 3),
       (@Q1_Id, N'What time is it?', 0, 4);

-- Question 2: Complete the introduction
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'My name ___ Sarah.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 5, 1);
DECLARE @Q2_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q2_Id, N'is');

-- Question 3: Select formal ways to ask for names
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Select formal ways to ask for someone''s name:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_multi'), 'medium', 10, 1);
DECLARE @Q3_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q3_Id, N'What is your name?', 1, 1),
       (@Q3_Id, N'What''s your name?', 0, 2),
       (@Q3_Id, N'May I have your name?', 1, 3),
       (@Q3_Id, N'What do they call you?', 0, 4),
       (@Q3_Id, N'Could you tell me your name?', 1, 5),
       (@Q3_Id, N'Who are you?', 0, 6);

-- Question 4: Reorder words for proper introduction
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Arrange these words: name / My / is / Tom',
        (SELECT id FROM dbo.Question_Types WHERE code = 'reorder_words'), 'easy', 8, 1);
DECLARE @Q4_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q4_Id, N'My', 1, 1),
       (@Q4_Id, N'name', 1, 2),
       (@Q4_Id, N'is', 1, 3),
       (@Q4_Id, N'Tom', 1, 4);

-- Question 5: Text input - Your name
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Write your first name:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 5, 1);
DECLARE @Q5_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q5_Id, N'Student');

-- Question 6: Speaking practice
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Say "My name is [your name]" clearly.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'easy', 8, 1);
DECLARE @Q6_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q6_Id, N'Correct pronunciation', 1, 1),
       (@Q6_Id, N'Incorrect pronunciation', 0, 2);

-- Question 7: Matching questions with responses
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Match questions with appropriate responses:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'matching'), 'medium', 8, 1);
DECLARE @Q7_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q7_Id, N'What''s your name? - My name is Anna', 1, 1),
       (@Q7_Id, N'What''s your last name? - Smith', 1, 2),
       (@Q7_Id, N'How do you spell that? - J-O-H-N', 1, 3),
       (@Q7_Id, N'Nice to meet you - Nice to meet you too', 1, 4);

-- Question 8: Listening comprehension
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Listen and choose the correct name:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'listening'), 'easy', 8, 1);
DECLARE @Q8_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q8_Id, N'Maria', 1, 1),
       (@Q8_Id, N'Mary', 0, 2),
       (@Q8_Id, N'Marina', 0, 3),
       (@Q8_Id, N'Marta', 0, 4);

-- LESSON 2: Where Are You From?
-- Question 9: How to ask about origin
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'How do you ask where someone is from?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q9_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q9_Id, N'Where are you from?', 1, 1),
       (@Q9_Id, N'What are you from?', 0, 2),
       (@Q9_Id, N'How are you from?', 0, 3),
       (@Q9_Id, N'When are you from?', 0, 4);

-- Question 10: Countries and nationalities
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'What nationality is someone from Japan?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q10_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q10_Id, N'Japanese', 1, 1),
       (@Q10_Id, N'Japan', 0, 2),
       (@Q10_Id, N'Japanish', 0, 3),
       (@Q10_Id, N'Japaner', 0, 4);

-- Question 11: Select correct country-nationality pairs
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Select correct country-nationality pairs:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_multi'), 'medium', 10, 1);
DECLARE @Q11_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q11_Id, N'France - French', 1, 1),
       (@Q11_Id, N'Germany - German', 1, 2),
       (@Q11_Id, N'China - Chinese', 1, 3),
       (@Q11_Id, N'Italy - Italian', 1, 4),
       (@Q11_Id, N'Brazil - Brasilian', 0, 5),
       (@Q11_Id, N'Spain - Spanist', 0, 6);

-- Question 12: Fill in the blank
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'I am ___ the United States.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 5, 1);
DECLARE @Q12_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q12_Id, N'from');

-- Question 13: Text input - Your country
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'What country are you from?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 5, 1);
DECLARE @Q13_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q13_Id, N'Vietnam');

-- Question 14: Reorder sentence about origin
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Arrange: from / am / I / Canada',
        (SELECT id FROM dbo.Question_Types WHERE code = 'reorder_words'), 'easy', 8, 1);
DECLARE @Q14_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q14_Id, N'I', 1, 1),
       (@Q14_Id, N'am', 1, 2),
       (@Q14_Id, N'from', 1, 3),
       (@Q14_Id, N'Canada', 1, 4);

-- Question 15: Speaking practice
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Say "I am from [your country]" clearly.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'easy', 8, 1);
DECLARE @Q15_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q15_Id, N'Correct pronunciation', 1, 1),
       (@Q15_Id, N'Incorrect pronunciation', 0, 2);

-- Question 16: Match countries with capitals
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Match countries with their capitals:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'matching'), 'medium', 8, 1);
DECLARE @Q16_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q16_Id, N'England - London', 1, 1),
       (@Q16_Id, N'France - Paris', 1, 2),
       (@Q16_Id, N'Germany - Berlin', 1, 3),
       (@Q16_Id, N'Japan - Tokyo', 1, 4);

-- LESSON 3: How Old Are You?
-- Question 17: Asking about age
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'What is the polite way to ask someone''s age?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q17_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q17_Id, N'How old are you?', 1, 1),
       (@Q17_Id, N'What age do you have?', 0, 2),
       (@Q17_Id, N'How many years?', 0, 3),
       (@Q17_Id, N'What is your years?', 0, 4);

-- Question 18: Numbers 20-100
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'How do you say "25" in English?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q18_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q18_Id, N'twenty-five', 1, 1),
       (@Q18_Id, N'twenty-fifty', 0, 2),
       (@Q18_Id, N'two-five', 0, 3),
       (@Q18_Id, N'twenty five', 0, 4);

-- Question 19: Fill in age response
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'I am ___ years old.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 5, 1);
DECLARE @Q19_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q19_Id, N'twenty');

-- Question 20: Select correct number words
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Select correctly written numbers:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_multi'), 'medium', 10, 1);
DECLARE @Q20_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q20_Id, N'thirty-seven', 1, 1),
       (@Q20_Id, N'fourty-two', 0, 2),
       (@Q20_Id, N'fifty-nine', 1, 3),
       (@Q20_Id, N'sixty-eight', 1, 4),
       (@Q20_Id, N'ninty-one', 0, 5),
       (@Q20_Id, N'eighty-three', 1, 6);

-- Question 21: Text input - Your age
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Write your age in words (e.g., twenty-five):',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 5, 1);
DECLARE @Q21_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q21_Id, N'twenty-five');

-- Question 22: Speaking numbers
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Say "I am thirty years old" clearly.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'easy', 8, 1);
DECLARE @Q22_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q22_Id, N'Correct pronunciation', 1, 1),
       (@Q22_Id, N'Incorrect pronunciation', 0, 2);

-- Question 23: Match numbers with digits
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Match number words with digits:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'matching'), 'medium', 8, 1);
DECLARE @Q23_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q23_Id, N'forty-three - 43', 1, 1),
       (@Q23_Id, N'sixty-seven - 67', 1, 2),
       (@Q23_Id, N'eighty-nine - 89', 1, 3),
       (@Q23_Id, N'ninety-two - 92', 1, 4);

-- LESSON 4: Personal Pronouns
-- Question 24: Subject pronouns
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Which pronoun refers to yourself?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q24_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q24_Id, N'I', 1, 1),
       (@Q24_Id, N'you', 0, 2),
       (@Q24_Id, N'he', 0, 3),
       (@Q24_Id, N'she', 0, 4);

-- Question 25: Choose correct pronoun
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Mary is a student. ___ is from Canada.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q25_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q25_Id, N'She', 1, 1),
       (@Q25_Id, N'He', 0, 2),
       (@Q25_Id, N'It', 0, 3),
       (@Q25_Id, N'They', 0, 4);

-- Question 26: Select all subject pronouns
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Select all subject pronouns:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_multi'), 'medium', 10, 1);
DECLARE @Q26_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q26_Id, N'I', 1, 1),
       (@Q26_Id, N'you', 1, 2),
       (@Q26_Id, N'he', 1, 3),
       (@Q26_Id, N'she', 1, 4),
       (@Q26_Id, N'me', 0, 5),
       (@Q26_Id, N'him', 0, 6);

-- Question 27: Fill in correct pronoun
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Tom and Sarah are students. ___ are from England.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 5, 1);
DECLARE @Q27_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q27_Id, N'They');

-- Question 28: Text input - Complete sentence
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Complete: John is a teacher. ___ works at a school.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 5, 1);
DECLARE @Q28_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q28_Id, N'He');

-- Question 29: Reorder with pronouns
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Arrange: students / We / are',
        (SELECT id FROM dbo.Question_Types WHERE code = 'reorder_words'), 'easy', 8, 1);
DECLARE @Q29_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q29_Id, N'We', 1, 1),
       (@Q29_Id, N'are', 1, 2),
       (@Q29_Id, N'students', 1, 3);

-- Question 30: Match pronouns with meanings
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Match pronouns with their uses:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'matching'), 'medium', 8, 1);
DECLARE @Q30_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q30_Id, N'I - referring to yourself', 1, 1),
       (@Q30_Id, N'you - referring to the person you''re talking to', 1, 2),
       (@Q30_Id, N'he - referring to a male person', 1, 3),
       (@Q30_Id, N'she - referring to a female person', 1, 4);

-- LESSON 5: The Verb 'To Be'
-- Question 31: Am, is, are forms
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'I ___ a student.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q31_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q31_Id, N'am', 1, 1),
       (@Q31_Id, N'is', 0, 2),
       (@Q31_Id, N'are', 0, 3),
       (@Q31_Id, N'be', 0, 4);

-- Question 32: Choose correct form
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'She ___ a teacher.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q32_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q32_Id, N'is', 1, 1),
       (@Q32_Id, N'am', 0, 2),
       (@Q32_Id, N'are', 0, 3),
       (@Q32_Id, N'be', 0, 4);

-- Question 33: Select correct 'to be' sentences
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Select grammatically correct sentences:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_multi'), 'medium', 10, 1);
DECLARE @Q33_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q33_Id, N'They are students', 1, 1),
       (@Q33_Id, N'You is a teacher', 0, 2),
       (@Q33_Id, N'We are happy', 1, 3),
       (@Q33_Id, N'He am doctor', 0, 4),
       (@Q33_Id, N'I am tired', 1, 5),
       (@Q33_Id, N'She are nurse', 0, 6);

-- Question 34: Fill in 'to be'
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'They ___ from Japan.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 5, 1);
DECLARE @Q34_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q34_Id, N'are');

-- Question 35: Text input - Complete sentence
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Complete: You ___ my friend.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 5, 1);
DECLARE @Q35_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q35_Id, N'are');

-- Question 36: Reorder 'to be' sentence
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Arrange: happy / am / I',
        (SELECT id FROM dbo.Question_Types WHERE code = 'reorder_words'), 'easy', 8, 1);
DECLARE @Q36_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q36_Id, N'I', 1, 1),
       (@Q36_Id, N'am', 1, 2),
       (@Q36_Id, N'happy', 1, 3);

-- Question 37: Speaking 'to be' forms
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Say "We are friends" clearly.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'easy', 8, 1);
DECLARE @Q37_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q37_Id, N'Correct pronunciation', 1, 1),
       (@Q37_Id, N'Incorrect pronunciation', 0, 2);

-- Question 38: Match pronouns with 'to be' forms
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Match pronouns with correct ''to be'' forms:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'matching'), 'medium', 8, 1);
DECLARE @Q38_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q38_Id, N'I - am', 1, 1),
       (@Q38_Id, N'you - are', 1, 2),
       (@Q38_Id, N'he/she/it - is', 1, 3),
       (@Q38_Id, N'we/you/they - are', 1, 4);

-- Question 39: Listening - 'to be' forms
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Listen and choose the correct sentence:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'listening'), 'easy', 8, 1);
DECLARE @Q39_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q39_Id, N'She is a doctor', 1, 1),
       (@Q39_Id, N'She am a doctor', 0, 2),
       (@Q39_Id, N'She are a doctor', 0, 3),
       (@Q39_Id, N'She be a doctor', 0, 4);

-- Question 40: Negative forms of 'to be'
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'How do you make "I am happy" negative?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'medium', 5, 1);
DECLARE @Q40_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q40_Id, N'I am not happy', 1, 1),
       (@Q40_Id, N'I not am happy', 0, 2),
       (@Q40_Id, N'I don''t am happy', 0, 3),
       (@Q40_Id, N'I no am happy', 0, 4);

GO