package com.company;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String filepath = "TrainInfo.xml";
        File xmlFile = new File(filepath);
        int a = 1;
        int b = 10;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            Document document1 = builder.newDocument();
            Element root = document1.createElement("Train");
            document1.appendChild(root);
            document.getDocumentElement().normalize();
            System.out.println("Корневой элемент: " + document.getDocumentElement().getNodeName());
            // теперь XML полностью загружен в память в виде объекта document класса Document
            NodeList CargoNodeList = document.getElementsByTagName("CargoWagon");     // достаем информацию о грузовых вагонах
            NodeList PassengerNodeList = document.getElementsByTagName("PassengerWagon");     // достаем информацию о пассажирских вагонах

            // создаем список грузовых вагонов объектов класса Train
            List<Train> ctrainList = new ArrayList<Train>();
            for (int i = 0; i < CargoNodeList.getLength(); i++) {
                ctrainList.add(getCTrain(CargoNodeList.item(i)));
            }
            // и пассажирских вагонов
            List<Train> ptrainList = new ArrayList<Train>();
            for (int i = 0; i < PassengerNodeList.getLength(); i++) {
                ptrainList.add(getPTrain(PassengerNodeList.item(i)));
            }
            // печатаем в консоль информацию по каждому вагону
            for (Train ctrain : ctrainList) {
                System.out.println(ctrain.toString());
            }
            for (Train ptrain : ptrainList) {
                System.out.println(ptrain.toString());
            }
            // собираем поезд случайным образом
            int TotalTonnage = 0;
            int TotalPassengers = 0;
            int WagonNumber = a + (int) (Math.random() * b); // случайная генерация числа вагонов в поезде и типа поезда
            List<CargoTrain> CargoTrainList = new ArrayList<CargoTrain>();
            List<PassengerTrain> PassengerTrainList = new ArrayList<PassengerTrain>();
            System.out.println("Train:");
            if (WagonNumber <= b/2) {
                for (int i = 0; i < WagonNumber; i++) {
                    CargoTrainList.add(getCTrain(CargoNodeList.item((int)Math.floor(Math.random() * CargoNodeList.getLength()))));
                }
                for (CargoTrain train : CargoTrainList) {
                    System.out.println(train.toString());
                    TotalTonnage = TotalTonnage + train.getTonnage();
                    Element wagon = document1.createElement("Wagon");
                    root.appendChild(wagon);
                    wagon.setAttribute(train.getName(), Integer.toString(train.getTonnage()));
                }
                System.out.println("Train's total tonnage = " + TotalTonnage);
                Element total = document1.createElement("Total");   // экспорт результата в xml-файл
                root.appendChild(total);
                total.setAttribute("Tonnage_sum", Integer.toString(TotalTonnage));
            } else {
                for (int i = 0; i < WagonNumber; i++) {
                    PassengerTrainList.add(getPTrain(PassengerNodeList.item((int) Math.floor(Math.random() * PassengerNodeList.getLength()))));
                }
                for (PassengerTrain train : PassengerTrainList) {
                    System.out.println(train.toString());
                    TotalPassengers = TotalPassengers + train.getPassengers();
                    Element wagon = document1.createElement("Wagon");
                    root.appendChild(wagon);
                    wagon.setAttribute(train.getName(), Integer.toString(train.getPassengers()));
                }
                System.out.println("Train's total number of passengers = " + TotalPassengers);
                Element total = document1.createElement("Total");
                root.appendChild(total);
                total.setAttribute("Passengers_sum", Integer.toString(TotalPassengers));
            }
            // создание XML файла
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.transform(new DOMSource(document1), new StreamResult(new FileOutputStream("output.xml")));
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    // создаем из узла документа объект класса CargoTrain
    private static CargoTrain getCTrain(Node node) {
        CargoTrain ctrain = new CargoTrain();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            ctrain.setName(getTagValue("name", element));
            ctrain.setTonnage(Integer.parseInt(getTagValue("tonnage", element)));
        }
        return ctrain;
    }

    private static PassengerTrain getPTrain(Node node) {
        PassengerTrain ptrain = new PassengerTrain();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            ptrain.setName(getTagValue("name", element));
            ptrain.setPassengers(Integer.parseInt(getTagValue("passengers", element)));
        }
        return ptrain;
    }

    // получаем значение элемента по указанному тегу
    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }

}
