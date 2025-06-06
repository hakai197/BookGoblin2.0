CREATE TABLE users (
    user_id SERIAL,
    username varchar(50) NOT NULL UNIQUE,
    password_hash varchar(200) NOT NULL,
    role varchar(50) NOT NULL,
    CONSTRAINT PK_user PRIMARY KEY (user_id)
);
CREATE TABLE books (
    book_id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(100) NOT NULL,
    isbn VARCHAR(20) UNIQUE,
    cover_image_url TEXT,
    publication_year INTEGER
);

-- Speed up searches by title/author
CREATE INDEX idx_books_title ON books(title);
CREATE INDEX idx_books_author ON books(author);

CREATE TABLE user_books (
    user_book_id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    book_id INTEGER REFERENCES books(book_id) ON DELETE CASCADE,
    date_added DATE DEFAULT CURRENT_DATE,
    is_owned BOOLEAN DEFAULT TRUE,
    current_status VARCHAR(20) CHECK (current_status IN ('unread', 'reading', 'finished', 'dnf')),  -- "dnf" = Did Not Finish
    UNIQUE (user_id, book_id)  -- Prevent duplicate entries for the same user/book
);

-- Optimize user-specific queries
CREATE INDEX idx_user_books_user ON user_books(user_id);
CREATE INDEX idx_user_books_book ON user_books(book_id);

CREATE TABLE reading_logs (
    log_id SERIAL PRIMARY KEY,
    user_book_id INTEGER REFERENCES user_books(user_book_id) ON DELETE CASCADE,
    start_date DATE NOT NULL DEFAULT CURRENT_DATE,
    end_date DATE,  -- NULL = currently reading
    rating SMALLINT CHECK (rating BETWEEN 1 AND 5 OR rating IS NULL),  -- Optional 1-5 scale
    notes TEXT      -- Free-text thoughts
);

-- Speed up "show all reads for a book" queries
CREATE INDEX idx_reading_logs_user_book ON reading_logs(user_book_id);

CREATE TABLE tags (
    tag_id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL  -- e.g., "Fantasy", "Signed", "To Re-Read"
);

CREATE TABLE book_tags (
    book_tag_id SERIAL PRIMARY KEY,
    book_id INTEGER REFERENCES books(book_id) ON DELETE CASCADE,
    tag_id INTEGER REFERENCES tags(tag_id) ON DELETE CASCADE,
    UNIQUE (book_id, tag_id)  -- Prevent duplicate tags per book
);

-- Optimize tag-based filtering
CREATE INDEX idx_book_tags_book ON book_tags(book_id);
CREATE INDEX idx_book_tags_tag ON book_tags(tag_id);

-- Insert Users
-- Password for all users is password
INSERT INTO users (username, password_hash, role) VALUES
('user', '$2a$10$tmxuYYg1f5T0eXsTPlq/V.DJUKmRHyFbJ.o.liI1T35TFbjs2xiem', 'ROLE_USER'),
('admin', '$2a$10$tmxuYYg1f5T0eXsTPlq/V.DJUKmRHyFbJ.o.liI1T35TFbjs2xiem', 'ROLE_ADMIN');