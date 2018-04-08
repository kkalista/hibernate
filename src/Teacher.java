import java.util.Set;


public class Teacher implements java.io.Serializable {

	private long id;
	private String name;
	private String surname;
	private Set<SchoolClass> schoolClasses;

	public Teacher() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public Set<SchoolClass> getSchoolClasses() {
		return schoolClasses;
	}

	public void setSchoolClasses(Set<SchoolClass> classes) {
		this.schoolClasses = classes;
	}

	public String toString() {
		return getName()+ " "+ getSurname();
	}

}