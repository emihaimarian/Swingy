package swingy.controller.mainmenu;

import swingy.controller.common.Controller;
import swingy.view.GuiView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionRunAndHide implements ActionListener {
    private GuiView guiView;
    private Controller controller;

    public ActionRunAndHide(Controller controller, GuiView guiView) {
        this.guiView = guiView;
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        this.controller.runAndHide();
    }
}
