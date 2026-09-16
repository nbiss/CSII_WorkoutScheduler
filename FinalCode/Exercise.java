import java.util.Set;

public class Exercise {
    private boolean isCompoundLift;
    private String muscGroupTargeted;
    private String name;
    private double minWeight;
    private double maxWeight;
    private int sets;
    private int reps;

    /*
     * groups:
     * Quads (Q), Hamstrings (H), Calves/feet (V), Glutes (G), Core(A), Biceps(B),
     * Triceps(T),Forearms (F), Shoulders (S), Lats (L), UpperBack (U), lowerBack
     * (W), Chest (C)
     */

    Set<Character> validMuscGroup = Set.of('V', 'Q', 'H', 'G', 'A', 'C', 'F', 'B', 'T', 'S', 'W', 'U', 'L');

    public Exercise(boolean isComp, String muscGroupTargeted, String name, double min, double max)
            throws InvalidException {
        // set is compund lift
        if (isComp) {
            this.isCompoundLift = true;
        } else {
            this.isCompoundLift = false;
        }
        // check if valid muscle group. set it
        if (muscGroupTargeted == null || !isValidMuscGroup(muscGroupTargeted)) {
            throw new InvalidException(String.format("Invalid Exericse muscGroupTargeted %s", muscGroupTargeted));
        } else {
            this.muscGroupTargeted = muscGroupTargeted;
        }
        // check if name is valid. set it
        if (name == null || name.isBlank() || name.length() > 20) {
            throw new InvalidException("Invalid Exercise name");
        } else {
            this.name = name;
        }
        // min max < 1500. set it
        if (min < 0 || min > 1500) {
            throw new InvalidException(String.format("Invalid Exercise weight %f", min));
        } else {
            this.minWeight = min;
        }
        if (max < 0 || max > 1500) {
            throw new InvalidException(String.format("Invalid Exercise weight %f", max));
        } else {
            this.maxWeight = max;
        }

    }// end constructor

    public Exercise(Exercise e) {
        this.isCompoundLift = e.isCompoundLift();
        this.muscGroupTargeted = e.getmuscGroupTargeted();
        this.name = e.getName();
        this.minWeight = e.getMin();
        this.maxWeight = e.getMax();
        this.sets = e.getSets();
        this.reps = e.getReps();
    }

    public boolean isValidMuscGroup(String muscle) {
        for (int i = 0; i < muscle.length(); i++) {
            if (!validMuscGroup.contains(muscle.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        if (sets < 0 || sets > 100) {
            sets = 1;
        }
        this.sets = sets;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        if (reps < 0 || reps > 100) {
            reps = 1;
        }
        this.reps = reps;
    }

    public double calcTime() {
        double time = 0.0;
        return time;
    }

    public String getmuscGroupTargeted() {
        return muscGroupTargeted;
    }

    public boolean isCompoundLift() {
        return isCompoundLift;
    }

    public String getName() {
        return name;
    }

    public double getMin() {
        return minWeight;
    }

    public double getMax() {
        return maxWeight;
    }

    public String toString() {
        return String.format("%s, %s, %s, %.1f, %.1f", (isCompoundLift) ? "C" : "A", getmuscGroupTargeted(), getName(),
                getMin(),
                getMax());
    }

}
