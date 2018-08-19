package swingy.controller.common;

import swingy.view.GuiView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionBack implements ActionListener {
    private GuiView guiView;
    private Controller controller;

    public ActionBack(Controller controller, GuiView guiView) {
        this.guiView = guiView;
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        this.controller.back();
    }
}