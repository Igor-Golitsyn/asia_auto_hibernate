package com.asia_auto.client;

import com.asia_auto.client.gui.main.MainController;
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
 * Created by Игорь on 19.08.2016.
 */
public class ClientGui extends Application {
    private Logger logger=Logger.getLogger(ClientGui.class.getName());
    @FXMLViewFlowContext
    private ViewFlowContext flowContext;

    @Override
    public void start(Stage stage) throws Exception {
        logger.log(Level.INFO,"start");
        stage.setTitle("Записная книжка");
        new Thread(() -> {
            try {
                SVGGlyphLoader.loadGlyphsFont(ClientGui.class.getResourceAsStream("/resources/fonts/icomoon.svg"), "icomoon.svg");
            } catch (Exception e) {
                logger.log(Level.WARNING,e.getMessage());
            }
        }).start();

        Flow flow = new Flow(MainController.class);
        DefaultFlowContainer container = new DefaultFlowContainer();
        flowContext = new ViewFlowContext();
        flowContext.register("Stage", stage);
        flow.createHandler(flowContext).start(container);

        JFXDecorator decorator = new JFXDecorator(stage, container.getView());
        decorator.setCustomMaximize(true);
        Scene scene = new Scene(decorator, 900, 800);
        scene.getStylesheets().add(ClientGui.class.getResource("/resources/css/jfoenix-fonts.css").toExternalForm());
        scene.getStylesheets().add(ClientGui.class.getResource("/resources/css/jfoenix-design.css").toExternalForm());
        scene.getStylesheets().add(ClientGui.class.getResource("/resources/css/jfoenix-client.css").toExternalForm());
        //		stage.initStyle(StageStyle.UNDECORATED);
        stage.setFullScreen(true);
        stage.setMinWidth(900);
        stage.setMinHeight(800);
        stage.setScene(scene);
        stage.show();
    }
}
