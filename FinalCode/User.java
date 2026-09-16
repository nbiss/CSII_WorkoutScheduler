import java.util.Set;

public class User {
    // attributes
    private String userName;
    private String password;
    private String name;
    private char gender;
    private int height;
    private int weight;
    // maxes
    // maxBench, maxSquat, maxDeadlift, max

    Set<Character> validChars = Set.of('!', '@', '#', '$', '%', '^', '&', '*', '?');

    // constructor
    public User(String userName, String password, String name, char gender) throws InvalidException {
        if (userName == null || userName.isBlank() || userName.isEmpty() || userName.length() > 20) {
            throw new InvalidException("Invalid userName");
        } else {
            this.userName = userName;
        }
        if (password == null || password.isBlank() || password.isEmpty() || password.length() > 20) {
            throw new InvalidException("Password is not valid");
        } else if (!checkValidPass(password)) {
            throw new InvalidException("Invalid password");
        } else {
            this.password = password;
        }
        if (name == null || name.isBlank() || name.isEmpty() || name.length() > 20) {
            throw new InvalidException("Invalid name");
        } else {
            this.name = name;
        }

        if (gender == 'm') {
            this.gender = 'M';
        } else if (gender == 'f') {
            this.gender = 'F';
        } else if (gender != 'M' && gender != 'F') {
            throw new InvalidException("Invalid gender");
        } else {
            this.gender = gender;
        }

    }

    // getters
    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getUserName() {
        return userName;
    }

    public char getGender() {
        return gender;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    // setters
    public void setUserName(String userName) throws InvalidException {
        if (userName.length() > 20 || userName.length() < 1) {
            throw new InvalidException("Invalid user name");
        } else {
            this.userName = userName;
        }
    }

    public void setPassword(String pass) throws InvalidException {
        if (matchPass(pass)) {
            throw new InvalidException("Can not make new password the same as the old one");
        }
        if (!checkValidPass(pass)) {
            throw new InvalidException(String.format("Invalid password %s", pass));
        }
        if (pass.length() > 20 || pass.length() < 1) {
            throw new InvalidException("Invalid length");
        } else {
            this.password = pass;
        }

    }

    public void setName(String name) throws InvalidException {
        if (name.length() > 20 || name.length() < 1) {
            throw new InvalidException("Invalid length");
        } else {
            this.name = name;
        }

    }

    public void setHeight(int height) throws InvalidException {
        if (height <= 0 || height >= 1500) {
            throw new InvalidException("Invalid height");
        } else {
            this.height = height;
        }
    }

    public void setWeight(int weight) throws InvalidException {
        if (weight <= 0 || weight >= 1500) {
            throw new InvalidException("Invalid weight");
        } else {
            this.weight = weight;
        }
    }

    // helper
    public boolean matchPass(String pass) {
        return (pass.equals(password));
    }

    public boolean checkValidPass(String pass) {
        // for valid pass
        // longer than 8 characters, contains one upper case,
        // contains one special character from set defined above.
        boolean upperCase = false;
        boolean specialChar = false;
        if (pass.length() < 8) {
            return false;
        }
        for (int i = 0; i < pass.length(); i++) {
            char c = pass.charAt(i);
            if (Character.isUpperCase(c)) {
                upperCase = true;
                break;
            }
        }
        for (int i = 0; i < pass.length(); i++) {
            char r = pass.charAt(i);
            if (validChars.contains(r)) {
                specialChar = true;
                break;
            }
        }

        return (upperCase && specialChar);
    }

    public String toString() {
        return String.format("%s, %s, %s, %c, %d, %d", getUserName(), getPassword(), getName(), getGender(),
                getHeight(),
                getWeight());
    }
}