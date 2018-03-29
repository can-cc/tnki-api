CREATE TABLE user (
       id INTEGER PRIMARY KEY AUTOINCREMENT,
       email TEXT NOT NULL UNIQUE,
       password TEXT NOT NULL,
       created_at TEXT,
       -- learn_time_base INTEGER DEFAULT 0
);

CREATE TABLE user_learn_data (
       email TEXT NOT NULL,
       learn_base_time NOT NULL DEFAULT 1
);

CREATE TABLE card (
       id INTEGER PRIMARY KEY AUTOINCREMENT,
       front_text TEXT NOT NULL,
       front_image TEXT,
       back_text TEXT NOT NULL,
       back_iamge TEXT,
       audio TEXT,
       created_at Date
);

CREATE TABLE learning_card (
       id INTEGER PRIMARY KEY AUTOINCREMENT,
       card_id INTEGER NOT NULL,
       easy_time INTEGER DEFAULT 0,
       remeber_time INTEGER DEFAULT 0,
       forget_time INTEGER DEFAULT 0,
       next_learn_date Date,
       created_at Date,
       update_at Date
);

CREATE TABLE user_learn_card (
       card_id INTEGER NOT NULL,
       user_email INTEGER NOT NULL,
       learn_time_base INTEGER NOT NULL DEFAULT 0,
       PRIMARY KEY(card_id, user_email)
);

CREATE TABLE user_daily_statistics (
       user_email TEXT NOT NULL,
       date Date NOT NULL,
       learn_time INTEGER DEFAULT 0,
       easy_time INTEGER DEFAULT 0,
       remeber_time INTEGER DEFAULT 0,
       forget_time INTEGER DEFAULT 0,
       PRIMARY KEY(user_email, date)
);
