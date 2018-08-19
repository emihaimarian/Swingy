package swingy.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

class GuiMainMenu {
    private JPanel panel;

    private GuiMainMenu(Builder builder) {
        this.panel = builder.panel;
    }

    // GETTER / SETTER
    JPanel getPanel() {
        return panel;
    }

    // BUILDER
    static class Builder {
        private JPanel panel;
        private JTextField messArea;
        private JButton btnNewHero;
        private JButton btnContinueA;
        private JButton btnRunAndHide;
        private ActionListener btnNewHero_listener;
        private ActionListener btnContinueA_listener;
        private ActionListener btnRunAndHide_listener;

        Builder setMessArea(JTextField messArea) {
            this.messArea = messArea;
            return this;
        }

        Builder setBtnNewHero_listener(ActionListener listener) {
            this.btnNewHero_listener = listener;
            return this;
        }

        Builder setBtnContinueA_listener(ActionListener listener) {
            this.btnContinueA_listener = listener;
            return this;
        }

        Builder setBtnRunAndHide_listener(ActionListener listener) {
            this.btnRunAndHide_listener = listener;
            return this;
        }

        GuiMainMenu build() {
            this.panel = new JPanel();
            this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.Y_AXIS));

            this.btnNewHero = new JButton("NEW HERO");
            this.btnNewHero.setAlignmentX(Component.CENTER_ALIGNMENT);
            this.btnNewHero.setMaximumSize(new Dimension(200, 50));
            this.btnNewHero.addActionListener(this.btnNewHero_listener);

            this.btnContinueA = new JButton("CONTINUE ADVENTURE");
            this.btnContinueA.setAlignmentX(Component.CENTER_ALIGNMENT);
            this.btnContinueA.setMaximumSize(new Dimension(200, 50));
            this.btnContinueA.addActionListener(this.btnContinueA_listener);

            this.btnRunAndHide = new JButton("RUN AND HIDE");
            this.btnRunAndHide.setAlignmentX(Component.CENTER_ALIGNMENT);
            this.btnRunAndHide.setMaximumSize(new Dimension(200, 50));
            this.btnRunAndHide.addActionListener(this.btnRunAndHide_listener);

            this.messArea.setBorder(BorderFactory.createEmptyBorder());
            this.messArea.setHorizontalAlignment(JTextField.CENTER);
            this.messArea.setEditable(false);
            this.messArea.setAlignmentX(Component.CENTER_ALIGNMENT);
            this.messArea.setMaximumSize(new Dimension(400, 50));

            this.panel.add(Box.createVerticalStrut(250));
            this.panel.add(btnNewHero);
            this.panel.add(Box.createVerticalStrut(10));
            this.panel.add(btnContinueA);
            this.panel.add(Box.createVerticalStrut(10));
            this.panel.add(btnRunAndHide);
            this.panel.add(Box.createVerticalStrut(50));
            this.panel.add(this.messArea);

            return new GuiMainMenu(this);
        }
    }
}
