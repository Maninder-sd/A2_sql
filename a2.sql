SET search_path TO A2;

--If you define any views for a question (you are encouraged to), you must drop them
--after you have populated the answer table for that question.
--Good Luck!

--Query 1

--***Comment**
-- Will still probably have to obtain the first row which will contain the largest number
Create view instructorWithoutPhd as
select count(idegree) as numNoPhD, dcode
from instructor, department
where department.dcode=instructor.dcode and instructor.idegree <> 'PhD'
group by dcode

--Does this select the largest number of iid?
INSERT INTO query1
(Select dname
From instructorWithoutPhd
Where numNoPhD = max(numNoPhD)
)

--Query 2
INSERT INTO query2

Create view femaleFourthyear as
Select count(sid) as num
From student
where sex = 'F' and yearofstudy = 4 and dcode = 'CSC'

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
Where enrollment = max(enrollment))

--Query 4
INSERT INTO query4
Select Distinct cname
From course Join courseSection on course.cid = courseSection.cid
Where  course.dcode = courseSection.dcode ='CSC' and  semester = 5


--Query 5
INSERT INTO query5



--Query 6
INSERT INTO query6

--Query 7
INSERT INTO query7

