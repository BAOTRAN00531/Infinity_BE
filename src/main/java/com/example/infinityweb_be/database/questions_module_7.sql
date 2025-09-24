-- =====================================================
-- MODULE 7: SHOPPING AND MONEY
-- =====================================================

-- Insert Module 7
INSERT INTO dbo.Modules (course_id, name, description, created_by, [order], duration, status)
VALUES (2, N'Shopping and Money', N'Learning about shopping, prices, and money', 1, 7, N'2-3 weeks', N'active');

DECLARE @Module7_Id INT = SCOPE_IDENTITY();

-- Insert Lessons for Module 7
INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module7_Id, N'At the Store', N'Common stores and shopping vocabulary',
        N'Learn about different types of stores: supermarket, clothing store, pharmacy, bookstore.',
        'vocabulary', 1, N'15 minutes', N'active', 1, N'https://example.com/lessons/stores', N'store');

DECLARE @Lesson1_Id INT = SCOPE_IDENTITY();

INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module7_Id, N'Money and Prices', N'Numbers, prices, and currency',
        N'Learn about money: dollars, cents, prices, cost. How to ask about prices and understand money amounts.', 'vocabulary', 2, N'12 minutes',
        N'active', 1, N'https://example.com/lessons/money', N'dollar-sign');

DECLARE @Lesson2_Id INT = SCOPE_IDENTITY();

INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module7_Id, N'Shopping Conversations', N'Asking for help and making purchases',
        N'Learn shopping phrases: Can I help you? How much is this? I would like to buy. Where is the...?', 'conversation', 3, N'18 minutes', N'active', 1,
        N'https://example.com/lessons/shopping-talk', N'shopping-cart');

DECLARE @Lesson3_Id INT = SCOPE_IDENTITY();

INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module7_Id, N'Clothing and Sizes', N'Clothes, colors, and sizes',
        N'Learn clothing vocabulary: shirt, pants, shoes, dress. Sizes: small, medium, large. Colors and descriptions.', 'vocabulary', 4, N'16 minutes', N'active', 1,
        N'https://example.com/lessons/clothing', N'shirt');

DECLARE @Lesson4_Id INT = SCOPE_IDENTITY();

INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module7_Id, N'Payment and Receipt', N'Paying and completing purchases',
        N'Learn about payment: cash, credit card, receipt. Payment phrases: Here is my card, cash or credit?, thank you.', 'conversation', 5, N'14 minutes', N'active', 1,
        N'https://example.com/lessons/payment', N'credit-card');

DECLARE @Lesson5_Id INT = SCOPE_IDENTITY();

-- =====================================================
-- QUESTIONS FOR MODULE 7
-- =====================================================

-- LESSON 1: At the Store
-- Question 1: Where do you buy food?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Where do you buy food?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q1_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q1_Id, N'Supermarket', 1, 1),
       (@Q1_Id, N'Library', 0, 2),
       (@Q1_Id, N'Hospital', 0, 3),
       (@Q1_Id, N'School', 0, 4);

-- Question 2: Complete: "I buy books at the ___"
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Complete the sentence: "I buy books at the ___"',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 5, 1);
DECLARE @Q2_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q2_Id, N'bookstore');

-- Question 3: Speaking - Name different types of stores
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Name three different types of stores.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'medium', 8, 1);
DECLARE @Q3_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q3_Id, N'Named three stores clearly', 1, 1),
       (@Q3_Id, N'Could not name three stores', 0, 2);

-- Question 4: Text input - Where do you buy medicine?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Where do you buy medicine?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 5, 1);
DECLARE @Q4_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q4_Id, N'pharmacy');

-- Question 5: Select all places where you can shop
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Select all places where you can shop:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_multi'), 'medium', 10, 1);
DECLARE @Q5_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q5_Id, N'Supermarket', 1, 1),
       (@Q5_Id, N'Clothing store', 1, 2),
       (@Q5_Id, N'Pharmacy', 1, 3),
       (@Q5_Id, N'Mall', 1, 4),
       (@Q5_Id, N'Hospital', 0, 5),
       (@Q5_Id, N'Library', 0, 6);

-- Question 6: Matching - Match items with stores
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Match each item with the store where you buy it:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'matching'), 'medium', 8, 1);
DECLARE @Q6_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q6_Id, N'Bread - Bakery', 1, 1),
       (@Q6_Id, N'Medicine - Pharmacy', 1, 2),
       (@Q6_Id, N'Books - Bookstore', 1, 3),
       (@Q6_Id, N'Clothes - Clothing store', 1, 4);

-- LESSON 2: Money and Prices
-- Question 7: How much is $5.50?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'How do you say $5.50?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q7_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q7_Id, N'Five dollars and fifty cents', 1, 1),
       (@Q7_Id, N'Five and five dollars', 0, 2),
       (@Q7_Id, N'Fifty five dollars', 0, 3),
       (@Q7_Id, N'Five five zero', 0, 4);

-- Question 8: Complete: "How much does this ___?"
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Complete the question: "How much does this ___?"',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 5, 1);
DECLARE @Q8_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q8_Id, N'cost');

-- Question 9: Speaking - Ask about the price of something
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Ask "How much is this shirt?"',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'medium', 8, 1);
DECLARE @Q9_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q9_Id, N'Asked about price clearly', 1, 1),
       (@Q9_Id, N'Did not ask clearly', 0, 2);

-- Question 10: Text input - What do you call the money you get back?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'What do you call the money you get back when you pay too much?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 5, 1);
DECLARE @Q10_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q10_Id, N'change');

-- Question 11: Select all ways to ask about price
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Select all ways to ask about price:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_multi'), 'medium', 10, 1);
DECLARE @Q11_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q11_Id, N'How much is this?', 1, 1),
       (@Q11_Id, N'What does this cost?', 1, 2),
       (@Q11_Id, N'How much does this cost?', 1, 3),
       (@Q11_Id, N'What is the price?', 1, 4),
       (@Q11_Id, N'What time is it?', 0, 5),
       (@Q11_Id, N'Where is this?', 0, 6);

-- Question 12: Reorder words - Arrange price question
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Arrange these words: "is How this much"',
        (SELECT id FROM dbo.Question_Types WHERE code = 'reorder_words'), 'medium', 8, 1);
DECLARE @Q12_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q12_Id, N'How', 1, 1),
       (@Q12_Id, N'much', 1, 2),
       (@Q12_Id, N'is', 1, 3),
       (@Q12_Id, N'this', 1, 4);

-- LESSON 3: Shopping Conversations
-- Question 13: What do you say when you enter a store?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'What might a store employee say when you enter?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q13_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q13_Id, N'Can I help you?', 1, 1),
       (@Q13_Id, N'Go away please', 0, 2),
       (@Q13_Id, N'We are closed', 0, 3),
       (@Q13_Id, N'You cannot buy anything', 0, 4);

-- Question 14: Complete: "I would like to ___ this shirt."
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Complete the sentence: "I would like to ___ this shirt."',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 5, 1);
DECLARE @Q14_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q14_Id, N'buy');

-- Question 15: Speaking - Ask where something is in a store
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Ask "Where is the milk?" in a store.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'medium', 8, 1);
DECLARE @Q15_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q15_Id, N'Asked location clearly', 1, 1),
       (@Q15_Id, N'Did not ask clearly', 0, 2);

-- Question 16: Text input - How do you ask for help in a store?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'How do you ask for help in a store?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 5, 1);
DECLARE @Q16_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q16_Id, N'Can you help me');

-- Question 17: Select polite shopping phrases
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Select polite shopping phrases:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_multi'), 'medium', 10, 1);
DECLARE @Q17_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q17_Id, N'Excuse me', 1, 1),
       (@Q17_Id, N'Please', 1, 2),
       (@Q17_Id, N'Thank you', 1, 3),
       (@Q17_Id, N'I would like', 1, 4),
       (@Q17_Id, N'Give me that', 0, 5),
       (@Q17_Id, N'I want now', 0, 6);

-- Question 18: Listening - Listen and identify what the customer wants
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Listen to the audio and identify what the customer wants:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'listening'), 'easy', 8, 1);
DECLARE @Q18_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q18_Id, N'To buy a jacket', 1, 1),
       (@Q18_Id, N'To return shoes', 0, 2),
       (@Q18_Id, N'To ask directions', 0, 3),
       (@Q18_Id, N'To complain', 0, 4);

-- LESSON 4: Clothing and Sizes
-- Question 19: What size comes between small and large?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'What size comes between small and large?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q19_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q19_Id, N'Medium', 1, 1),
       (@Q19_Id, N'Extra small', 0, 2),
       (@Q19_Id, N'Extra large', 0, 3),
       (@Q19_Id, N'Tiny', 0, 4);

-- Question 20: Complete: "I wear size ___ shirt."
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Complete the sentence: "I wear size ___ shirt."',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 5, 1);
DECLARE @Q20_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q20_Id, N'medium');

-- Question 21: Speaking - Describe what you are wearing
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Describe what you are wearing today.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'medium', 8, 1);
DECLARE @Q21_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q21_Id, N'Described clothing clearly', 1, 1),
       (@Q21_Id, N'Did not describe clearly', 0, 2);

-- Question 22: Text input - What do you wear on your feet?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'What do you wear on your feet?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 5, 1);
DECLARE @Q22_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q22_Id, N'shoes');

-- Question 23: Select all clothing items
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Select all clothing items:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_multi'), 'medium', 10, 1);
DECLARE @Q23_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q23_Id, N'Shirt', 1, 1),
       (@Q23_Id, N'Pants', 1, 2),
       (@Q23_Id, N'Shoes', 1, 3),
       (@Q23_Id, N'Hat', 1, 4),
       (@Q23_Id, N'Apple', 0, 5),
       (@Q23_Id, N'Chair', 0, 6);

-- Question 24: Matching - Match clothing with body parts
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Match each clothing item with the body part:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'matching'), 'medium', 8, 1);
DECLARE @Q24_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q24_Id, N'Hat - Head', 1, 1),
       (@Q24_Id, N'Shirt - Chest', 1, 2),
       (@Q24_Id, N'Pants - Legs', 1, 3),
       (@Q24_Id, N'Shoes - Feet', 1, 4);

-- LESSON 5: Payment and Receipt
-- Question 25: How do you pay with a credit card?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'What do you say when paying with a credit card?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q25_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q25_Id, N'I will pay by card', 1, 1),
       (@Q25_Id, N'I have no money', 0, 2),
       (@Q25_Id, N'I do not want this', 0, 3),
       (@Q25_Id, N'This is too expensive', 0, 4);

-- Question 26: Complete: "Here is my ___"
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Complete the sentence when paying: "Here is my ___"',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 5, 1);
DECLARE @Q26_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q26_Id, N'card');

-- Question 27: Speaking - Ask for a receipt
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Ask for a receipt: "Can I have a receipt, please?"',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'medium', 8, 1);
DECLARE @Q27_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q27_Id, N'Asked for receipt politely', 1, 1),
       (@Q27_Id, N'Did not ask politely', 0, 2);

-- Question 28: Text input - What shows what you bought and how much you paid?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'What shows what you bought and how much you paid?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 5, 1);
DECLARE @Q28_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q28_Id, N'receipt');

-- Question 29: Select all payment methods
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Select all payment methods:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_multi'), 'medium', 10, 1);
DECLARE @Q29_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q29_Id, N'Cash', 1, 1),
       (@Q29_Id, N'Credit card', 1, 2),
       (@Q29_Id, N'Debit card', 1, 3),
       (@Q29_Id, N'Check', 1, 4),
       (@Q29_Id, N'Smile', 0, 5),
       (@Q29_Id, N'Promise', 0, 6);

-- Question 30: Reorder words - Arrange payment sentence
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Arrange these words: "cash or Credit card"',
        (SELECT id FROM dbo.Question_Types WHERE code = 'reorder_words'), 'medium', 8, 1);
DECLARE @Q30_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q30_Id, N'Credit', 1, 1),
       (@Q30_Id, N'card', 1, 2),
       (@Q30_Id, N'or', 1, 3),
       (@Q30_Id, N'cash', 1, 4);

GO