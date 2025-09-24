-- =====================================================
-- MODULE 10: REVIEW AND COMMUNICATION
-- =====================================================

-- Insert Module 10
INSERT INTO dbo.Modules (course_id, name, description, created_by, [order], duration, status)
VALUES (2, N'Review and Communication', N'Reviewing learned concepts and advanced communication', 1, 10, N'2-3 weeks', N'active');

DECLARE @Module10_Id INT = SCOPE_IDENTITY();

-- Insert Lessons for Module 10
INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module10_Id, N'Past Tense - To Be', N'Using was and were',
        N'Learn past tense of "to be": I was, you were, he/she was, we were, they were. Past tense in sentences.',
        'grammar', 1, N'18 minutes', N'active', 1, N'https://example.com/lessons/past-tense', N'clock-arrow-up');

DECLARE @Lesson1_Id INT = SCOPE_IDENTITY();

INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module10_Id, N'Making Phone Calls', N'Telephone conversations and etiquette',
        N'Learn phone conversations: Hello, may I speak to...? Hold on please. Can you call back later? Taking messages.', 'conversation', 2, N'20 minutes',
        N'active', 1, N'https://example.com/lessons/phone-calls', N'phone');

DECLARE @Lesson2_Id INT = SCOPE_IDENTITY();

INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module10_Id, N'Expressing Opinions', N'Sharing thoughts and preferences',
        N'Learn to express opinions: I think, I believe, in my opinion. Agreeing and disagreeing politely.', 'conversation', 3, N'16 minutes', N'active', 1,
        N'https://example.com/lessons/opinions', N'message-circle');

DECLARE @Lesson3_Id INT = SCOPE_IDENTITY();

INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module10_Id, N'Review: All Topics', N'Comprehensive review of all modules',
        N'Review all learned topics: introductions, family, food, shopping, work, transportation. Practice exercises.', 'review', 4, N'25 minutes', N'active', 1,
        N'https://example.com/lessons/comprehensive-review', N'book-open');

DECLARE @Lesson4_Id INT = SCOPE_IDENTITY();

INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module10_Id, N'Final Assessment', N'Complete assessment of English skills',
        N'Final test covering all course materials. Speaking, listening, reading, and writing assessment.', 'assessment', 5, N'30 minutes', N'active', 1,
        N'https://example.com/lessons/final-assessment', N'graduation-cap');

DECLARE @Lesson5_Id INT = SCOPE_IDENTITY();

-- =====================================================
-- QUESTIONS FOR MODULE 10
-- =====================================================

-- LESSON 1: Past Tense - To Be
-- Question 1: What's the past tense of "am"?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'What''s the past tense of "am"?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q1_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q1_Id, N'Was', 1, 1),
       (@Q1_Id, N'Were', 0, 2),
       (@Q1_Id, N'Is', 0, 3),
       (@Q1_Id, N'Are', 0, 4);

-- Question 2: Complete: "Yesterday, I ___ at home."
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Complete the sentence: "Yesterday, I ___ at home."',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 5, 1);
DECLARE @Q2_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q2_Id, N'was');

-- Question 3: Speaking - Use past tense of "to be"
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Tell me what you were doing yesterday using past tense of "to be".',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'medium', 8, 1);
DECLARE @Q3_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q3_Id, N'Correct past tense usage', 1, 1),
       (@Q3_Id, N'Incorrect past tense usage', 0, 2);

-- Question 4: Text input - What's the past tense of "are"?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'What''s the past tense of "are"?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 5, 1);
DECLARE @Q4_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q4_Id, N'were');

-- Question 5: Select all correct past tense forms
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Select all correct past tense forms:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_multi'), 'medium', 10, 1);
DECLARE @Q5_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q5_Id, N'I was', 1, 1),
       (@Q5_Id, N'You were', 1, 2),
       (@Q5_Id, N'He was', 1, 3),
       (@Q5_Id, N'They were', 1, 4),
       (@Q5_Id, N'I were', 0, 5),
       (@Q5_Id, N'He were', 0, 6);

-- Question 6: Matching - Match present with past tense
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Match each present tense with its past tense:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'matching'), 'medium', 8, 1);
DECLARE @Q6_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q6_Id, N'I am - I was', 1, 1),
       (@Q6_Id, N'You are - You were', 1, 2),
       (@Q6_Id, N'We are - We were', 1, 3),
       (@Q6_Id, N'He is - He was', 1, 4);

-- LESSON 2: Making Phone Calls
-- Question 7: How do you answer the phone politely?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'How do you answer the phone politely?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q7_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q7_Id, N'Hello, how can I help you?', 1, 1),
       (@Q7_Id, N'What do you want?', 0, 2),
       (@Q7_Id, N'Who is this?', 0, 3),
       (@Q7_Id, N'I am busy', 0, 4);

-- Question 8: Complete: "May I ___ to John, please?"
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Complete the sentence: "May I ___ to John, please?"',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 5, 1);
DECLARE @Q8_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q8_Id, N'speak');

-- Question 9: Speaking - Practice a phone conversation
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Practice calling someone and asking to speak to them politely.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'medium', 8, 1);
DECLARE @Q9_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q9_Id, N'Polite phone conversation', 1, 1),
       (@Q9_Id, N'Impolite phone conversation', 0, 2);

-- Question 10: Text input - How do you ask someone to wait?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'How do you ask someone to wait on the phone?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 5, 1);
DECLARE @Q10_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q10_Id, N'Hold on please');

-- Question 11: Select all polite phone expressions
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Select all polite phone expressions:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_multi'), 'medium', 10, 1);
DECLARE @Q11_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q11_Id, N'May I speak to...?', 1, 1),
       (@Q11_Id, N'Hold on please', 1, 2),
       (@Q11_Id, N'Can you call back?', 1, 3),
       (@Q11_Id, N'Thank you for calling', 1, 4),
       (@Q11_Id, N'Go away', 0, 5),
       (@Q11_Id, N'I don''t care', 0, 6);

-- Question 12: Reorder words - Arrange phone greeting
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Arrange these words: "Hello speak may John to I"',
        (SELECT id FROM dbo.Question_Types WHERE code = 'reorder_words'), 'medium', 8, 1);
DECLARE @Q12_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q12_Id, N'Hello', 1, 1),
       (@Q12_Id, N'may', 1, 2),
       (@Q12_Id, N'I', 1, 3),
       (@Q12_Id, N'speak', 1, 4),
       (@Q12_Id, N'to', 1, 5),
       (@Q12_Id, N'John', 1, 6);

-- LESSON 3: Expressing Opinions
-- Question 13: How do you start expressing your opinion?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'How do you start expressing your opinion?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q13_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q13_Id, N'I think that...', 1, 1),
       (@Q13_Id, N'You are wrong', 0, 2),
       (@Q13_Id, N'This is stupid', 0, 3),
       (@Q13_Id, N'I don''t care', 0, 4);

-- Question 14: Complete: "In my ___, this is a good idea."
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Complete the sentence: "In my ___, this is a good idea."',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 5, 1);
DECLARE @Q14_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q14_Id, N'opinion');

-- Question 15: Speaking - Express your opinion about a topic
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Express your opinion about your favorite season.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'medium', 8, 1);
DECLARE @Q15_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q15_Id, N'Expressed opinion clearly', 1, 1),
       (@Q15_Id, N'Did not express opinion clearly', 0, 2);

-- Question 16: Text input - How do you politely disagree?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'How do you politely disagree with someone?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 5, 1);
DECLARE @Q16_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q16_Id, N'I respectfully disagree');

-- Question 17: Select all opinion expressions
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Select all opinion expressions:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_multi'), 'medium', 10, 1);
DECLARE @Q17_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q17_Id, N'I think', 1, 1),
       (@Q17_Id, N'I believe', 1, 2),
       (@Q17_Id, N'In my opinion', 1, 3),
       (@Q17_Id, N'I feel that', 1, 4),
       (@Q17_Id, N'My name is', 0, 5),
       (@Q17_Id, N'What time is it', 0, 6);

-- Question 18: Listening - Listen and identify the opinion
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Listen to the audio and identify the speaker''s opinion:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'listening'), 'easy', 8, 1);
DECLARE @Q18_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q18_Id, N'Pizza is delicious', 1, 1),
       (@Q18_Id, N'Pizza is expensive', 0, 2),
       (@Q18_Id, N'Pizza is unhealthy', 0, 3),
       (@Q18_Id, N'Pizza is Italian', 0, 4);

-- LESSON 4: Review - All Topics
-- Question 19: Review question - How do you introduce yourself?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'How do you introduce yourself?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q19_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q19_Id, N'My name is...', 1, 1),
       (@Q19_Id, N'I am hungry', 0, 2),
       (@Q19_Id, N'What time is it?', 0, 3),
       (@Q19_Id, N'Where is the bank?', 0, 4);

-- Question 20: Review question - Complete: "I ___ pizza for lunch."
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Complete the sentence: "I ___ pizza for lunch."',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 5, 1);
DECLARE @Q20_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q20_Id, N'ate');

-- Question 21: Review speaking - Describe your daily routine
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Describe your daily routine from morning to evening.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'medium', 8, 1);
DECLARE @Q21_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q21_Id, N'Described routine clearly', 1, 1),
       (@Q21_Id, N'Did not describe clearly', 0, 2);

-- Question 22: Review text input - Where do you buy groceries?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Where do you buy groceries?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 5, 1);
DECLARE @Q22_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q22_Id, N'supermarket');

-- Question 23: Review multiple choice - Select all family members
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Select all family members:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_multi'), 'medium', 10, 1);
DECLARE @Q23_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q23_Id, N'Mother', 1, 1),
       (@Q23_Id, N'Father', 1, 2),
       (@Q23_Id, N'Sister', 1, 3),
       (@Q23_Id, N'Brother', 1, 4),
       (@Q23_Id, N'Teacher', 0, 5),
       (@Q23_Id, N'Doctor', 0, 6);

-- Question 24: Review matching - Match jobs with workplaces
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Match each job with its workplace:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'matching'), 'medium', 8, 1);
DECLARE @Q24_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q24_Id, N'Teacher - School', 1, 1),
       (@Q24_Id, N'Doctor - Hospital', 1, 2),
       (@Q24_Id, N'Chef - Restaurant', 1, 3),
       (@Q24_Id, N'Pilot - Airport', 1, 4);

-- LESSON 5: Final Assessment
-- Question 25: Final assessment - Comprehensive grammar
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Choose the correct sentence:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'hard', 10, 1);
DECLARE @Q25_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q25_Id, N'She is going to the store', 1, 1),
       (@Q25_Id, N'She are going to the store', 0, 2),
       (@Q25_Id, N'She going to the store', 0, 3),
       (@Q25_Id, N'She go to the store', 0, 4);

-- Question 26: Final assessment - Complex fill in the blank
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Complete: "I have been ___ English for six months."',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'hard', 10, 1);
DECLARE @Q26_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q26_Id, N'studying');

-- Question 27: Final assessment - Advanced speaking
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Give a 2-minute presentation about your hometown.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'hard', 15, 1);
DECLARE @Q27_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q27_Id, N'Comprehensive presentation', 1, 1),
       (@Q27_Id, N'Incomplete presentation', 0, 2);

-- Question 28: Final assessment - Complex text input
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Write a sentence using past tense, present tense, and future tense.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'hard', 10, 1);
DECLARE @Q28_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q28_Id, N'Yesterday I studied, today I am learning, tomorrow I will practice');

-- Question 29: Final assessment - Comprehensive multiple choice
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Select all correct statements about English learning:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_multi'), 'hard', 15, 1);
DECLARE @Q29_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q29_Id, N'Practice makes perfect', 1, 1),
       (@Q29_Id, N'Speaking helps fluency', 1, 2),
       (@Q29_Id, N'Reading improves vocabulary', 1, 3),
       (@Q29_Id, N'Grammar is important', 1, 4),
       (@Q29_Id, N'English is impossible to learn', 0, 5),
       (@Q29_Id, N'Only native speakers can teach', 0, 6);

-- Question 30: Final assessment - Ultimate challenge
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Arrange these words to make a complex sentence: "because study I English want future my improve to I"',
        (SELECT id FROM dbo.Question_Types WHERE code = 'reorder_words'), 'hard', 15, 1);
DECLARE @Q30_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q30_Id, N'I', 1, 1),
       (@Q30_Id, N'study', 1, 2),
       (@Q30_Id, N'English', 1, 3),
       (@Q30_Id, N'because', 1, 4),
       (@Q30_Id, N'I', 1, 5),
       (@Q30_Id, N'want', 1, 6),
       (@Q30_Id, N'to', 1, 7),
       (@Q30_Id, N'improve', 1, 8),
       (@Q30_Id, N'my', 1, 9),
       (@Q30_Id, N'future', 1, 10);

GO