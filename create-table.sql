CREATE TABLE user (
       email TEXT NOT NULL,
       password TEXT NOT NULL,
       created_at TEXT
);

CREATE TABLE card (
       id INTEGER PRIMARY KEY AUTOINCREMENT,
       front_text TEXT NOT NULL,
       back_text TEXT NOT NULL,
       audio TEXT,
       learn_time INTEGER,
       created_at TEXT
);

CREATE TABLE learning_card (
       id INTEGER PRIMARY KEY AUTOINCREMENT,
       card_id INTEGER NOT NULL,
       remeber_time INTEGER,
       forget_time INTEGER,
       next_learn_date TEXT,
       created_at TEXT,
       update_at TEXT
);
