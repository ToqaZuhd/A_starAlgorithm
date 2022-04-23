module com.example.demo6 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;


    opens com.example.A_starAlgo to javafx.fxml;
    exports com.example.A_starAlgo;
}