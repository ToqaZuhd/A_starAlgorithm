package com.example.A_starAlgo;
import java.io.*;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class SampleController implements Initializable{
    A_star a_star;

    @FXML
    private AnchorPane anchor_P;

    @FXML
    private ComboBox<String> CB_source;

    @FXML
    private ComboBox<String> CB_target;

    @FXML
    private TextField DistanceTF;

    @FXML
    private TextArea pathTF;

    @FXML
    private TextField TimeTF;

    @FXML
    private TextField SpaceTF;

    HashMap<String, City> branches;
    ArrayList<City> cities = new ArrayList<>();
    ArrayList<Circle> circles =new ArrayList<>();

    //Read files method
    public void readFile() throws FileNotFoundException {
        File CityFile = new File("towns.csv");
        Scanner ReadCity =  new Scanner(CityFile);

        File BranchingFile = new File("roads.csv");
        Scanner ReadBranching =  new Scanner(BranchingFile);

        int counter = 0;
        //Read CityFile and store the cities in array
        while(ReadCity.hasNext()){
            String [] data = ReadCity.nextLine().split(",");
            cities.add(new City((data[0].trim()), Double.parseDouble(data[1]), Double.parseDouble(data[2])));
            circles.add(new Circle(Double.parseDouble(data[1]), Double.parseDouble(data[2]), 4, javafx.scene.paint.Color.RED));
            cities.get(counter).Circle = circles.get(counter);
            circles.get(counter).setStroke(Color.BLACK);
            anchor_P.getChildren().add(circles.get(counter));
            counter++;
        }

        branches = new HashMap<>();
        //put cities array in hashmap to reach the cit via its name
        for (City value : cities) {
            branches.put(value.cityName, value);
        }

        //Read RoadsFile
        while (ReadBranching.hasNext()) {
            Branching branch_destination = new Branching();
            Branching branch_src = new Branching();

            String[] arr = (ReadBranching.nextLine()).split(",");
            double distance = Double.parseDouble(arr[2].trim());

            City src = branches.get(arr[0].trim());

            //Properties of destination city
            City destination = branches.get(arr[1].trim());
            branch_destination.branchName =destination.cityName;
            branch_destination.x=destination.X;
            branch_destination.y=destination.Y;
            branch_destination.weight=distance;

            //Properties of source city
            branch_src.branchName =src.cityName;
            branch_src.x=src.X;
            branch_src.y=src.Y;
            branch_src.weight=distance;

            //put the source road for the goal
            src.branches.add(branch_destination);

            //put the goal road for the source
            destination.branches.add(branch_src);


        }

        ReadCity.close();
        ReadBranching.close();

        //fill combo boxes for both source and target city
        for (City city : cities) {
            CB_source.getItems().add(city.cityName);
            CB_target.getItems().add(city.cityName);

        }
    }


    Line []line = new Line[0];


    public void find() {

        //Begin with red button for each city
        for (City city : cities) {
            city.Circle.setFill(javafx.scene.paint.Color.RED);
        }

        //Begin with no line in the map, remove if there
        for (Line value : line)
            anchor_P.getChildren().remove(value);

        //if user select the source and the goal from the combo boxes
        if(CB_source.getSelectionModel().getSelectedItem()!=null && CB_target.getSelectionModel().getSelectedItem()!= null) {

            //take the selected source city value
            City source= branches.get(CB_source.getSelectionModel().getSelectedItem());
            //fill the circle with selected city in Black color
            source.Circle.setFill(javafx.scene.paint.Color.BLACK);

            //take the selected goal city value
            City destination= branches.get(CB_target.getSelectionModel().getSelectedItem());
            destination.Circle.setFill(javafx.scene.paint.Color.BLACK);

            //pass the cities and the source city to initialize the method of find the path
            a_star =new A_star(cities, source);
            //pass the goal city to method in A_star class to find the path
            a_star.A_Star_Search(destination);
            //print the city will pass through
            a_star.printPath(a_star.convert(destination));

            //if there is no roads for selected city
            if(a_star.distance ==Double.MAX_VALUE) {
                pathTF.setText("There is No Path");
                pathTF.setFont(Font.font("Andalusia", FontWeight.BOLD, 16));
                DistanceTF.setText("There is No Path");
                DistanceTF.setFont(Font.font("Andalusia", FontWeight.BOLD, 18));
            }

            //if there are roads and value for the distance
            else {

                //print path in UI
                pathTF.setText(a_star.S);
                pathTF.setFont(Font.font("Andalusia", FontWeight.BOLD, 16));

                //print Distance in UI
                DistanceTF.setText(Math.round(a_star.distance *100.0)/100.0+" KM");
                DistanceTF.setFont(Font.font("Andalusia", FontWeight.BOLD, 18));

                //split the path for many cities to draw a line between cities
                String []s = a_star.S.split("\n");

                //number of lines depends on number of city
                line = new Line[s.length-1];

                for(int i=0;i<s.length;i++) {
                    if(i!=s.length-1) {

                        City city1= branches.get(s[i]);
                        City city2= branches.get(s[i+1]);
                        line[i]=new Line();
                        //take the coordinates of the source and the destination cities
                        line[i].setStartX(city1.X);
                        line[i].setStartY(city1.Y);
                        line[i].setEndX(city2.X);
                        line[i].setEndY(city2.Y);
                        anchor_P.getChildren().add(line[i]);//put lines in the map
                    }
                }

                //print the spaceComplexity on the UI
                SpaceTF.setText(String.valueOf(a_star.counterSpaceComplexity));
                SpaceTF.setFont(Font.font("Andalusia", FontWeight.BOLD, 18));

                //print the timeComplexity on the UI
                TimeTF.setText(String.valueOf(a_star.counterTimeComplexity));
                TimeTF.setFont(Font.font("Andalusia", FontWeight.BOLD, 18));
            }
        }

        else {
            //if no source or goal city selected

            if(CB_source.getSelectionModel().getSelectedItem()==null)
                JOptionPane.showMessageDialog(null, "Insert the source city",null, JOptionPane.PLAIN_MESSAGE);
            else if
            (CB_target.getSelectionModel().getSelectedItem()==null)
                JOptionPane.showMessageDialog(null, "Insert the target city",null, JOptionPane.PLAIN_MESSAGE);


        }

    }

    int counter=0;//counter for the number of clicking on th map
    @Override
    public void initialize(URL arg0, ResourceBundle arg1)  {
        try {
            readFile();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        anchor_P.setOnMouseClicked((MouseEvent e) -> {
            double srcPointX=e.getSceneX();
            double srcPointY=e.getSceneY();
            City srcCity;
            City destCity;
            if(counter==0)
                //select source city from the map not from combo box
                for (City city : cities) {
                    //coordinates of city or near it
                    if (city.X - 4 <= srcPointX && city.X + 4 >= srcPointX &&
                            city.Y - 4 <= srcPointY && city.Y + 4 >= srcPointY) {

                        srcCity = city;
                        srcCity.Circle.setFill(javafx.scene.paint.Color.BLACK);
                        CB_source.setValue(srcCity.cityName);
                        counter = 1;
                        break;

                    }
                }
            else if(counter==1)
                //select goal city from the map not from combo box
                for (City city : cities) {
                    //coordinates of city or near it
                    if (city.X - 4 <= srcPointX && city.X + 4 >= srcPointX &&
                            city.Y - 4 <= srcPointY && city.Y + 4 >= srcPointY) {

                        destCity = city;
                        destCity.Circle.setFill(javafx.scene.paint.Color.BLACK);
                        counter = 0;
                        CB_target.setValue(destCity.cityName);
                        find();//call method of find path and print on the UI
                        break;

                    }
                }

        });



    }





}
