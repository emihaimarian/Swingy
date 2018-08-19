package swingy.controller.newhero;

import swingy.controller.common.Controller;
import swingy.view.GuiView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionSelectClass implements ActionListener {
    private GuiView guiView;
    private Controller controller;

    public ActionSelectClass(Controller controller, GuiView guiView) {
        this.guiView = guiView;
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        // Update hero name before changing classs
        this.controller.getHeroNameFromGuiFieldThenToAdventure();

        // Update hero class
        JComboBox selectClassCb = (JComboBox)e.getSource();
        controller.selectClass(selectClassCb.getSelectedIndex());
    }
}