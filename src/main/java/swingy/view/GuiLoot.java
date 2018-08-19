package swingy.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GuiLoot {
    private JPanel panel;

    private GuiLoot(Builder builder) {
        this.panel = builder.panel;
    }

    // GETTER / SETTER
    JPanel getPanel() {
        return panel;
    }

    // BUILDER
    static class Builder {
        private JPanel panel;
        private JTextArea messArea;
        private JButton btnKeepMyStuff;
        private JButton btnGetNewItem;
        private ActionListener btnKeepMyStuff_listener;
        private ActionListener btnGetNewItem_listener;

        Builder setMessArea(JTextArea messArea) {
            this.messArea = messArea;
            return this;
        }

        Builder setBtnKeepMyStuff_listener(ActionListener listener) {
            this.btnKeepMyStuff_listener = listener;
            return this;
        }

        Builder setBtnGetNewItem_listener(ActionListener listener) {
            this.btnGetNewItem_listener = listener;
            return this;
        }

        GuiLoot build() {
            this.panel = new JPanel();
            this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.Y_AXIS));

            this.btnKeepMyStuff = new JButton("I'll keep MY STUFF !");
            this.btnKeepMyStuff.setAlignmentX(Component.CENTER_ALIGNMENT);
            this.btnKeepMyStuff.setMaximumSize(new Dimension(200, 50));
            this.btnKeepMyStuff.addActionListener(this.btnKeepMyStuff_listener);

            this.btnGetNewItem = new JButton("I want it NOW !");
            this.btnGetNewItem.setAlignmentX(Component.CENTER_ALIGNMENT);
            this.btnGetNewItem.setMaximumSize(new Dimension(200, 50));
            this.btnGetNewItem.addActionListener(this.btnGetNewItem_listener);

            this.messArea.setBorder(BorderFactory.createEmptyBorder());
            //this.messArea.setHorizontalAlignment(JTextField.CENTER);
            this.messArea.setEditable(false);
            this.messArea.setAlignmentX(Component.CENTER_ALIGNMENT);
            this.messArea.setMaximumSize(new Dimension(400, 150));

            this.panel.add(Box.createVerticalStrut(250));
            this.panel.add(this.messArea);
            this.panel.add(Box.createVerticalStrut(80));
            this.panel.add(btnKeepMyStuff);
            this.panel.add(Box.createVerticalStrut(10));
            this.panel.add(btnGetNewItem);

            return new GuiLoot(this);
        }
    }
}
