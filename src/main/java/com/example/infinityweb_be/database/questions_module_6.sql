-- =====================================================
-- MODULE 6: FOOD AND DRINKS
-- =====================================================

-- Insert Module 6
INSERT INTO dbo.Modules (course_id, name, description, created_by, [order], duration, status)
VALUES (2, N'Food and Drinks', N'Basic food vocabulary and eating habits', 1, 6, N'2-3 weeks', N'active');

DECLARE @Module6_Id INT = SCOPE_IDENTITY();

-- Insert Lessons for Module 6
INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module6_Id, N'Common Foods', N'Basic food vocabulary and food groups',
        N'Learn common foods: fruits, vegetables, meat, dairy, grains. Practice food names and categories.',
        'vocabulary', 1, N'15 minutes', N'active', 1, N'https://example.com/lessons/foods', N'apple');

DECLARE @Lesson1_Id INT = SCOPE_IDENTITY();

INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module6_Id, N'Drinks and Beverages', N'Different types of drinks',
        N'Learn about drinks: water, juice, coffee, tea, milk. Hot and cold beverages.', 'vocabulary', 2, N'12 minutes',
        N'active', 1, N'https://example.com/lessons/drinks', N'coffee');

DECLARE @Lesson2_Id INT = SCOPE_IDENTITY();

INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module6_Id, N'Meals of the Day', N'Breakfast, lunch, and dinner',
        N'Learn about different meals: breakfast, lunch, dinner, snack. Meal times and typical foods.', 'vocabulary', 3, N'10 minutes', N'active', 1,
        N'https://example.com/lessons/meals', N'utensils');

DECLARE @Lesson3_Id INT = SCOPE_IDENTITY();

INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module6_Id, N'Food Preferences', N'Expressing likes and dislikes',
        N'Learn to express food preferences: I like, I don''t like, my favorite food is.', 'grammar', 4, N'18 minutes', N'active', 1,
        N'https://example.com/lessons/preferences', N'heart');

DECLARE @Lesson4_Id INT = SCOPE_IDENTITY();

INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module6_Id, N'At the Restaurant', N'Ordering food and drinks',
        N'Practice restaurant conversations: ordering, asking about food, paying the bill.', 'conversation', 5, N'20 minutes', N'active', 1,
        N'https://example.com/lessons/restaurant', N'chef-hat');

DECLARE @Lesson5_Id INT = SCOPE_IDENTITY();

-- =====================================================
-- QUESTIONS FOR MODULE 6
-- =====================================================

-- LESSON 1: Common Foods
-- Question 1: What fruit is red and round?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'What fruit is red and round?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q1_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q1_Id, N'Apple', 1, 1),
       (@Q1_Id, N'Banana', 0, 2),
       (@Q1_Id, N'Orange', 0, 3),
       (@Q1_Id, N'Grape', 0, 4);

-- Question 2: Complete: "I eat ___ for breakfast."
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Complete the sentence: "I eat ___ for breakfast."',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 5, 1);
DECLARE @Q2_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q2_Id, N'bread');

-- Question 3: Speaking - Describe your favorite food
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Describe your favorite food clearly.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'medium', 8, 1);
DECLARE @Q3_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q3_Id, N'Clear description', 1, 1),
       (@Q3_Id, N'Unclear description', 0, 2);

-- Question 4: Text input - What vegetable is orange and long?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'What vegetable is orange and long?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 5, 1);
DECLARE @Q4_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q4_Id, N'carrot');

-- Question 5: Select all fruits
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Select all fruits:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_multi'), 'medium', 10, 1);
DECLARE @Q5_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q5_Id, N'Apple', 1, 1),
       (@Q5_Id, N'Banana', 1, 2),
       (@Q5_Id, N'Orange', 1, 3),
       (@Q5_Id, N'Grape', 1, 4),
       (@Q5_Id, N'Carrot', 0, 5),
       (@Q5_Id, N'Potato', 0, 6);

-- Question 6: Matching - Match foods with categories
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Match each food with its category:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'matching'), 'medium', 8, 1);
DECLARE @Q6_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q6_Id, N'Apple - Fruit', 1, 1),
       (@Q6_Id, N'Carrot - Vegetable', 1, 2),
       (@Q6_Id, N'Bread - Grain', 1, 3),
       (@Q6_Id, N'Chicken - Meat', 1, 4);

-- LESSON 2: Drinks and Beverages
-- Question 7: What hot drink do people drink in the morning?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'What hot drink do people drink in the morning?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q7_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q7_Id, N'Coffee', 1, 1),
       (@Q7_Id, N'Ice cream', 0, 2),
       (@Q7_Id, N'Cake', 0, 3),
       (@Q7_Id, N'Bread', 0, 4);

-- Question 8: Complete: "I drink ___ when I'm thirsty."
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Complete the sentence: "I drink ___ when I''m thirsty."',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 5, 1);
DECLARE @Q8_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q8_Id, N'water');

-- Question 9: Speaking - Describe your favorite drink
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Describe your favorite drink clearly.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'medium', 8, 1);
DECLARE @Q9_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q9_Id, N'Clear description', 1, 1),
       (@Q9_Id, N'Unclear description', 0, 2);

-- Question 10: Text input - What cold drink is made from fruits?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'What cold drink is made from fruits?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 5, 1);
DECLARE @Q10_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q10_Id, N'juice');

-- Question 11: Select all hot drinks
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Select all hot drinks:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_multi'), 'medium', 10, 1);
DECLARE @Q11_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q11_Id, N'Coffee', 1, 1),
       (@Q11_Id, N'Tea', 1, 2),
       (@Q11_Id, N'Hot chocolate', 1, 3),
       (@Q11_Id, N'Soup', 1, 4),
       (@Q11_Id, N'Ice water', 0, 5),
       (@Q11_Id, N'Cold juice', 0, 6);

-- Question 12: Matching - Match drinks with temperatures
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Match each drink with its temperature:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'matching'), 'medium', 8, 1);
DECLARE @Q12_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q12_Id, N'Coffee - Hot', 1, 1),
       (@Q12_Id, N'Ice water - Cold', 1, 2),
       (@Q12_Id, N'Tea - Hot', 1, 3),
       (@Q12_Id, N'Cold juice - Cold', 1, 4);

-- LESSON 3: Meals of the Day
-- Question 13: What meal do you eat in the morning?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'What meal do you eat in the morning?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q13_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q13_Id, N'Breakfast', 1, 1),
       (@Q13_Id, N'Lunch', 0, 2),
       (@Q13_Id, N'Dinner', 0, 3),
       (@Q13_Id, N'Snack', 0, 4);

-- Question 14: Complete: "I eat ___ at 12:00 PM."
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Complete the sentence: "I eat ___ at 12:00 PM."',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 5, 1);
DECLARE @Q14_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q14_Id, N'lunch');

-- Question 15: Speaking - Describe your meals
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Describe what you eat for each meal clearly.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'medium', 8, 1);
DECLARE @Q15_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q15_Id, N'Clear description', 1, 1),
       (@Q15_Id, N'Unclear description', 0, 2);

-- Question 16: Text input - What meal do you eat in the evening?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'What meal do you eat in the evening?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 5, 1);
DECLARE @Q16_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q16_Id, N'dinner');

-- Question 17: Select all meals of the day
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Select all meals of the day:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_multi'), 'medium', 10, 1);
DECLARE @Q17_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q17_Id, N'Breakfast', 1, 1),
       (@Q17_Id, N'Lunch', 1, 2),
       (@Q17_Id, N'Dinner', 1, 3),
       (@Q17_Id, N'Snack', 1, 4),
       (@Q17_Id, N'Drink', 0, 5),
       (@Q17_Id, N'Food', 0, 6);

-- Question 18: Matching - Match meals with times
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Match each meal with the appropriate time:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'matching'), 'medium', 8, 1);
DECLARE @Q18_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q18_Id, N'Breakfast - Morning', 1, 1),
       (@Q18_Id, N'Lunch - Noon', 1, 2),
       (@Q18_Id, N'Dinner - Evening', 1, 3),
       (@Q18_Id, N'Snack - Anytime', 1, 4);

-- LESSON 4: Food Preferences
-- Question 19: How do you express that you enjoy a food?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'How do you express that you enjoy a food?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q19_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q19_Id, N'I like it', 1, 1),
       (@Q19_Id, N'I hate it', 0, 2),
       (@Q19_Id, N'It is bad', 0, 3),
       (@Q19_Id, N'I don''t eat it', 0, 4);

-- Question 20: Complete: "My favorite food ___"
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Complete the sentence: "My favorite food ___"',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 5, 1);
DECLARE @Q20_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q20_Id, N'is pizza');

-- Question 21: Speaking - Express your food preferences
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Say what foods you like and don''t like.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'medium', 8, 1);
DECLARE @Q21_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q21_Id, N'Clear preferences expressed', 1, 1),
       (@Q21_Id, N'Unclear preferences', 0, 2);

-- Question 22: Text input - What is your favorite food?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'What is your favorite food?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 5, 1);
DECLARE @Q22_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q22_Id, N'pizza');

-- Question 23: Select ways to express food preferences
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Select ways to express food preferences:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_multi'), 'medium', 10, 1);
DECLARE @Q23_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q23_Id, N'I like', 1, 1),
       (@Q23_Id, N'I love', 1, 2),
       (@Q23_Id, N'I don''t like', 1, 3),
       (@Q23_Id, N'I hate', 1, 4),
       (@Q23_Id, N'My name is', 0, 5),
       (@Q23_Id, N'How are you', 0, 6);

-- Question 24: Reorder words - Arrange preference sentence
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Arrange these words: "don''t I vegetables like"',
        (SELECT id FROM dbo.Question_Types WHERE code = 'reorder_words'), 'medium', 8, 1);
DECLARE @Q24_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q24_Id, N'I', 1, 1),
       (@Q24_Id, N'don''t', 1, 2),
       (@Q24_Id, N'like', 1, 3),
       (@Q24_Id, N'vegetables', 1, 4);

-- LESSON 5: At the Restaurant
-- Question 25: What do you say to order food?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'What do you say to order food?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q25_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q25_Id, N'I would like', 1, 1),
       (@Q25_Id, N'I am hungry', 0, 2),
       (@Q25_Id, N'Where is the food', 0, 3),
       (@Q25_Id, N'I don''t want anything', 0, 4);

-- Question 26: Complete restaurant conversation: "Can I ___ you?"
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Complete the restaurant conversation: "Can I ___ you?"',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 5, 1);
DECLARE @Q26_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q26_Id, N'help');

-- Question 27: Speaking - Order food at a restaurant
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Practice ordering food: "I would like a burger, please."',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'medium', 8, 1);
DECLARE @Q27_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q27_Id, N'Clear order', 1, 1),
       (@Q27_Id, N'Unclear order', 0, 2);

-- Question 28: Text input - How do you ask for the bill?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'How do you ask for the bill?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 5, 1);
DECLARE @Q28_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q28_Id, N'Can I have the check please');

-- Question 29: Select polite restaurant phrases
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Select polite restaurant phrases:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_multi'), 'medium', 10, 1);
DECLARE @Q29_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q29_Id, N'Please', 1, 1),
       (@Q29_Id, N'Thank you', 1, 2),
       (@Q29_Id, N'Excuse me', 1, 3),
       (@Q29_Id, N'I would like', 1, 4),
       (@Q29_Id, N'Give me now', 0, 5),
       (@Q29_Id, N'I want this', 0, 6);

-- Question 30: Listening - Listen and identify the restaurant order
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Listen to the audio and identify what is being ordered:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'listening'), 'easy', 8, 1);
DECLARE @Q30_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q30_Id, N'Chicken sandwich', 1, 1),
       (@Q30_Id, N'Beef burger', 0, 2),
       (@Q30_Id, N'Fish and chips', 0, 3),
       (@Q30_Id, N'Vegetable salad', 0, 4);

GO