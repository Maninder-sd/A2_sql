import java.sql.*;

public class Assignment2 {

	// A connection to the database.
	// This variable is kept public.
	public Connection connection;

	/*
	 * Constructor for Assignment2. Identifies the PostgreSQL driver using
	 * Class.forName() method.
	 */
	public Assignment2() {
		try {
			Class clas = Class.forName("org.postgresql.Driver");

			ClassLoader clsLoader = clas.getClassLoader();

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * Using the String input parameters which are the URL, username, and password,
	 * establish the connection to be used for this object instance. If a connection
	 * already exists, it will be closed. Return true if a new connection instance
	 * was successfully established.
	 */
	public boolean connectDB(String URL, String username, String password) {
		if (connection == null) {
			try {

				connection = DriverManager.getConnection(URL, username, password);
				return true;
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				return false;
			}
		} else {
			if (disconnectDB()) {
				try {

					connection = DriverManager.getConnection(URL, username, password);
					return true;
				} catch (SQLException e) {
					System.out.println(e.getMessage());
					return false;
				}
			}
		}
		return false;
	}

	/*
	 * Closes the connection in this object instance. Returns true if the closure
	 * was successful. Returns false if this object instance previously had no
	 * active connections.
	 */
	public boolean disconnectDB() {

		if (connection == null) {
			return false;
		} else {
			try {
				connection.close();
			} catch (SQLException e) {

				e.printStackTrace();
				return false;
			}
			return true;
		}

	}

	/*
	 * Inserts a row into the student table.
	 * 
	 * dcodeis the code of the department.
	 * 
	 * Inputs and constraints must be validated.
	 * 
	 * You must check if the department exists, if the sex is one of the two values
	 * ('M' or 'F'), and if the year of study is a valid number ( > 0 && < 6 ).
	 * Returns true if the insertion was successful, false otherwise.
	 */
	public boolean insertStudent(int sid, String lastName, String firstName, String sex, int age, String dcode,
			int yearOfStudy) {
		String SQL = "Select dcode From department Where dcode = ? ";
		try {

			PreparedStatement pstmt = connection.prepareStatement(SQL);
			pstmt.setString(1, dcode);
			ResultSet rs = pstmt.executeQuery();
			// Check if department exists, if so
			// then we proceed to insert
			if (rs.next()) {
				if ((sex.equals('F') || sex.equals('M')) && (0 < yearOfStudy) && yearOfStudy < 6) {
					SQL = "INSERT INTO student (sid, slastname, sfirstname, sex, age, dcode, yearofstudy) VALUES(?,?,?,?,?,?,?)";
					pstmt = connection.prepareStatement(SQL);
					pstmt.setInt(1, sid);
					pstmt.setString(2, lastName);
					pstmt.setString(3, firstName);
					pstmt.setString(4, sex);
					pstmt.setInt(5, age);
					pstmt.setString(6, dcode);
					pstmt.setInt(7, yearOfStudy);
					if (pstmt.executeUpdate() > 0) {
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}

		} catch (Exception e) {
			return false;
		}
	}

	/*
	 * Returns the number of students in department dname. Returns -1 if the
	 * department does not exist.
	 */
	public int getStudentsCount(String dname) {
		String SQL = "Select dcode From department Where dcode = ? ";
		try {
			PreparedStatement pstmt = connection.prepareStatement(SQL);
			pstmt.setString(1, dname);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				SQL = "Select count(sid) as number_student From department JOIN student on department.dcode = student.dcode Where dname = ?";
				pstmt = connection.prepareStatement(SQL);
				pstmt.setString(1, dname);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					return rs.getInt("number_student");
				}
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return -1;
	}

	/*
	 * Returns a string with student information of student with student id sid. The
	 * output must be formatted as
	 * "firstName:lastName:sex:age:yearOfStudy:department". Returns an empty string
	 * "" if the student does not exist.
	 */
	public String getStudentInfo(int sid) {
		String SQL = "Select * From student Where sid = ?";
		try {
			PreparedStatement pstmt = connection.prepareStatement(SQL);
			pstmt.setInt(1, sid);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				String result = "";
				result = rs.getString("sfirstname ") + ":" + rs.getString("slastName") + ":" + rs.getString("sex") + ":"
						+ rs.getInt("age") + ":" + rs.getInt("yearofstudy") + ":" + rs.getString("dcode");

				return result;
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return "";
	}

	/*
	 * Changes a department's name. Returns true if the change was successful, false
	 * otherwise.
	 */
	public boolean chgDept(String dcode, String newName) {
		throw new RuntimeException("Function Not Implemented");
	}

	/*
	 * Deletes a department. Returns true if the deletion was successful, false
	 * otherwise.
	 */
	public boolean deleteDept(String dcode) {
		throw new RuntimeException("Function Not Implemented");
	}

	/*
	 * Returns a string with all the courses a student with student id sid has
	 * taken. Each course will be in a separate line.
	 * 
	 * Eg. "courseName1:department:semester:year:grade
	 * courseName2:department:semester:year:grade"
	 * 
	 * Returns an empty string "" if the student does not exist.
	 */
	public String listCourses(int sid) {
		throw new RuntimeException("Function Not Implemented");
	}

	/*
	 * Increases the grades of all the students who took a course in the course
	 * section identified by csid by 10%. Returns true if the update was successful,
	 * false otherwise. Do not not allow grades to go over 100%.
	 */
	public boolean updateGrades(int csid) {
		throw new RuntimeException("Function Not Implemented");
	}

	/*
	 * Create a table containing all the female students in "Computer Science"
	 * department who are in their fourth year of study.
	 * 
	 * The name of the table is femaleStudents and the attributes are: - sid INTEGER
	 * (student id) - fname CHAR (20) (first name) - lname CHAR (20) (last name)
	 * 
	 * Returns false and does not nothing if the table already exists. Returns true
	 * if the database was successfully created, false otherwise.
	 */
	public boolean updateDB() {
		throw new RuntimeException("Function Not Implemented");
	}
}
