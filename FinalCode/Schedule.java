import java.util.ArrayList;

public class Schedule {
    private int fitnessScore;
    private ArrayList<Day> days;

    public Schedule(int numRestDays) {
        days = new ArrayList<>();
        for (int i = 0; i < (7 - numRestDays); i++) {
            Day d = new Day();
            days.add(d);
        }
    }

    public Schedule(Schedule s) {
        ArrayList<Day> res = new ArrayList<>();
        for (int i = 0; i < s.getArraySize(); i++) {
            Day d = new Day(s.getDayAtIdx(i));// copy day
            res.add(d);
        }
        this.setArray(res);
    }

    public void fillArray(ArrayList<Exercise> compExercises, ArrayList<Exercise> acesExercises, int maxSets,
            int minSets,
            int maxReps, int minReps) {
        Exercise e = null, ex = null;
        for (int i = 0; i < days.size(); i++) {
            Day day = days.get(i);
            // get random compund exercise from array
            e = getRandCompleteExerciseFromArray(compExercises, maxSets, minSets, maxReps, minReps);
            day.addExercise(e);

            // get rand accessory excerise from array
            e = getRandCompleteExerciseFromArray(acesExercises, maxSets, minSets, maxReps, minReps);
            day.addExercise(e);
            ex = getRandCompleteExerciseFromArray(acesExercises, maxSets, minSets, maxReps, minReps);
            if (ex.getName().equalsIgnoreCase(e.getName())) {
                ex = getRandCompleteExerciseFromArray(acesExercises, maxSets, minSets, maxReps, minReps);
            }
            day.addExercise(ex);

        }
    }

    public Exercise getRandCompleteExerciseFromArray(ArrayList<Exercise> exercises, int maxSets, int minSets,
            int maxReps, int minReps) {
        Exercise e = null;
        e = exercises.get(randInt(0, exercises.size()));
        e.setReps(randInt(minReps, maxReps));
        e.setSets(randInt(minSets, maxSets));
        return e;
    }

    public int randInt(int min, int max) {
        // min inclusive max excusive
        int randInt = (int) ((Math.random() * max) + min);
        return randInt;
    }

    // getters and setters
    public int getFitnessScore() {
        return fitnessScore;
    }

    public void setFitnessScore(int fitnessScore) {
        this.fitnessScore = fitnessScore;
    }

    public void incrimentFitScore(int score) {
        if (score < 0) {
            score = 0;
        }
        this.fitnessScore += score;
    }

    public void decrementFitScore(int score) {
        if ((fitnessScore - score) < 0) {
            fitnessScore = 0;
        } else {
            fitnessScore -= score;
        }
    }

    public int getArraySize() {
        return days.size();
    }

    public Day getDayAtIdx(int idx) {
        return days.get(idx);
    }

    public void setArray(ArrayList<Day> array) {
        this.days = array;
    }

    public String toString() {
        return String.format("%d", getArraySize());
    }
}
