public class StrengthGeneticAlgorithum extends GeneticAlgorithum {
    public StrengthGeneticAlgorithum() {
        super();
    }

    @Override
    public void ScoreExercises(Schedule s) {
        for (int i = 0; i < s.getArraySize(); i++) {
            Day d = s.getDayAtIdx(i); // Get day from schedule

            for (int j = 0; j < d.getArraySize(); j++) {// loop throught the day exercises
                Exercise e = d.getExerciseAtIdx(j); // get exercise
                int sets = e.getSets();
                int reps = e.getReps();
                // check and assign points based on number of sets
                if (sets < 2) {
                    s.incrimentFitScore(3);
                } else if (sets < 4) {
                    s.incrimentFitScore(5);
                } else if (sets < 6) {
                    s.decrementFitScore(3);
                } else {
                    s.decrementFitScore(5);
                }
                // check and assign points based on number of reps
                if (reps < 5) {
                    s.incrimentFitScore(5);
                } else if (reps < 8) {
                    s.incrimentFitScore(3);
                } else if (reps < 12) {
                    s.decrementFitScore(1);
                } else if (reps < 15) {
                    s.decrementFitScore(3);
                } else {
                    s.decrementFitScore(5);
                }

            }
        }
    }
}
