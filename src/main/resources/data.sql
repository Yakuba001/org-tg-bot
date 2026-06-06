INSERT INTO invite_codes (code)
VALUES ('Афина') ON CONFLICT (code) DO NOTHING;