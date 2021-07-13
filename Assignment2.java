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
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {

			pstmt = connection.prepareStatement(SQL);
			pstmt.setString(1, dcode);
			rs = pstmt.executeQuery();
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
						try {
							if (rs != null) {
								rs.close();
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
						try {
							if (pstmt != null) {
								pstmt.close();
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
						return true;
					} else {
						try {
							if (rs != null) {
								rs.close();
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
						try {
							if (pstmt != null) {
								pstmt.close();
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
						return false;
					}
				} else {
					try {
						if (rs != null) {
							rs.close();
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
					try {
						if (pstmt != null) {
							pstmt.close();
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
					return false;
				}
			} else {
				try {
					if (rs != null) {
						rs.close();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				try {
					if (pstmt != null) {
						pstmt.close();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
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
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(SQL);
			pstmt.setString(1, dname);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				SQL = "Select count(sid) as number_of_student From department JOIN student on department.dcode = student.dcode Where dname = ?";
				pstmt = connection.prepareStatement(SQL);
				pstmt.setString(1, dname);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					int result = rs.getInt("number_of_student");
					try {
						if (rs != null) {
							rs.close();
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
					try {
						if (pstmt != null) {
							pstmt.close();
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
					return result;
				}
			}
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
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
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(SQL);
			pstmt.setInt(1, sid);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				String result = "";
				result = rs.getString("sfirstname ") + ":" + rs.getString("slastName") + ":" + rs.getString("sex") + ":"
						+ rs.getInt("age") + ":" + rs.getInt("yearofstudy") + ":" + rs.getString("dcode");
				try {
					if (rs != null) {
						rs.close();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				try {
					if (pstmt != null) {
						pstmt.close();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

				return result;
			}
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
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
		String SQL = "UPDATE department Set dname = ? Where dcode = ?";
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(SQL);
			pstmt.setString(1, newName);
			pstmt.setString(2, dcode);
			int result = pstmt.executeUpdate();
			if (result > 0) {
				try {
					if (pstmt != null) {
						pstmt.close();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				return true;
			} else {
				try {
					if (pstmt != null) {
						pstmt.close();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

	/*
	 * Deletes a department. Returns true if the deletion was successful, false
	 * otherwise.
	 */
	public boolean deleteDept(String dcode) {
		String SQL = "DELETE FROM department Where dcode = ?";
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(SQL);
			pstmt.setString(1, dcode);
			int result = pstmt.executeUpdate();
			if (result > 0) {
				try {
					if (pstmt != null) {
						pstmt.close();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

				return true;
			} else {
				try {
					if (pstmt != null) {
						pstmt.close();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
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
		String SQL = "SELECT sid FROM student Where sid = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String result = "";
		try {
			pstmt = connection.prepareStatement(SQL);
			pstmt.setInt(1, sid);
			rs = pstmt.executeQuery();
			// we know we found sid
			if (rs.next()) {
				SQL = "SELECT cname, dcode, semester, year, grade FROM studentCourse JOIN courseSection on studentCourse.csid = courseSection.csid JOIN course on course.cid = courseSection.csid Where sid = ?";
				pstmt = connection.prepareStatement(SQL);
				pstmt.setInt(1, sid);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					result = result + rs.getString("cname") + ":" + rs.getString("dcode") + ":" + rs.getInt("semester")
							+ ":" + rs.getInt("year") + ":" + rs.getInt("grade") + "\n";
				}

				try {
					if (rs != null) {
						rs.close();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				try {
					if (pstmt != null) {
						pstmt.close();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				return result.substring(0, result.length() - 2);

			} else {
				try {
					if (rs != null) {
						rs.close();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				try {
					if (pstmt != null) {
						pstmt.close();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/*
	 * Increases the grades of all the students who took a course in the course
	 * section identified by csid by 10%. Returns true if the update was successful,
	 * false otherwise. Do not not allow grades to go over 100%.
	 */
	public boolean updateGrades(int csid) {
		String SQL = "Select sid, grade From studentCourse Where csid = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int results = 0;
		try {
			pstmt = connection.prepareStatement(SQL);
			pstmt.setInt(1, csid);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				SQL = "UPDATE studentCourse SET grade = ? Where sid = ? and csid = ?";
				int sid = rs.getInt("sid");
				int grade = (int) (rs.getInt("grade") * 1.1);
				if (grade > 100) {
					grade = 100;
				}
				pstmt = connection.prepareStatement(SQL);
				pstmt.setInt(1, grade);
				pstmt.setInt(2, sid);
				pstmt.setInt(3, csid);
				results = results + pstmt.executeUpdate();
			}

			if (results > 0) {
				try {
					if (rs != null) {
						rs.close();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				try {
					if (pstmt != null) {
						pstmt.close();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				return true;

			} else {
				try {
					if (rs != null) {
						rs.close();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				try {
					if (pstmt != null) {
						pstmt.close();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
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
		String SQL = "CREATE TABLE femaleStudents (sid INTEGER, fname CHAR (20),lname CHAR (20))";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int insertResults = 0;
		try {
			pstmt = connection.prepareStatement(SQL);
			int result = pstmt.executeUpdate();
			if (result > 0) {
				SQL = "SELECT sid, sfirstname, slastname FROM student JOIN department on student.dcode = department.dcode Where sex = 'F'";
				pstmt = connection.prepareStatement(SQL);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					SQL = "INSERT INTO femaleStudents VALUES (?,?,?)";
					pstmt = connection.prepareStatement(SQL);
					pstmt.setInt(1, rs.getInt("sid"));
					pstmt.setString(2, rs.getString("sfirstname"));
					pstmt.setString(3, rs.getString("slastname"));
				}

				try {
					if (rs != null) {
						rs.close();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				try {
					if (pstmt != null) {
						pstmt.close();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				return true;
			} else {
				try {
					if (pstmt != null) {
						pstmt.close();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}
}
