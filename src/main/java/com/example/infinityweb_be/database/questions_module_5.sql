-- Module 5: Home and Environment
INSERT INTO dbo.Modules (course_id, name, description, created_by, [order], duration, status)
VALUES (2, N'Home and Environment', N'Describing your home, rooms, and surroundings', 1, 5, N'2-3 weeks', N'active');
DECLARE @Module5_Id INT = SCOPE_IDENTITY();

-- Lesson 1: Rooms in the House
INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module5_Id, N'Rooms in the House', N'Different rooms and their purposes', N'Room vocabulary: bedroom, bathroom, kitchen, living room, dining room, garage, garden, basement, attic', N'vocabulary', 1, 45, N'active', 1, NULL, N'🏠');
DECLARE @Lesson1_Id INT = SCOPE_IDENTITY();

-- Lesson 2: Furniture and Objects
INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module5_Id, N'Furniture and Objects', N'Common household items and furniture', N'Furniture: bed, table, chair, sofa, wardrobe, bookshelf, desk, lamp, mirror, television, refrigerator', N'vocabulary', 2, 45, N'active', 1, NULL, N'🪑');
DECLARE @Lesson2_Id INT = SCOPE_IDENTITY();

-- Lesson 3: Prepositions of Place
INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module5_Id, N'Prepositions of Place', N'Describing where things are located', N'Location prepositions: in, on, under, behind, in front of, next to, between, beside, above, below', N'grammar', 3, 45, N'active', 1, NULL, N'📍');
DECLARE @Lesson3_Id INT = SCOPE_IDENTITY();

-- Lesson 4: There is/There are
INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module5_Id, N'There is/There are', N'Describing what exists in a place', N'There is/are constructions: There is a bed, There are two chairs, Is there...?, Are there...?, There isn''t, There aren''t', N'grammar', 4, 45, N'active', 1, NULL, N'✅');
DECLARE @Lesson4_Id INT = SCOPE_IDENTITY();

-- Lesson 5: Describing Your Home
INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module5_Id, N'Describing Your Home', N'Talking about your living space', N'Home descriptions: My house has..., In my room there is..., I live in..., My favorite room is...', N'conversation', 5, 45, N'active', 1, NULL, N'🗣️');
DECLARE @Lesson5_Id INT = SCOPE_IDENTITY();

-- Questions for Lesson 1: Rooms in the House

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Where do you cook food?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 10, 1);
DECLARE @Q1_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q1_Id, N'Kitchen', 1, 1),
       (@Q1_Id, N'Bedroom', 0, 2),
       (@Q1_Id, N'Bathroom', 0, 3);
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q1_Id, N'kitchen');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Complete: I sleep in the _______.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 10, 1);
DECLARE @Q2_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q2_Id, N'bedroom');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Where do you take a shower?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 10, 1);
DECLARE @Q3_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q3_Id, N'bathroom');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Match rooms with activities',
        (SELECT id FROM dbo.Question_Types WHERE code = 'matching'), 'medium', 15, 1);
DECLARE @Q4_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q4_Id, N'Kitchen|Cooking', 1, 1),
       (@Q4_Id, N'Living room|Watching TV', 1, 2),
       (@Q4_Id, N'Bedroom|Sleeping', 1, 3);

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Say "living room"',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'easy', 10, 1);
DECLARE @Q5_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q5_Id, N'living room');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Arrange: house / has / my / five / rooms',
        (SELECT id FROM dbo.Question_Types WHERE code = 'reorder_words'), 'medium', 15, 1);
DECLARE @Q6_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q6_Id, N'My', 1, 1),
       (@Q6_Id, N'house', 1, 2),
       (@Q6_Id, N'has', 1, 3),
       (@Q6_Id, N'five', 1, 4),
       (@Q6_Id, N'rooms', 1, 5);

-- Questions for Lesson 2: Furniture and Objects

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'What do you sit on at the dining table?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 10, 1);
DECLARE @Q7_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q7_Id, N'Chair', 1, 1),
       (@Q7_Id, N'Bed', 0, 2),
       (@Q7_Id, N'Table', 0, 3);
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q7_Id, N'chair');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Complete: I put my clothes in the _______.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 10, 1);
DECLARE @Q8_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q8_Id, N'wardrobe');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'What do you use to store books?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 10, 1);
DECLARE @Q9_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q9_Id, N'bookshelf');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Choose the image that shows "sofa"',
        (SELECT id FROM dbo.Question_Types WHERE code = 'single_choice_image'), 'easy', 10, 1);
DECLARE @Q10_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q10_Id, N'image_sofa.jpg', 1, 1),
       (@Q10_Id, N'image_bed.jpg', 0, 2),
       (@Q10_Id, N'image_table.jpg', 0, 3);

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'What furniture do you sleep on?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 10, 1);
DECLARE @Q11_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q11_Id, N'bed');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Say "refrigerator"',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'easy', 10, 1);
DECLARE @Q12_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q12_Id, N'refrigerator');

-- Questions for Lesson 3: Prepositions of Place

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'The book is _____ the table.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 10, 1);
DECLARE @Q13_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q13_Id, N'on', 1, 1),
       (@Q13_Id, N'under', 0, 2),
       (@Q13_Id, N'behind', 0, 3);
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q13_Id, N'on');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Complete: The cat is _____ the box.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 10, 1);
DECLARE @Q14_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q14_Id, N'in');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'What preposition means "next to"?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 10, 1);
DECLARE @Q15_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q15_Id, N'beside');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Select all prepositions of place',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_multi'), 'medium', 15, 1);
DECLARE @Q16_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q16_Id, N'under', 1, 1),
       (@Q16_Id, N'behind', 1, 2),
       (@Q16_Id, N'always', 0, 3),
       (@Q16_Id, N'between', 1, 4);

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Arrange: is / the / dog / under / table / the',
        (SELECT id FROM dbo.Question_Types WHERE code = 'reorder_words'), 'medium', 15, 1);
DECLARE @Q17_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q17_Id, N'The', 1, 1),
       (@Q17_Id, N'dog', 1, 2),
       (@Q17_Id, N'is', 1, 3),
       (@Q17_Id, N'under', 1, 4),
       (@Q17_Id, N'the', 1, 5),
       (@Q17_Id, N'table', 1, 6);

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Say "in front of"',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'easy', 10, 1);
DECLARE @Q18_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q18_Id, N'in front of');

-- Questions for Lesson 4: There is/There are

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Which is correct?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 10, 1);
DECLARE @Q19_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q19_Id, N'There are two chairs', 1, 1),
       (@Q19_Id, N'There is two chairs', 0, 2),
       (@Q19_Id, N'There have two chairs', 0, 3);
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q19_Id, N'there are two chairs');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Complete: _______ a bed in my room.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 10, 1);
DECLARE @Q20_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q20_Id, N'there is');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Make question: There are books on the shelf.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 10, 1);
DECLARE @Q21_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q21_Id, N'are there books on the shelf');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Match with correct form',
        (SELECT id FROM dbo.Question_Types WHERE code = 'matching'), 'medium', 15, 1);
DECLARE @Q22_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q22_Id, N'One lamp|There is', 1, 1),
       (@Q22_Id, N'Three windows|There are', 1, 2),
       (@Q22_Id, N'A table|There is', 1, 3);

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Arrange: there / many / are / books / the / on / shelf',
        (SELECT id FROM dbo.Question_Types WHERE code = 'reorder_words'), 'medium', 15, 1);
DECLARE @Q23_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q23_Id, N'There', 1, 1),
       (@Q23_Id, N'are', 1, 2),
       (@Q23_Id, N'many', 1, 3),
       (@Q23_Id, N'books', 1, 4),
       (@Q23_Id, N'on', 1, 5),
       (@Q23_Id, N'the', 1, 6),
       (@Q23_Id, N'shelf', 1, 7);

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Say "There isn''t a TV in my bedroom"',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'easy', 10, 1);
DECLARE @Q24_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q24_Id, N'there isn''t a tv in my bedroom');

-- Questions for Lesson 5: Describing Your Home

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'How do you start describing your home?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 10, 1);
DECLARE @Q25_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q25_Id, N'My house has...', 1, 1),
       (@Q25_Id, N'My house is has...', 0, 2),
       (@Q25_Id, N'My house have...', 0, 3);
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q25_Id, N'my house has');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Complete: I live _____ an apartment.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 10, 1);
DECLARE @Q26_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q26_Id, N'in');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Describe your favorite room',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 10, 1);
DECLARE @Q27_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q27_Id, N'my favorite room is the bedroom');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Listen and answer: How many bedrooms? (Audio: "My house has three bedrooms")',
        (SELECT id FROM dbo.Question_Types WHERE code = 'listening'), 'easy', 10, 1);
DECLARE @Q28_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q28_Id, N'Three', 1, 1),
       (@Q28_Id, N'Two', 0, 2),
       (@Q28_Id, N'Four', 0, 3);
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q28_Id, N'three');

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Arrange: room / my / favorite / is / the / living / room',
        (SELECT id FROM dbo.Question_Types WHERE code = 'reorder_words'), 'medium', 15, 1);
DECLARE @Q29_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q29_Id, N'My', 1, 1),
       (@Q29_Id, N'favorite', 1, 2),
       (@Q29_Id, N'room', 1, 3),
       (@Q29_Id, N'is', 1, 4),
       (@Q29_Id, N'the', 1, 5),
       (@Q29_Id, N'living', 1, 6),
       (@Q29_Id, N'room', 1, 7);

INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Say "I live in a small house"',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'easy', 10, 1);
DECLARE @Q30_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q30_Id, N'i live in a small house');