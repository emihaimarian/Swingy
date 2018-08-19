package swingy.controller.mainmenu;

import swingy.controller.common.Controller;
import swingy.view.GuiView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionContinueA implements ActionListener {
    private GuiView guiView;
    private Controller controller;

    public ActionContinueA(Controller controller, GuiView guiView) {
        this.guiView = guiView;
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        this.controller.continueA();
    }
}
