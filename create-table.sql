CREATE TABLE user (
       email TEXT NOT NULL PRIMARY KEY,
       password TEXT NOT NULL,
       created_at TEXT,
       learn_time_base INTEGER DEFAULT 0
);

CREATE TABLE card (
       id INTEGER PRIMARY KEY AUTOINCREMENT,
       front_text TEXT NOT NULL,
       back_text TEXT NOT NULL,
       audio TEXT,
       created_at TEXT
);

CREATE TABLE learning_card (
       id INTEGER PRIMARY KEY AUTOINCREMENT,
       card_id INTEGER NOT NULL,
       easy_time INTEGER DEFAULT 0,
       remeber_time INTEGER DEFAULT 0,
       forget_time INTEGER DEFAULT 0,
       next_learn_date TEXT,
       created_at TEXT,
       update_at TEXT
);

CREATE TABLE user_learn_card (
       card_id INTEGER NOT NULL,
       user_email INTEGER NOT NULL,
       learn_time_base INTEGER NOT NULL DEFAULT 0,
       PRIMARY KEY(card_id, user_email)
);

CREATE TABLE user_daily_statistics (
       user_email TEXT NOT NULL,
       date TEXT NOT NULL,
       learn_time INTEGER DEFAULT 0,
       easy_time INTEGER DEFAULT 0,
       remeber_time INTEGER DEFAULT 0,
       forget_time INTEGER DEFAULT 0,
       PRIMARY KEY(user_email, date)
);
