CREATE TABLE invite_codes
(
    id   BIGSERIAL PRIMARY KEY,
    code VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE bookkeeper_records
(
    id               BIGSERIAL PRIMARY KEY,
    telegram_chat_id BIGINT         NOT NULL,
    item_name        VARCHAR(255)   NOT NULL,
    price            NUMERIC(10, 2) NOT NULL,
    purchase_date    DATE           NOT NULL,
    created_at       TIMESTAMP      NOT NULL
);

CREATE TABLE reminders
(
    id               BIGSERIAL PRIMARY KEY,
    telegram_chat_id BIGINT,
    text             VARCHAR(255) NOT NULL,
    target_time      TIMESTAMP    NOT NULL,
    is_sent          BOOLEAN      NOT NULL DEFAULT FALSE
);

CREATE TABLE dates_entry
(
    id   BIGSERIAL PRIMARY KEY,
    date VARCHAR(255) NOT NULL
);

CREATE TABLE report_entry
(
    id         BIGSERIAL PRIMARY KEY,
    day_number INTEGER      NOT NULL,
    route      VARCHAR(255) NOT NULL,
    morning_km INTEGER      NOT NULL,
    evening_km INTEGER      NOT NULL,
    total_km   INTEGER      NOT NULL
);

CREATE TABLE general_entry
(
    id                   BIGSERIAL PRIMARY KEY,
    name                 VARCHAR(255)   NOT NULL,
    date                 VARCHAR(255)   NOT NULL,
    car_model            VARCHAR(255)   NOT NULL,
    car_number           VARCHAR(255)   NOT NULL,
    start_week_probeg    INTEGER        NOT NULL,
    end_week_probeg      INTEGER        NOT NULL,
    start_balance_litres NUMERIC(19, 2) NOT NULL,
    end_balance_litres   NUMERIC(19, 2) NOT NULL,
    total_week_km        INTEGER        NOT NULL,
    fuel_norm            NUMERIC(19, 2) NOT NULL,
    litres_spend         NUMERIC(19, 2) NOT NULL,
    fueling              INTEGER        NOT NULL
);

CREATE TABLE users
(
    id               BIGSERIAL PRIMARY KEY,
    telegram_chat_id BIGINT       NOT NULL UNIQUE,
    name             VARCHAR(255) NOT NULL,
    role             VARCHAR(255) NOT NULL,
    step             VARCHAR(255) NOT NULL
);

CREATE TABLE state_manager
(
    telegram_chat_id        BIGINT PRIMARY KEY,
    current_field           VARCHAR(255) NOT NULL,
    last_bot_menu_id        INTEGER,
    user_last_activity_time TIMESTAMP
);

CREATE TABLE user_workspaces
(
    id               BIGSERIAL PRIMARY KEY,
    user_id          BIGINT NOT NULL UNIQUE,
    general_entry_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_workspace_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_workspace_general FOREIGN KEY (general_entry_id) REFERENCES general_entry (id)
);

CREATE TABLE workspace_dates
(
    workspace_id  BIGINT NOT NULL,
    date_entry_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_wd_workspace FOREIGN KEY (workspace_id) REFERENCES user_workspaces (id),
    CONSTRAINT fk_wd_date FOREIGN KEY (date_entry_id) REFERENCES dates_entry (id)
);

CREATE TABLE workspace_reports
(
    workspace_id    BIGINT NOT NULL,
    report_entry_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_wr_workspace FOREIGN KEY (workspace_id) REFERENCES user_workspaces (id),
    CONSTRAINT fk_wr_report FOREIGN KEY (report_entry_id) REFERENCES report_entry (id)
);