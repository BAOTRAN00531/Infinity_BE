-- =====================================================
-- MODULE 1: GETTING STARTED
-- =====================================================

-- Insert Module 1
INSERT INTO dbo.Modules (course_id, name, description, created_by, [order], duration, status)
VALUES (2, N'Getting Started', N'Basic communication fundamentals', 1, 1, N'2-3 weeks', N'active');

DECLARE @Module1_Id INT = SCOPE_IDENTITY();

-- Insert Lessons for Module 1
INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url,
                         icon)
VALUES (@Module1_Id, N'English Alphabet and Sounds', N'26 letters and their pronunciation',
        N'Learn the English alphabet and basic phonetic sounds. Practice letter recognition and pronunciation.',
        'video', 1, N'15 minutes', N'active', 1, N'https://example.com/lessons/alphabet', N'type');

DECLARE @Lesson1_Id INT = SCOPE_IDENTITY();

INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url,
                         icon)
VALUES (@Module1_Id, N'Numbers 1-20', N'Counting from 1 to 20',
        N'Master numbers 1-20 with pronunciation practice and basic math vocabulary.', 'video', 2, N'12 minutes',
        N'active', 1, N'https://example.com/lessons/numbers', N'hash');

DECLARE @Lesson2_Id INT = SCOPE_IDENTITY();

INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url,
                         icon)
VALUES (@Module1_Id, N'Basic Greetings', N'Common greeting expressions',
        N'Learn essential greetings for formal and informal situations.', 'video', 3, N'10 minutes', N'active', 1,
        N'https://example.com/lessons/greetings', N'hand-heart');

DECLARE @Lesson3_Id INT = SCOPE_IDENTITY();

INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url,
                         icon)
VALUES (@Module1_Id, N'Courtesy Words', N'Please, thank you, excuse me',
        N'Master polite expressions for everyday interactions.',
        'video', 4, N'8 minutes', N'active', 1, N'https://example.com/lessons/courtesy', N'heart-handshake');

DECLARE @Lesson4_Id INT = SCOPE_IDENTITY();

INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url,
                         icon)
VALUES (@Module1_Id, N'First Conversations', N'Combining basic elements',
        N'Practice simple dialogues using greetings and courtesy words.', 'video', 5, N'18 minutes', N'active', 1,
        N'https://example.com/lessons/conversations1', N'messages-square');

DECLARE @Lesson5_Id INT = SCOPE_IDENTITY();

-- =====================================================
-- QUESTIONS FOR MODULE 1
-- =====================================================

-- LESSON 1: English Alphabet and Sounds
-- Question 1: Which letter comes after 'M'?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Which letter comes after ''M'' in the English alphabet?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q1_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q1_Id, N'L', 0, 1),
       (@Q1_Id, N'N', 1, 2),
       (@Q1_Id, N'O', 0, 3),
       (@Q1_Id, N'P', 0, 4);

-- Question 2: How many letters in alphabet?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'How many letters are there in the English alphabet?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q2_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q2_Id, N'24', 0, 1),
       (@Q2_Id, N'25', 0, 2),
       (@Q2_Id, N'26', 1, 3),
       (@Q2_Id, N'27', 0, 4);

-- Question 3: Which is a vowel?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Which of these is a vowel?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q3_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q3_Id, N'B', 0, 1),
       (@Q3_Id, N'D', 0, 2),
       (@Q3_Id, N'E', 1, 3),
       (@Q3_Id, N'F', 0, 4);

-- Question 4: Select all vowels (Multiple choice multiple)
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Select all the vowels from the options below:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_multi'), 'medium', 10, 1);
DECLARE @Q4_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q4_Id, N'A', 1, 1),
       (@Q4_Id, N'B', 0, 2),
       (@Q4_Id, N'E', 1, 3),
       (@Q4_Id, N'F', 0, 4),
       (@Q4_Id, N'I', 1, 5),
       (@Q4_Id, N'J', 0, 6);

-- Question 5: Text input - What letter comes before 'C'?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'What letter comes before ''C'' in the alphabet?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 5, 1);
DECLARE @Q5_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q5_Id, N'B');

-- Question 6: Fill in blank - The letter _ comes after G
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'The letter _ comes after G in the alphabet.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 5, 1);
DECLARE @Q6_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q6_Id, N'H');

-- Question 7: Speaking - Pronounce the letter 'A'
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Please pronounce the letter ''A'' clearly.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'easy', 8, 1);
DECLARE @Q7_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q7_Id, N'Correct pronunciation', 1, 1),
       (@Q7_Id, N'Incorrect pronunciation', 0, 2);

-- Question 8: Matching - Match letters with their positions
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Match each letter with its position in the alphabet:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'matching'), 'medium', 8, 1);
DECLARE @Q8_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q8_Id, N'A - 1st', 1, 1),
       (@Q8_Id, N'B - 2nd', 1, 2),
       (@Q8_Id, N'C - 3rd', 1, 3),
       (@Q8_Id, N'D - 4th', 1, 4);

-- Question 9: Reorder words - Arrange letters in alphabetical order
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Arrange these letters in alphabetical order: C, A, B, D',
        (SELECT id FROM dbo.Question_Types WHERE code = 'reorder_words'), 'medium', 8, 1);
DECLARE @Q9_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q9_Id, N'A', 1, 1),
       (@Q9_Id, N'B', 1, 2),
       (@Q9_Id, N'C', 1, 3),
       (@Q9_Id, N'D', 1, 4);

-- Question 10: Listening - Listen and identify the letter
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Listen to the audio and identify which letter is being pronounced:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'listening'), 'easy', 8, 1);
DECLARE @Q10_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q10_Id, N'F', 1, 1),
       (@Q10_Id, N'G', 0, 2),
       (@Q10_Id, N'H', 0, 3),
       (@Q10_Id, N'I', 0, 4);

-- Question 11: Which letter is pronounced as "double-u"?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Which letter is pronounced as "double-u"?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'medium', 5, 1);
DECLARE @Q11_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q11_Id, N'V', 0, 1),
       (@Q11_Id, N'W', 1, 2),
       (@Q11_Id, N'X', 0, 3),
       (@Q11_Id, N'Y', 0, 4);

-- Question 12: Text input - What are the first 5 letters of the alphabet?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'What are the first 5 letters of the English alphabet? (Write them in order)',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 8, 1);
DECLARE @Q12_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q12_Id, N'ABCDE');

-- LESSON 2: Numbers 1-20
-- Question 13: What number comes after 15?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'What number comes after 15?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q13_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q13_Id, N'14', 0, 1),
       (@Q13_Id, N'16', 1, 2),
       (@Q13_Id, N'17', 0, 3),
       (@Q13_Id, N'18', 0, 4);

-- Question 14: How do you write "twelve" in numbers?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'How do you write "twelve" in numbers?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 5, 1);
DECLARE @Q14_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q14_Id, N'12');

-- Question 15: Select all even numbers from 1-20
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Select all even numbers from 1 to 20:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_multi'), 'medium', 10, 1);
DECLARE @Q15_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q15_Id, N'2', 1, 1),
       (@Q15_Id, N'4', 1, 2),
       (@Q15_Id, N'6', 1, 3),
       (@Q15_Id, N'8', 1, 4),
       (@Q15_Id, N'10', 1, 5),
       (@Q15_Id, N'12', 1, 6),
       (@Q15_Id, N'14', 1, 7),
       (@Q15_Id, N'16', 1, 8),
       (@Q15_Id, N'18', 1, 9),
       (@Q15_Id, N'20', 1, 10);

-- Question 16: Fill in blank - I have _ apples
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'I have _ apples. (Write the number)',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 5, 1);
DECLARE @Q16_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q16_Id, N'5');

-- Question 17: Speaking - Count from 1 to 10
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Count from 1 to 10 out loud.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'easy', 8, 1);
DECLARE @Q17_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q17_Id, N'Correct counting', 1, 1),
       (@Q17_Id, N'Incorrect counting', 0, 2);

-- Question 18: Matching - Match numbers with words
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Match each number with its word form:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'matching'), 'medium', 8, 1);
DECLARE @Q18_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q18_Id, N'7 - seven', 1, 1),
       (@Q18_Id, N'11 - eleven', 1, 2),
       (@Q18_Id, N'19 - nineteen', 1, 3),
       (@Q18_Id, N'20 - twenty', 1, 4);

-- Question 19: Reorder words - Arrange numbers in order
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Arrange these numbers in order from smallest to largest: 18, 3, 12, 7',
        (SELECT id FROM dbo.Question_Types WHERE code = 'reorder_words'), 'medium', 8, 1);
DECLARE @Q19_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q19_Id, N'3', 1, 1),
       (@Q19_Id, N'7', 1, 2),
       (@Q19_Id, N'12', 1, 3),
       (@Q19_Id, N'18', 1, 4);

-- Question 20: Listening - Listen and identify the number
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Listen to the audio and identify which number is being said:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'listening'), 'easy', 8, 1);
DECLARE @Q20_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q20_Id, N'13', 1, 1),
       (@Q20_Id, N'14', 0, 2),
       (@Q20_Id, N'15', 0, 3),
       (@Q20_Id, N'16', 0, 4);

-- Question 21: What is 5 + 7?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'What is 5 + 7?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q21_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q21_Id, N'11', 0, 1),
       (@Q21_Id, N'12', 1, 2),
       (@Q21_Id, N'13', 0, 3),
       (@Q21_Id, N'14', 0, 4);

-- Question 22: Text input - Write the number "seventeen"
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Write the number "seventeen" in digits:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 5, 1);
DECLARE @Q22_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q22_Id, N'17');

-- LESSON 3: Basic Greetings
-- Question 23: What is a common greeting in English?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'What is a common greeting in English?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q23_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q23_Id, N'Hello', 1, 1),
       (@Q23_Id, N'Chair', 0, 2),
       (@Q23_Id, N'Apple', 0, 3),
       (@Q23_Id, N'Book', 0, 4);

-- Question 24: How do you respond to "How are you?"
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'How do you respond to "How are you?"',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q24_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q24_Id, N'I am fine, thank you', 1, 1),
       (@Q24_Id, N'My name is John', 0, 2),
       (@Q24_Id, N'I like apples', 0, 3),
       (@Q24_Id, N'It is Monday', 0, 4);

-- Question 25: Select all formal greetings
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Select all formal greetings:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_multi'), 'medium', 10, 1);
DECLARE @Q25_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q25_Id, N'Good morning', 1, 1),
       (@Q25_Id, N'Hey', 0, 2),
       (@Q25_Id, N'Good afternoon', 1, 3),
       (@Q25_Id, N'What''s up', 0, 4),
       (@Q25_Id, N'Good evening', 1, 5),
       (@Q25_Id, N'Yo', 0, 6);

-- Question 26: Fill in the blank - Good ___, how are you?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Good ___, how are you?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 5, 1);
DECLARE @Q26_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q26_Id, N'morning');

-- Question 27: Speaking - Say "Nice to meet you"
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Please say "Nice to meet you" clearly.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'easy', 8, 1);
DECLARE @Q27_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q27_Id, N'Correct pronunciation', 1, 1),
       (@Q27_Id, N'Incorrect pronunciation', 0, 2);

-- Question 28: Matching - Match greetings with time
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Match each greeting with the appropriate time:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'matching'), 'medium', 8, 1);
DECLARE @Q28_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q28_Id, N'Good morning - 9 AM', 1, 1),
       (@Q28_Id, N'Good afternoon - 2 PM', 1, 2),
       (@Q28_Id, N'Good evening - 7 PM', 1, 3),
       (@Q28_Id, N'Good night - 10 PM', 1, 4);

-- Question 29: Reorder words - Make a proper greeting
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Arrange these words to make a proper greeting: you / are / How',
        (SELECT id FROM dbo.Question_Types WHERE code = 'reorder_words'), 'medium', 8, 1);
DECLARE @Q29_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q29_Id, N'How', 1, 1),
       (@Q29_Id, N'are', 1, 2),
       (@Q29_Id, N'you', 1, 3);

-- Question 30: Text input - What do you say when you first meet someone?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'What do you say when you first meet someone?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 5, 1);
DECLARE @Q30_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q30_Id, N'Nice to meet you');

-- LESSON 4: Courtesy Words
-- Question 31: What do you say when someone helps you?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'What do you say when someone helps you?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q31_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q31_Id, N'Thank you', 1, 1),
       (@Q31_Id, N'Hello', 0, 2),
       (@Q31_Id, N'Goodbye', 0, 3),
       (@Q31_Id, N'Sorry', 0, 4);

-- Question 32: When should you say "Excuse me"?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'When should you say "Excuse me"?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q32_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q32_Id, N'When you need to get someone''s attention', 1, 1),
       (@Q32_Id, N'When you are happy', 0, 2),
       (@Q32_Id, N'When you are eating', 0, 3),
       (@Q32_Id, N'When you are sleeping', 0, 4);

-- Question 33: Select all courtesy words
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Select all courtesy words:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_multi'), 'medium', 10, 1);
DECLARE @Q33_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q33_Id, N'Please', 1, 1),
       (@Q33_Id, N'Cat', 0, 2),
       (@Q33_Id, N'Thank you', 1, 3),
       (@Q33_Id, N'Dog', 0, 4),
       (@Q33_Id, N'Excuse me', 1, 5),
       (@Q33_Id, N'Tree', 0, 6);

-- Question 34: Fill in the blank - ___ pass the salt
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'___ pass the salt.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 5, 1);
DECLARE @Q34_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q34_Id, N'Please');

-- Question 35: Text input - How do you apologize politely?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'How do you apologize politely?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 5, 1);
DECLARE @Q35_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q35_Id, N'I am sorry');

-- Question 36: Speaking - Say "You''re welcome"
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Please say "You''re welcome" clearly.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'easy', 8, 1);
DECLARE @Q36_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q36_Id, N'Correct pronunciation', 1, 1),
       (@Q36_Id, N'Incorrect pronunciation', 0, 2);

-- Question 37: Matching - Match situations with courtesy words
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Match each situation with the appropriate courtesy word:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'matching'), 'medium', 8, 1);
DECLARE @Q37_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q37_Id, N'Asking for help - Please', 1, 1),
       (@Q37_Id, N'Receiving help - Thank you', 1, 2),
       (@Q37_Id, N'Making a mistake - Sorry', 1, 3),
       (@Q37_Id, N'Getting attention - Excuse me', 1, 4);

-- LESSON 5: First Conversations
-- Question 38: What is a good way to start a conversation?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'What is a good way to start a conversation?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q38_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q38_Id, N'Hello, how are you?', 1, 1),
       (@Q38_Id, N'I don''t like you', 0, 2),
       (@Q38_Id, N'Go away', 0, 3),
       (@Q38_Id, N'I am hungry', 0, 4);

-- Question 39: How do you end a conversation politely?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'How do you end a conversation politely?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q39_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q39_Id, N'It was nice talking to you', 1, 1),
       (@Q39_Id, N'Stop talking', 0, 2),
       (@Q39_Id, N'I hate this', 0, 3),
       (@Q39_Id, N'You are boring', 0, 4);

-- Question 40: Reorder words to make a conversation starter
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Arrange these words to make a conversation starter: is / your / What / name',
        (SELECT id FROM dbo.Question_Types WHERE code = 'reorder_words'), 'medium', 8, 1);
DECLARE @Q40_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q40_Id, N'What', 1, 1),
       (@Q40_Id, N'is', 1, 2),
       (@Q40_Id, N'your', 1, 3),
       (@Q40_Id, N'name', 1, 4);

-- Question 41: Text input - How do you introduce yourself?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Complete this introduction: "Hi, my name is ___"',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 5, 1);
DECLARE @Q41_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q41_Id, N'John');

-- Question 42: Speaking - Practice introducing yourself
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Say "Hello, my name is [your name]. Nice to meet you."',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'easy', 8, 1);
DECLARE @Q42_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q42_Id, N'Correct introduction', 1, 1),
       (@Q42_Id, N'Incorrect introduction', 0, 2);

-- Question 43: Fill in the blank conversation
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Complete the conversation: A: "How are you?" B: "I am ___, thank you."',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 5, 1);
DECLARE @Q43_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q43_Id, N'fine');

-- Question 44: Select appropriate conversation topics for first meetings
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Select appropriate conversation topics for first meetings:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_multi'), 'medium', 10, 1);
DECLARE @Q44_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q44_Id, N'The weather', 1, 1),
       (@Q44_Id, N'Your salary', 0, 2),
       (@Q44_Id, N'Your hobbies', 1, 3),
       (@Q44_Id, N'Personal problems', 0, 4),
       (@Q44_Id, N'Where you work', 1, 5),
       (@Q44_Id, N'Family secrets', 0, 6);

-- Question 45: Listening - Identify the correct response
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Listen to "What''s your name?" and choose the correct response:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'listening'), 'easy', 8, 1);
DECLARE @Q45_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q45_Id, N'My name is Sarah', 1, 1),
       (@Q45_Id, N'I am 25 years old', 0, 2),
       (@Q45_Id, N'I like pizza', 0, 3),
       (@Q45_Id, N'It is sunny today', 0, 4);

GO