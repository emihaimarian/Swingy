package swingy.controller.common;

import swingy.view.GuiView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionToAdventure implements ActionListener {
    private GuiView guiView;
    private Controller controller;

    public ActionToAdventure(Controller controller, GuiView guiView) {
        this.guiView = guiView;
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        this.controller.getHeroNameFromGuiFieldThenToAdventure();
        //this.controller.toAdventure(true);
    }
}