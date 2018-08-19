package swingy.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

class GuiContinueA {
    private JPanel panel;

    private GuiContinueA(Builder builder) {
        this.panel = builder.panel;
    }

    // GETTER / SETTER
    JPanel getPanel() {
        return panel;
    }

    // BUILDER
    static class Builder {
        private JPanel panel;
        private JComboBox heroList;
        private JLabel heroInfo;
        private JButton btnBack;
        private JButton btnToAdventure;
        private JButton btnExecute;
        private ActionListener selectHero_listener;
        private ActionListener btnBack_listener;
        private ActionListener btnToAdventure_listener;
        private ActionListener btnExecute_listener;

        Builder setHeroList(JComboBox heroList) {
            this.heroList = heroList;
            return this;
        }

        Builder setHeroInfo(JLabel heroInfo) {
            this.heroInfo = heroInfo;
            return this;
        }

        Builder setSelectHero_listener(ActionListener listener) {
            this.selectHero_listener = listener;
            return this;
        }

        Builder setBtnBack_listener(ActionListener listener) {
            this.btnBack_listener = listener;
            return this;
        }

        Builder setBtnToAdventure_listener(ActionListener listener) {
            this.btnToAdventure_listener = listener;
            return this;
        }

        Builder setBtnExecute_listener(ActionListener listener) {
            this.btnExecute_listener = listener;
            return this;
        }

        GuiContinueA build() {
            this.panel = new JPanel();
            this.panel.setLayout(new GridBagLayout());

            this.heroInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
            this.heroInfo.setHorizontalAlignment(JTextField.CENTER);

            this.heroList.addActionListener(this.selectHero_listener);

            this.btnBack = new JButton("Back");
            this.btnBack.setAlignmentX(Component.CENTER_ALIGNMENT);
            this.btnBack.setMaximumSize(new Dimension(200, 50));
            this.btnBack.addActionListener(this.btnBack_listener);

            this.btnToAdventure = new JButton("To ADVENTURE !");
            this.btnToAdventure.setAlignmentX(Component.CENTER_ALIGNMENT);
            this.btnToAdventure.setMaximumSize(new Dimension(200, 50));
            this.btnToAdventure.addActionListener(this.btnToAdventure_listener);

            this.btnExecute = new JButton("Execute this fool !");
            this.btnExecute.setAlignmentX(Component.CENTER_ALIGNMENT);
            this.btnExecute.setMaximumSize(new Dimension(200, 50));
            this.btnExecute.addActionListener(this.btnExecute_listener);

            GridBagConstraints c = new GridBagConstraints();
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.fill = GridBagConstraints.HORIZONTAL;

            c.insets = new Insets(20, 250, 5, 250);
            this.panel.add(this.heroList, c);

            c.insets = new Insets(0, 100, 20, 100);
            this.panel.add(this.heroInfo, c);

            c.insets = new Insets(5, 250, 5, 250);
            this.panel.add(this.btnExecute, c);
            this.panel.add(this.btnBack, c);
            this.panel.add(this.btnToAdventure, c);

            return new GuiContinueA(this);
        }
    }
}
