import data.infrastructure.Covid19FileReader;
import data.models.CovidPredictionMode;
import gpLibrary.concepts.FunctionalSet;
import gpLibrary.infrastructure.GeneticAlgorithm;
import gpLibrary.infrastructure.ITreeManager;
import gpLibrary.infrastructure.Statistics;
import gpLibrary.primitives.other.PopulationMember;
import helpers.ArtDrawer;
import helpers.FileManager;
import solution.infrastructure.Covid19FitnessFunction;
import solution.infrastructure.CovidTreeManager;
import solution.infrastructure.primitives.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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

        Statistics<Double> statisticsHandler = new Statistics<>(new BasicStats());

        CovidTreeManager treeManager = new CovidTreeManager(fitnessFunction,seed,functionalSet,statisticsHandler,covidEntries,CovidPredictionMode.Cases);
        treeManager.setProblemValues(16,2);
        GeneticAlgorithm<Double> geneticAlgorithm = new GeneticAlgorithm<>(100,2,treeManager);

        geneticAlgorithm.setRates(0.3,0.3);
        geneticAlgorithm.setNumberOfGenerations(5);
        geneticAlgorithm.setPrint(true);

        var bestTrees = geneticAlgorithm.run();

        printWinner(bestTrees);

        //Do work

//        NodeTree<Double> tree = new NodeTree<>(3,2);
//        GeneticFunction<Double> addFunc = new AddFunction("Add 1");
//        GeneticFunction<Double> addFunc2 = new AddFunction("Add 2");
//        GeneticFunction<Double> addFunc3 = new AddFunction("Add 3");
//        CovidReport terminal1 = new CovidReport(2,"T 1");
//        CovidReport terminal2 = new CovidReport(2,"T 2");
//        CovidReport terminal3 = new CovidReport(2,"T 3");
//        CovidReport terminal4 = new CovidReport(2,"T 4");
//
//        terminal1.confirmedCases = 1;
//        terminal2.confirmedCases = 1;
//        terminal3.confirmedCases = 1;
//        terminal4.confirmedCases = 1;
//
//        tree.addNode(addFunc);
//        tree.addNode(addFunc2);
//        tree.addNode(addFunc3);
//        tree.addNode(terminal1);
//        tree.addNode(terminal2);
//        tree.addNode(terminal3);
//        tree.addNode(terminal4);
//
//        TreePrinter treePrinter = new TreePrinter(2,3);
//
//        //tree.VisitTree(treePrinter);
//        //treePrinter.print();
//        //System.out.println("Tree value : " + tree.Root.getValue());
//        treePrinter.drawTree(tree.Root);
    }

    private static void printWinner(List<PopulationMember<Double>> bestTrees) {
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
    }
}
