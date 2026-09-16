import java.util.ArrayList;

public class Day {
    private ArrayList<Exercise> dayExercises;

    public Day() {
        dayExercises = new ArrayList<>();
    }

    public Day(Day d) {
        dayExercises = new ArrayList<>();
        for (int i = 0; i < d.getArraySize(); i++) {
            Exercise e = new Exercise(d.getExerciseAtIdx(i));
            addExercise(e);
        }

    }

    public Exercise getExerciseAtIdx(int idx) {
        if (idx < 0) {
            return null;
        }
        return dayExercises.get(idx);
    }

    public void replaceExerciseAtIdx(Exercise e, int idx) {
        dayExercises.set(idx, e);
    }

    public void addExercise(Exercise e) {
        dayExercises.add(e);
    }

    public int getArraySize() {
        return dayExercises.size();
    }

}
