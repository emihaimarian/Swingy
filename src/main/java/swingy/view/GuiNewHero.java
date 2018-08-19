package swingy.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

class GuiNewHero {
    private JPanel panel;

    private GuiNewHero(Builder builder) {
        this.panel = builder.panel;
    }

    // GETTER / SETTER
    JPanel getPanel() {
        return panel;
    }

    // BUILDER
    static class Builder {
        private JPanel panel;
        private JTextField heroName;
        private String[] heroClassArr;
        private JComboBox heroClass;
        private JButton btnBack;
        private JButton btnToAdventure;
        private ActionListener heroClass_listener;
        private ActionListener btnBack_listener;
        private ActionListener btnToAdventure_listener;

        Builder setHeroName(JTextField heroName) {
            this.heroName = heroName;
            return this;
        }

        Builder setHeroClass(JComboBox heroClass) {
            this.heroClass = heroClass;
            return this;
        }

        Builder setHeroClassArr(String[] heroClassArr) {
            this.heroClassArr = heroClassArr;
            return this;
        }

        Builder setHeroClass_listener(ActionListener listener) {
            this.heroClass_listener = listener;
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

        GuiNewHero build() {
            Font fontName = new Font("Helvetica", Font.BOLD, 34);

            this.panel = new JPanel();
            //this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.Y_AXIS));
            this.panel.setLayout(new GridBagLayout());

            //this.heroName = new JTextField(15);
            this.heroName.setHorizontalAlignment(JTextField.CENTER);
            this.heroName.setFont(fontName);

            //this.heroClass = new JComboBox(this.heroClassArr);
            this.heroClass.setSelectedIndex(0);
            this.heroClass.addActionListener(this.heroClass_listener);

            this.btnBack = new JButton("Back");
            this.btnBack.setAlignmentX(Component.CENTER_ALIGNMENT);
            this.btnBack.setMaximumSize(new Dimension(200, 50));
            this.btnBack.addActionListener(this.btnBack_listener);

            this.btnToAdventure = new JButton("To ADVENTURE !");
            this.btnToAdventure.setAlignmentX(Component.CENTER_ALIGNMENT);
            this.btnToAdventure.setMaximumSize(new Dimension(200, 50));
            this.btnToAdventure.addActionListener(this.btnToAdventure_listener);

            GridBagConstraints c = new GridBagConstraints();
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.fill = GridBagConstraints.HORIZONTAL;

            c.insets = new Insets(0, 100, 20, 100);
            this.panel.add(this.heroName, c);

            c.insets = new Insets(20, 250, 5, 250);
            this.panel.add(this.heroClass, c);

            c.insets = new Insets(5, 250, 5, 250);
            this.panel.add(this.btnBack, c);
            this.panel.add(this.btnToAdventure, c);

            return new GuiNewHero(this);
        }
    }
}
