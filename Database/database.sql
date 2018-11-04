DROP DATABASE if exists ScheduleMe;

CREATE DATABASE ScheduleMe;

USE ScheduleMe;

CREATE TABLE Users (
	userID int(11) primary key auto_increment not null,
    facebookID varchar(50),
    name varchar(50) not null,
    email varchar(50)
);

CREATE TABLE Schools (
    schoolID int(11) primary key auto_increment not null, 
    schoolName varchar(100) not null
);

CREATE TABLE Courses (
	courseID int(11) primary key auto_increment not null,
    name varchar(100) not null,
    department varchar(6) not null,
    schoolID int(11) not null, 
    # reference to school ID for schools
    FOREIGN KEY fkschoolID1(schoolID) REFERENCES Schools(schoolID)
);

CREATE TABLE Sections (
	sectionID int(10) not null primary key,
    courseID int(11) not null,
    type varchar(10) not null,
    startTime varchar(10) not null,
    endTime varchar(10) not null,
    # seperate days by commas
    days varchar(20) not null,
    # Link each section to a specific course
    FOREIGN KEY fkCourseID1(courseID) REFERENCES Courses(courseID)
);

CREATE TABLE Schedules (
    scheduleID int(11) primary key auto_increment not null,
    sectionID int(11) not null, 
    userID int(11) not null, 
    FOREIGN KEY fksectionID1(sectionID) REFERENCES Sections(sectionID),
    FOREIGN KEY fkuserID1(userID) REFERENCES Users(userID)
);