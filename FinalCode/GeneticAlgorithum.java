import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public abstract class GeneticAlgorithum {
    private ArrayList<Schedule> schedules;
    private Schedule finalSchedule;
    private int totalGenerations;

    public GeneticAlgorithum() {
        totalGenerations = 0;
        schedules = new ArrayList<>();

    }

    public void runGeneticAlgorithum(GASettings settings, ArrayList<Exercise> compExercises,
            ArrayList<Exercise> acesExercises) {
        initializeSchedules(settings, compExercises, acesExercises);
        fitness();

        Schedule s = null;
        int numGenerations = settings.getNumGenerations();

        while (s == null || (numGenerations > 0 && s.getFitnessScore() < 300)) {
            /* curr best possible is 175??. */

            selection(settings.getStartPopulation());// tourniment style
            breading(settings.getNumRestDays(), settings.getStartPopulation());
            // 2 parents random. switch on random mid point.
            mutate(settings.getMutationRate(), settings.getMaxReps(), settings.getMinSets());
            fitness();

            numGenerations--;
            totalGenerations++;

            int idx = findMaxFitScore(schedules);
            s = schedules.get(idx);

        }
        int idx = findMaxFitScore(schedules);
        this.finalSchedule = schedules.get(idx);
    }

    public void mutate(double mutateRate, int maxReps, int maxSets) {
        for (int i = 0; i < schedules.size(); i++) {
            Schedule s = schedules.get(i);
            Day d = s.getDayAtIdx(randInt(0, s.getArraySize()));

            if (Math.random() < mutateRate) {
                Exercise e = d.getExerciseAtIdx(randInt(0, d.getArraySize()));
                int rand = randInt(0, 2);
                if (rand == 0) {
                    e.setReps(randInt(2, maxReps));
                } else {
                    e.setSets(randInt(2, maxSets));
                }
            }
        }
    }

    public ArrayList<Schedule> makeSchedulesCopy(ArrayList<Schedule> schedules) {
        ArrayList<Schedule> res = new ArrayList<>();
        for (int i = 0; i < schedules.size(); i++) {
            Schedule s = new Schedule(schedules.get(i));// copy the schedule;
            res.add(s);
        }
        return res;
    }

    public void breading(int restDays, int startingPop) {
        ArrayList<Schedule> postBreadArray = new ArrayList<>();
        ArrayList<Schedule> schedulesCopy = makeSchedulesCopy(this.schedules);
        int k = 0;
        while (schedules.size() != 0 && k < 5) {// get and advance the best 5 from the post selection method.
            Schedule s = schedulesCopy.get(findMaxFitScore(schedulesCopy));// get best schedule from schedules.
            s.setFitnessScore(0);// reset fitness

            Schedule parent = new Schedule(s); // copy schedule.
            postBreadArray.add(parent);
            k++;
        }

        while ((schedulesCopy.size() != 0) && (postBreadArray.size() < startingPop)) {
            for (int i = 0; i < 10; i++) {
                Schedule parent1 = new Schedule(schedulesCopy.get(i));
                Schedule parent2 = new Schedule(schedulesCopy.get(i++));

                ArrayList<Day> child = new ArrayList<>();
                double midPoint = Math.floor(randInt(0, parent1.getArraySize()));

                for (int j = 0; j < parent1.getArraySize(); j++) {
                    if (j < midPoint) {
                        child.add(parent1.getDayAtIdx(j));
                    } else {
                        child.add(parent2.getDayAtIdx(j));
                    }
                }
                Schedule grownUpChild = new Schedule(restDays);
                grownUpChild.setArray(child);
                postBreadArray.add(grownUpChild);
            }

        }
        this.schedules = postBreadArray;
    }

    public void selection(int startingPop) {
        double sizeOfTourniments = Math.floor(startingPop / 20);
        // the divisior is the number of terms left after the selction

        ArrayList<Schedule> postSelctionSchedules = new ArrayList<Schedule>();
        ArrayList<Schedule> currTourniment = new ArrayList<Schedule>();

        while (!schedules.isEmpty() && schedules.size() > sizeOfTourniments) {
            for (int i = 0; i < sizeOfTourniments; i++) {

                currTourniment.add(schedules.remove(randInt(0, schedules.size())));
                // get a random schedule from schedules and add it to the tourniment
            }
            int idx = findMaxFitScore(currTourniment);// find winner of tourniment
            postSelctionSchedules.add(currTourniment.get(idx));// add them to the post selection
            currTourniment = new ArrayList<Schedule>();// reseet current tourniment
        }

        this.schedules = postSelctionSchedules;// repopulate the schedule array with the winners
    }

    public int findMaxFitScore(ArrayList<Schedule> schedules) {
        int idx = 0;
        int max = 0;
        for (int i = 0; i < schedules.size(); i++) {
            int score = schedules.get(i).getFitnessScore();
            if (max < score) {
                max = score;
                idx = i;
            }
        }

        return idx;
    }

    // no same exciercies on consecutive days
    // simalar muscle groups per day
    public void fitness() {
        for (int i = 0; i < schedules.size(); i++) {
            Schedule s = schedules.get(i);// get the schedule

            if (hitsAllMuscleGroups(s)) {
                s.incrimentFitScore(10);
            } else {
                s.decrementFitScore(10);
            }
            if (hasDuplicateExercise(s)) {
                s.decrementFitScore(20);
            } else {
                s.incrimentFitScore(10);
            }
            scoreSimalarMuscle(s);
            scoreConsecutiveExercises(s);
            ScoreExercises(s);
        }

    }

    public void scoreSimalarMuscle(Schedule s) {
        for (int i = 0; i < s.getArraySize(); i++) {
            Day d = s.getDayAtIdx(i);
            Set<String> strSet = new HashSet<>();
            for (int j = 0; j < d.getArraySize(); j++) {
                Exercise e = d.getExerciseAtIdx(j);// get exercise
                String muscleStr = e.getmuscGroupTargeted(); // get muscle string
                String[] parts = muscleStr.split(""); // split it

                for (int k = 0; k < muscleStr.length(); k++) {// add the individual characters to the set
                    if (!strSet.add(parts[k])) {// if the character is already in the set
                        s.incrimentFitScore(5);
                    }
                }

            }
        }
    }

    public void scoreConsecutiveExercises(Schedule s) {
        Set<String> prevExercises = new HashSet<>();
        for (int i = 0; i < s.getArraySize(); i++) {
            Day d = s.getDayAtIdx(i);
            for (int j = 0; j < d.getArraySize(); j++) {
                Exercise e = d.getExerciseAtIdx(j);
                String eNameStr = e.getName();

                if (!prevExercises.add(eNameStr)) {
                    s.decrementFitScore(10);
                } else {
                    s.incrimentFitScore(1);
                }
            }
        }
    }

    public abstract void ScoreExercises(Schedule s);

    public boolean hasDuplicateExercise(Schedule s) {
        Set<Exercise> exerciseSet = new HashSet<>();// create empty hashSet
        for (int i = 0; i < s.getArraySize(); i++) {
            Day d = s.getDayAtIdx(i);// get a day
            for (int j = 0; j < d.getArraySize(); j++) {
                Exercise e = d.getExerciseAtIdx(j); // get an exercise
                if (!exerciseSet.add(e)) {// if it cant add it then duplicate exists
                    return true;
                }
            }
        }

        return false;
    }

    public boolean hitsAllMuscleGroups(Schedule s) {
        String muscleGroups = "VQHGACFBTSWUL";

        for (int i = 0; i < s.getArraySize(); i++) {
            Day d = s.getDayAtIdx(i); // Get day from schedule

            for (int j = 0; j < d.getArraySize(); j++) {// loop throught the days exercises

                Exercise e = d.getExerciseAtIdx(j);
                String muscleString = e.getmuscGroupTargeted();
                for (int k = 0; k < muscleString.length(); k++) {
                    muscleGroups = muscleGroups.replace(muscleString.charAt(k), ' ');
                }

                // try and replace the muscle group of the exercise with an empty space.
                // if sucessfull the muscle group string will have one of the "groups" removed
                // if not it will be the same string as before
            }

            if (muscleGroups.isBlank()) {// check if the muscle group string is empty
                return true;
            }

        }
        return false;
    }

    public void initializeSchedules(GASettings settings, ArrayList<Exercise> compExercises,
            ArrayList<Exercise> acesExercises) {

        for (int i = 0; i < settings.getStartPopulation(); i++) {
            Schedule s = new Schedule(settings.getNumRestDays());
            s.fillArray(compExercises, acesExercises, settings.getMaxSets(), settings.getMinSets(),
                    settings.getMaxReps(), settings.getMinReps());
            schedules.add(s);
        }
    }

    public int randInt(int min, int max) {
        // min inclusive max excusive
        int randInt = (int) ((Math.random() * max) + min);
        return randInt;
    }

    public Schedule getFinalSchedule() {
        return finalSchedule;
    }

    public int getTotalGenerations() {
        return totalGenerations;
    }

}
