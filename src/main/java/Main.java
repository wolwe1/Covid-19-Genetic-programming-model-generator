import data.infrastructure.Covid19FileReader;
import data.models.CovidPredictionMode;
import gpLibrary.concepts.FunctionalSet;
import gpLibrary.infrastructure.GeneticAlgorithm;
import gpLibrary.infrastructure.Statistics;
import gpLibrary.primitives.other.PopulationMember;
import helpers.ArtDrawer;
import helpers.FileManager;
import helpers.TreePrinter;
import solution.infrastructure.Covid19FitnessFunction;
import solution.infrastructure.CovidTreeManager;
import solution.infrastructure.functions.primitives.*;
import solution.infrastructure.stats.BasicStats;
import solution.infrastructure.stats.MSEStats;

import java.util.ArrayList;
import java.util.List;

public class Main {

    static ArtDrawer drawer = new ArtDrawer();
    public static Covid19FileReader greet() throws Exception {

        drawer.drawLine();
        drawer.draw("COVID-19 Forecaster");
        drawer.drawLine();

        FileManager fileManager = new FileManager();
        fileManager.setupDirectories();

        Covid19FileReader fileReader = new Covid19FileReader(fileManager.baseDataDirectory);
        try{
            fileReader.readFile(fileManager.fileName);
        }catch (Exception e){
            throw new Exception("Unable to successfully load file: " + e.getMessage());
        }

        String country = fileManager.getCountry();

        while( !fileReader.setCountry(country)){
            System.out.println("Country not found");
            country = fileManager.getCountry();
        }

        return fileReader;
    }

    public static void main(String[] args) throws Exception {

        int seed = 1;
        List<PopulationMember<Double>> bestTrees = new ArrayList<>();
        MSEStats stats = null;
        //Load data
        Covid19FileReader fileReader = greet();
        var covidEntries = fileReader.getData();
        System.out.println("Successfully loaded file,"+ covidEntries.size() + " entries read.");

        //Create components
        Covid19FitnessFunction fitnessFunction = new Covid19FitnessFunction(2);
        FunctionalSet<Double> functionalSet = new FunctionalSet<>();
        functionalSet.addFunction( new AddFunction());
        functionalSet.addFunction( new SubtractFunction());
        functionalSet.addFunction( new MultiplicationFunction());
        functionalSet.addFunction( new DivisionFunction());
        //functionalSet.addFunction(new ChangeFunction());

        Statistics<Double> statisticsHandler = new Statistics<>(new BasicStats());

        CovidTreeManager treeManager = new CovidTreeManager(fitnessFunction,0,functionalSet,statisticsHandler,covidEntries,CovidPredictionMode.Cases);
        treeManager.setProblemValues(16,2);
        stats = new MSEStats(treeManager.getTerminals(),2);

        GeneticAlgorithm<Double> geneticAlgorithm = new GeneticAlgorithm<>(500,2,treeManager);

        geneticAlgorithm.setRates(0.6,0.2);
        geneticAlgorithm.setNumberOfGenerations(50);
        geneticAlgorithm.setPrint(true);

        for (int i = 0; i < 3; i++) {
            treeManager.setSeed(i);

            var bestTree = geneticAlgorithm.run();
            bestTrees.add(bestTree);
            geneticAlgorithm.resetPopulation();
        }

        stats.createNew(bestTrees);
        stats.print();
        var overallBestTree = printWinner(bestTrees);

        treeManager.printTreeEstimates(overallBestTree);
        //Do work
    }

    private static PopulationMember<Double> printWinner(List<PopulationMember<Double>> bestTrees) throws Exception {
        drawer.drawLine();
        drawer.draw("Winner!");
        drawer.drawLine();

        double bestFitness = Double.POSITIVE_INFINITY;
        PopulationMember<Double> ultimateBestTree = null;
                
        for (PopulationMember<Double> tree : bestTrees) {
            if(tree.fitness < bestFitness){
                bestFitness = tree.fitness;
                ultimateBestTree = tree;
            }
        }
        System.out.println("Combination : " + ultimateBestTree.id);
        System.out.println("Fitness : " + ultimateBestTree.fitness);

        TreePrinter printer = new TreePrinter();
        printer.drawTree(ultimateBestTree.tree.root);

        return ultimateBestTree;
    }
}
