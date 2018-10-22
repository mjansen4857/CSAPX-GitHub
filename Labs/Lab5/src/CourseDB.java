import java.util.Collection;
import java.util.HashMap;

/**
 * Storage of all courses where the key is the unique course id and the value is the associated Course object
 * @author Michael Jansen
 */
public class CourseDB implements DB<Integer, Course>{
    /** course storage */
    private HashMap<Integer, Course> courses;

    /** Create the course database */
    public CourseDB(){
        courses = new HashMap<>();
    }

    @Override
    public Course addValue(Course value) {
        return courses.put(value.getId(), value);
    }

    @Override
    public Collection<Course> getAllValues() {
        return courses.values();
    }

    @Override
    public Course getValue(Integer key) {
        return courses.get(key);
    }

    @Override
    public boolean hasKey(Integer key) {
        return courses.containsKey(key);
    }
}
