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
group by dname

--Does this select the largest number of iid?
INSERT INTO query1
(Select dname
From instructorWithoutPhd
Where numNoPhD = (SELECT max(numNoPhD) from instructorWithoutPhd)
);

--Query 2
INSERT INTO query2
(Select count(sid) as num
From student, department
where student.dcode=department.dcode and sex = 'F' and yearofstudy = 4 and dname = 'Computer Science'
);

--Query 3
-- need to take the first row or largest somehow 
Create view yearEnrollment as (
Select year, count(Distinct sid) as enrollment 
From courseSection JOIN studentCourse on courseSection.csid = studentCourse.csid
Where (year = 2016 or year = 2017 or year = 2018 or year = 2019 or year = 2020) and dcode = 'CSC'
Group by year)

INSERT INTO query3
(Select year, enrollment
From yearEnrollment
Where enrollment = (select max(enrollment) from yearEnrollment )
)

--Query 4
INSERT INTO query4

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


--Query 5

Create view agvGrades as (
select department.dname as dept, sid, sfirstname,slastname, avg(grade) as avgGrade
from student, studentCourse, department, courseSection
where student.sid=studentCourse.sid and student.dcode=department.dcode and 
studentCourse.cid=courseSection.cid and 
(courseSection.year <> (select max(year) from courseSection )) or  
courseSection.semester <> (select max(semester) from courseSection))
group by department.dname, sid, sfirstname,slastname
);

INSERT INTO query5
(
(Select dept, sid, sfirstname,slastname, avgGrade
From agvGrades
Where avgGrade = (select max(avgGrade) from agvGrades )



);



select *
from student, took, offering
where student.sid=took.sid and took.oid=offering.oid and
offering.term <> (select max(term) from offering)

select *
from offering
where 
( offering.term <> (select min(term) from offering) or cnum <> (select min(cnum) from offering) )






--Query 6
INSERT INTO query6

--Query 7
INSERT INTO query7

