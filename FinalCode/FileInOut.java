import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class FileInOut {
    public void saveUsers(ArrayList<User> users, String fileName) throws InvalidException {
        try {
            PrintWriter pw = new PrintWriter(fileName);
            for (int i = 0; i < users.size(); i++) {
                User u = users.get(i);
                pw.println(u);
            }
            pw.close();
        } catch (Exception e) {
            throw new InvalidException("Save Unsucessfull");
        }
    }

    public ArrayList<User> getUsers(String fileName) throws InvalidException {
        ArrayList<User> users = new ArrayList<>();
        String errorString = "";
        try {
            Scanner fpnt = new Scanner(new File(fileName));
            while (fpnt.hasNext()) {
                User user = null;
                String line = fpnt.nextLine();
                String[] lineParts = line.split(",");

                try {
                    user = new User(lineParts[0].trim(), lineParts[1].trim(), lineParts[2].trim(),
                            lineParts[3].trim().charAt(0));
                    int heightInt = Integer.parseInt(lineParts[4].trim());
                    user.setHeight(heightInt);
                    int weightInt = Integer.parseInt(lineParts[5].trim());
                    user.setWeight(weightInt);
                    users.add(user);

                } catch (InvalidException x) {
                    errorString = String.join(errorString,
                            String.format("Corrupted data or incorrect format: %s", line));
                    continue;
                }
            }

            fpnt.close();
        } catch (FileNotFoundException e) {
            throw new InvalidException("File not found");
        }

        return users;
    }

    public void saveExercises(ArrayList<Exercise> exercises, String fileName) throws InvalidException {
        try {
            PrintWriter pw = new PrintWriter(fileName);
            for (int i = 0; i < exercises.size(); i++) {
                Exercise e = exercises.get(i);
                pw.println(e);
            }
            pw.close();
        } catch (FileNotFoundException e) {
            throw new InvalidException(String.format("Save unsucessful %s ", fileName));
        }
    }

    public ArrayList<Exercise> getExercises(String fileName) throws InvalidException {
        ArrayList<Exercise> exercises = new ArrayList<>();
        String errorString = "";
        try {
            Scanner fpnt = new Scanner(new File(fileName));
            while (fpnt.hasNext()) {
                Exercise e = null;
                String line = fpnt.nextLine();
                String[] lineParts = line.split(",");

                try {
                    e = makeExercise(lineParts);
                } catch (Exception x) {
                    errorString = String.join(errorString,
                            String.format("Corrupted data or incorrect format: %s", line));
                    continue;
                }
                if (e != null) {
                    exercises.add(e);
                }
            }
            fpnt.close();
        } catch (Exception x) {
            throw new InvalidException(x + errorString);
        }
        return exercises;
    }

    public Exercise makeExercise(String[] lineParts) throws InvalidException {
        boolean isComp = false;
        Exercise e = null;
        try {
            String c = (lineParts[1].trim());
            double min = Double.parseDouble(lineParts[3]);
            double max = Double.parseDouble(lineParts[4]);

            if (lineParts[0].trim().charAt(0) == 'C') {
                isComp = true;
            }

            e = new Exercise(isComp, c, lineParts[2].trim(), min, max);

        } catch (Exception x) {
            throw new InvalidException("Failed");
        }
        return e;
    }

    public void printScheduleToFile(Schedule s, int restDays, String fileName, String[] restDayArray)
            throws InvalidException {
        try {
            PrintWriter pw = new PrintWriter(new File(fileName));
            pw.println("Printed schedule: ");
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

                    pw.print(String.format("| %-23s |", outStr));
                }
                if ((row % 2 == 0) && row > 3) {
                    exceriseIdx++;
                }
                pw.println();
            }
            pw.println("\n");
            pw.close();
        } catch (FileNotFoundException e) {
            throw new InvalidException("File not found.");
        } catch (Exception ex) {
            throw new InvalidException("Print to file failed.");
        }

    }
}
