package swingy.controller.loot;

import swingy.controller.common.Controller;
import swingy.view.GuiView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionKeepMyStuff implements ActionListener {
    private GuiView guiView;
    private Controller controller;

    public ActionKeepMyStuff(Controller controller, GuiView guiView) {
        this.guiView = guiView;
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        this.controller.move('0');
    }
}
