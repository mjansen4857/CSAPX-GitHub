import java.util.Collection;
import java.util.HashMap;

public class UserDB implements DB<String, User> {
    /** user storage */
    private HashMap<String, User> users;

    public UserDB(){
        users = new HashMap<>();
    }

    @Override
    public User addValue(User value) {
        return this.users.put(value.getUsername(), value);
    }

    @Override
    public Collection<User> getAllValues() {
        return this.users.values();
    }

    @Override
    public User getValue(String key) {
        return this.users.get(key);
    }

    @Override
    public boolean hasKey(String key) {
        return this.users.containsKey(key);
    }
}
