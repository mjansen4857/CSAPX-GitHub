/**
 * A professor prefers to list their courses by ascending course level,
 * and if there is a tie then alphabetically by course name.
 *
 * @author Michael Jansen
 */
public class Professor extends User {
    /**
     * Create a new professor
     * @param username the username of the professor
     */
    public Professor(String username){
        super(username, UserType.PROFESSOR, (c1, c2) -> {
            if(c1.getLevel() == c2.getLevel()){
                return c1.getName().compareTo(c2.getName());
            }
            return c1.getLevel() - c2.getLevel();
        });
    }
}
