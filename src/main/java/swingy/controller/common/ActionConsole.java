package swingy.controller.common;

import swingy.view.GuiView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 * From ADVENTURE window
 * Change from GUI mode to Console mode
 */
public class ActionConsole implements ActionListener {
    private GuiView guiView;
    private Controller controller;

    public ActionConsole(Controller controller, GuiView guiView) {
        this.guiView = guiView;
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        this.controller.console();
    }
}