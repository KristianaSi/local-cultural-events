CREATE TABLE Location (
    id INT PRIMARY KEY,
    name VARCHAR(255),
    address VARCHAR(255)
);

CREATE TABLE Category (
    id INT PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE Event (
    id INT PRIMARY KEY,
    name VARCHAR(255),
    location_id INT,
    category_id INT,
    date DATE,
    FOREIGN KEY (location_id) REFERENCES Location(id),
    FOREIGN KEY (category_id) REFERENCES Category(id)
);

CREATE TABLE User (
    id INT PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255)
);

CREATE TABLE Attendance (
    event_id INT,
    user_id INT,
    status VARCHAR(50),
    PRIMARY KEY (event_id, user_id),
    FOREIGN KEY (event_id) REFERENCES Event(id),
    FOREIGN KEY (user_id) REFERENCES User(id)
);
