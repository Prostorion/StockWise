package com.example.stockwise.task;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AStarShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class SimpleGA {

    Population population = new Population();
    Individual fittest;
    Individual secondFittest;
    int generationCount = 0;

    int size;

    public SimpleGA(int size) {
        this.size = size;
    }

    public static List<GraphPath<String, DefaultWeightedEdge>> optimize(AStarShortestPath<String, DefaultWeightedEdge> aStar, List<String> vertices) {

        Random rn = new Random();

        SimpleGA demo = new SimpleGA(vertices.size());

        //Initialize population
        demo.population.initializePopulation(vertices);

        //Calculate fitness of each individual
        demo.population.calculateFitness(aStar);

        demo.population.getMinimumWeight();

        //While population gets an individual with maximum fitness
        double currentMin = Double.MAX_VALUE;
        boolean toStop = false;
        for (int i = 0; i < 1000 && !toStop; i++) {


            ++demo.generationCount;

            //Do selection
            demo.selection();

            //Do crossover
            demo.crossover();

            //Do mutation under a random probability
            if (rn.nextInt() % 7 < 5) {
                demo.mutation();
            }

            //Add minimumWeight offspring to population
            demo.addFittestOffspring(aStar);

            //Calculate new fitness value
            demo.population.calculateFitness(aStar);
            System.out.println(demo.population.getMinimumWeight().fitness);
            if (i % 50 == 0) {
                if (currentMin == demo.population.getMinimumWeight().fitness) {
                    toStop = true;
                } else {
                    currentMin = demo.population.getMinimumWeight().fitness;
                }
            }
        }


        System.out.print(demo.population.getMinimumWeight().genes);
        List<String> gen = demo.population.getMinimumWeight().genes;
        List<GraphPath<String, DefaultWeightedEdge>> result = new ArrayList<>();
        for (int i = 0; i < gen.size() - 1; i++) {
            result.add(aStar.getPath(gen.get(i), gen.get(i + 1)));
        }
        result.add(aStar.getPath(gen.get(gen.size() - 1), gen.get(0)));
        return result;

    }

    //Selection
    void selection() {

        //Select the most minimumWeight individual
        fittest = population.getMinimumWeight();

        //Select the second most minimumWeight individual
        secondFittest = population.getSecondFittest();
    }

    //Crossover
    void crossover() {
        Random rn = new Random();

        //Select a random crossover point
        int crossOverPoint = rn.nextInt(population.individuals[0].genes.size() - 1) + 1;
        List<String> currentGenes = new ArrayList<>();
        for (int i = 0; i < crossOverPoint; i++) {
            currentGenes.add(fittest.genes.get(i));
        }
        Individual randInd = population.individuals[rn.nextInt(population.individuals.length)];
        for (int i = crossOverPoint; i < fittest.genes.size(); i++) {
            boolean swapped = false;
            int index = 0;
            while (!swapped) {
                if (!currentGenes.contains(randInd.genes.get(index))) {
                    currentGenes.add(randInd.genes.get(index));
                    swapped = true;
                }
                index++;
            }

        }
        population.individuals[rn.nextInt(population.individuals.length)].genes = new ArrayList<>(List.copyOf(currentGenes));

    }

    //Mutation
    void mutation() {
        Random rn = new Random();

        //Select a random mutation point
        int mutationPoint1 = rn.nextInt(population.individuals[0].genes.size() - 1) + 1;
        int mutationPoint2 = rn.nextInt(population.individuals[0].genes.size() - 1) + 1;

        //Flip values at the mutation point
        String temp = fittest.genes.get(mutationPoint1);
        fittest.genes.set(mutationPoint1, fittest.genes.get(mutationPoint2));
        fittest.genes.set(mutationPoint2, temp);

        mutationPoint1 = rn.nextInt(population.individuals[0].genes.size() - 1) + 1;
        mutationPoint2 = rn.nextInt(population.individuals[0].genes.size() - 1) + 1;

        //Flip values at the mutation point
        temp = secondFittest.genes.get(mutationPoint1);
        secondFittest.genes.set(mutationPoint1, secondFittest.genes.get(mutationPoint2));
        secondFittest.genes.set(mutationPoint2, temp);
    }

    //Get minimumWeight offspring
    Individual getFittestOffspring() {
        if (fittest.fitness > secondFittest.fitness) {
            return fittest;
        }
        return secondFittest;
    }


    //Replace least minimumWeight individual from most minimumWeight offspring
    void addFittestOffspring(AStarShortestPath<String, DefaultWeightedEdge> aStar) {

        //Update fitness values of offspring
        fittest.calcFitness(aStar);
        secondFittest.calcFitness(aStar);

        //Get index of least fit individual
        int leastFittestIndex = population.getWorstIndex();

        //Replace least minimumWeight individual from most minimumWeight offspring
        population.individuals[leastFittestIndex] = getFittestOffspring();
    }

}


//Individual class
class Individual implements Cloneable {

    double fitness = 0;
    List<String> genes = new ArrayList<>();

    public Individual(List<String> vertices) {
        List<String> verticesNew = new ArrayList<>(List.copyOf(vertices));
        Random rn = new Random();
        genes.add(verticesNew.get(0));
        verticesNew.remove(0);
        //Set genes randomly for each individual
        for (int i = 1; i < vertices.size(); i++) {
            int n = rn.nextInt(verticesNew.size());
            genes.add(verticesNew.get(n));
            verticesNew.remove(n);
        }

        fitness = 0;
    }

    //Calculate fitness
    public void calcFitness(AStarShortestPath<String, DefaultWeightedEdge> aStar) {

        fitness = 0;
        for (int i = 0; i < genes.size() - 1; i++) {
            fitness += aStar.getPath(genes.get(i), genes.get(i + 1)).getWeight();
        }
        fitness += aStar.getPath(genes.get(0), genes.get(genes.size() - 1)).getWeight();
    }


    @Override
    protected Object clone() throws CloneNotSupportedException {
        Individual individual = (Individual) super.clone();
        individual.genes = new ArrayList<>(List.copyOf(this.genes));
        return individual;
    }
}

//Population class
class Population {

    int popSize = 10;
    Individual[] individuals = new Individual[10];
    double minimumWeight = 0;

    //Initialize population
    public void initializePopulation(List<String> vertices) {
        for (int i = 0; i < individuals.length; i++) {
            individuals[i] = new Individual(vertices);
        }
    }

    //Get the minimumWeight individual
    public Individual getMinimumWeight() {
        double minWeight = Double.MAX_VALUE;
        int minWeightIndex = 0;
        for (int i = 0; i < individuals.length; i++) {
            if (minWeight >= individuals[i].fitness) {
                minWeight = individuals[i].fitness;
                minWeightIndex = i;
            }
        }
        minimumWeight = individuals[minWeightIndex].fitness;
        try {
            return (Individual) individuals[minWeightIndex].clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Get the second most minimumWeight individual
    public Individual getSecondFittest() {
        int minWeight1 = 0;
        int minWeight2 = 0;
        for (int i = 0; i < individuals.length; i++) {
            if (individuals[i].fitness < individuals[minWeight1].fitness) {
                minWeight2 = minWeight1;
                minWeight1 = i;
            } else if (individuals[i].fitness < individuals[minWeight2].fitness) {
                minWeight2 = i;
            }
        }
        try {
            return (Individual) individuals[minWeight2].clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Get index of least minimumWeight individual
    public int getWorstIndex() {
        double maxWeight = Double.MIN_VALUE;
        int maxWeightIndex = 0;
        for (int i = 0; i < individuals.length; i++) {
            if (maxWeight < individuals[i].fitness) {
                maxWeight = individuals[i].fitness;
                maxWeightIndex = i;
            }
        }
        return maxWeightIndex;
    }

    //Calculate fitness of each individual
    public void calculateFitness(AStarShortestPath<String, DefaultWeightedEdge> aStar) {
        for (Individual individual : individuals) {
            individual.calcFitness(aStar);
        }
    }

}