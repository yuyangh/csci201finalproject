DROP DATABASE if exists ScheduleMe;

CREATE DATABASE ScheduleMe;

USE ScheduleMe;

CREATE TABLE Users (
	userID int(11) primary key auto_increment not null,
    name varchar(50) not null,
    email varchar(50),
    phone int(10)
);

CREATE TABLE Courses (
	courseID int(11) primary key auto_increment not null,
    name varchar(100) not null,
    units int(2) not null
);

CREATE TABLE Sections (
	sectionID varchar(10) not null,
    courseID int(11) not null,
    type varchar(10),
    startTime varchar(10) not null,
    endTime varchar(10) not null,
    days varchar(20) not null,
    availableSpots int(5) not null,
    totalSpots int(5) not null,
    instructor varchar(50),
    location varchar(10),
    # Link each section to a specific course
    FOREIGN KEY fkCourseID1(courseID) REFERENCES Courses(courseID)
);

# A constraint can either be a required class or a custom constraint
# In the case of a custom constraint, courseID will be null
# In the case of a required class, the name and times will be returned from the the course and section
CREATE TABLE Constraints (
	constraintID int(11) primary key auto_increment not null,
    userID int(11) not null,
    courseID int(11),
    name varchar(50),
    startTime varchar(50),
    endTime varchar(50),
    # Link each constraint to a user, and possibly a course
    FOREIGN KEY fkUserID(userID) REFERENCES Users(userID),
    FOREIGN KEY fkCourseID2(courseID) REFERENCES Courses(courseID)
);

# Our connections will be undirected, 
CREATE TABLE Relationships (
	fromUser int(11) not null,
    toUser int(11) not null,
    FOREIGN KEY fkFromUser(fromUser) REFERENCES Users(userID),
    FOREIGN KEY fkToUser(toUser) REFERENCES Users(userID)
);