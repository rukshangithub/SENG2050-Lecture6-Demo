use UniX;

select * from student;

select * from studentCourseRegistration;

select * from courseofferings;

select * from semester;

insert into courseofferings VALUES (101, 'COMP1140', 3);

insert into studentCourseRegistration(stdNo, courseID, semesterID) VALUES ('c0004', 'COMP1140', 101);

UPDATE studentCourseRegistration
SET courseID = 'SENG1110', semesterID = 102
WHERE stdNo = 'c0004';


DELETE FROM studentCourseRegistration WHERE stdNo = 'c0004'