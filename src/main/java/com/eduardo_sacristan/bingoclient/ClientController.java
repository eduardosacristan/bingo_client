package com.eduardo_sacristan.bingoclient;

import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import utils.Messages;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.security.Provider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * This class contains the methods to control the javaFX application
 */
public class ClientController implements Initializable {
    @FXML
    private Label numero1_label;
    @FXML
    private Button btn_connect_id;
    @FXML
    private Button numero_1;
    @FXML
    private Button numero_2;
    @FXML
    private Button numero_3;
    @FXML
    private Button numero_4;
    @FXML
    private Button numero_5;
    @FXML
    private Label final_text;
    @FXML
    private TextField server_address;
    @FXML
    private TextField port;
    @FXML
    private Label welcomeText;
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    private ArrayList<Integer> carton;
    ArrayList<Integer> bolasExtraidas = new ArrayList<>();
    int bola;
    boolean bingo = false;
    boolean finDeJuego;

    /**
     * The initialize method
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        server_address.setText("localhost");
        port.setText("2010");
        final_text.setText("");
//        ArrayList<Integer> bolasExtraidas = new ArrayList<>();

    }

    /**
     * This method is launched when the Connect button us clicked and make the
     * game begin
     * @param actionEvent
     */
    @FXML
    private void btn_connect(ActionEvent actionEvent) {
        btn_connect_id.setDisable(true);
        Thread t = new Thread(jugar());
        t.start();
    }

    /**
     * This method controls the game
     * @return
     */
    private Runnable jugar() {
        return () -> {
            try (
                    Socket mySocket =
                            new Socket(server_address.getText(), Integer.parseInt(port.getText()));
                    ObjectOutputStream objectOut =
                            new ObjectOutputStream(mySocket.getOutputStream());
                    ObjectInputStream objectIn =
                            new ObjectInputStream(mySocket.getInputStream()))
            {

                System.out.println("recibiendo carton");
                carton = (ArrayList<Integer>) objectIn.readObject();
                Collections.sort(carton);
                carton.forEach(System.out::print);
                System.out.println();

                Platform.runLater(() -> {
                    numero_1.setText(carton.get(0).toString());
                    numero_2.setText(carton.get(1).toString());
                    numero_3.setText(carton.get(2).toString());
                    numero_4.setText(carton.get(3).toString());
                    numero_5.setText(carton.get(4).toString());
                });

                do {
                    bola = (int) objectIn.readObject();
                    System.out.println(bola);
                    verdecitos();
                    bolasExtraidas.add(bola);
                    Collections.sort(bolasExtraidas);
                    bingo = comprobarCarton(carton, bolasExtraidas);
                    objectOut.writeObject(bingo);
                    finDeJuego = (boolean) objectIn.readObject();
                }
                while(!finDeJuego);

                if(bingo){
                    System.out.println("GANADOR");
                    Platform.runLater(()->final_text.setText("YOU WIN"));
                }
                else{
                    System.out.println("PERDEDOR");
                    Platform.runLater(()->final_text.setText("YOU LOOSE"));
                }
            }

            catch (IOException e) {
                e.printStackTrace();
                System.out.println(e);
                Platform.runLater(()-> Messages.showError("ERROR",
                        "No ha sido posible conectarse al servidor"));
            }

            catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println(e);
                Messages.showError("ERROR", "Error interno.\n" +
                        "Cuelgue al programador y vayase a la playa");
            }
        };
    }

    /**
     * This method changes the style of the numbers background to green when
     * the number is sent by the server
     */
    private void verdecitos() {
        if(Integer.parseInt(numero_1.getText()) == bola)
            Platform.runLater(()-> numero_1.setStyle("-fx-background-color:#00ff00"));
        if(Integer.parseInt(numero_2.getText()) == bola)
            Platform.runLater(()-> numero_2.setStyle("-fx-background-color:#00ff00"));
        if(Integer.parseInt(numero_3.getText()) == bola)
            Platform.runLater(()-> numero_3.setStyle("-fx-background-color:#00ff00"));
        if(Integer.parseInt(numero_4.getText()) == bola)
            Platform.runLater(()-> numero_4.setStyle("-fx-background-color:#00ff00"));
        if(Integer.parseInt(numero_5.getText()) == bola)
            Platform.runLater(()-> numero_5.setStyle("-fx-background-color:#00ff00"));
    }

    /**
     * This method checks if all the numbers of the player have been sent by
     * the server
     * @param carton list with the 5 numbers of the player
     * @param bolas list with all the numbers
     * @return boolean if there is a bingo
     */
    public static boolean comprobarCarton(ArrayList<Integer> carton, ArrayList<Integer> bolas) {
        boolean bingo = false;
        int coincidencias = 0;
        if(bolas.size() >= 5) {

            for(int numero : bolas) {
                for(int numero2 : carton) {
                    if (numero == numero2)
                        coincidencias++;
                }
            }

            if (coincidencias == 5)
                bingo = true;
        }
        return bingo;
    }
}