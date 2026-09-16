import java.util.ArrayList;
import java.util.Scanner;

public class WorkoutScheduleApp {
    public static void main(String[] args) {
        WorkoutScheduleApp app = new WorkoutScheduleApp();
        app.runApp();
    }

    public void runApp() {
        UserDataBase userDataBase = new UserDataBase();
        ExerciseDataBase exerciseDataBase = new ExerciseDataBase();
        FileInOut fileIO = new FileInOut();
        populateUsersToDataBase(userDataBase, fileIO, "usersFile.txt");
        populateExercisesToDataBase(exerciseDataBase, fileIO, "ExerciseFile.txt");
        GASettings settings = new GASettings();
        Scanner scan = new Scanner(System.in);

        User currentUser = null;

        // Sign in loop to ensure a user has been signed in.

        while (currentUser == null) {
            int signInt = signInMenu(scan);
            try {
                currentUser = processSignIn(scan, signInt, userDataBase);
            } catch (InvalidException ex) {
                System.out.println(ex);
                return;
            }
        }
        System.out.println(String.format("Welocome %s", currentUser.getName()));

        // Main menu loop
        boolean repeat = true;
        while (repeat) {
            int choice = menu(scan);
            repeat = processChoice(scan, choice, fileIO, userDataBase, exerciseDataBase,
                    currentUser, settings);
        }

    }// end runApp

    public int menu(Scanner scan) {
        String userIn = "";
        int choice = 0;
        do {
            try {
                System.out.print("""
                        \tMain menu
                        1) Stage a Genetic Algorithm
                        2) Add or mutate Exercise
                        3) Edit user data
                        4) Save data
                        5) Exit \n> """);
                userIn = scan.nextLine();
                choice = Integer.parseInt(userIn);
            } catch (Exception e) {
                System.out.println("Invalid choice");
            }

        } while (choice <= 0 || choice > 5);
        return choice;
    }

    public boolean processChoice(Scanner scan, int choice, FileInOut fileIO, UserDataBase userDataBase,
            ExerciseDataBase exerciseDataBase, User currentUser, GASettings settings) {
        switch (choice) {
            case 1:
                computeGeneticAlgorithum(scan, exerciseDataBase, settings, fileIO);
                break;
            case 2:
                boolean repeat2 = true;
                do {
                    int eChoice = mutateExerciseMenu(scan);
                    repeat2 = processExerciseChoice(scan, eChoice, exerciseDataBase);
                } while (repeat2);
                break;
            case 3:
                editUserData(scan, userDataBase, currentUser);
                break;
            case 4:
                saveData(userDataBase, exerciseDataBase, fileIO);
                break;
            case 5:
                return false;
            default:
                break;
        }
        return true;
    }

    public void saveData(UserDataBase userDataBase, ExerciseDataBase exerciseDataBase, FileInOut fileIO) {
        try {
            fileIO.saveExercises(exerciseDataBase.getExercises(), "ExerciseFile.txt");
            fileIO.saveUsers(userDataBase.getUsers(), "usersFile.txt");
            System.out.println("Save sucess\n");
        } catch (InvalidException e) {
            System.out.println(e);
        }
    }

    public void editUserData(Scanner scan, UserDataBase userDataBase, User currentUser) {
        boolean repeat = true;

        do {
            int choice = askUserForInt(scan,
                    "\tUser menu\n1) Change height\n2) Change weight\n3) Change user name\n4) Change password\n5) Change name\n6) Exit",
                    1, 6);
            if (choice == -1) {
                break;
            }
            repeat = processUserMenu(choice, scan, userDataBase, currentUser);
        } while (repeat);
    }

    public boolean processUserMenu(int choice, Scanner scan, UserDataBase userDataBase, User currentuser) {
        switch (choice) {
            case 1:
                changeUserHeight(scan, currentuser);
                break;
            case 2:
                changeUserWeight(scan, currentuser);
                break;
            case 3:
                changeUserUserName(scan, currentuser);
                break;
            case 4:
                changeUserPassword(scan, currentuser);
                break;
            case 5:
                changeUserName(scan, currentuser);
                break;
            case 6:
                return false;

            default:
                break;
        }
        return true;
    }

    public void changeUserHeight(Scanner scan, User currentUser) {
        try {
            int choice = askUserForInt(scan,
                    "Enter a new height in inches (multiply feet by 12 and then add remainder)\nEx: 5 feet 5 inches would be 65 inches.",
                    1, 1500);
            if (choice == -1) {
                return;
            }
            currentUser.setHeight(choice);
        } catch (InvalidException ex) {
            System.out.println(ex);
        }
    }

    public void changeUserWeight(Scanner scan, User currentUser) {
        try {
            int choice = askUserForInt(scan,
                    "Enter a new weight in pounds", 1, 1500);
            if (choice == -1) {
                return;
            }
            currentUser.setWeight(choice);
        } catch (InvalidException ex) {
            System.out.println(ex);
        }
    }

    public void changeUserUserName(Scanner scan, User currentUser) {
        boolean sucess = false;
        do {
            try {
                System.out.print("Enter new user name\n> ");
                String userIn = scan.nextLine();
                if (userIn.isEmpty()) {
                    return;
                }
                currentUser.setUserName(userIn);
                sucess = true;
            } catch (InvalidException ex) {
                System.out.println(ex);
            }
        } while (!sucess);
    }

    public void changeUserPassword(Scanner scan, User currentUser) {
        boolean sucess = false;
        do {
            try {
                System.out.print("Enter new password\n> ");
                String userIn = scan.nextLine();
                if (userIn.isEmpty()) {
                    return;
                }
                currentUser.setPassword(userIn);
                sucess = true;
            } catch (InvalidException ex) {
                System.out.println(ex);
            }
        } while (!sucess);
    }

    public void changeUserName(Scanner scan, User currentUser) {
        boolean sucess = false;
        do {
            try {
                System.out.print("Enter new name\n> ");
                String userIn = scan.nextLine();
                if (userIn.isEmpty()) {
                    return;
                }
                currentUser.setName(userIn);
                sucess = true;
            } catch (InvalidException ex) {
                System.out.println(ex);
            }
        } while (!sucess);
    }

    public void computeGeneticAlgorithum(Scanner scan, ExerciseDataBase exerciseDataBase, GASettings settings,
            FileInOut fileIO) {
        // current values goal: 1-Strength, 2-Hybrid, 3-Endurence
        int choice = 0;
        if (settings.getGoal() == 0) {
            int goal = selectFocusGoal(scan, "");// set the focus goal
            settings.setGoal(goal);
        }

        boolean repeat1 = true;
        do {
            choice = geneticAlgorithumMenu(scan);
            repeat1 = processStageGeneticAlgorithum(choice, scan, exerciseDataBase, settings, fileIO);

        } while (repeat1);

    }

    public int selectFocusGoal(Scanner scan, String prompt) {
        int goal = 1;
        do {
            try {
                System.out.print(String.format("""
                        Select a %sworkout focus:
                        1) Strength
                        2) Hybrid
                        3) Endurence\n> """, prompt));
                String userIn = scan.nextLine();
                goal = Integer.parseInt(userIn);
            } catch (Exception exe) {
                System.out.println("Invalid input");
            }

        } while (goal < 1 || goal > 3);
        if (goal == 1) {
            System.out.println("You selected Strength");
        } else if (goal == 2) {
            System.out.println("You selected Hybrid");
        } else if (goal == 3) {
            System.out.println("You selected Endurence");
        }
        return goal;
    }

    public int geneticAlgorithumMenu(Scanner scan) {
        String userIn = "";
        int choice = 0;
        do {
            try {
                System.out.print("""
                        \tGenetic Algorithm Menu
                        1) ** Compute and print schedule **
                        2) Change number of rest days
                        3) Change number of generations
                        4) Change starting population
                        5) Change mutation rate
                        6) Change workout focus
                        7) Exit \n> """);
                userIn = scan.nextLine();
                choice = Integer.parseInt(userIn);
            } catch (Exception e) {
                System.out.println("Invalid choice");
            }
        } while (choice <= 0 || choice > 7);
        return choice;
    }

    public boolean processStageGeneticAlgorithum(int choice, Scanner scan, ExerciseDataBase exerciseDataBase,
            GASettings settings, FileInOut fileIO) {

        switch (choice) {
            case 1:
                runGeneticAlgorithum(settings, exerciseDataBase, fileIO);
                return false;
            case 2:
                changeRestDays(scan, settings);
                break;
            case 3:
                changeNumGen(scan, settings);
                break;
            case 4:
                changeStartingPop(scan, settings);
                break;
            case 5:
                changeMutateRate(scan, settings);
                break;
            case 6:
                changeWorkoutFocus(scan, settings);
                break;
            case 7:
                return false;
            default:
                break;
        }
        return true;
    }

    public void runGeneticAlgorithum(GASettings settings, ExerciseDataBase exerciseDataBase, FileInOut fileIO) {
        int goal = settings.getGoal();
        GeneticAlgorithum GA = null;
        if (goal == 1) {
            GA = new StrengthGeneticAlgorithum();
        } else if (goal == 2) {
            GA = new HybridGeneticAlgorithum();
        } else if (goal == 3) {
            GA = new EndurenceGeneticAlgorithum();
        }

        System.out.println("\nProcessing...\n");
        GA.runGeneticAlgorithum(settings, exerciseDataBase.getCompExercises(), exerciseDataBase.getAcesExercises());

        String focusStr = "";
        int focusGoal = settings.getGoal();
        if (focusGoal == 1) {
            focusStr = "Strength";
        } else if (focusGoal == 2) {
            focusStr = "Hybrid";
        } else {
            focusStr = "Endurence";
        }

        System.out.println(
                String.format(
                        "Total generations: %d | Starting population: %d | Mutation rate: %.2f  | Workout Focus: %s",
                        GA.getTotalGenerations(), settings.getStartPopulation(), settings.getMutationRate(), focusStr));
        System.out.println(
                " --- For the weights of exercises. Pick weights where after doing a set you feel as though you could have done 1-2 more. ---");
        System.out.println("To easy go up. To hard go down. Whatever feels comfortable.\n");
        displayFormattedSchedule(GA.getFinalSchedule(), settings.getRestDayIntArray(), settings.getNumRestDays());
        try {
            fileIO.printScheduleToFile(GA.getFinalSchedule(), settings.getNumRestDays(), "FinalSchedule.txt",
                    makeRestDayArray(settings.getRestDayIntArray()));
        } catch (InvalidException e) {
            System.out.println(e);
        }
    }

    public int askUserForInt(Scanner scan, String prompt, int min, int max) {
        // min max inclusive
        String userIn = "";
        int choice = 0;
        do {
            try {
                System.out.print(String.format("%s\n> ", prompt));
                userIn = scan.nextLine();
                if (userIn.isEmpty()) {
                    return -1;
                }
                choice = Integer.parseInt(userIn);
            } catch (Exception e) {
                System.out.println("Invalid choice");
            }
        } while (choice < min || choice > max);
        return choice;

    }

    public double askUserForDouble(Scanner scan, String prompt, int min, int max) {
        // min max inclusive
        String userIn = "";
        Double choice = 0.0;
        do {
            try {
                System.out.print(String.format("%s\n> ", prompt));
                userIn = scan.nextLine();
                if (userIn.isEmpty()) {
                    return -1;
                }
                choice = Double.parseDouble(userIn);
            } catch (Exception e) {
                System.out.println("Invalid choice");
            }
        } while (choice < min || choice > max);
        return choice;

    }

    public void changeRestDays(Scanner scan, GASettings settings) {
        ArrayList<Integer> restDayArray = settings.getRestDayIntArray();
        displayRestDays(makeRestDayArray(restDayArray));
        boolean repeat = true;
        do {
            int choice = askUserForInt(scan,
                    "Enter the index of the day which to rest \nOr input the index of a rest day to negate it. Enter to quit",
                    0, 6);
            if (choice == -1) {
                repeat = false;
            } else {
                int idx = isAlreadyInArray(choice, restDayArray);
                if (idx != -1) {
                    restDayArray.remove(idx);
                } else {
                    restDayArray.add(choice);
                }

                settings.setRestDayIntArray(restDayArray);
                displayRestDays(makeRestDayArray(restDayArray));
            }
        } while (repeat);

    }

    public int isAlreadyInArray(int num, ArrayList<Integer> intArray) {
        for (int i = 0; i < intArray.size(); i++) {
            if (num == intArray.get(i)) {
                return i;
            }
        }
        return -1;
    }

    public void displayRestDays(String[] restDayArray) {
        String outStr = "";
        System.out.println("\nCurrent rest schedule:");
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 7; col++) {
                if (row == 0) {
                    outStr = restDayArray[col];
                } else if (row == 1) {
                    outStr = "-".repeat(10);
                } else {
                    outStr = String.format("%d", col);
                }
                System.out.print(String.format("| %-10s |", outStr));
            }
            System.out.println();
        }
        System.out.println();
    }

    public void changeNumGen(Scanner scan, GASettings settings) {
        int choice = askUserForInt(scan, "Enter new maximum number of generations", 1, 100000);
        settings.setNumGenerations(choice);
    }

    public void changeStartingPop(Scanner scan, GASettings settings) {
        int choice = askUserForInt(scan, "Enter new starting population", 1, 10000);
        settings.setStartPopulation(choice);
    }

    public void changeMutateRate(Scanner scan, GASettings settings) {
        double choice = askUserForDouble(scan, "Enter new mutation rate (typically less than 1)", 0, 1);
        settings.setMutationRate(choice);
    }

    public void changeWorkoutFocus(Scanner scan, GASettings settings) {
        int goal = selectFocusGoal(scan, "new ");
        settings.setGoal(goal);
    }

    public int mutateExerciseMenu(Scanner scan) {
        String userIn = "";
        int userInt = 0;
        do {
            System.out.print("""
                    \tExercise menu:
                    1) Display exercises
                    2) Add exercise
                    3) Remove exercise
                    4) back\n> """);
            userIn = scan.nextLine();
            try {
                userInt = Integer.parseInt(userIn);
            } catch (Exception e) {
                System.out.println("Invalid input");
            }
        } while (userInt < 1 || userInt > 4);

        return userInt;
    }

    public boolean processExerciseChoice(Scanner scan, int choice, ExerciseDataBase exerciseDataBase) {
        switch (choice) {
            case 1:
                displayExercises(exerciseDataBase);
                break;
            case 2:
                addExerciseFromUserInput(scan, exerciseDataBase);
                break;
            case 3:
                removeExerciseFromUserInput(scan, exerciseDataBase);
                break;
            case 4:
                return false;
            default:
                break;
        }
        return true;
    }

    public void displayExercises(ExerciseDataBase exerciseDataBase) {
        ArrayList<Exercise> exers = exerciseDataBase.getExercises();
        for (int i = 0; i < exers.size(); i++) {
            System.out.print(String.format(">- %s \n", exers.get(i).getName()));
        }
    }

    public void addExerciseFromUserInput(Scanner scan, ExerciseDataBase exerciseDataBase) {
        Exercise e = null;
        String userIn = "", name = "", muscGroups = "", minStr = "", maxStr = "", compundStr = "";
        do {
            try {
                System.out.print("Enter a name for a new exercise or enter to quit\n> ");
                name = scan.nextLine();
                if (name.isEmpty()) {
                    return;
                }
                System.out.println("Enter ALL muscles that are targeted no spaces. ALL capitals");
                System.out.print(
                        """
                                Quads (Q), Hamstrings (H), Calves/feet (V), Glutes (G), Core(A), Biceps(B),Triceps(T),
                                Forearms (F), Shoulders (S), Lats (L), UpperBack (U), lowerBack(W), Chest (C)
                                > """);
                muscGroups = scan.nextLine();
                System.out.print("Enter minimum weight no greater than 1500\n> ");
                minStr = scan.nextLine();
                System.out.print("Enter maximum weight no greater than 1500\n> ");
                maxStr = scan.nextLine();
                System.out.println("Is it a compound exercise Y/N?");
                userIn = scan.nextLine();
                if (userIn.trim().equalsIgnoreCase("y")) {
                    compundStr = "C";
                } else {
                    compundStr = "A";
                }
                String[] lineParts = { compundStr, muscGroups, name, minStr, maxStr };
                e = makeExercise(lineParts);
                exerciseDataBase.addExercise(e);

            } catch (InvalidException x) {
                System.out.println(x);
            }
        } while (e == null);
    }

    public Exercise makeExercise(String[] lineParts) throws InvalidException {
        boolean isComp = false;
        Exercise e = null;
        try {
            double min = Double.parseDouble(lineParts[3]);
            double max = Double.parseDouble(lineParts[4]);

            if (lineParts[0].trim().charAt(0) == 'C') {
                isComp = true;
            }

            e = new Exercise(isComp, lineParts[1].trim(), lineParts[2].trim(), min, max);

        } catch (InvalidException x) {
            throw new InvalidException(String.format("%s", x));
        } catch (Exception exception) {
            throw new InvalidException(String.format("%s", exception));
        }
        return e;
    }

    public void removeExerciseFromUserInput(Scanner scan, ExerciseDataBase exerciseDataBase) {
        String userIn = " ";
        do {
            try {
                System.out.print("Enter the name of the exericse remove or enter to quit\n> ");
                userIn = scan.nextLine();
                if (exerciseDataBase.removeExerciseByName(userIn) == null) {
                    System.out.println("Exercise not found");
                } else {
                    System.out.println("Sucessfully removed");
                }
            } catch (Exception e) {
                System.out.println("Invalid input");
            }
        } while (!userIn.isEmpty());
    }

    public void populateUsersToDataBase(UserDataBase base, FileInOut file, String fileName) {
        try {
            // Read from file. Add to user data base.
            base.fillArray(file.getUsers(fileName));
        } catch (InvalidException ex) {
            System.out.println(ex);
        }
    }

    public void populateExercisesToDataBase(ExerciseDataBase base, FileInOut file, String fileName) {
        try {
            base.fillArray(file.getExercises(fileName));
        } catch (InvalidException ex) {
            System.out.println(ex);
        }
    }

    public int signInMenu(Scanner scan) {
        int userInt = 0;
        do {
            System.out.println("""
                    Please Enter an option
                    1) Sign in
                    2) Create new profile""");
            String userIn = scan.nextLine();
            try {
                userInt = Integer.parseInt(userIn);
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice");
                continue;
            }
        } while (userInt != 1 && userInt != 2);

        return userInt;
    }

    public User processSignIn(Scanner scan, int choice, UserDataBase userDataBase) throws InvalidException {
        switch (choice) {
            case 1:
                try {
                    return signInToProfile(scan, userDataBase);
                } catch (InvalidException ex) {
                    throw ex;
                }
            case 2:
                return createNewProfile(scan, userDataBase);

            default:
                return null;
        }
    }

    public User signInToProfile(Scanner scan, UserDataBase userDataBase) throws InvalidException {
        String userIn = " ";
        User u = null;
        while (u == null && !userIn.isEmpty()) {
            try {
                System.out.print("Enter your username or enter\n> ");
                userIn = scan.nextLine();
                u = userDataBase.getUserByUserName(userIn);
                if (u == null) {
                    System.out.println("Could not find user");
                }

            } catch (Exception e) {
                System.out.println(e);
            }
        }
        int signInAttempts = 0;
        while (u != null && !userIn.isEmpty()) {
            if (signInAttempts == 5) {
                throw new InvalidException("To many attempts. Bye Bye!");
            }
            try {
                System.out.print("Enter your password or enter\n> ");
                userIn = scan.nextLine();
                if (!u.matchPass(userIn)) {
                    System.out.println("Incorrect password");
                } else {
                    System.out.println("Success!");
                    return u;
                }
                signInAttempts++;
            } catch (Exception e) {
                System.out.println("Invalid password");
            }
        }

        return null;
    }

    public String askUserForString(Scanner scan, String prompt, int length) {
        String userIn = "";
        do {
            try {
                System.out.print(String.format("%s\n> ", prompt));
                userIn = scan.nextLine();
                if (userIn.length() > length || userIn.isEmpty()) {
                    userIn = "";
                    throw new InvalidException("");
                }
            } catch (InvalidException ex) {
                System.out.println("Invalid input" + userIn);
            } catch (Exception e) {
                System.out.println("Invalid");
            }
        } while (userIn.isEmpty() || userIn.isBlank());

        return userIn;

    }

    public User createNewProfile(Scanner scan, UserDataBase userDataBase) {
        String userName = "", password = "", name = "", genderStr = "";
        char gender = 'M';
        User defaultUser = userDataBase.getUserByUserName("defaultUser");
        User u = null;

        while (u == null) {
            try {
                name = askUserForString(scan, "Enter your name", 20);
                userName = askUserForString(scan, "Enter a valid user name\nHas to be less than 20 characters", 20);
                genderStr = askUserForString(scan, "Enter a gender M/F", 1);
                gender = genderStr.charAt(0);
                password = askUserForString(scan,
                        "Enter a valid password\nHas to be: \n-longer than 8 characters and less than 20\n-Contain one upper case\n-Include one special character (! @ # $ % ^ & * ?) ",
                        20);
                while (!defaultUser.checkValidPass(password)) {
                    password = askUserForString(scan,
                            "Enter a valid password\nHas to be: \n-longer than 8 characters and less than 20\n-Contain one upper case\n-Include one special character (! @ # $ % ^ & * ?) ",
                            20);
                }

                u = new User(userName, password, name, gender);

            } catch (InvalidException e) {
                System.out.println("Could not create new profile" + e);
            }
        }

        int height = 0, weight = 0;
        while (height == 0 || weight == 0) {
            try {
                height = askUserForInt(scan, "Enter a height in inches. Round to nearest whole number", 0,
                        1500);

                weight = askUserForInt(scan, "Enter a weight in pounds. Round to nearest whole number", 0, 1500);
                u.setHeight(height);
                u.setWeight(weight);
            } catch (Exception e) {
                System.out.println("Invalid height or weight");
                weight = 0;
                height = 0;
            }
        }
        userDataBase.addUser(u);
        return u;
    }

    public String[] makeRestDayArray(ArrayList<Integer> indexs) {
        String[] restDayArray = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
        for (int i = 0; i < indexs.size(); i++) {
            restDayArray[indexs.get(i)] = "Rest";
        }
        return restDayArray;
    }

    // restDayArray[int] (0) work out. (1) restday. [sun,mon,tue,wed,thr,fri,sat]
    public void displayFormattedSchedule(Schedule s, ArrayList<Integer> restDayIndexs, int restDays) {
        String[] restDayArray = makeRestDayArray(restDayIndexs);
        System.out.println(String.format("Schedule score: %d\n", s.getFitnessScore()));
        String outStr = "";
        for (int row = 0, exceriseIdx = 0; row < 9; row++) {
            for (int col = 0, idxDay = 0; col < 7; col++) {
                if (row == 0) {
                    outStr = restDayArray[col];
                } else if (row == 1) {
                    outStr = "-".repeat(23);
                } else if (row == 2) {
                    outStr = (restDayArray[col].equalsIgnoreCase("rest")) ? "" : "Warm up";
                } else if ((row % 2) == 1) {

                    if (restDayArray[col].equalsIgnoreCase("rest")) {
                        outStr = "";

                    } else if (idxDay != (7 - restDays) && exceriseIdx < 3) {
                        Exercise e = s.getDayAtIdx(idxDay).getExerciseAtIdx(exceriseIdx);
                        outStr = String.format("%s", e.getName());
                        idxDay++;

                    }

                } else if ((row % 2) == 0) {
                    if (restDayArray[col].equalsIgnoreCase("rest")) {
                        outStr = "";

                    } else if (idxDay != (7 - restDays) && exceriseIdx < 3) {
                        Exercise e = s.getDayAtIdx(idxDay).getExerciseAtIdx(exceriseIdx);
                        outStr = String.format("-> %d X %d", e.getSets(), e.getReps());
                        idxDay++;

                    }

                }

                System.out.print(String.format("| %-23s |", outStr));
            }
            if ((row % 2 == 0) && row > 3) {
                exceriseIdx++;
            }
            System.out.println();
        }
        System.out.println("\n");
    }

    public void printScheduleToFile() {

    }

}// end WorkoutScheduleApp