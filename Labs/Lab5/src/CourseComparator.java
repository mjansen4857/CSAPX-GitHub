import java.util.Comparator;

/**
 * A class that overrides the natural order comparison of courses to order
 * them alphabetically by course name.
 *
 * @author Michael Jansen
 */
public class CourseComparator implements Comparator<Course> {
    @Override
    public int compare(Course o1, Course o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
