Use UniX;

DROP TRIGGER  checkMaxCapacity_insert

DELIMITER $$

CREATE TRIGGER checkMaxCapacity_insert
BEFORE INSERT ON studentcourseregistration
FOR EACH ROW
BEGIN
	/* Declaring local variables */
    DECLARE maxCourseSize INT;
    DECLARE noCourseEnrolments INT;

	/* Finding the maximum enrolment capacity for course in semester */
    SELECT maxCapacity INTO maxCourseSize
    FROM courseOfferings
    WHERE courseID = NEW.courseID AND semesterID = NEW.semesterID;

	/* Finding the number of enrolments for course in semester */    
    SELECT COUNT(*) INTO noCourseEnrolments
    FROM studentcourseregistration
    WHERE courseID = NEW.courseID AND semesterID = NEW.semesterID;
    
    IF maxCourseSize = noCourseEnrolments THEN 
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Course is at capacity. Cannot enroll more students', 
                MYSQL_ERRNO = 10003;
    END IF;
END$$





DELIMITER $$

CREATE TRIGGER checkMaxCapacity_update
BEFORE UPDATE ON studentcourseregistration
FOR EACH ROW
BEGIN
	/* Declaring local variables */
    DECLARE maxCourseSize INT;
    DECLARE noCourseEnrolments INT;

	/* Finding the maximum enrolment capacity for course in semester */
    SELECT maxCapacity INTO maxCourseSize
    FROM courseOfferings
    WHERE courseID = NEW.courseID AND semesterID = NEW.semesterID;

	/* Finding the number of enrolments for course in semester */    
    SELECT COUNT(*) INTO noCourseEnrolments
    FROM studentcourseregistration
    WHERE courseID = NEW.courseID AND semesterID = NEW.semesterID;
    
    IF maxCourseSize = noCourseEnrolments THEN 
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Course is at capacity. Cannot enroll more students', 
                MYSQL_ERRNO = 10003;
    END IF;
END$$