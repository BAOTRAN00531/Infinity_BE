-- Sample data for Lexicon_Units
-- Make sure you have languages in the Languages table first

-- Insert sample languages if not exists
IF NOT EXISTS (SELECT 1 FROM dbo.Languages WHERE code = 'EN')
BEGIN
    INSERT INTO dbo.Languages (code, name, flag, difficulty, popularity)
    VALUES ('EN', 'English', '🇺🇸', 'beginner', 'high');
END

IF NOT EXISTS (SELECT 1 FROM dbo.Languages WHERE code = 'FR')
BEGIN
    INSERT INTO dbo.Languages (code, name, flag, difficulty, popularity)
    VALUES ('FR', 'French', '🇫🇷', 'intermediate', 'medium');
END

IF NOT EXISTS (SELECT 1 FROM dbo.Languages WHERE code = 'JP')
BEGIN
    INSERT INTO dbo.Languages (code, name, flag, difficulty, popularity)
    VALUES ('JP', 'Japanese', '🇯🇵', 'advanced', 'medium');
END

-- Insert sample vocabulary (type = 'vocabulary')
INSERT INTO dbo.Lexicon_Units (text, language_id, ipa, meaning_eng, part_of_speech, type, difficulty)
SELECT 
    'Hello',
    l.id,
    '/həˈloʊ/',
    'A greeting',
    'noun',
    'vocabulary',
    'beginner'
FROM dbo.Languages l WHERE l.code = 'EN';

INSERT INTO dbo.Lexicon_Units (text, language_id, ipa, meaning_eng, part_of_speech, type, difficulty)
SELECT 
    'Goodbye',
    l.id,
    '/ˌɡʊdˈbaɪ/',
    'A farewell',
    'noun',
    'vocabulary',
    'beginner'
FROM dbo.Languages l WHERE l.code = 'EN';

INSERT INTO dbo.Lexicon_Units (text, language_id, ipa, meaning_eng, part_of_speech, type, difficulty)
SELECT 
    'Bonjour',
    l.id,
    '/bɔ̃ʒuʁ/',
    'Good morning/Hello',
    'noun',
    'vocabulary',
    'beginner'
FROM dbo.Languages l WHERE l.code = 'FR';

INSERT INTO dbo.Lexicon_Units (text, language_id, ipa, meaning_eng, part_of_speech, type, difficulty)
SELECT 
    'こんにちは',
    l.id,
    '/konnichiwa/',
    'Hello/Good afternoon',
    'noun',
    'vocabulary',
    'beginner'
FROM dbo.Languages l WHERE l.code = 'JP';

-- Insert sample phrases (type = 'phrase')
INSERT INTO dbo.Lexicon_Units (text, language_id, ipa, meaning_eng, part_of_speech, type, difficulty)
SELECT 
    'How are you?',
    l.id,
    '/haʊ ɑːr juː/',
    'A question asking about someone''s well-being',
    'phrase',
    'phrase',
    'beginner'
FROM dbo.Languages l WHERE l.code = 'EN';

INSERT INTO dbo.Lexicon_Units (text, language_id, ipa, meaning_eng, part_of_speech, type, difficulty)
SELECT 
    'Comment allez-vous?',
    l.id,
    '/kɔmɑ̃ ale vu/',
    'How are you? (formal)',
    'phrase',
    'phrase',
    'intermediate'
FROM dbo.Languages l WHERE l.code = 'FR';

INSERT INTO dbo.Lexicon_Units (text, language_id, ipa, meaning_eng, part_of_speech, type, difficulty)
SELECT 
    'お元気ですか',
    l.id,
    '/ogenki desu ka/',
    'How are you? (polite)',
    'phrase',
    'phrase',
    'intermediate'
FROM dbo.Languages l WHERE l.code = 'JP';

-- Insert more advanced vocabulary
INSERT INTO dbo.Lexicon_Units (text, language_id, ipa, meaning_eng, part_of_speech, type, difficulty)
SELECT 
    'Serendipity',
    l.id,
    '/ˌserənˈdɪpəti/',
    'The occurrence and development of events by chance in a happy or beneficial way',
    'noun',
    'vocabulary',
    'advanced'
FROM dbo.Languages l WHERE l.code = 'EN';

INSERT INTO dbo.Lexicon_Units (text, language_id, ipa, meaning_eng, part_of_speech, type, difficulty)
SELECT 
    'Éphémère',
    l.id,
    '/efemɛʁ/',
    'Short-lived, fleeting',
    'adjective',
    'vocabulary',
    'advanced'
FROM dbo.Languages l WHERE l.code = 'FR';

INSERT INTO dbo.Lexicon_Units (text, language_id, ipa, meaning_eng, part_of_speech, type, difficulty)
SELECT 
    '一期一会',
    l.id,
    '/ichigo ichie/',
    'Once in a lifetime opportunity',
    'idiom',
    'vocabulary',
    'advanced'
FROM dbo.Languages l WHERE l.code = 'JP';



select * from Lexicon_Units;