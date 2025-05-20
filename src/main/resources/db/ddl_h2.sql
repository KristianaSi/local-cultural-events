DROP TABLE IF EXISTS Attendances;
DROP TABLE IF EXISTS Events;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Categorys;
DROP TABLE IF EXISTS Locations;

CREATE TABLE IF NOT EXISTS Locations (
    location_id INT PRIMARY KEY,
    name VARCHAR(255),
    address VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS Categorys (
    category_id INT PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS Events (
    event_id INT PRIMARY KEY,
    name VARCHAR(255),
    location_id INT,
    category_id INT,
    date DATE,
    FOREIGN KEY (location_id) REFERENCES Locations(location_id),
    FOREIGN KEY (category_id) REFERENCES Categorys(category_id)
);

CREATE TABLE IF NOT EXISTS Users (
    user_id INT PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS Attendances (
    event_id INT,
    user_id INT,
    status VARCHAR(50),
    PRIMARY KEY (event_id, user_id),
    FOREIGN KEY (event_id) REFERENCES Events(event_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);