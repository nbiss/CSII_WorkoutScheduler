import java.util.ArrayList;

public class GASettings {
    private int goal; // 1 Strength, 2 Hybrid, 3, Endurence
    private int numGenerations;
    private int startPopulation;
    private double mutationRate;
    private ArrayList<Integer> restDayIntArray;
    private int maxSets;
    private int minSets;
    private int maxReps;
    private int minReps;

    public GASettings() {
        goal = 0;
        numGenerations = 50;
        startPopulation = 500;
        mutationRate = 0.2;
        restDayIntArray = new ArrayList<>();
        restDayIntArray.add(0);
        restDayIntArray.add(3);
        restDayIntArray.add(6);
        maxSets = 8;
        minSets = 2;
        maxReps = 20;
        minReps = 2;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public int getNumGenerations() {
        return numGenerations;
    }

    public void setNumGenerations(int numGenerations) {
        this.numGenerations = numGenerations;
    }

    public int getStartPopulation() {
        return startPopulation;
    }

    public void setStartPopulation(int startPopulation) {
        this.startPopulation = startPopulation;
    }

    public double getMutationRate() {
        return mutationRate;
    }

    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    public ArrayList<Integer> getRestDayIntArray() {
        return restDayIntArray;
    }

    public void setRestDayIntArray(ArrayList<Integer> restDayIntArray) {
        this.restDayIntArray = restDayIntArray;
    }

    public int getMaxSets() {
        return maxSets;
    }

    public int getMinSets() {
        return minSets;
    }

    public int getMaxReps() {
        return maxReps;
    }

    public int getMinReps() {
        return minReps;
    }

    public int getNumRestDays() {
        return restDayIntArray.size();
    }

}
