INSERT INTO invite_codes (code)
VALUES ('Afina') ON CONFLICT (code) DO NOTHING;