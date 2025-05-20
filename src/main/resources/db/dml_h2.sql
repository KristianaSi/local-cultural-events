-- Locations
INSERT INTO Locations (location_id, name, address) VALUES 
(1, 'Main Hall', '123 Center St'),
(2, 'Open Air Stage', '456 Park Blvd'),
(3, 'Downtown Club', '789 Urban Rd');

-- Categorys
INSERT INTO Categorys (category_id, name) VALUES 
(1, 'Music'),
(2, 'Art'),
(3, 'Tech');

-- Events
INSERT INTO Events (event_id, name, location_id, category_id, date) VALUES 
(1, 'Rock Concert', 1, 1, '2025-06-21'),
(2, 'Gallery Opening', 2, 2, '2025-07-05'),
(3, 'Startup Pitch Night', 3, 3, '2025-08-12');

-- Users
INSERT INTO Users (user_id, name, email) VALUES 
(1, 'Alice Smith', 'alice@example.com'),
(2, 'Bob Johnson', 'bob@example.com'),
(3, 'Charlie Brown', 'charlie@example.com');

-- Attendances
INSERT INTO Attendances (event_id, user_id, status) VALUES 
(1, 1, 'Attending'),
(2, 2, 'Interested'),
(3, 3, 'Not Attending'),
(1, 3, 'Attending');
