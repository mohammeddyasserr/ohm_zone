module team.ohm_zone {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;

    opens main_package to javafx.fxml;
    opens admin_gui_controller to javafx.fxml;
    exports main_package;
    exports admin_gui_controller ;
    exports user_gui_controller;
}