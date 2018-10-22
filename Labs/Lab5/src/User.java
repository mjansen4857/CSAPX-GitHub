import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * Represents a user who is either a professor or student.
 * All users have a unique username and store the courses they are currently teaching (professor) or enrolled in (student),
 * in a tree set that is organized based on their preference (see subclasses).
 *
 * @author Michael Jansen
 */
public class User implements Comparable<User> {
    /** The courses the professor is teaching, or the student is enrolled in */
    private TreeSet<Course> courses;
    /** The user type */
    private UserType type;
    /** The username (unique) */
    private String username;

    /** Create a new user */
    public User(String username, UserType type, Comparator<Course> comp){
        this.username = username;
        this.type = type;
        this.courses = new TreeSet<>(comp);
    }

    /**
     * Gets the username
     * @return username
     */
    public String getUsername(){
        return this.username;
    }

    /**
     * Gets the user type
     * @return user type
     */
    public UserType getType(){
        return this.type;
    }

    /**
     * Add a course for this user. For a professor it means they add it to the courses they are teaching.
     * If they are a student, they are enrolling in the course.
     * @param course the course to add
     * @return whether the course was added or not
     */
    public boolean addCourse(Course course){
        return this.courses.add(course);
    }

    /**
     * Remove of a course for this user.
     * In both cases for a professor or student the meaning is the course no longer exists in their collection of courses.
     * @param course the course to remove
     * @return true if the user was removed from the course, false if they did not have the course
     */
    public boolean removeCourse(Course course){
        return this.courses.remove(course);
    }

    /**
     * Returns the courses the user is currently teaching or enrolled in
     * @return the courses
     */
    public Collection<Course> getCourses(){
        return this.courses;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof User){
            return this.username.equals(((User) o).getUsername());
        }
        return false;
    }

    @Override
    public int hashCode(){
        return this.username.hashCode();
    }

    @Override
    public String toString() {
        String courseList = "[";
        for(Course course:courses){
            courseList += course.getName() + ", ";
        }
        courseList = courseList.substring(0, courseList.length() - 2) + "]";
        return "User{" +
                "username=" + this.username +
                ", type=" + this.type +
                ", courses=" + courseList +
                '}';
    }

    @Override
    public int compareTo(User o) {
        return this.username.compareTo(o.getUsername());
    }

    /** The type of user */
    public enum UserType{
        PROFESSOR,
        STUDENT
    }
}
