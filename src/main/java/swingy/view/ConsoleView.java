package swingy.view;

import swingy.common.GamePhase;
import swingy.controller.common.Controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ConsoleView implements IView{
    private Controller controller;
    private String mapData;
    private String messData;

    private InputStreamReader reader = new InputStreamReader(System.in);
    private BufferedReader in = new BufferedReader(reader);
    //private boolean active = false;

    // Elements CONTINUE ADVENTURE / LOAD HERO
    private String[] heroList;
    private String selectedHeroInfo; // [TODO] Unused for Console mode !!!

    // --- SHOW OUTPUT BASED ON GAME PHASE ---

    private void showMainMenu() {
        System.out.print(
                this.messData +
                "\nMAIN MENU\n\n" +
                "1. New HERO\n" +
                "2. Continue ADVENTURE\n" +
                "0. Run and HIDE !\n"
        );
    }

    private void showNewHero() {
        String output =
                this.messData +
                "NEW HERO\n\n" +
                "1. Change Name\n" +
                "2. Change Class\n" +
                "42. To ADVENTURE_MOVE !\n" +
                "0. Back\n";
        System.out.print(output);
    }

    private void showNewHeroSetName() {
        System.out.print("SET HERO NAME\n\n");
    }

    private void showNewHeroSetClass() {
        System.out.print("SET HERO CLASS\n\n" +
                "1. Warrior - defence bonus, expertise attack\n" +
                "2. Shinobi - can dodge, sneak attack\n" +
                "3 Berseker - hp bonus, power attack\n");
    }

    private void showContinueA() {
        int heroIndex;
        System.out.print("CONTINUE ADVENTURE\n\n");
        for (int i = 0; i < this.heroList.length; i++) {
            heroIndex = i + 1;
            System.out.println(heroIndex + ". " + this.heroList[i]);
        }
    }

    private void showAdventureMove() {
        System.out.print(this.mapData);
        System.out.print("\n" + this.messData);
        System.out.print("\nmove: w a s d | commands: save, guimode or execute (will destroy hero)\n");
    }

    private void showAdventureEncounter() {
        String output =
                this.messData +
                "\n1. FIGHT !\n" +
                "0. Run like a scaredy cat !\n";;
        System.out.print(output);
    }

    private void showLoot() {
        String output =
                this.messData +
                "\n\n1. Keep my stuff\n" +
                "2. I want it NOW !!!\n";
        System.out.print(output);
    }

    // --- READ INPUT ---

    private void readInput() {
        String input = "";

        System.out.print("\n|: ");
        try {
            input = this.in.readLine();
        }
        catch (Exception e) {
            System.out.printf(e + "\n");
            System.exit(1);
        }

        if (input.equals("guimode"))
            this.controller.guiMode();
        if (input.equals("execute"))
            this.controller.executeHero();

        switch (this.controller.getGamePhase()) {
            case MAIN_MENU:
                processMainMenu(input);
                break;
            case NEW_HERO:
                processNewHero(input);
                break;
            case NEW_HERO_SET_NAME:
                processNewHeroSetName(input);
                break;
            case NEW_HERO_SET_CLASS:
                processNewHeroSetClass(input);
                break;
            case CONTINUE_A:
                processContinueA(input);
                break;
            case ADVENTURE_MOVE:
                processAdventureMove(input);
                break;
            case ADVENTURE_ENCOUNTER:
                processAdventureEncounter(input);
                break;
            case LOOT:
                processLoot(input);
                break;
        }

    }

    // --- PROCESS INPUT, BASED ON GAME PHASE ---

    private void processMainMenu(String input) {
        switch (input) {
            case "1":
                this.controller.newHero(true);
                break;
            case "2":
                this.controller.continueA();
                break;
            case "0":
                this.controller.runAndHide();
                break;
            default:
                this.controller.mainMenu();
        }
    }

    private void processNewHero(String input) {
        switch (input) {
            case "1":
                this.controller.choseName();
                break;
            case "2":
                this.controller.choseClass();
                break;
            case "42":
                this.controller.toAdventure(true);
                break;
            case "0":
                this.controller.mainMenu();
                break;
            default:
                this.controller.newHero(true);
        }
    }

    private void processNewHeroSetName(String input) {
        this.controller.selectName(input);
    }

    private void processNewHeroSetClass(String input) {
        if (input.matches("\\d+")) {
            int i = Integer.parseInt(input);
            if (i == 1 || i == 2 || i == 3)
                this.controller.selectClass(Integer.parseInt(input) - 1);
            else
                this.showGamePhase(this.controller.getGamePhase(), this.mapData, this.messData);
        }
        else
            this.showGamePhase(this.controller.getGamePhase(), this.mapData, this.messData);
    }

    private void processContinueA(String input) {
        if (input.equals("0"))
            this.controller.mainMenu();
        else if (input.matches("\\d+")) {
            //System.out.println("sent index: " + Integer.parseInt(input));
            this.controller.selectHero(Integer.parseInt(input) - 1, true);
        }
        else
            this.controller.continueA();
    }

    private void processAdventureMove(String input) {
        switch (input.toLowerCase()) {
            case "w":
            case "a":
            case "s":
            case "d":
                this.controller.move(input.charAt(0));
                break;
            case "save":
                this.controller.save();
                break;
            default:
                this.controller.move(' ');
        }
    }

    private void processAdventureEncounter(String input) {
        //while (!(input.equals("0") || input.equals("1")))
        {
            switch (input) {
                case "1":
                    this.controller.fight();
                    break;
                case "0":
                    this.controller.run();
                    break;
                default:
                    this.showGamePhase(this.controller.getGamePhase(), this.mapData, this.messData);
            }
        }
    }

    private void processLoot(String input) {
        {
            switch (input) {
                case "1":
                    this.controller.move('0');
                    break;
                case "2":
                    this.controller.keepNewItem();
                    break;
                default:
                    this.showGamePhase(this.controller.getGamePhase(), this.mapData, this.messData);
            }
        }
    }

    // --- INTERFACE IMPLEMENTATION ---

    public void registerController(Controller controller){
        this.controller = controller;
    }

    /*
     * Show the selected "layer" / card in CONSOLE MODE
     * After, call read input
     */
    public void showGamePhase(GamePhase gamePhase, String mapData, String messData) {
        //System.out.println("Show window: " + gamePhase.toString());
        this.mapData = mapData;
        this.messData = messData;

        // CLEAR CONSOLE <NOT FOR WINDOWS !>
        System.out.print("\033[H\033[2J");
        System.out.flush();

        switch (this.controller.getGamePhase()) {
            case MAIN_MENU:
                showMainMenu();
                break;
            case NEW_HERO:
                showNewHero();
                break;
            case NEW_HERO_SET_NAME:
                showNewHeroSetName();
                break;
            case NEW_HERO_SET_CLASS:
                showNewHeroSetClass();
                break;
            case CONTINUE_A:
                showContinueA();
                break;
            case ADVENTURE_MOVE:
                showAdventureMove();
                break;
            case ADVENTURE_ENCOUNTER:
                showAdventureEncounter();
                break;
            case LOOT:
                showLoot();
                break;
        }

        this.readInput();
    }

    // [TODO] Get mapData from CONTROLLER !
    public void setActive(boolean isActive, String mapData, String messData) {
        if (isActive) {
            this.mapData = mapData;
            this.messData = messData;
            this.showGamePhase(this.controller.getGamePhase(), this.mapData, this.messData);
        }
    }

    public void setNewPlayerData(String name, int classIndex, String heroInfo) {

    }

    public void updateHeroList(String[] heroListArr) {
        this.heroList = heroListArr;
    }

    // [TODO] Unused for Console mode !!!
    // Set hero info label
    public void updateSelectedHero(String selectedHeroInfo) {
        this.selectedHeroInfo = selectedHeroInfo;
    }

    // CONSOLE ONLY
    public String getHeroNameValue() {
        return "";
    }
}
