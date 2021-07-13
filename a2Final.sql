SET search_path TO A2;

--If you define any views for a question (you are encouraged to), you must drop them
--after you have populated the answer table for that question.
--Good Luck!

--Query 1

--***Comment**
-- Will still probably have to obtain the first row which will contain the largest number
Create view instructorWithoutPhd as
select count(idegree) as numNoPhD, dname
from instructor, department
where department.dcode=instructor.dcode and instructor.idegree <> 'PhD'
group by dname;

--Does this select the largest number of iid?

CREATE TABLE query1(dname VARCHAR(20) NOT NULL );
INSERT INTO query1
(Select dname
From instructorWithoutPhd
Where numNoPhD = (SELECT max(numNoPhD) from instructorWithoutPhd)
);

DROP VIEW instructorWithoutPhd;


--Query 2
CREATE TABLE query2(num INTEGER);
INSERT INTO query2(
	Select count(sid) as num
	From student, department
	where student.dcode=department.dcode and sex = 'F' and yearofstudy = 4 and dname = 'Computer Science'
);

--Query 3
-- need to take the first row or largest somehow 
Create view yearEnrollment as 
	Select year, count(Distinct sid) as enrollment 
	From courseSection JOIN studentCourse on courseSection.csid = studentCourse.csid
	Where (year = 2016 or year = 2017 or year = 2018 or year = 2019 or year = 2020) and dcode = 'CSC'
	Group by year;

CREATE TABLE query3(year INTEGER NOT NULL, enrollment INTEGER NOT NULL);
INSERT INTO query3(
	Select year, enrollment
	From yearEnrollment
	Where enrollment = (select max(enrollment) from yearEnrollment )
)


--Query 4
CREATE TABLE query4(cname CHAR(20) NOT NULL);
INSERT INTO query4(

	(Select Distinct cname
	From course, courseSection, department
	Where 
	course.cid = courseSection.cid and 
	course.dcode = courseSection.dcode and 
	course.dcode = department.dcode and
	course.dcode = 'CSC' and  semester = 5)

EXCEPT

	(Select Distinct cname
	From course, courseSection, department
	Where 
	course.cid = courseSection.cid and 
	course.dcode = courseSection.dcode and 
	course.dcode = department.dcode and
	course.dcode = 'CSC' and  semester <> 5)
);


--Query 5

Create view avgGrades as 
	select dname as dept, student.sid, sfirstname,slastname, avg(grade) as avgGrade
	from student, studentCourse, department, courseSection
	where student.sid=studentCourse.sid and student.dcode=department.dcode and 
	studentCourse.csid=courseSection.csid 
	group by dname, student.sid, sfirstname,slastname
;


CREATE TABLE query5(
	dept VARCHAR(20) NOT NULL,
	sid INTEGER,
	sfirstname CHAR(20) NOT NULL,
	slastname CHAR(20) NOT NULL,
	avgGrade REAL
);
INSERT INTO query5(
	Select dept, sid, sfirstname,slastname, avgGrade
	From avgGrades
	Where avgGrade = (select max(avgGrade) from avgGrades )
);

DROP VIEW avgGrades;


--Query 6

Create view noPrereq as

	-- gets sids of students and csids of courses
	select sid,courseSection.cid,year,semester
	from studentCourse, courseSection, prerequisites
	where studentCourse.csid=courseSection.csid and 
	courseSection.cid=prerequisites.cid and courseSection.dcode=prerequisites.dcode and
	EXISTS(
		select *
		from studentCourse as R1, courseSection as R2
		where R1.sid=studentCourse.sid and R1.csid=R2.csid and 
		(prerequisites.pcid=R2.cid) and (prerequisites.pdcode=R2.dcode) and  
		( (R2.year < courseSection.year ) or (R2.year = courseSection.year and  R2.semester < courseSection.semester) )
	)
;

CREATE TABLE query6(
	fname CHAR(20) NOT NULL,
	lname CHAR(20) NOT NULL,
	cname CHAR(20) NOT NULL,
	year INTEGER NOT NULL,
	semester INTEGER NOT NULL
);

INSERT INTO query6(
	select sfirstname as fname, slastname as lname, cname,year, semester
	from noPrereq, student, course
	where noPrereq.sid=student.sid and noPrereq.cid=course.cid 
);

DROP VIEW noPrereq;


--Query 7
Create view courseAvgs as 
	select cname,semester,year,avg(grade) as avgmark
	from studentCourse,course, courseSection,department
	where studentCourse.csid=courseSection.csid and courseSection.cid=course.cid and courseSection.dcode=course.dcode
	and department.dname='Computer Science' and department.dcode=course.dcode and 
	(courseSection.year <> (select max(year) from courseSection ) or  
	courseSection.semester <> (select max(semester) from courseSection))
	group by course.cname,courseSection.semester, courseSection.year
	having count(*)>3
;


CREATE TABLE query7(
	cname CHAR(20) NOT NULL,
	semester INTEGER NOT NULL,
	year INTEGER NOT NULL,
	avgmark REAL
);

INSERT INTO query7(
	Select cname,semester,year,avgmark
	From courseAvgs
	Where avgmark = (select max(avgmark) from courseAvgs ) or 
	avgmark = (select min(avgmark) from courseAvgs )
);


DROP VIEW courseAvgs;

