import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Main {

	Session session;

	public static void main(String[] args) {
		Main main = new Main();
		// main.addNewData();
		main.printSchools();
		//main.executeQueries();
		main.close();
	}

	public Main() {
		session = HibernateUtil.getSessionFactory().openSession();
	}

	public void close() {
		session.close();
		HibernateUtil.shutdown();
	}

	private void printSchools() {
		Criteria crit = session.createCriteria(School.class);
		List<School> schools = crit.list();

		System.out.println("### Schools and classes");
		for (School s : schools) {
			System.out.println(s);
			for (SchoolClass schoolClass : s.getClasses()) {
				System.out.println(schoolClass);
				System.out.println("       > Teachers:");
				for (Teacher teacher : schoolClass.getTeachers()) {
					System.out.println("            " + teacher);
				}
				System.out.println("       > Students:");
				for (Student student : schoolClass.getStudents()) {
					System.out.println("            " + student);
				}
			}
		}
	}

	public void addNewData() {
		School newSchool = new School();
		newSchool.setName("Nowa Szkola");
		newSchool.setAddress("ul. Kawiory 12");

		SchoolClass newClass = new SchoolClass();
		newClass.setProfile("human");
		newClass.setCurrentYear(3);
		newClass.setStartYear(2011);

		Set<SchoolClass> newClasses = new HashSet<SchoolClass>();
		newClasses.add(newClass);
		newSchool.setClasses(newClasses);

		Student newStudent1 = new Student();
		newStudent1.setName("Julian");
		newStudent1.setSurname("Rokita");
		newStudent1.setPesel("84062041501");

		Student newStudent2 = new Student();
		newStudent2.setName("Katarzyna");
		newStudent2.setSurname("Wojcik");
		newStudent2.setPesel("83062041502");

		Set<Student> newStudents = new HashSet<Student>();
		newStudents.add(newStudent1);
		newStudents.add(newStudent2);
		newClass.setStudents(newStudents);
		newSchool.setClasses(newClasses);

		Transaction transaction = session.beginTransaction();
		session.save(newSchool);
		transaction.commit();
	}

	private void executeQueries() {
		String hql = "FROM School WHERE name='AGH'";
		Query query = session.createQuery(hql);
		List results = query.list();
		System.out.println(results);
	}

	private void jdbcTest() {
		Connection conn = null;
		Statement stmt = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName("org.sqlite.JDBC");

			// STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection("jdbc:sqlite:school.db", "", "");

			// STEP 4: Execute a query
			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM schools";
			ResultSet rs = stmt.executeQuery(sql);

			// STEP 5: Extract data from result set
			while (rs.next()) {
				// Retrieve by column name
				String name = rs.getString("name");
				String address = rs.getString("address");

				// Display values
				System.out.println("Name: " + name);
				System.out.println(" address: " + address);
			}
			// STEP 6: Clean-up environment
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} // end finally try
		} // end try
		System.out.println("Goodbye!");
	}// end jdbcTest

}
