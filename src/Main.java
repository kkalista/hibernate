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
		// main.executeQueries();
		// main.updatingSchool();
		main.close();
	}

	public Main() {
		session = HibernateUtil.getSessionFactory().openSession();
	}

	public void close() {
		session.close();
		HibernateUtil.shutdown();
	}
	
	private void executeQueries() {
		// query0();
		// query1();
		// query2();
		// query3();
		// query4();
		// query5();
		// query6();
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

	private void addNewData() {
		School newSchool = new School();
		newSchool.setName("UE");
		newSchool.setAddress("ul. Rakowicka 7, Kraków");
		
		SchoolClass newClass = new SchoolClass();
		newClass.setProfile("mat-inf");
		newClass.setStartYear(2012);
		newClass.setCurrentYear(4);
		
		Student newStudent = new Student();
		newStudent.setName("Ryszard");
		newStudent.setSurname("Trzebitowski");
		newStudent.setPesel("8406204141401");
		
		Student newStudent2 = new Student();
		newStudent2.setName("Julia");
		newStudent2.setSurname("Nowacka");
		newStudent2.setPesel("8401041502");
		
		newSchool.addClass(newClass);
		newClass.addStudent(newStudent);
		newClass.addStudent(newStudent2);
		
		
		Transaction transaction = session.beginTransaction();
		session.save(newSchool);
		transaction.commit();
	}
		
	private void query0() {
		String hql = "FROM School";
		Query query = session.createQuery(hql);
		List results = query.list();
		System.out.println(results);
	}

	private void query1() {
		String hql = "FROM School WHERE name='AGH'";
		Query query = session.createQuery(hql);
		List results = query.list();
		System.out.println("School with 'AGH' in the name" + results);
	}
	
	private void query2() {
		String hql = "FROM School WHERE name='UE'";
		Query query = session.createQuery(hql);
		List<School> results = query.list();
		Transaction transaction = session.beginTransaction();
		for (School s : results) {
			session.delete(s);
		}
		transaction.commit();
	}
		
	private void query3() {
		String hql = "SELECT COUNT(S) FROM School S";
		Query query = session.createQuery(hql);
		Integer schoolsCount = (Integer) query.uniqueResult();
		System.out.println("Schools count: " + schoolsCount);
	}

	private void query4() {
		String hql = "SELECT COUNT(S) FROM Student S";
		Query query = session.createQuery(hql);
		Integer studentsCount = (Integer) query.uniqueResult();
		System.out.println("Students count: " + studentsCount);
	}

	private void query5() {
		String hql = "SELECT COUNT(S) FROM School S WHERE size(S.classes)>=2";
		Query query = session.createQuery(hql);
		Integer schoolsCount = (Integer) query.uniqueResult();
		System.out.println("Schools count (equals or more than two): " + schoolsCount);
	}

	private void query6() {
		String hql = "SELECT s FROM School s INNER JOIN s.classes classes WHERE classes.profile = 'mat-fiz' AND classes.currentYear>=2";
		Query query = session.createQuery(hql);
		List results = query.list();
		System.out.println("School with 'mat-fiz' profile and current class equals or more than two:" +results);
	}
	
	private void updatingSchool() {
		Query query = session.createQuery("from School where id= :id");
		query.setLong("id", 3);
		School school = (School) query.uniqueResult();
		school.setAddress("Lesna 11");
		Transaction transaction = session.beginTransaction();
		session.save(school);
		transaction.commit();
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
