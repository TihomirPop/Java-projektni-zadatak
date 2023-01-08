module hr.java.projekt.main {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;


    opens hr.java.projekt.main to javafx.fxml;
    exports hr.java.projekt.main;
}