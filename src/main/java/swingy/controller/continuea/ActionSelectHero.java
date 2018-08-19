package swingy.controller.continuea;

import swingy.controller.common.Controller;
import swingy.view.GuiView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 * From CONTINUE ADVENTURE / LOAD HERO
 * Action of select hero ComboBox
 */
public class ActionSelectHero implements ActionListener {
    private Controller controller;
    private GuiView guiView;

    public ActionSelectHero(Controller controller, GuiView guiView) {
        this.guiView = guiView;
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        JComboBox selectHeroCb = (JComboBox)e.getSource();
        controller.selectHero(selectHeroCb.getSelectedIndex(), false);
    }
}
