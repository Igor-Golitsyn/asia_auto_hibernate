package com.asia_auto.server;

import com.asia_auto.server.gui.main.ServerMainController;
import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.svg.SVGGlyphLoader;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс для каждого входящего подключения создает отдельный поток,
 * в котором обрабатывает полученное сообщение и формирует ответное сообщение.
 */
public class ServerGui extends Application {
    Logger logger = Logger.getLogger(ServerGui.class.getName());
    @FXMLViewFlowContext
    private ViewFlowContext flowContext;

    @Override
    public void start(Stage stage) throws Exception {
        logger.log(Level.INFO, "start");
        stage.setTitle("СЕРВЕР");
        new Thread(() -> {
            try {
                SVGGlyphLoader.loadGlyphsFont(ServerGui.class.getResourceAsStream("/resources/fonts/icomoon.svg"), "icomoon.svg");
            } catch (Exception e) {
                logger.log(Level.WARNING, e.getMessage());
            }
        }).start();

        Flow flow = new Flow(ServerMainController.class);
        DefaultFlowContainer container = new DefaultFlowContainer();
        flowContext = new ViewFlowContext();
        flowContext.register("Stage", stage);
        flow.createHandler(flowContext).start(container);


        JFXDecorator decorator = new JFXDecorator(stage, container.getView());
        decorator.setCustomMaximize(true);
        Scene scene = new Scene(decorator, 800, 400);
        scene.getStylesheets().add(ServerGui.class.getResource("/resources/css/jfoenix-fonts.css").toExternalForm());
        scene.getStylesheets().add(ServerGui.class.getResource("/resources/css/jfoenix-design.css").toExternalForm());
        scene.getStylesheets().add(ServerGui.class.getResource("/resources/css/jfoenix-client.css").toExternalForm());
        //		stage.initStyle(StageStyle.UNDECORATED);
        //      stage.setFullScreen(true);
        stage.setMinWidth(800);
        stage.setMinHeight(400);
        stage.setScene(scene);
        stage.show();
    }
}