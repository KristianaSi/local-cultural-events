-- Locations
INSERT INTO Location (id, name, address) VALUES
(1, 'City Hall', '123 Main St'),
(2, 'Community Center', '456 Oak Ave');

-- Categories
INSERT INTO Category (id, name) VALUES
(1, 'Music'),
(2, 'Workshop');

-- Events
INSERT INTO Event (id, name, location_id, category_id, date) VALUES
(1, 'Jazz Night', 1, 1, '2025-06-01'),
(2, 'Coding Bootcamp', 2, 2, '2025-07-15');

-- Users
INSERT INTO User (id, name, email) VALUES
(1, 'Alice Johnson', 'alice@example.com'),
(2, 'Bob Smith', 'bob@example.com');

-- Attendance
INSERT INTO Attendance (event_id, user_id, status) VALUES
(1, 1, 'confirmed'),
(1, 2, 'pending'),
(2, 1, 'confirmed');
