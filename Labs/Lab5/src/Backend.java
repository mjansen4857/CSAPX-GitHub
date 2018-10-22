import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

/**
 * This class represents that backend that SIS (frontend) interfaces with. It creates the course and user databases from the input files.
 * It is responsible for taking requests from SIS and invoking the appropriate operations on the databases.
 *
 * A course file is a comma separated text file in the format (one per line):
 * {CourseId},{Course_Name},{CourseLevel}
 *
 * A professor file is a comma separated text file in the format (one per line):
 * {ProfessorUsername},{CourseId_1},{CourseId_2},...
 *
 * A student file is a comma separated text file in the format (one per line):
 * {StudentUsername},{CourseId_1},{CourseId_2},...
 *
 * @author Michael Jansen
 */
public class Backend {
    /** The database of courses */
    private CourseDB courseDB;
    /** The database of users */
    private UserDB userDB;

    /**
     * Creates the backend by initializing the course and user databases
     * @param courseFile name of course file
     * @param professorFile name of professor file
     * @param studentFile name of student file
     * @throws FileNotFoundException if any of the files cannot be found
     */
    public Backend(String courseFile, String professorFile, String studentFile) throws FileNotFoundException {
        initializeCourseDB(courseFile);
        initializeUserDB(professorFile, studentFile);
    }

    /**
     * A utility method for initializing the course database
     * @param courseFile name of the course file
     * @throws FileNotFoundException if the course file cannot be found
     */
    private void initializeCourseDB(String courseFile) throws FileNotFoundException {
        courseDB = new CourseDB();

        try (Scanner in = new Scanner(new File(courseFile))){
            while (in.hasNext()){
                String[] fields = in.nextLine().split(",");

                int id = Integer.parseInt(fields[0]);
                String name = fields[1];
                int level = Integer.parseInt(fields[2]);

                courseDB.addValue(new Course(id, name, level));
            }
        }
    }

    /**
     * A utility method for initializing the user database
     * @param professorFile name of the professor file
     * @param studentFile name of the student file
     * @throws FileNotFoundException if either file is not found
     */
    private void initializeUserDB(String professorFile, String studentFile) throws FileNotFoundException {
        userDB = new UserDB();

        try (Scanner in = new Scanner(new File(professorFile))){
            while(in.hasNext()){
                String[] fields = in.nextLine().split(",");
                String username = fields[0];
                String[] courseIds = Arrays.copyOfRange(fields, 1, fields.length);

                Professor professor = new Professor(username);
                userDB.addValue(professor);
                addCourses(professor, courseIds);
            }
        }

        try (Scanner in = new Scanner(new File(studentFile))){
            while(in.hasNext()){
                String[] fields =  in.nextLine().split(",");
                String username = fields[0];
                String[] courseIds = Arrays.copyOfRange(fields, 1, fields.length);

                Student student = new Student(username);
                userDB.addValue(student);
                addCourses(student, courseIds);
            }
        }
    }

    /**
     * A utility method. Used by initializeUserDB when adding a professor or student to collection of courses
     * @param user the user
     * @param courseIds a collection of course ids
     */
    private void addCourses(User user, String[] courseIds){
        for(String id:courseIds){
            int courseId = Integer.parseInt(id);

            if(user.getType() == User.UserType.PROFESSOR){
                Course course = getCourse(courseId);
                course.addProfessor(user.getUsername());
                user.addCourse(course);
            }else if(user.getType() == User.UserType.STUDENT){
                enrollStudent(user.getUsername(), courseId);
            }
        }
    }

    /**
     * Check whether a course exists or not
     * @param id the course id
     * @return whether the course exists or not
     */
    public boolean courseExists(int id){
        return courseDB.hasKey(id);
    }

    /**
     * Enroll a student in a course. This requires they are added to both the course and the student's courses.
     * @param username the username of the student
     * @param courseId the course to enroll in
     * @return whether the student was enrolled or not (false if already enrolled)
     */
    public boolean enrollStudent(String username, int courseId){
        Course course = getCourse(courseId);
        User student = userDB.getValue(username);

        return student.addCourse(course) && course.addStudent(username);
    }

    /**
     * Get all of the courses in the system
     * @return all of the courses
     */
    public Collection<Course> getAllCourses(){
        return courseDB.getAllValues();
    }

    /**
     * Get all of the users in the system
     * @return all of the users
     */
    public Collection<User> getAllUsers(){
        return userDB.getAllValues();
    }

    /**
     * Check whether a username belongs to a student
     * @param username the username
     * @return whether the username belongs to a student
     */
    public boolean isStudent(String username){
        return userDB.getValue(username).getType() == User.UserType.STUDENT;
    }

    /**
     * Get a course by id
     * @param id the course id
     * @return the course
     */
    public Course getCourse(int id){
        return courseDB.getValue(id);
    }

    /**
     * Unenroll a student from a course
     * @param username the username of the student to unenroll
     * @param courseId the id of the course
     * @return true if the student was unenrolled, false if the student was not enrolled in the course
     */
    public boolean unenrollStudent(String username, int courseId){
        Course course = getCourse(courseId);
        User student = userDB.getValue(username);

        return student.removeCourse(course) && course.removeStudent(username);
    }

    /**
     * Check if a username exists in a system
     * @param username the username to check
     * @return whether the username is in the system or not
     */
    public boolean userExists(String username){
        return userDB.hasKey(username);
    }

    /**
     * Get the courses for a particular user
     * @param username the username
     * @return the collection of courses for a user
     */
    public Collection<Course> getCourseUser(String username){
        return userDB.getValue(username).getCourses();
    }
}
