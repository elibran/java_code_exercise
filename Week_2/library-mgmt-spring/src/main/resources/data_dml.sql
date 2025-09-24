-- Insert sample books
INSERT INTO books (book_id, title, author, isbn, is_available) VALUES
('B001', 'The Java Complete Reference', 'Herbert Schildt', '978-1260440232', true),
('B002', 'Effective Java', 'Joshua Bloch', '978-0134685991', true),
('B003', 'Head First Java', 'Kathy Sierra', '978-0596009205', true),
('B004', 'Java Concurrency in Practice', 'Brian Goetz', '978-0321349606', true);

-- Insert sample members
INSERT INTO members (member_id, name, email) VALUES
('M001', 'Rahul', 'rahul@dummymail.com'),
('M002', 'Abinash', 'abinash@dummymail.com'),
('M003', 'Arun', 'arun@dummymail.com');