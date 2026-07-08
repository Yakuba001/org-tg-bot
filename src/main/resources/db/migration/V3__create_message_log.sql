CREATE TABLE message_log
(
    id               BIGSERIAL PRIMARY KEY,
    telegram_chat_id BIGINT      NOT NULL,
    message_id       INTEGER     NOT NULL,
    message_type     VARCHAR(50) NOT NULL,
    created_at       TIMESTAMP   NOT NULL
);

CREATE INDEX idx_message_log_chat ON message_log (telegram_chat_id);