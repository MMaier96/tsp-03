package main;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XMLParser {

    private List<Scenario> scenarioList = new ArrayList<>();

    public XMLParser(File xmlFile) {
        XMLInputFactory read = XMLInputFactory.newInstance();
        Reader in = null;

        try {
            in = new FileReader(xmlFile);
            XMLEventReader reader = read.createXMLEventReader(in);
            Scenario tempScenario = null;
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();

                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    if (startElement.getName().getLocalPart().equals("scenario")) {
                        tempScenario = new Scenario();
                        Iterator<Attribute> attributes = startElement.getAttributes();
                        while (attributes.hasNext()) {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals("id")) {
                                tempScenario.setId(attribute.getValue());
                            }

                        }
                    }

                    if (event.asStartElement().getName().getLocalPart().equals("crossover")) {
                        event = reader.nextEvent();
                        tempScenario.setCrossover(getCrossover(event.asCharacters().getData()));
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart().equals("crossoverRatio")) {
                        event = reader.nextEvent();
                        tempScenario.setCrossoverRatio(Double.parseDouble(event.asCharacters().getData()));
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart().equals("mutation")) {
                        event = reader.nextEvent();
                        tempScenario.setMutation(getMutation(event.asCharacters().getData()));
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart().equals("mutationRatio")) {
                        event = reader.nextEvent();
                        tempScenario.setMutationRatio(Double.parseDouble(event.asCharacters().getData()));
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart().equals("selection")) {
                        event = reader.nextEvent();
                        tempScenario.setSelection(getSelection(event.asCharacters().getData()));
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart().equals("buildStatistics")) {
                        event = reader.nextEvent();
                        tempScenario.setBuildStatistics(Boolean.parseBoolean(
                                event.asCharacters().getData().replaceAll("yes", "true").replaceAll("no", "false")));
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart().equals("isEvaluated")) {
                        event = reader.nextEvent();
                        tempScenario.setEvaluated(Boolean.parseBoolean(
                                event.asCharacters().getData().replaceAll("yes", "true").replaceAll("no", "false")));
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart().equals("evaluation")) {
                        event = reader.nextEvent();
                        tempScenario.setEvaluation(event.asCharacters().getData());
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart().equals("maximumNumberOfEvaluations")) {
                        event = reader.nextEvent();
                        tempScenario.setMaximumNumberOfEvaluations(Long.parseLong(event.asCharacters().getData()));
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart().equals("isEvaluated")) {
                        event = reader.nextEvent();
                        tempScenario.setIsEvaluated(event.asCharacters().getData().equals("yes"));
                        continue;
                    }

                }
                if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    if (endElement.getName().getLocalPart().equals("scenario")) {
                        scenarioList.add(tempScenario);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Scenario getFirstScenario() {
        if (scenarioList.size() == 0)
            return null;
        return scenarioList.get(0);
    }

    public Scenario[] getScenarios() {
        return scenarioList.toArray(new Scenario[scenarioList.size()]);
    }

    private Scenario.CrossoverMode getCrossover(String c) {
        switch (c.toLowerCase()) {
            case "cycle":
                return Scenario.CrossoverMode.CYCLE;
            case "heuristic":
                return Scenario.CrossoverMode.HEURISTIC;
            case "ordered":
                return Scenario.CrossoverMode.ORDERED;
            case "partiallymatched":
                return Scenario.CrossoverMode.PARTIALLY_MATCHED;
            case "positionbased":
                return Scenario.CrossoverMode.POSITION;
            case "subtourexchange":
                return Scenario.CrossoverMode.SUB_TOUR_EXCHANGE;
            default:
                return Scenario.CrossoverMode.CYCLE;
        }
    }

    private Scenario.SelectionMode getSelection(String s) {
        switch (s.toLowerCase()) {
            case "roulettewheel":
                return Scenario.SelectionMode.ROULETTE_WHEEL;
            case "tournament":
                return Scenario.SelectionMode.TOURNAMENT;
            default:
                return Scenario.SelectionMode.TOURNAMENT;
        }
    }

    private Scenario.MutationMode getMutation(String m) {
        switch (m.toLowerCase()) {
            case "displacement":
                return Scenario.MutationMode.DISPLACEMENT;
            case "exchange":
                return Scenario.MutationMode.EXCHANGE;
            case "heuristic":
                return Scenario.MutationMode.HEURISTIC;
            case "insertion":
                return Scenario.MutationMode.INSERTION;
            case "inversion":
                return Scenario.MutationMode.INVERSION;
            default:
                return Scenario.MutationMode.DISPLACEMENT;
        }
    }
}
