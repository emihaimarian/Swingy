package swingy.controller.continuea;

import swingy.controller.common.Controller;
import swingy.view.GuiView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 * From CONTINUE ADVENTURE / LOAD HERO
 * Action of execute / delete hero Button
 */
public class ActionExecuteHero implements ActionListener {
    private GuiView guiView;
    private Controller controller;

    public ActionExecuteHero(Controller controller, GuiView guiView) {
        this.guiView = guiView;
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        controller.executeHero();
    }
}

