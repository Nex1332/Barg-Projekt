CREATE TABLE IF NOT EXISTS app_user (
    id BIGSERIAL PRIMARY KEY,
    telegram_user_id BIGINT,
    chat_id BIGINT,
    username VARCHAR(100),
    email VARCHAR(100),
    city VARCHAR(50),
    user_state VARCHAR(50),
    bot_state VARCHAR(50)
);