DROP DATABASE if exists ScheduleMe;

CREATE DATABASE ScheduleMe;

USE ScheduleMe;

CREATE TABLE ActiveGroups (
    groupCode int(11) primary key not null, 
    # 0 = not locked, 1 = locked
    locked int(1) not null 
);

CREATE TABLE Users (
	userID int(11) primary key auto_increment not null,
    facebookID varchar(50),
    name varchar(50) not null,
    email varchar(50), 
    groupCode int(11),
    FOREIGN KEY fkGroupCode(groupCode) REFERENCES ActiveGroups(groupCode)
);

CREATE TABLE Schools (
    schoolID int(11) primary key auto_increment not null, 
    schoolName varchar(100) not null
);

CREATE TABLE Departments (
	departmentID int(11) primary key auto_increment not null,
    departmentName varchar(100) not null,
    schoolID int(11) not null,
    FOREIGN KEY fkSchoolID(schoolID) REFERENCES Schools(schoolID)
);

CREATE TABLE Courses (
	courseID int(11) primary key auto_increment not null,
    courseName varchar(100) not null,
    departmentID int(11) not null, 
    # reference to department ID for departments
    FOREIGN KEY fkDepartmentID(departmentID) REFERENCES Departments(departmentID)
);

CREATE TABLE Sections (
	sectionID int(11) primary key auto_increment not null,
    sectionName varchar(10) not null,
    courseID int(11) not null,
    type varchar(25) not null,
    startTime varchar(10) not null,
    endTime varchar(10) not null,
    # seperate days by commas
    days varchar(20) not null,
    # Link each section to a specific course
    FOREIGN KEY fkCourseID(courseID) REFERENCES Courses(courseID)
);

CREATE TABLE Schedules (
    scheduleID int(11) primary key auto_increment not null,
    sectionID int(11) not null, 
    userID int(11) not null, 
    FOREIGN KEY fkSectionID(sectionID) REFERENCES Sections(sectionID),
    FOREIGN KEY fkUserID(userID) REFERENCES Users(userID)
);