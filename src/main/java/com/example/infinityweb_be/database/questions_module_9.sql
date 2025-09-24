-- =====================================================
-- MODULE 9: TRANSPORTATION AND DIRECTIONS
-- =====================================================

-- Insert Module 9
INSERT INTO dbo.Modules (course_id, name, description, created_by, [order], duration, status)
VALUES (2, N'Transportation and Directions', N'Getting around and finding your way', 1, 9, N'2-3 weeks', N'active');

DECLARE @Module9_Id INT = SCOPE_IDENTITY();

-- Insert Lessons for Module 9
INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module9_Id, N'Modes of Transportation', N'Different ways to travel',
        N'Learn about transportation: car, bus, train, airplane, bicycle. Public and private transportation.',
        'vocabulary', 1, N'15 minutes', N'active', 1, N'https://example.com/lessons/transportation', N'car');

DECLARE @Lesson1_Id INT = SCOPE_IDENTITY();

INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module9_Id, N'Asking for Directions', N'How to ask for and give directions',
        N'Learn direction phrases: Where is...? How do I get to...? Turn left, turn right, straight ahead.', 'conversation', 2, N'18 minutes',
        N'active', 1, N'https://example.com/lessons/directions', N'compass');

DECLARE @Lesson2_Id INT = SCOPE_IDENTITY();

INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module9_Id, N'Places in the City', N'Common locations and landmarks',
        N'Learn city locations: bank, post office, library, park, hospital, school. Prepositions of place.', 'vocabulary', 3, N'16 minutes', N'active', 1,
        N'https://example.com/lessons/city-places', N'building-2');

DECLARE @Lesson3_Id INT = SCOPE_IDENTITY();

INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module9_Id, N'Reading Maps and Signs', N'Understanding maps and street signs',
        N'Learn to read maps: north, south, east, west. Street signs, traffic lights, pedestrian crossings.', 'vocabulary', 4, N'14 minutes', N'active', 1,
        N'https://example.com/lessons/maps', N'map');

DECLARE @Lesson4_Id INT = SCOPE_IDENTITY();

INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module9_Id, N'Travel and Tourism', N'Vacation and travel vocabulary',
        N'Learn travel vocabulary: hotel, airport, ticket, passport, luggage. Travel conversations and planning.', 'conversation', 5, N'20 minutes', N'active', 1,
        N'https://example.com/lessons/travel', N'plane');

DECLARE @Lesson5_Id INT = SCOPE_IDENTITY();

-- =====================================================
-- QUESTIONS FOR MODULE 9
-- =====================================================

-- LESSON 1: Modes of Transportation
-- Question 1: How do you travel by air?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'How do you travel by air?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q1_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q1_Id, N'Airplane', 1, 1),
       (@Q1_Id, N'Car', 0, 2),
       (@Q1_Id, N'Bus', 0, 3),
       (@Q1_Id, N'Train', 0, 4);

-- Question 2: Complete: "I take the ___ to work every day."
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Complete the sentence: "I take the ___ to work every day."',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 5, 1);
DECLARE @Q2_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q2_Id, N'bus');

-- Question 3: Speaking - Describe how you travel
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Describe how you travel to school or work clearly.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'medium', 8, 1);
DECLARE @Q3_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q3_Id, N'Clear description', 1, 1),
       (@Q3_Id, N'Unclear description', 0, 2);

-- Question 4: Text input - What vehicle has two wheels?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'What vehicle has two wheels?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 5, 1);
DECLARE @Q4_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q4_Id, N'bicycle');

-- Question 5: Select all public transportation
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Select all public transportation:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_multi'), 'medium', 10, 1);
DECLARE @Q5_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q5_Id, N'Bus', 1, 1),
       (@Q5_Id, N'Train', 1, 2),
       (@Q5_Id, N'Subway', 1, 3),
       (@Q5_Id, N'Taxi', 1, 4),
       (@Q5_Id, N'Car', 0, 5),
       (@Q5_Id, N'Bicycle', 0, 6);

-- Question 6: Matching - Match transportation with speed
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Match each transportation with its speed:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'matching'), 'medium', 8, 1);
DECLARE @Q6_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q6_Id, N'Airplane - Fastest', 1, 1),
       (@Q6_Id, N'Car - Fast', 1, 2),
       (@Q6_Id, N'Bicycle - Slow', 1, 3),
       (@Q6_Id, N'Walking - Slowest', 1, 4);

-- LESSON 2: Asking for Directions
-- Question 7: How do you ask where something is?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'How do you ask where something is?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q7_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q7_Id, N'Where is the bank?', 1, 1),
       (@Q7_Id, N'What is the bank?', 0, 2),
       (@Q7_Id, N'Who is the bank?', 0, 3),
       (@Q7_Id, N'When is the bank?', 0, 4);

-- Question 8: Complete: "Turn ___ at the traffic light."
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Complete the sentence: "Turn ___ at the traffic light."',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 5, 1);
DECLARE @Q8_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q8_Id, N'left');

-- Question 9: Speaking - Ask for directions to the hospital
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Ask for directions to the hospital.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'medium', 8, 1);
DECLARE @Q9_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q9_Id, N'Asked directions clearly', 1, 1),
       (@Q9_Id, N'Did not ask clearly', 0, 2);

-- Question 10: Text input - What's the opposite of "turn left"?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'What''s the opposite of "turn left"?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 5, 1);
DECLARE @Q10_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q10_Id, N'turn right');

-- Question 11: Select all direction words
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Select all direction words:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_multi'), 'medium', 10, 1);
DECLARE @Q11_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q11_Id, N'Left', 1, 1),
       (@Q11_Id, N'Right', 1, 2),
       (@Q11_Id, N'Straight', 1, 3),
       (@Q11_Id, N'Forward', 1, 4),
       (@Q11_Id, N'Happy', 0, 5),
       (@Q11_Id, N'Hungry', 0, 6);

-- Question 12: Reorder words - Arrange direction sentence
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Arrange these words: "straight Go ahead"',
        (SELECT id FROM dbo.Question_Types WHERE code = 'reorder_words'), 'medium', 8, 1);
DECLARE @Q12_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q12_Id, N'Go', 1, 1),
       (@Q12_Id, N'straight', 1, 2),
       (@Q12_Id, N'ahead', 1, 3);

-- LESSON 3: Places in the City
-- Question 13: Where do you deposit money?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Where do you deposit money?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q13_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q13_Id, N'Bank', 1, 1),
       (@Q13_Id, N'Library', 0, 2),
       (@Q13_Id, N'Park', 0, 3),
       (@Q13_Id, N'School', 0, 4);

-- Question 14: Complete: "I need to go to the ___ to mail a letter."
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Complete the sentence: "I need to go to the ___ to mail a letter."',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 5, 1);
DECLARE @Q14_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q14_Id, N'post office');

-- Question 15: Speaking - Describe your neighborhood
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Describe what places are in your neighborhood.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'medium', 8, 1);
DECLARE @Q15_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q15_Id, N'Described neighborhood clearly', 1, 1),
       (@Q15_Id, N'Did not describe clearly', 0, 2);

-- Question 16: Text input - Where do you borrow books?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Where do you borrow books?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 5, 1);
DECLARE @Q16_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q16_Id, N'library');

-- Question 17: Select all city places
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Select all city places:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_multi'), 'medium', 10, 1);
DECLARE @Q17_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q17_Id, N'Hospital', 1, 1),
       (@Q17_Id, N'School', 1, 2),
       (@Q17_Id, N'Park', 1, 3),
       (@Q17_Id, N'Museum', 1, 4),
       (@Q17_Id, N'Ocean', 0, 5),
       (@Q17_Id, N'Mountain', 0, 6);

-- Question 18: Matching - Match places with activities
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Match each place with its activity:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'matching'), 'medium', 8, 1);
DECLARE @Q18_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q18_Id, N'Library - Read books', 1, 1),
       (@Q18_Id, N'Hospital - See doctor', 1, 2),
       (@Q18_Id, N'Bank - Get money', 1, 3),
       (@Q18_Id, N'Park - Exercise', 1, 4);

-- LESSON 4: Reading Maps and Signs
-- Question 19: Which direction is opposite to north?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Which direction is opposite to north?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q19_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q19_Id, N'South', 1, 1),
       (@Q19_Id, N'East', 0, 2),
       (@Q19_Id, N'West', 0, 3),
       (@Q19_Id, N'Up', 0, 4);

-- Question 20: Complete: "The red light means ___."
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Complete the sentence: "The red light means ___."',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 5, 1);
DECLARE @Q20_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q20_Id, N'stop');

-- Question 21: Speaking - Give directions using compass directions
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Give directions using north, south, east, west.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'medium', 8, 1);
DECLARE @Q21_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q21_Id, N'Used compass directions correctly', 1, 1),
       (@Q21_Id, N'Did not use compass directions', 0, 2);

-- Question 22: Text input - What do you call the path for people to walk?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'What do you call the path for people to walk?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 5, 1);
DECLARE @Q22_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q22_Id, N'sidewalk');

-- Question 23: Select all compass directions
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Select all compass directions:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_multi'), 'medium', 10, 1);
DECLARE @Q23_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q23_Id, N'North', 1, 1),
       (@Q23_Id, N'South', 1, 2),
       (@Q23_Id, N'East', 1, 3),
       (@Q23_Id, N'West', 1, 4),
       (@Q23_Id, N'Up', 0, 5),
       (@Q23_Id, N'Down', 0, 6);

-- Question 24: Listening - Listen and identify the traffic sound
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Listen to the audio and identify the traffic sound:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'listening'), 'easy', 8, 1);
DECLARE @Q24_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q24_Id, N'Car horn', 1, 1),
       (@Q24_Id, N'Dog barking', 0, 2),
       (@Q24_Id, N'Phone ringing', 0, 3),
       (@Q24_Id, N'Music playing', 0, 4);

-- LESSON 5: Travel and Tourism
-- Question 25: Where do you catch a flight?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Where do you catch a flight?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q25_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q25_Id, N'Airport', 1, 1),
       (@Q25_Id, N'Train station', 0, 2),
       (@Q25_Id, N'Bus stop', 0, 3),
       (@Q25_Id, N'Hospital', 0, 4);

-- Question 26: Complete: "I need to show my ___ at the airport."
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Complete the sentence: "I need to show my ___ at the airport."',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 5, 1);
DECLARE @Q26_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q26_Id, N'passport');

-- Question 27: Speaking - Describe your dream vacation
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Describe your dream vacation destination.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'medium', 8, 1);
DECLARE @Q27_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q27_Id, N'Described vacation clearly', 1, 1),
       (@Q27_Id, N'Did not describe clearly', 0, 2);

-- Question 28: Text input - What do you carry your clothes in when traveling?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'What do you carry your clothes in when traveling?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 5, 1);
DECLARE @Q28_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q28_Id, N'suitcase');

-- Question 29: Select all travel documents
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Select all travel documents:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_multi'), 'medium', 10, 1);
DECLARE @Q29_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q29_Id, N'Passport', 1, 1),
       (@Q29_Id, N'Ticket', 1, 2),
       (@Q29_Id, N'Boarding pass', 1, 3),
       (@Q29_Id, N'Visa', 1, 4),
       (@Q29_Id, N'Sandwich', 0, 5),
       (@Q29_Id, N'Bicycle', 0, 6);

-- Question 30: Reorder words - Arrange travel sentence
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Arrange these words: "to I want travel Paris"',
        (SELECT id FROM dbo.Question_Types WHERE code = 'reorder_words'), 'medium', 8, 1);
DECLARE @Q30_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q30_Id, N'I', 1, 1),
       (@Q30_Id, N'want', 1, 2),
       (@Q30_Id, N'to', 1, 3),
       (@Q30_Id, N'travel', 1, 4),
       (@Q30_Id, N'to', 1, 5),
       (@Q30_Id, N'Paris', 1, 6);

GO