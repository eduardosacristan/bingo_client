module com.eduardo_sacristan.bingoclient {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.eduardo_sacristan.bingoclient to javafx.fxml;
    exports com.eduardo_sacristan.bingoclient;
}