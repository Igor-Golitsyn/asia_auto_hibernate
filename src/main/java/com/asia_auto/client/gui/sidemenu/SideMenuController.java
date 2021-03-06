package com.asia_auto.client.gui.sidemenu;

import com.asia_auto.client.gui.uicomponents.MainTableViewController;
import com.asia_auto.client.gui.uicomponents.MastersVeiwController;
import com.asia_auto.client.gui.uicomponents.TimesTableViewController;
import com.jfoenix.controls.JFXListView;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;

import javax.annotation.PostConstruct;

/**
 * Created by Игорь on 22.08.2016.
 */
@FXMLController(value = "/resources/fxml/SideMenu.fxml")
public class SideMenuController {
    @FXMLViewFlowContext
    private ViewFlowContext context;

    @FXML
    @ActionTrigger("treetableview")
    private Label treetableview;
    @FXML
    @ActionTrigger("mastersView")
    private Label mastersView;
    @FXML
    @ActionTrigger("timesView")
    private Label timesView;

    @FXML
    private JFXListView<?> sideList;

    @PostConstruct
    public void init() throws FlowException, VetoException {
        sideList.propagateMouseEventsToParent();
        FlowHandler contentFlowHandler = (FlowHandler) context.getRegisteredObject("ContentFlowHandler");
        Flow contentFlow = (Flow) context.getRegisteredObject("ContentFlow");
        bindNodeToController(treetableview, MainTableViewController.class, contentFlow, contentFlowHandler);
        bindNodeToController(mastersView, MastersVeiwController.class, contentFlow, contentFlowHandler);
        bindNodeToController(timesView, TimesTableViewController.class, contentFlow, contentFlowHandler);
    }

    private void bindNodeToController(Node node, Class<?> controllerClass, Flow flow, FlowHandler flowHandler) {
        flow.withGlobalLink(node.getId(), controllerClass);
        node.setOnMouseClicked((e) -> {
            try {
                flowHandler.handle(node.getId());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
    }

}

