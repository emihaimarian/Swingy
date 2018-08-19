package swingy.view;

import swingy.common.GamePhase;
import swingy.controller.common.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GuiView extends JFrame implements IView{
    private Controller controller;
    private JFrame guiWindow;
    private JPanel guiContainer;

    private String mapData;
    private String messData;

    // Elements MAIN MENU
    private JTextField messAreaMainMenu = new JTextField();

    // Elements NEW HERO
    private JTextField heroName;
    private JComboBox heroClass;

    // Elements ADVENTURE
    private JTextArea mapAreaTxt = new JTextArea();
    private JTextArea messAreaTxt = new JTextArea();
    private JPanel panelTravel = new JPanel(new GridBagLayout());
    private JPanel panelCombat = new JPanel();

    // Elements CONTINUE ADVENTURE / LOAD HERO
    private JComboBox<String> heroList = new JComboBox<>();
    private JLabel heroInfo = new JLabel();

    // Elements LOOT
    private JTextArea messAreaLoot = new JTextArea();

    // Actions COMMON
    private ActionListener console_listener;

    // Actions MAIN MENU
    private ActionListener btnNewHero_listener;
    private ActionListener btnContinueA_listener;
    private ActionListener btnRunAndHide_listener;

    // Actions NEW HERO
    private ActionListener heroClass_listener;
    private ActionListener btnBack_listener;
    private ActionListener btnToAdventure_listener;

    // Actions CONTINUE ADVENTURE / LOAD HERO
    private ActionListener selectHero_listener;
    private ActionListener executeHero_listener;

    // Actions ADVENTURE
    private ActionListener save_listener;
    private ActionListener moveN_listener;
    private ActionListener moveE_listener;
    private ActionListener moveS_listener;
    private ActionListener moveW_listener;
    private ActionListener run_listener;
    private ActionListener fight_listener;

    // Actions LOOT
    private ActionListener keepMyStuff_listener;
    private ActionListener getNewItem_listener;

    public GuiView() {

    }

    // Set Hero name
    private void setHeroNameTxt(String name) {
        this.heroName.setText(name);
    }

    private void setHeroClassIndex(int i) {
        this.heroClass.setSelectedIndex(i);
    }

    // Set hero class

    // --- INTERACTIONS for NEW HERO
    // Get hero name
    public String getHeroNameValue() {
        return this.heroName.getText();
    }

    // --- INTERACTIONS for CONTINUE ADVENTURE / LOAD HERO ---
    // Set items in hero list combo box
    public void updateHeroList(String[] heroListArr) {
        for(int i = this.heroList.getItemCount() - 1; i >= 0 ; i--) {
            this.heroList.removeItemAt(i);
        }

        for (String current : heroListArr) {
            //System.out.println(current);
            this.heroList.addItem(current);
        }

        //System.out.println("done updating hero list");
    }

    // Set hero info label
    public void updateSelectedHero(String heroInfo) {
        this.heroInfo.setText(heroInfo);
    }

    // --- INTERACTIONS for ADVENTURE ---
    // Set GUI to Travel mode
    public void setTravelMode() {
        this.panelTravel.setVisible(true);
        this.panelCombat.setVisible(false);
    }

    // set GUI to Combat mode
    public void setCombatMode() {
        this.panelTravel.setVisible(false);
        this.panelCombat.setVisible(true);

        //this.mapAreaTxt.setText(this.mapData);
        //this.messAreaTxt.setText(this.messData);
    }

    // Update MAP area
    public void updateMapArea(String content) {
        this.mapAreaTxt.setText(content);
    }

    // Update MESSAGE area
    public void updateMessArea(String content) {
        this.messAreaTxt.append(content);
    }

    // --- SET LISTENERS ---
    public void setMainMenu_listeners(ActionListener btnNewHero_listener, ActionListener btnContinueA_listener, ActionListener btnRunAndHide_listener) {
        this.btnNewHero_listener = btnNewHero_listener;
        this.btnContinueA_listener = btnContinueA_listener;
        this.btnRunAndHide_listener = btnRunAndHide_listener;
    }

    public void setNewHero_listeners(ActionListener heroClass_listener, ActionListener btnBack_listener, ActionListener btnToAdventure_listener) {
        this.heroClass_listener = heroClass_listener;
        this.btnBack_listener = btnBack_listener;
        this.btnToAdventure_listener = btnToAdventure_listener;
    }

    public void setContinueA_listeners(ActionListener selectHero_listener, ActionListener executeHero_listener) {
        this.selectHero_listener = selectHero_listener;
        this.executeHero_listener = executeHero_listener;
    }

    public void setLoot_Actions(ActionListener keepMyStuff_listener, ActionListener getNewItem_listener) {
        this.keepMyStuff_listener = keepMyStuff_listener;
        this.getNewItem_listener = getNewItem_listener;
    }

    public void setAdventure_listeners(ActionListener console_listener,
                                       ActionListener save_listener,
                                       ActionListener moveN_listener, ActionListener moveW_listener, ActionListener moveS_listener, ActionListener moveE_listener,
                                       ActionListener run_listener,
                                       ActionListener fight_listener) {
        this.console_listener = console_listener;
        this.save_listener = save_listener;
        this.moveN_listener = moveN_listener;
        this.moveW_listener = moveW_listener;
        this.moveS_listener = moveS_listener;
        this.moveE_listener = moveE_listener;
        this.run_listener = run_listener;
        this.fight_listener = fight_listener;
    }

    public void buildGui() {
        this.guiWindow = new JFrame("DUNGEONS OF DOOM !");
        this.guiWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.guiWindow.setSize(1200, 800);
        this.guiWindow.setPreferredSize(new Dimension(1200, 800));
        this.guiWindow.setResizable(false);

        this.heroName = new JTextField(15);
        //this.heroClass = new JComboBox(new String[]{"warrior", "shinobi", "berseker"});
        this.heroClass = new JComboBox(new String[]{
                "Warrior - defence bonus, expertise attack",
                "Shinobi - can dodge, sneak attack",
                "Berseker - hp bonus, power attack"});

        this.guiContainer = new JPanel(new CardLayout());
        this.guiContainer.add((new GuiMainMenu.Builder().
                setMessArea(this.messAreaMainMenu).
                setBtnNewHero_listener(this.btnNewHero_listener).
                setBtnContinueA_listener(this.btnContinueA_listener).
                setBtnRunAndHide_listener(this.btnRunAndHide_listener).
                build()).getPanel(), GamePhase.MAIN_MENU.toString());
        this.guiContainer.add((new GuiNewHero.Builder().
                setHeroName(this.heroName).
                setHeroClass(this.heroClass).
                setHeroClass_listener(this.heroClass_listener).
                setBtnBack_listener(this.btnBack_listener).
                setBtnToAdventure_listener(this.btnToAdventure_listener).
                build()).getPanel(), GamePhase.NEW_HERO.toString());
        this.guiContainer.add((new GuiContinueA.Builder().
                setHeroList(this.heroList).
                setHeroInfo(this.heroInfo).
                setSelectHero_listener(this.selectHero_listener).
                setBtnBack_listener(this.btnBack_listener).
                setBtnToAdventure_listener(this.btnToAdventure_listener).
                setBtnExecute_listener(this.executeHero_listener).
                build()).getPanel(), GamePhase.CONTINUE_A.toString());
        this.guiContainer.add((new GuiAdventure.Builder().
                setMapAreaTxt(this.mapAreaTxt).
                setMessAreaTxt(this.messAreaTxt).
                setPanelTravel(this.panelTravel).
                setPanelCombat(this.panelCombat).
                setSave_listener(this.save_listener).
                setMoveListeners(this.moveN_listener, this.moveE_listener, this.moveS_listener, this.moveW_listener).
                setRun_listener(this.run_listener).
                setFight_listener(this.fight_listener).
                build()).getPanel(), GamePhase.ADVENTURE_MOVE.toString());
        this.guiContainer.add(new GuiLoot.Builder().
                setMessArea(this.messAreaLoot).
                setBtnKeepMyStuff_listener(this.keepMyStuff_listener).
                setBtnGetNewItem_listener(this.getNewItem_listener).
                build().getPanel(), GamePhase.LOOT.toString());

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new FlowLayout(FlowLayout.RIGHT));
        wrapper.setMinimumSize(new Dimension(1200, 800));
        wrapper.setPreferredSize(new Dimension(1200, 800));
        wrapper.setMaximumSize(new Dimension(1200, 800));
        JButton btnConsole = new JButton("console");
        btnConsole.addActionListener(this.console_listener);
        btnConsole.setPreferredSize(new Dimension(60,  20));

        wrapper.add(btnConsole);
        btnConsole.setAlignmentX(Component.RIGHT_ALIGNMENT);
        wrapper.add(this.guiContainer);
        this.guiWindow.add(wrapper);

        //this.guiWindow.add(this.guiContainer);
        this.guiWindow.setVisible(true);
        this.guiWindow.pack();
    }

    // --- INTERFACE IMPLEMENTATION ---

    // [TODO] UNUSED FOR GUI ???
    public void registerController(Controller controller) {
        this.controller = controller;
    }

    /*
     * Show the selected "layer" / card in GUI MODE
     */
    public void showGamePhase(GamePhase gamePhase, String mapData, String messData) {
        this.mapData = mapData;
        this.messData = messData;

        this.mapAreaTxt.setText(mapData);
        this.messAreaMainMenu.setText(messData);

        if (gamePhase == GamePhase.ADVENTURE_ENCOUNTER) {
            this.messAreaTxt.append("\n" + messData);
            CardLayout cl = (CardLayout) (this.guiContainer.getLayout());
            cl.show(this.guiContainer, GamePhase.ADVENTURE_MOVE.toString());
            this.setCombatMode();
        }
        else if (gamePhase == GamePhase.LOOT) {
            this.messAreaLoot.setText(this.messData);
            this.setTravelMode();
            CardLayout cl = (CardLayout) (this.guiContainer.getLayout());
            cl.show(this.guiContainer, gamePhase.toString());
        }
        else {
            this.messAreaTxt.setText(messData);
            this.setTravelMode();
            CardLayout cl = (CardLayout) (this.guiContainer.getLayout());
            cl.show(this.guiContainer, gamePhase.toString());
        }
    }

    public void setNewPlayerData(String name, int classIndex, String heroInfo) {
        this.setHeroNameTxt(name);
        this.setHeroClassIndex(classIndex);
    }

    // [TODO] Get mapData from CONTROLLER !
    public void setActive(boolean status, String mapData, String messData) {
        this.mapData = mapData;
        this.messData = messData;
        this.guiWindow.setVisible(status);
    }

    // TEST
    public void exit() {
        System.exit(0);
    }
}
