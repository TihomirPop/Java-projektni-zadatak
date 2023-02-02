module hr.java.projekt.main {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires java.sql;
    requires java.mail;
    requires java.datatransfer;
    requires java.desktop;
    requires Mal4J;


    opens hr.java.projekt.main to javafx.fxml;
    opens hr.java.projekt.entitet to javafx.base;
    exports hr.java.projekt.main;
}