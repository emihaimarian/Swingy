package swingy.controller.adventure;

import swingy.controller.common.Controller;
import swingy.view.GuiView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 * From ADVENTURE window
 * Calculate a FIGHT round
 */
public class ActionFight implements ActionListener {
    private GuiView guiView;
    private Controller controller;

    public ActionFight(Controller controller, GuiView guiView) {
        this.guiView = guiView;
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        this.controller.fight();
    }
}