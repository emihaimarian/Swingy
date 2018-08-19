package swingy.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GuiAdventure {
    private JPanel panel;

    private GuiAdventure(Builder builder) {
        this.panel = builder.panel;
    }

    // GETTER / SETTER
    JPanel getPanel() {
        return panel;
    }

    // BUILDER
    static class Builder {
        private JPanel panel;
        private JTextArea mapAreaTxt;
        private JTextArea messAreaTxt;
        private JPanel panelTravel;
        private JPanel panelCombat;
        private ActionListener save_listener;
        private ActionListener moveN_listener;
        private ActionListener moveE_listener;
        private ActionListener moveS_listener;
        private ActionListener moveW_listener;
        private ActionListener run_listener;
        private ActionListener fight_listener;

        Builder setMapAreaTxt(JTextArea mapAreaTxt) {
            this.mapAreaTxt = mapAreaTxt;
            return this;
        }

        Builder setMessAreaTxt(JTextArea messAreaTxt) {
            this.messAreaTxt = messAreaTxt;
            return this;
        }

        Builder setPanelTravel(JPanel panelTravel) {
            this.panelTravel = panelTravel;
            return this;
        }

        Builder setPanelCombat(JPanel panelCombat) {
            this.panelCombat = panelCombat;
            return this;
        }

        Builder setSave_listener(ActionListener listener) {
            this.save_listener = listener;
            return this;
        }

        Builder setMoveListeners(ActionListener moveN_listener, ActionListener moveE_listener, ActionListener moveS_listener, ActionListener moveW_listener) {
            this.moveN_listener = moveN_listener;
            this.moveE_listener = moveE_listener;
            this.moveS_listener = moveS_listener;
            this.moveW_listener = moveW_listener;
            return this;
        }

        Builder setRun_listener(ActionListener listener) {
            this.run_listener = listener;
            return this;
        }

        Builder setFight_listener(ActionListener listener) {
            this.fight_listener = listener;
            return this;
        }

        GuiAdventure build() {
            Font fontMap = new Font("Courier", Font.BOLD, 14);
            Font fontMess = new Font("Courier", Font.BOLD, 11);
            Font fontDirection = new Font("Helvetica", Font.PLAIN, 10);

            this.panel = new JPanel();

            JSplitPane mainSplit = new JSplitPane();
            //mainSplit.setBackground(Color.BLACK);
            JSplitPane rightSplit = new JSplitPane();
            //rightSplit.setBackground(Color.BLACK);
            JSplitPane rightBtmSplit = new JSplitPane();
            //rightBtmSplit.setBackground(Color.BLACK);

            JPanel left = new JPanel();
            JPanel rightTop = new JPanel();
            //JPanel rightBtmLeft = new JPanel(new GridBagLayout());
            //JPanel rightBtmRight = new JPanel();

            // --- PANELS ---
            // PANEL LEFT / MAP AREA
            JScrollPane mapArea;
            //JTextArea mapAreaTxt = new JTextArea();
            this.mapAreaTxt.setBackground(Color.BLACK);
            this.mapAreaTxt.setForeground(Color.WHITE);
            this.mapAreaTxt.setFont(fontMap);
            this.mapAreaTxt.setEditable(false);
            //this.mapAreaTxt.setMinimumSize(new Dimension(750, 750));
            //this.mapAreaTxt.setPreferredSize(new Dimension(750, 750));
            this.mapAreaTxt.setMaximumSize(new Dimension(780, 780));
            this.mapAreaTxt.setAlignmentX(Component.CENTER_ALIGNMENT);

            this.mapAreaTxt.append(getCastle());
            mapArea = new JScrollPane(this.mapAreaTxt, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            left.setLayout(new BorderLayout());
            left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
            //left.add(mapArea, BorderLayout.CENTER);
            //left.add(mapAreaTxt, BorderLayout.CENTER);
            left.add(mapAreaTxt);
            left.setMinimumSize(new Dimension(800, 800));
            left.setPreferredSize(new Dimension(800, 800));

            // PANEL RIGHT-TOP / MESSAGE AREA
            JScrollPane messArea;
            this.messAreaTxt.setLineWrap(true);
            this.messAreaTxt.setBackground(Color.BLACK);
            this.messAreaTxt.setForeground(Color.WHITE);
            this.messAreaTxt.setFont(fontMess);
            this.messAreaTxt.setEditable(false);
            messArea = new JScrollPane(this.messAreaTxt, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            rightTop.setLayout(new BorderLayout());
            rightTop.add(messArea, BorderLayout.CENTER);
            rightTop.setMinimumSize(new Dimension(400, 600));
            rightTop.setPreferredSize(new Dimension(400, 600));

            // PANEL RIGHT-BTM-LEFT
            this.panelTravel.setMinimumSize(new Dimension(200, 200));
            this.panelTravel.setPreferredSize(new Dimension(200, 200));

            JButton btnSaveAndQuit = new JButton("Save & Quit");
            btnSaveAndQuit.addActionListener(this.save_listener);
            btnSaveAndQuit.setPreferredSize(new Dimension(90,  20));

            Dimension directionBtnDim = new Dimension(30, 30);

            JButton btnGoNorth = new JButton(" N ");
            btnGoNorth.setMargin(new Insets(0, 0, 0, 0));
            btnGoNorth.setFont(fontDirection);
            btnGoNorth.addActionListener(this.moveN_listener);
            btnGoNorth.setPreferredSize(directionBtnDim);

            JButton btnGoEast = new JButton(" E ");
            btnGoEast.setMargin(new Insets(0, 0, 0, 0));
            btnGoEast.setFont(fontDirection);
            btnGoEast.addActionListener(this.moveE_listener);
            btnGoEast.setPreferredSize(directionBtnDim);

            JButton btnGoSouth = new JButton(" S ");
            btnGoSouth.setMargin(new Insets(0, 0, 0, 0));
            btnGoSouth.setFont(fontDirection);
            btnGoSouth.addActionListener(this.moveS_listener);
            btnGoSouth.setPreferredSize(directionBtnDim);

            JButton btnGoWest = new JButton(" W ");
            btnGoWest.setMargin(new Insets(0, 0, 0, 0));
            btnGoWest.setFont(fontDirection);
            btnGoWest.addActionListener(this.moveW_listener);
            btnGoWest.setPreferredSize(directionBtnDim);

            GridBagConstraints c = new GridBagConstraints();

            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 3;
            c.weightx = 0.0;
            c.weighty = 0;
            c.anchor = GridBagConstraints.LINE_END;
            c.insets = new Insets(0, 0, 15, 0);
            this.panelTravel.add(btnSaveAndQuit, c);

            c.gridx = 4;
            c.gridy = 2;
            c.gridwidth = 1;
            c.weightx = 0.2;
            c.weighty = 0.0;
            c.anchor = GridBagConstraints.CENTER;
            c.insets = new Insets(0, 0, 0, 0);
            this.panelTravel.add(btnGoNorth, c);

            c.gridx = 3;
            c.gridy = 3;
            c.gridwidth = 1;
            c.weightx = 0.2;
            c.weighty = 0.0;
            c.anchor = GridBagConstraints.LINE_END;
            c.insets = new Insets(0, 0, 0, 0);
            this.panelTravel.add(btnGoWest, c);

            c.gridx = 4;
            c.gridy = 4;
            c.gridwidth = 1;
            c.weightx = 0.2;
            c.weighty = 0.0;
            c.anchor = GridBagConstraints.CENTER;
            c.insets = new Insets(0, 0, 0, 0);
            this.panelTravel.add(btnGoSouth, c);

            c.gridx = 5;
            c.gridy = 3;
            c.gridwidth = 1;
            c.weightx = 0.0;
            c.weighty = 0.0;
            c.anchor = GridBagConstraints.CENTER;
            c.insets = new Insets(0, 0, 0, 0);
            this.panelTravel.add(btnGoEast, c);

            Component emptySpace = Box.createRigidArea(new Dimension(0, 25));
            c.gridx = 7;
            c.gridy = 5;
            c.gridwidth = 3;
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.anchor = GridBagConstraints.CENTER;
            c.insets = new Insets(0, 0, 0, 0);
            this.panelTravel.add(emptySpace, c);

            //rightBtmLeft.setVisible(false);

            // PANEL RIGT-BTM-RIGHT
            this.panelCombat.setLayout(new BoxLayout(this.panelCombat, BoxLayout.Y_AXIS));
            this.panelCombat.setMinimumSize(new Dimension(200, 200));
            this.panelCombat.setPreferredSize(new Dimension(200, 200));

            Dimension btnRunDim = new Dimension(150, 25);
            Dimension btnFightDim = new Dimension(180, 80);

            JButton btnRun = new JButton("Run you fool !");
            btnRun.addActionListener(this.run_listener);
            btnRun.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnRun.setMinimumSize(btnRunDim);
            btnRun.setPreferredSize(btnRunDim);
            btnRun.setMaximumSize(btnRunDim);

            JButton btnFight = new JButton("FIGHT !");
            btnFight.addActionListener(this.fight_listener);
            btnFight.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnFight.setMinimumSize(btnFightDim);
            btnFight.setPreferredSize(btnFightDim);
            btnFight.setMaximumSize(btnFightDim);

            this.panelCombat.add(btnRun);
            this.panelCombat.add(Box.createRigidArea(new Dimension(10, 30)));
            this.panelCombat.add(btnFight);

            this.panelCombat.setVisible(false);

            // RIGHT BOTTOM SPLIT
            rightBtmSplit.setPreferredSize( new Dimension(400, 200));
            rightBtmSplit.setDividerSize(0);
            rightBtmSplit.setDividerLocation(200);
            rightBtmSplit.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
            rightBtmSplit.setLeftComponent(this.panelTravel);
            rightBtmSplit.setRightComponent(this.panelCombat);

            // RIGHT SPLIT
            rightSplit.setPreferredSize( new Dimension(400, 800));
            rightSplit.setDividerSize(0);
            rightSplit.setDividerLocation(600);
            rightSplit.setOrientation(JSplitPane.VERTICAL_SPLIT);
            rightSplit.setTopComponent(rightTop);
            rightSplit.setBottomComponent(rightBtmSplit);

            // MAIN SPLIT
            mainSplit.setPreferredSize( new Dimension(1200, 800));
            mainSplit.setDividerSize(0);
            mainSplit.setDividerLocation(800);
            mainSplit.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
            mainSplit.setLeftComponent(left);
            mainSplit.setRightComponent(rightSplit);

            this.panel.add(mainSplit);

            return new GuiAdventure(this);
        }

        private String getCastle() {
            return "                                                                \n" +
                    "                             `,                                 \n" +
                    "                             ;:                                 \n" +
                    "                            `;:,                                \n" +
                    "                            ;;::                                \n" +
                    "                            ;;::                                \n" +
                    "                           ;;::::                               \n" +
                    "                           ;;::::                               \n" +
                    "                           ,,,...                               \n" +
                    "                            ..``     :                          \n" +
                    "                            .''`     ;:                         \n" +
                    "                            ...`    :;:                         \n" +
                    "                            ..``    ;:::                        \n" +
                    "                     :      ..``   `;:::                        \n" +
                    "                    ;:.     ..``   ;;:::.                       \n" +
                    "                    ;::     ..``   ;;::::                       \n" +
                    "                   ;;::     ..``   ,,....                       \n" +
                    "                   ;;:::   ``````   ..``                        \n" +
                    "                  `;;:::  .```````  .''`                        \n" +
                    "                  :::,,,:`.```````  .''`                        \n" +
                    "                   ,,... ..```````` ..``                        \n" +
                    "                   ..```.....```````..``                        \n" +
                    "                   .+':`.....````````.``                        \n" +
                    "                   ..``.......```````````                       \n" +
                    "                   ..`........````..`````                       \n" +
                    "                   ..`.....'..````....````                      \n" +
                    "                   .......'''.````....`````                     \n" +
                    "                   ......'''''.```.....`````                    \n" +
                    "                   ....''''''''```..;;;;;;,`                    \n" +
                    "                   ...;'''''''';;`.;;;;;;;;;`                   \n" +
                    "              `'''''.''''''''';;;;;;;;;;;;;;;;                  \n" +
                    "           '''''''''''''''''';;;;;;;;;;;;;;;;;                  \n" +
                    "          ''''''''''''''''''';;;;;;;;;;;;;;;;;;                 \n" +
                    "         ''''''''''''''''''';;;;;;;;;;;;;;;;;;;:                \n" +
                    "        ''''''''''''''''''';;;;;;;;;;;;;;;;;;;;;                \n" +
                    "       '''''''''''''''''''';;;;;;;;;;;;;;;;;;;;;;   ;;          \n" +
                    "      .''''''''''''''';''';;;;;;;;'';;;;';;;;;;;;; ;;;;,        \n" +
                    "     ,''''''''''''''';;;';;;;;;;;''''''''';;;;;;;;;;;;;;;       \n" +
                    "    .'''''''''''''';;;;;;;;;;;;;''''''''''';;;;;;;;;;;;;;;      \n" +
                    "   .''''''''''''''';;;;;;;;;;;;'''''''''''';;;;;;;;;;;;;;;;`    \n" +
                    "   '''''''''''''';;;;;;;;;;;;;'''''''''''''';;;;;;;;;;;;;;;;;   \n" +
                    "  ''''''''''''''';;;;;;;;;;;;''''''''''''''';;''''''';;;;;;;;;  \n" +
                    " '''''''''''''';;;;;;;;;;;;;'''''''''''''''''''''''''';;;;;;;;;,\n" +
                    "                                                                \n";
        }
    }
}
