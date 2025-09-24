-- =====================================================
-- MODULE 8: WORK AND ACTIVITIES
-- =====================================================

-- Insert Module 8
INSERT INTO dbo.Modules (course_id, name, description, created_by, [order], duration, status)
VALUES (2, N'Work and Activities', N'Jobs, occupations, and daily activities', 1, 8, N'2-3 weeks', N'active');

DECLARE @Module8_Id INT = SCOPE_IDENTITY();

-- Insert Lessons for Module 8
INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module8_Id, N'Jobs and Occupations', N'Common jobs and professions',
        N'Learn about different jobs: teacher, doctor, engineer, nurse, chef. What people do at work.',
        'vocabulary', 1, N'15 minutes', N'active', 1, N'https://example.com/lessons/jobs', N'briefcase');

DECLARE @Lesson1_Id INT = SCOPE_IDENTITY();

INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module8_Id, N'Daily Activities', N'Everyday actions and routines',
        N'Learn about daily activities: work, study, eat, sleep, exercise. Present simple for daily routines.', 'vocabulary', 2, N'14 minutes',
        N'active', 1, N'https://example.com/lessons/activities', N'clock');

DECLARE @Lesson2_Id INT = SCOPE_IDENTITY();

INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module8_Id, N'At the Office', N'Workplace vocabulary and situations',
        N'Learn office vocabulary: computer, desk, meeting, email. Office conversations and workplace etiquette.', 'vocabulary', 3, N'16 minutes', N'active', 1,
        N'https://example.com/lessons/office', N'building');

DECLARE @Lesson3_Id INT = SCOPE_IDENTITY();

INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module8_Id, N'Hobbies and Interests', N'Free time activities and hobbies',
        N'Learn about hobbies: reading, sports, music, cooking. Expressing preferences and talking about free time.', 'vocabulary', 4, N'18 minutes', N'active', 1,
        N'https://example.com/lessons/hobbies', N'game-controller');

DECLARE @Lesson4_Id INT = SCOPE_IDENTITY();

INSERT INTO dbo.Lessons (module_id, name, description, content, type, [order], duration, status, created_by, video_url, icon)
VALUES (@Module8_Id, N'Time and Schedules', N'Telling time and talking about schedules',
        N'Learn to tell time: hours, minutes, AM/PM. Daily schedules and time expressions: in the morning, at night.', 'grammar', 5, N'20 minutes', N'active', 1,
        N'https://example.com/lessons/time', N'calendar');

DECLARE @Lesson5_Id INT = SCOPE_IDENTITY();

-- =====================================================
-- QUESTIONS FOR MODULE 8
-- =====================================================

-- LESSON 1: Jobs and Occupations
-- Question 1: Who teaches students in school?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Who teaches students in school?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q1_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q1_Id, N'Teacher', 1, 1),
       (@Q1_Id, N'Doctor', 0, 2),
       (@Q1_Id, N'Engineer', 0, 3),
       (@Q1_Id, N'Lawyer', 0, 4);

-- Question 2: Complete: "A ___ helps sick people."
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Complete the sentence: "A ___ helps sick people."',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 5, 1);
DECLARE @Q2_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q2_Id, N'doctor');

-- Question 3: Speaking - Describe your dream job
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Describe your dream job clearly.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'medium', 8, 1);
DECLARE @Q3_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q3_Id, N'Clear description', 1, 1),
       (@Q3_Id, N'Unclear description', 0, 2);

-- Question 4: Text input - What job involves cooking food?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'What job involves cooking food?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 5, 1);
DECLARE @Q4_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q4_Id, N'chef');

-- Question 5: Select all medical professions
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Select all medical professions:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_multi'), 'medium', 10, 1);
DECLARE @Q5_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q5_Id, N'Doctor', 1, 1),
       (@Q5_Id, N'Nurse', 1, 2),
       (@Q5_Id, N'Dentist', 1, 3),
       (@Q5_Id, N'Pharmacist', 1, 4),
       (@Q5_Id, N'Teacher', 0, 5),
       (@Q5_Id, N'Engineer', 0, 6);

-- Question 6: Matching - Match jobs with workplaces
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson1_Id, N'Match each job with its workplace:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'matching'), 'medium', 8, 1);
DECLARE @Q6_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q6_Id, N'Teacher - School', 1, 1),
       (@Q6_Id, N'Doctor - Hospital', 1, 2),
       (@Q6_Id, N'Chef - Restaurant', 1, 3),
       (@Q6_Id, N'Engineer - Office', 1, 4);

-- LESSON 2: Daily Activities
-- Question 7: What do you do in the morning?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'What is a common morning activity?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q7_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q7_Id, N'Wake up', 1, 1),
       (@Q7_Id, N'Go to sleep', 0, 2),
       (@Q7_Id, N'Have dinner', 0, 3),
       (@Q7_Id, N'Watch TV', 0, 4);

-- Question 8: Complete: "I ___ to work every day."
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Complete the sentence: "I ___ to work every day."',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 5, 1);
DECLARE @Q8_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q8_Id, N'go');

-- Question 9: Speaking - Describe your daily routine
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Describe what you do in a typical day.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'medium', 8, 1);
DECLARE @Q9_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q9_Id, N'Described routine clearly', 1, 1),
       (@Q9_Id, N'Did not describe clearly', 0, 2);

-- Question 10: Text input - What activity do you do when you're tired?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'What activity do you do when you''re tired?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 5, 1);
DECLARE @Q10_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q10_Id, N'sleep');

-- Question 11: Select all daily activities
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Select all daily activities:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_multi'), 'medium', 10, 1);
DECLARE @Q11_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q11_Id, N'Eat breakfast', 1, 1),
       (@Q11_Id, N'Brush teeth', 1, 2),
       (@Q11_Id, N'Take shower', 1, 3),
       (@Q11_Id, N'Go to work', 1, 4),
       (@Q11_Id, N'Fly to moon', 0, 5),
       (@Q11_Id, N'Become invisible', 0, 6);

-- Question 12: Reorder words - Arrange daily routine
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson2_Id, N'Arrange these words: "breakfast I eat morning the in"',
        (SELECT id FROM dbo.Question_Types WHERE code = 'reorder_words'), 'medium', 8, 1);
DECLARE @Q12_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q12_Id, N'I', 1, 1),
       (@Q12_Id, N'eat', 1, 2),
       (@Q12_Id, N'breakfast', 1, 3),
       (@Q12_Id, N'in', 1, 4),
       (@Q12_Id, N'the', 1, 5),
       (@Q12_Id, N'morning', 1, 6);

-- LESSON 3: At the Office
-- Question 13: What do you use to type emails?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'What do you use to type emails?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q13_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q13_Id, N'Computer', 1, 1),
       (@Q13_Id, N'Spoon', 0, 2),
       (@Q13_Id, N'Bicycle', 0, 3),
       (@Q13_Id, N'Apple', 0, 4);

-- Question 14: Complete: "We have a ___ at 3 PM."
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Complete the sentence: "We have a ___ at 3 PM."',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 5, 1);
DECLARE @Q14_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q14_Id, N'meeting');

-- Question 15: Speaking - Describe your workspace
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Describe your ideal workspace.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'medium', 8, 1);
DECLARE @Q15_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q15_Id, N'Described workspace clearly', 1, 1),
       (@Q15_Id, N'Did not describe clearly', 0, 2);

-- Question 16: Text input - Where do you sit at work?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Where do you sit at work?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 5, 1);
DECLARE @Q16_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q16_Id, N'desk');

-- Question 17: Select all office equipment
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Select all office equipment:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_multi'), 'medium', 10, 1);
DECLARE @Q17_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q17_Id, N'Computer', 1, 1),
       (@Q17_Id, N'Printer', 1, 2),
       (@Q17_Id, N'Phone', 1, 3),
       (@Q17_Id, N'Desk', 1, 4),
       (@Q17_Id, N'Swimming pool', 0, 5),
       (@Q17_Id, N'Elephant', 0, 6);

-- Question 18: Listening - Listen and identify the office sound
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson3_Id, N'Listen to the audio and identify the office sound:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'listening'), 'easy', 8, 1);
DECLARE @Q18_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q18_Id, N'Printer printing', 1, 1),
       (@Q18_Id, N'Dog barking', 0, 2),
       (@Q18_Id, N'Car honking', 0, 3),
       (@Q18_Id, N'Birds singing', 0, 4);

-- LESSON 4: Hobbies and Interests
-- Question 19: What is a common hobby?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'What is a common hobby?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q19_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q19_Id, N'Reading', 1, 1),
       (@Q19_Id, N'Working', 0, 2),
       (@Q19_Id, N'Sleeping', 0, 3),
       (@Q19_Id, N'Eating', 0, 4);

-- Question 20: Complete: "I like to ___ music."
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Complete the sentence: "I like to ___ music."',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 5, 1);
DECLARE @Q20_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q20_Id, N'listen to');

-- Question 21: Speaking - Talk about your hobbies
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Tell me about your favorite hobbies.',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'medium', 8, 1);
DECLARE @Q21_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q21_Id, N'Talked about hobbies clearly', 1, 1),
       (@Q21_Id, N'Did not talk clearly', 0, 2);

-- Question 22: Text input - What hobby involves using a camera?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'What hobby involves using a camera?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 5, 1);
DECLARE @Q22_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q22_Id, N'photography');

-- Question 23: Select all sports hobbies
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Select all sports hobbies:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_multi'), 'medium', 10, 1);
DECLARE @Q23_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q23_Id, N'Tennis', 1, 1),
       (@Q23_Id, N'Soccer', 1, 2),
       (@Q23_Id, N'Swimming', 1, 3),
       (@Q23_Id, N'Basketball', 1, 4),
       (@Q23_Id, N'Cooking', 0, 5),
       (@Q23_Id, N'Painting', 0, 6);

-- Question 24: Matching - Match hobbies with equipment
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson4_Id, N'Match each hobby with its equipment:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'matching'), 'medium', 8, 1);
DECLARE @Q24_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q24_Id, N'Photography - Camera', 1, 1),
       (@Q24_Id, N'Tennis - Racket', 1, 2),
       (@Q24_Id, N'Painting - Brush', 1, 3),
       (@Q24_Id, N'Reading - Book', 1, 4);

-- LESSON 5: Time and Schedules
-- Question 25: What time is 3:30?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'How do you say 3:30?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_single'), 'easy', 5, 1);
DECLARE @Q25_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q25_Id, N'Three thirty', 1, 1),
       (@Q25_Id, N'Three thirteen', 0, 2),
       (@Q25_Id, N'Thirty three', 0, 3),
       (@Q25_Id, N'Three three', 0, 4);

-- Question 26: Complete: "I wake up at 7 ___ the morning."
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Complete the sentence: "I wake up at 7 ___ the morning."',
        (SELECT id FROM dbo.Question_Types WHERE code = 'fill_in_the_blank'), 'easy', 5, 1);
DECLARE @Q26_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q26_Id, N'in');

-- Question 27: Speaking - Tell the current time
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'What time is it now? (Look at a clock)',
        (SELECT id FROM dbo.Question_Types WHERE code = 'speaking'), 'medium', 8, 1);
DECLARE @Q27_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q27_Id, N'Told time correctly', 1, 1),
       (@Q27_Id, N'Did not tell time correctly', 0, 2);

-- Question 28: Text input - What do you call the middle of the day?
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'What do you call the middle of the day?',
        (SELECT id FROM dbo.Question_Types WHERE code = 'text_input'), 'easy', 5, 1);
DECLARE @Q28_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Answers (question_id, answer_text)
VALUES (@Q28_Id, N'noon');

-- Question 29: Select all time expressions
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Select all time expressions:',
        (SELECT id FROM dbo.Question_Types WHERE code = 'multiple_choice_multi'), 'medium', 10, 1);
DECLARE @Q29_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q29_Id, N'In the morning', 1, 1),
       (@Q29_Id, N'At night', 1, 2),
       (@Q29_Id, N'In the afternoon', 1, 3),
       (@Q29_Id, N'At midnight', 1, 4),
       (@Q29_Id, N'Under the table', 0, 5),
       (@Q29_Id, N'Behind the door', 0, 6);

-- Question 30: Reorder words - Arrange time sentence
INSERT INTO dbo.Questions (course_id, lesson_id, question_text, question_type_id, difficulty, points, created_by)
VALUES (2, @Lesson5_Id, N'Arrange these words: "at I work go 9 to"',
        (SELECT id FROM dbo.Question_Types WHERE code = 'reorder_words'), 'medium', 8, 1);
DECLARE @Q30_Id INT = SCOPE_IDENTITY();
INSERT INTO dbo.Question_Options (question_id, option_text, is_correct, position)
VALUES (@Q30_Id, N'I', 1, 1),
       (@Q30_Id, N'go', 1, 2),
       (@Q30_Id, N'to', 1, 3),
       (@Q30_Id, N'work', 1, 4),
       (@Q30_Id, N'at', 1, 5),
       (@Q30_Id, N'9', 1, 6);

GO