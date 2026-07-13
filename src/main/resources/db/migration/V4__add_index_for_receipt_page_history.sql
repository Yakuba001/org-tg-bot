CREATE INDEX idx_bookkeeper_records_chat_id_purchase_date_desc
    ON bookkeeper_records (telegram_chat_id, purchase_date DESC);