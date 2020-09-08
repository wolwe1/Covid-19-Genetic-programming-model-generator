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
        MSEStats trainingStats = null;
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

        CovidTreeManager trainSetTreeManager = new CovidTreeManager(fitnessFunction,0,functionalSet,statisticsHandler,covidEntries,CovidPredictionMode.Cases);
        trainSetTreeManager.setProblemValues(16,2);
        fileReader.setCountry("Nigeria");
        //var testCovidEntries = fileReader.getData();
        //System.out.println("Successfully loaded file,"+ covidEntries.size() + " entries read.");

        //CovidTreeManager testTreeManager = new CovidTreeManager(fitnessFunction,0,functionalSet,statisticsHandler,testCovidEntries,CovidPredictionMode.Cases);
        //testTreeManager.setProblemValues(32,2);

        trainingStats = new MSEStats(trainSetTreeManager.getTerminals(),2);
        //var testingStats = new MSEStats(testTreeManager.getTerminals(),2);

        GeneticAlgorithm<Double> geneticAlgorithm = new GeneticAlgorithm<>(1500,2,trainSetTreeManager);

        geneticAlgorithm.setRates(0.5,0.3);
        geneticAlgorithm.setNumberOfGenerations(1000);
        geneticAlgorithm.setPrint(true);

        System.out.println("Testing on size 16 trees");
        for (int i = 0; i < 10; i++) {
            trainSetTreeManager.setSeed(i);

            var bestTree = geneticAlgorithm.run();
            bestTrees.add(bestTree);
            geneticAlgorithm.resetPopulation();
            System.out.println("Run "+ i + " completed");
        }


        trainingStats.createNew(bestTrees);
        trainingStats.print();
        var overallBestTree = printWinner(bestTrees);

        trainSetTreeManager.printTreeEstimates(overallBestTree);

//        System.out.println("Testing");
//        var list = new ArrayList<PopulationMember<Double>>();
//        list.add(overallBestTree);
//        testingStats.createNew(list);
//        testingStats.print();
//        testTreeManager.printTreeEstimates(overallBestTree);
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
