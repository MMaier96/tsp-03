package main;

public class Scenario {
    private String id;
    private CrossoverMode crossover;
    private double crossoverRatio;
    private MutationMode mutation;
    private double mutationRatio;
    private SelectionMode selection;
    private boolean buildStatistics;
    private boolean isEvaluated;
    private String evaluation;
    private long maximumNumberOfEvaluations;

    public enum SelectionMode {
        ROULETTE_WHEEL,
        TOURNAMENT
    }

    public enum CrossoverMode {
        CYCLE,
        HEURISTIC,
        ORDERED,
        PARTIALLY_MATCHED,
        POSITION,
        SUB_TOUR_EXCHANGE
    }

    public enum MutationMode {
        DISPLACEMENT,
        EXCHANGE,
        HEURISTIC,
        INSERTION,
        INVERSION
    }


    public void setCrossover(CrossoverMode crossover) {
        this.crossover = crossover;
    }

    public CrossoverMode getCrossover() {
        return crossover;
    }

    public double getCrossoverRatio() {
        return crossoverRatio;
    }

    public SelectionMode getSelection() {
        return selection;
    }


    public void setCrossoverRatio(double crossoverRatio) {
        this.crossoverRatio = crossoverRatio;
    }

    public void setMutation(MutationMode mutation) {
        this.mutation = mutation;
    }

    public void setMutationRatio(double mutationRatio) {
        this.mutationRatio = mutationRatio;
    }

    public double getMutationRatio() {
        return mutationRatio;
    }

    public MutationMode getMutation() {
        return mutation;
    }

    public void setSelection(SelectionMode selection) {
        this.selection = selection;
    }

    public void setBuildStatistics(boolean buildStatistics) {
        this.buildStatistics = buildStatistics;
    }


    public boolean isEvaluated() {
        return isEvaluated;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public long getMaximumNumberOfEvaluations() {
        return maximumNumberOfEvaluations;
    }

    public void setEvaluated(boolean isEvaluated) {
        this.isEvaluated = isEvaluated;
    }

    public void setIsEvaluated(boolean isEvaluated) {
        this.isEvaluated = isEvaluated;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

    public void setMaximumNumberOfEvaluations(long maximumNumberOfEvaluations) {
            this.maximumNumberOfEvaluations = maximumNumberOfEvaluations;
    }

    public boolean getIsEvaluated() {
        return isEvaluated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
