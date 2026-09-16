import java.util.ArrayList;

public class ExerciseDataBase {
    private ArrayList<Exercise> exercises;
    private ArrayList<Exercise> compExercises;
    private ArrayList<Exercise> acesExercises;

    public ExerciseDataBase() {
        exercises = new ArrayList<>();
        compExercises = new ArrayList<>();
        acesExercises = new ArrayList<>();
    }

    private void splitExercises() {
        for (int i = 0; i < exercises.size(); i++) {
            Exercise e = exercises.get(i);
            if (e.isCompoundLift()) {
                compExercises.add(e);
            } else {
                acesExercises.add(e);
            }
        }
    }

    public void addExercise(Exercise e) {
        exercises.add(e);
        splitExercises();
    }

    public Exercise getExerciseByName(String name) {
        Exercise e = null;
        for (int i = 0; i < exercises.size(); i++) {
            e = exercises.get(i);
            if (e != null && e.getName().equals(name)) {
                return e;
            }
        }
        return null;
    }

    public Exercise removeExerciseByName(String name) {
        Exercise e = null;
        for (int i = 0; i < exercises.size(); i++) {
            e = exercises.get(i);
            if (e != null && e.getName().equals(name)) {

                Exercise x = exercises.remove(i);
                splitExercises();
                return x;
            }
        }
        return null;

    }

    public void fillArray(ArrayList<Exercise> exercises) {
        for (int i = 0; i < exercises.size(); i++) {
            Exercise e = exercises.get(i);
            this.exercises.add(e);
        }
        splitExercises();
    }

    public ArrayList<Exercise> getCompExercises() {
        return compExercises;
    }

    public ArrayList<Exercise> getAcesExercises() {
        return acesExercises;
    }

    public ArrayList<Exercise> getExercises() {
        return exercises;
    }
}
