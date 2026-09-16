import java.util.ArrayList;

public class UserDataBase {
    private ArrayList<User> users;

    public UserDataBase() {
        users = new ArrayList<>();
    }

    public void addUser(User user) {
        users.add(user);
    }

    public User getUserByUserName(String username) {
        User u = null;
        for (int i = 0; i < users.size(); i++) {
            u = users.get(i);
            if (u != null && u.getUserName().equals(username)) {
                return u;
            }
        }
        return null;
    }

    public User removeUserByName(String name) {
        User u = null;
        for (int i = 0; i < users.size(); i++) {
            u = users.get(i);
            if (u != null && u.getName().equals(name)) {
                return users.remove(i);
            }
        }
        return null;
    }

    public void fillArray(ArrayList<User> users) {
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            this.users.add(u);
        }
    }

    public ArrayList<User> getUsers() {
        return users;
    }
}
