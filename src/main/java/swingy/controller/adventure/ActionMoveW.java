package swingy.controller.adventure;

import swingy.controller.common.Controller;
import swingy.view.GuiView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionMoveW implements ActionListener {
    private GuiView guiView;
    private Controller controller;

    public ActionMoveW(Controller controller, GuiView guiView) {
        this.guiView = guiView;
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        this.controller.move('a');
    }
}