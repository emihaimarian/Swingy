package swingy.controller.mainmenu;

import swingy.controller.common.Controller;
import swingy.view.GuiView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionNewHero implements ActionListener {
    private GuiView guiView;
    private Controller controller;

    public ActionNewHero(Controller controller, GuiView guiView) {
        this.guiView = guiView;
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        this.controller.newHero(true);
    }
}
