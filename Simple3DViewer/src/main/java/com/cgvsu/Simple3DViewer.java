package com.cgvsu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Simple3DViewer extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Загружаем FXML файл
        AnchorPane viewport = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxml/gui.fxml")));

        // Создаем сцену
        Scene scene = new Scene(viewport);
        viewport.prefWidthProperty().bind(scene.widthProperty());
        viewport.prefHeightProperty().bind(scene.heightProperty());

        // Настройки окна
        stage.setTitle("Simple3DViewer");
        stage.setScene(scene);

        // Установка минимальных размеров окна
        stage.setMinWidth(1080);
        stage.setMinHeight(720);

        // Установка размеров окна на весь экран
        stage.setWidth(java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth());
        stage.setHeight(java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight());

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}