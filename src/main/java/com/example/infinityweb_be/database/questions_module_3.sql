-- Module 3: Family and People
INSERT INTO dbo.Modules (course_id, name, description, created_by, [order], duration, status)
VALUES (2, N'Family and People', N'Describing family members and people', 1, 3, N'2-3 weeks', N'active');
DECLARE @Module3_Id INT = SCOPE_IDENTITY();

-- Lesson 1: Family Members
INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url,
                         icon)
VALUES (@Module3_Id, N'Family Members', N'Learn family vocabulary and relationships',
        N'Basic family terms: mother, father, sister, brother, grandmother, grandfather, aunt, uncle, cousin, parents, children',
        N'vocabulary', 1, 45, N'active', 1, NULL, N'👨‍👩‍👧‍👦');
DECLARE @Lesson1_Id INT = SCOPE_IDENTITY();

-- Lesson 2: Describing Appearance
INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url,
                         icon)
VALUES (@Module3_Id, N'Describing Appearance', N'Physical descriptions and characteristics',
        N'Appearance vocabulary: tall, short, young, old, hair color, eye color, beautiful, handsome', N'vocabulary', 2,
        45, N'active', 1, NULL, N'👤');
DECLARE @Lesson2_Id INT = SCOPE_IDENTITY();

-- Lesson 3: Age and Personality
INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url,
                         icon)
VALUES (@Module3_Id, N'Age and Personality', N'Talking about age and character traits',
        N'Age expressions and personality adjectives: kind, friendly, funny, smart, nice, serious', N'vocabulary', 3,
        45, N'active', 1, NULL, N'😊');
DECLARE @Lesson3_Id INT = SCOPE_IDENTITY();

-- Lesson 4: Possessive Adjectives
INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url,
                         icon)
VALUES (@Module3_Id, N'Possessive Adjectives', N'My, your, his, her family members',
        N'Possessive adjectives: my, your, his, her, our, their + family members', N'grammar', 4, 45, N'active', 1,
        NULL, N'📝');
DECLARE @Lesson4_Id INT = SCOPE_IDENTITY();

-- Lesson 5: Questions About People
INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url,
                         icon)
VALUES (@Module3_Id, N'Questions About People', N'Asking and answering about family and friends',
        N'Question patterns: Who is this? How old is he/she? What does he/she look like?', N'conversation', 5, 45,
        N'active', 1, NULL, N'❓');
DECLARE @Lesson5_Id INT = SCOPE_IDENTITY();

-- Questions for Lesson 1: Family Members

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'What do you call your father''s mother?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 10, 1);
DECLARE @Q1_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q1_Id, N'Grandmother', 1, 1), (@Q1_Id, N'Aunt', 0, 2), (@Q1_Id, N'Sister', 0, 3);
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q1_Id, N'grandmother');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Complete: My _______ are my mother and father.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 10, 1);
DECLARE @Q2_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q2_Id, N'parents');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Your brother''s son is your _______.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 10, 1);
DECLARE @Q3_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q3_Id, N'nephew');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Match the family members',
        (SELECT id FROM dbo.Question_Types WHERE code = 'matching'), 'medium', 15, 1);
DECLARE @Q4_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q4_Id, N'Uncle|Father''s brother', 1, 1), (@Q4_Id, N'Cousin|Uncle''s child', 1, 2), (@Q4_Id, N'Aunt|Mother''s sister', 1, 3);

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Say "sister"',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'easy', 10, 1);
DECLARE @Q5_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q5_Id, N'sister');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Arrange: have / I / two / brothers',
        (SELECT id FROM dbo.Question_Types WHERE code = 'reorder_words'), 'medium', 15, 1);
DECLARE @Q6_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q6_Id, N'I', 1, 1), (@Q6_Id, N'have', 1, 2), (@Q6_Id, N'two', 1, 3), (@Q6_Id, N'brothers', 1, 4);

-- Questions for Lesson 2: Describing Appearance

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Which describes someone who is not short?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 10, 1);
DECLARE @Q7_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q7_Id, N'Tall', 1, 1),
       (@Q7_Id, N'Young', 0, 2),
       (@Q7_Id, N'Kind', 0, 3);
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q7_Id, N'tall');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'She has _______ hair.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 10, 1);
DECLARE @Q8_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q8_Id, N'black', 1, 1),
       (@Q8_Id, N'tall', 0, 2),
       (@Q8_Id, N'young', 0, 3);
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q8_Id, N'black');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'My grandfather is very _______.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 10, 1);
DECLARE @Q9_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q9_Id, N'old');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Choose the image that shows "beautiful"',
        (SELECT id FROM dbo.Question_Types WHERE code = 'single_choice_image'), 'easy', 10, 1);
DECLARE @Q10_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q10_Id, N'image_beautiful.jpg', 1, 1),
       (@Q10_Id, N'image_ugly.jpg', 0, 2),
       (@Q10_Id, N'image_tall.jpg', 0, 3);

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'What color are your eyes?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 10, 1);
DECLARE @Q11_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q11_Id, N'brown');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Say "handsome"',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'easy', 10, 1);
DECLARE @Q12_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q12_Id, N'handsome');

-- Questions for Lesson 3: Age and Personality

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'How do you ask about someone''s age?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 10, 1);
DECLARE @Q13_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q13_Id, N'How old are you?', 1, 1),
       (@Q13_Id, N'What is your age?', 0, 2),
       (@Q13_Id, N'How many years?', 0, 3);
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q13_Id, N'how old are you');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'My sister is very _______. She always helps people.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 10, 1);
DECLARE @Q14_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q14_Id, N'kind');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'He makes everyone laugh. He is _______.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 10, 1);
DECLARE @Q15_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q15_Id, N'funny');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Select all positive personality traits',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_multi'), 'medium', 15, 1);
DECLARE @Q16_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q16_Id, N'Friendly', 1, 1),
       (@Q16_Id, N'Smart', 1, 2),
       (@Q16_Id, N'Mean', 0, 3),
       (@Q16_Id, N'Nice', 1, 4);

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Arrange: is / twenty / old / years / she',
        (SELECT id FROM dbo.Question_Types WHERE code = 'reorder_words'), 'medium', 15, 1);
DECLARE @Q17_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q17_Id, N'She', 1, 1),
       (@Q17_Id, N'is', 1, 2),
       (@Q17_Id, N'twenty', 1, 3),
       (@Q17_Id, N'years', 1, 4),
       (@Q17_Id, N'old', 1, 5);

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Say "friendly"',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'easy', 10, 1);
DECLARE @Q18_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q18_Id, N'friendly');

-- Questions for Lesson 4: Possessive Adjectives

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'_____ sister is a doctor.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 10, 1);
DECLARE @Q19_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q19_Id, N'His', 1, 1),
       (@Q19_Id, N'Him', 0, 2),
       (@Q19_Id, N'He', 0, 3);
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q19_Id, N'his');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'This is _____ mother. (speaking about yourself)',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 10, 1);
DECLARE @Q20_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q20_Id, N'my');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Complete: _____ parents live in New York. (about a woman)',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 10, 1);
DECLARE @Q21_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q21_Id, N'her');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Match possessive adjectives with examples',
        (SELECT id FROM dbo.Question_Types WHERE code = 'matching'), 'medium', 15, 1);
DECLARE @Q22_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q22_Id, N'Our|Our family is big', 1, 1),
       (@Q22_Id, N'Your|Your brother is tall', 1, 2),
       (@Q22_Id, N'Their|Their house is new', 1, 3);

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Arrange: my / is / this / family',
        (SELECT id FROM dbo.Question_Types WHERE code = 'reorder_words'), 'medium', 15, 1);
DECLARE @Q23_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q23_Id, N'This', 1, 1),
       (@Q23_Id, N'is', 1, 2),
       (@Q23_Id, N'my', 1, 3),
       (@Q23_Id, N'family', 1, 4);

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Say "their children"',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'easy', 10, 1);
DECLARE @Q24_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q24_Id, N'their children');

-- Questions for Lesson 5: Questions About People

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'How do you ask about someone''s identity?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 10, 1);
DECLARE @Q25_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q25_Id, N'Who is this?', 1, 1),
       (@Q25_Id, N'What is this?', 0, 2),
       (@Q25_Id, N'Where is this?', 0, 3);
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q25_Id, N'who is this');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Complete the question: What _____ he look like?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 10, 1);
DECLARE @Q26_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q26_Id, N'does');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Answer: How old is your sister?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 10, 1);
DECLARE @Q27_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q27_Id, N'she is twenty years old');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Listen and answer: Who is calling? (Audio: "This is my father")',
        (SELECT id FROM dbo.Question_Types WHERE code = 'listening'), 'easy', 10, 1);
DECLARE @Q28_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q28_Id, N'Father', 1, 1),
       (@Q28_Id, N'Mother', 0, 2),
       (@Q28_Id, N'Brother', 0, 3);
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q28_Id, N'father');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Arrange: does / what / like / she / look',
        (SELECT id FROM dbo.Question_Types WHERE code = 'reorder_words'), 'medium', 15, 1);
DECLARE @Q29_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q29_Id, N'What', 1, 1),
       (@Q29_Id, N'does', 1, 2),
       (@Q29_Id, N'she', 1, 3),
       (@Q29_Id, N'look', 1, 4),
       (@Q29_Id, N'like', 1, 5);

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Say "Who is your best friend?"',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'easy', 10, 1);
DECLARE @Q30_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q30_Id, N'who is your best friend');