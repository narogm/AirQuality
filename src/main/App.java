package main;


import org.apache.commons.cli.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;


/**
 * App that download necessary records from chosen service
 * and then execute chosen functionalities on them
 *
 * @author Mateusz Naróg
 */
public class App {

    public static void main(String[] args) {
        App app = new App();
        CommandLineParser commandLineParser = new DefaultParser();
        HelpFormatter helpFormatter = new HelpFormatter();

        Option first = Option.builder("1")
                .hasArg()
                .desc("[stationName]\nShow index for given station")
                .build();
        Option second = Option.builder("2")
                .hasArgs()
                .desc("[date,stationName,pollutionType]\nShow value of given pollution type for given station for given date")
                .build();
        Option third = Option.builder("3")
                .hasArgs()
                .desc("[startDate,endDate,stationName,pollutionName]\nShow average value of given pollution type for given station in given period")
                .build();
        Option fourth = Option.builder("4")
                .hasArgs()
                .desc("[startDate,stationName]\nShow maximal fluctuation for given station")
                .build();
        Option fifth = Option.builder("5")
                .hasArgs()
                .desc("[date,stationName]\nShow minimal value of pollution types for given station")
                .build();
        Option sixth = Option.builder("6")
                .hasArg()
                .desc("[stationName,amount,date]\nShow N sensors with pollution standards exceedance for given station")
                .build();
        Option seventh = Option.builder("7")
                .hasArgs()
                .desc("[pollutionType]\nShow minimal and maximal values of given pollution type, as also their place and time")
                .build();
        Option eighth = Option.builder("8")
                .hasArgs()
                .desc("[pollutionType,[stationNames],startDate,endDate]\nShow graph of changes in value of given pollution for given stations in given period")
                .build();

        Options options = new Options();
        options.addOption(first);
        options.addOption(second);
        options.addOption(third);
        options.addOption(fourth);
        options.addOption(fifth);
        options.addOption(sixth);
        options.addOption(seventh);
        options.addOption(eighth);

        try {
            CommandLine commandLine = commandLineParser.parse(options,args);
            if(commandLine.getArgs().length != 1){
                System.out.println("You need to choose one service you want to use\nAvailible services:\nGIOS\nAirly");
                app.showHelp(options);
                System.exit(1);
            }
            else{
                Results results = new Results(commandLine.getArgs()[0]);
                if(commandLine.hasOption("h")){
                    app.showHelp(options);
                }
                if(commandLine.hasOption("1")){
                    String stationName = commandLine.getOptionValue("1");
                    System.out.println(results.getIndexForStation(stationName));
                }
                if(commandLine.hasOption("2")){
                    String[] arguments = commandLine.getOptionValues("2");
                    if(arguments.length != 3){
                        System.out.println("Incorrect amount of arguments for option 2");
                        app.showHelp(options);
                        System.exit(2);
                    }
                    System.out.println(results.getValueOfPollutionForStation(arguments[0],arguments[1],arguments[2]));
                }
                if(commandLine.hasOption("3")){
                    String[] arguments = commandLine.getOptionValues("3");
                    if(arguments.length != 4){
                        System.out.println("Incorrect amount of arguments for option 3");
                        app.showHelp(options);
                        System.exit(2);
                    }
                    System.out.println("Average of " + arguments[3] + " in given period was: " + results.getAverageOfPollutionForStation(arguments[0],arguments[1],arguments[2],arguments[3]));
                }
                if(commandLine.hasOption("4")){
                    String[] arguments = commandLine.getOptionValues("4");
                    if(arguments.length != 2){
                        System.out.println("Incorrect amount of arguments for option 4");
                        app.showHelp(options);
                        System.exit(2);
                    }
                    System.out.println(results.getMaximalFluctuationForStation(arguments[0],arguments[1]));
                }
                if(commandLine.hasOption("5")){
                    String[] arguments = commandLine.getOptionValues("5");
                    if(arguments.length != 2){
                        System.out.println("Incorrect amount of arguments for option 5");
                        app.showHelp(options);
                        System.exit(2);
                    }
                    System.out.println(results.getMinimalValue(arguments[0],arguments[1]));
                }
                if(commandLine.hasOption("6")){
                    String[] arguments = commandLine.getOptionValues("6");
                    if(arguments.length != 3){
                        System.out.println("Incorrect amount of arguments for option 6");
                        app.showHelp(options);
                        System.exit(2);
                    }
                    System.out.println(results.getSensorsWithPollutionStandardsExceedance(arguments[0],Integer.parseInt(arguments[1]),arguments[2]));
                }
                if(commandLine.hasOption("7")){
                    String pollutionType = commandLine.getOptionValue("7");
                    System.out.println(results.getMaximalAndMinimalValueForPollution(pollutionType));
                }
                if(commandLine.hasOption("8")){
                    String[] arguments = commandLine.getOptionValues("8");
                    List<String> stationsName = new ArrayList<>(Arrays.asList(arguments).subList(1, arguments.length - 2));
                    System.out.println(results.getGraph(arguments[0], stationsName,arguments[arguments.length-2],arguments[arguments.length-1]));
                }
            }
        } catch (IOException | ClassNotFoundException | org.apache.commons.cli.ParseException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void showHelp(Options options){
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("Program parameters:\n(values in brackets [] are args of option, which you need to give)" +
                "\n---------------------------------",options);
    }
}