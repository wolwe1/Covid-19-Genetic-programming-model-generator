import data.infrastructure.Covid19FileReader;
import data.models.CovidPredictionMode;
import gpLibrary.concepts.FunctionalSet;
import gpLibrary.infrastructure.GeneticAlgorithm;
import gpLibrary.infrastructure.ITreeManager;
import helpers.ArtDrawer;
import helpers.FileManager;
import solution.infrastructure.Covid19FitnessFunction;
import solution.infrastructure.CovidTreeManager;
import solution.infrastructure.primitives.AddFunction;
import solution.infrastructure.primitives.DivisionFunction;
import solution.infrastructure.primitives.MultiplicationFunction;
import solution.infrastructure.primitives.SubtractFunction;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    public static Covid19FileReader greet() throws Exception {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        ArtDrawer drawer = new ArtDrawer();
        drawer.drawLine();
        drawer.draw("COVID-19 Forecaster");
        drawer.drawLine();

        FileManager fileManager = new FileManager();
        fileManager.setupDirectories();

        try{
            Covid19FileReader fileReader = new Covid19FileReader(fileManager.baseDataDirectory);
            fileReader.readFile(fileManager.fileName);

            return fileReader;
        }catch (Exception e){
            throw new Exception("Unable to successfully load file: " + e.getMessage());
        }

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

        CovidTreeManager treeManager = new CovidTreeManager(fitnessFunction,seed,functionalSet,covidEntries);
        treeManager.setProblemValues(4,2);
        treeManager.predictOn(CovidPredictionMode.Cases);
        GeneticAlgorithm<Double> geneticAlgorithm = new GeneticAlgorithm<>(2,treeManager);

        geneticAlgorithm.setRates(0.3,0.3);
        geneticAlgorithm.setNumberOfGenerations(2);
        geneticAlgorithm.setPrint(true);

        geneticAlgorithm.run();

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
}
