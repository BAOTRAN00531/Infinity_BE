-- Add sample data for Lexicon_Units
-- Make sure you have run infinityv2.sql first

USE InfinityWeb
GO

-- Check if we have languages
SELECT * FROM dbo.Languages;

-- Insert sample lexicon units
INSERT INTO dbo.Lexicon_Units (text, language_id, ipa, meaning_eng, part_of_speech, type, difficulty)
SELECT 
    'Hello',
    l.id,
    '/həˈloʊ/',
    'A greeting',
    'noun',
    'vocabulary',
    'beginner'
FROM dbo.Languages l WHERE l.code = 'en';

INSERT INTO dbo.Lexicon_Units (text, language_id, ipa, meaning_eng, part_of_speech, type, difficulty)
SELECT 
    'Goodbye',
    l.id,
    '/ˌɡʊdˈbaɪ/',
    'A farewell',
    'noun',
    'vocabulary',
    'beginner'
FROM dbo.Languages l WHERE l.code = 'en';

INSERT INTO dbo.Lexicon_Units (text, language_id, ipa, meaning_eng, part_of_speech, type, difficulty)
SELECT 
    'こんにちは',
    l.id,
    '/konnichiwa/',
    'Hello/Good afternoon',
    'noun',
    'vocabulary',
    'beginner'
FROM dbo.Languages l WHERE l.code = 'ja';

INSERT INTO dbo.Lexicon_Units (text, language_id, ipa, meaning_eng, part_of_speech, type, difficulty)
SELECT 
    'Xin chào',
    l.id,
    '/sin tʃaʊ/',
    'Hello',
    'phrase',
    'vocabulary',
    'beginner'
FROM dbo.Languages l WHERE l.code = 'vi';

-- Insert sample phrases
INSERT INTO dbo.Lexicon_Units (text, language_id, ipa, meaning_eng, part_of_speech, type, difficulty)
SELECT 
    'How are you?',
    l.id,
    '/haʊ ɑːr juː/',
    'A question asking about someone''s well-being',
    'phrase',
    'phrase',
    'beginner'
FROM dbo.Languages l WHERE l.code = 'en';

INSERT INTO dbo.Lexicon_Units (text, language_id, ipa, meaning_eng, part_of_speech, type, difficulty)
SELECT 
    'お元気ですか',
    l.id,
    '/ogenki desu ka/',
    'How are you? (polite)',
    'phrase',
    'phrase',
    'intermediate'
FROM dbo.Languages l WHERE l.code = 'ja';

INSERT INTO dbo.Lexicon_Units (text, language_id, ipa, meaning_eng, part_of_speech, type, difficulty)
SELECT 
    'Bạn khỏe không?',
    l.id,
    '/ban kwe kʰoŋ/',
    'How are you?',
    'phrase',
    'phrase',
    'beginner'
FROM dbo.Languages l WHERE l.code = 'vi';

-- Verify the data
SELECT 
    lu.id,
    lu.text,
    lu.ipa,
    lu.meaning_eng,
    lu.part_of_speech,
    lu.type,
    lu.difficulty,
    l.code as language_code,
    l.name as language_name
FROM dbo.Lexicon_Units lu
JOIN dbo.Languages l ON lu.language_id = l.id
ORDER BY lu.id; 