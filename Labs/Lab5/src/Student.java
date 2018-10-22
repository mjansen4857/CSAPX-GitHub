import java.util.Comparator;

public class Student extends User {
    /**
     * Create a new student
     * @param username the username of the student
     */
    public Student(String username) {
        super(username, UserType.STUDENT, Comparator.comparing(Course::getName));
    }
}
