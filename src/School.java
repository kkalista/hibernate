import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class School implements java.io.Serializable {

	private long id;
	private String name;
	private String address;
	
	private Set<SchoolClass> classes;

	public School() {
		classes = new HashSet<SchoolClass>();
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Set<SchoolClass> getClasses() {
		return classes;
	}

	public void setClasses(Set<SchoolClass> classes) {
		this.classes = classes;
	}

	public void addClass(SchoolClass newClass) {
		classes.add(newClass);
	}
	
	public String toString() {
		return "School: " + getName() + " (" + getAddress() + ")";
	}

}
