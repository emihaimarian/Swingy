package swingy.controller.common;

import swingy.common.GamePhase;
import swingy.common.ItemType;
import swingy.model.*;
import swingy.view.ConsoleView;
import swingy.view.GuiView;
import swingy.view.IView;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

public class Controller {
    private boolean playerDied = false;
    //private boolean guiMode;
    private GamePhase gamePhase;
    private GuiView guiView;
    private ConsoleView consoleView;
    private IView view;
    private Player player;
    Enemy enemy;
    GameMap gameMap = new GameMap();

    private LinkedHashMap<Integer, Player> heroList;

    Item newItem; // item you find

    // Data to send to View
    private String mapData;
    private String messData;
    private String enemyImg;
    private String enemyStats;
    private String playerStats;

    // TEST
    String[] heroListArr = new String[]{"Gorgona", "Zurggy", "Harkum", "Baium", "Nertqal"};

    public Controller (GuiView guiView, ConsoleView consoleView, Player player, GameMap gameMap) {
        this.gamePhase = GamePhase.MAIN_MENU;
        this.guiView = guiView;
        this.guiView.registerController(this);
        this.consoleView = consoleView;
        this.consoleView.registerController(this);
        this.player = player;
        this.enemy = enemy;
        this.gameMap = gameMap;

        this.consoleView.setActive(false, this.mapData, this.messData);
        this.view = this.guiView;
    }

    public GamePhase getGamePhase() {
        return this.gamePhase;
    }

    /*
     * ACTIONS
     * GUI: Called by Actions
     * Console: Called from "process input" methods
     */

    // --- COMMON ---

    public void back() {
        //this.player.setName("");
        this.mainMenu();
    }

    // Switch to Console mode from Gui mode
    public void console() {
        //System.out.println("Switch to Console mode");
        this.view.setActive(false, this.mapData, this.messData);
        this.view = this.consoleView;
        this.view.updateHeroList(this.heroListArr); // [TODO] send full game data
        this.view.setActive(true, this.mapData, this.messData);
    }

    // Switch to Gui mode from Console mode
    public void guiMode() {
        //System.out.println("Switch to Gui mode");
        this.view.setActive(false, this.mapData, this.messData);
        this.view = this.guiView;

        switch (this.gamePhase) {
            case MAIN_MENU:
                this.mainMenu();
                break;
            case NEW_HERO:
                this.newHero(false);
                break;
            case NEW_HERO_SET_NAME:
                this.newHero(false);
                break;
            case NEW_HERO_SET_CLASS:
                this.newHero(false);
                break;
            case CONTINUE_A:
                this.view.updateHeroList(this.heroListArr); // [TODO] send full game data
                this.continueA();
                break;
            case ADVENTURE_MOVE:
                this.toAdventure(false);
                break;
            case ADVENTURE_ENCOUNTER:
                this.toAdventure(false);
                break;
        }

        this.view.setActive(true, this.mapData, this.messData);
    }

    // Start adventure from New Hero or Continue Adventure
    // OR switch from gui / console mode
    // [TODO] If from GuiView, get name from textBox
    public void toAdventure(boolean newAdventure) {
        if (newAdventure) {
            boolean nameUnique = true;
            this.heroList = Player.loadPlayers();
            for (Map.Entry<Integer, Player> el : this.heroList.entrySet()) {
                Player currentPlayer = el.getValue();
                if (currentPlayer.getName().equalsIgnoreCase(this.player.getName())) {
                    nameUnique = false;
                    break;
                }
            }

            if (!nameUnique && !this.player.getIsInserted()) {
                this.gamePhase = GamePhase.NEW_HERO;
                this.newHero(true);
                return;
            }
            else {

                this.gameMap.generateMap(this.player.getLevel());
                //this.mapData = this.gameMap.dbg_getMap();
                this.mapData = this.getMap();
                //this.player.insertPlayer();
            }
        }
        //this.player.updatePlayer(); // CASE: NAME / CLASS CHANGED BEFORE START
        this.messData = this.player.getPlayerData();
        this.gamePhase = GamePhase.ADVENTURE_MOVE;
        this.view.showGamePhase(GamePhase.ADVENTURE_MOVE, this.mapData, this.messData);
    }

    // Show Main Menu
    public void mainMenu() {
        if (!this.playerDied) {
            this.messData = "";
        }
        else {
            this.playerDied = false;
        }

        this.gamePhase = GamePhase.MAIN_MENU;
        this.view.showGamePhase(GamePhase.MAIN_MENU, this.mapData, this.messData);
    }

    // --- MAIN MENU ---
    public void newHero(boolean random) {
        if (random) {
        //if (this.player.getName().equals("")) {
            this.player = Player.generateStartingPlayer();
            this.player.setXp(0);
            //this.player.insertPlayer(); // SAVE TO DATABASE
        }
        this.gamePhase = GamePhase.NEW_HERO;
        this.messData = this.player.getPlayerData();
        this.view.setNewPlayerData(this.player.getName(), this.player.getPlayerClassIndex(), "xxx");
        this.view.showGamePhase(GamePhase.NEW_HERO, this.mapData, this.messData);
    }

    // [TODO] Send full list of player data: name, level, class, ...
    public void continueA() {

        this.heroList = Player.loadPlayers();

        if (heroList.size() > 0) {

            ArrayList<String> heroListArr2 = new ArrayList<>();

            for (Map.Entry<Integer, Player> el : this.heroList.entrySet()) {
                Player player = el.getValue();
                heroListArr2.add(player.getName());

                //System.out.println(el.getKey() + " | " + player.getName());
            }

            //System.out.println("-------------------");

            this.heroListArr = heroListArr2.toArray(new String[0]);

            // --- HERO LIST TEST---
            this.view.updateHeroList(this.heroListArr);

            this.view.updateSelectedHero("level " + this.player.getLevel() + " " + this.player.getPlayerClass() + " | atk: " + this.player.getTotalAttack() + ", def: " + this.player.getTotalDefence() + ", hp: " + this.player.getTotalHitPoints());

            this.gamePhase = GamePhase.CONTINUE_A;
            this.view.showGamePhase(GamePhase.CONTINUE_A, this.mapData, this.messData);
        }
        else {
            this.messData = "All HEROES are out there, FIGHTING !!!";
            this.gamePhase = GamePhase.MAIN_MENU;
            this.view.showGamePhase(GamePhase.MAIN_MENU, this.mapData, this.messData);
        }
    }

    public void runAndHide() {
        System.out.println("Bye now !");
        System.exit(0);
    }

    // --- NEW HERO ---

    public void selectClass(int index) {
        if (index >= 0 && index < 3) {
            if (index == 0)
                this.player.setPlayerClass("warrior");
            else if (index == 1)
                this.player.setPlayerClass("shinobi");
            else
                this.player.setPlayerClass("berseker");
            this.newHero(false);
            //this.gamePhase = GamePhase.NEW_HERO;
            //this.view.showGamePhase(GamePhase.NEW_HERO, this.mapData, this.messData);
        }
        else
            this.choseClass();
    }

    // GUI MODE ONLY
    public void getHeroNameFromGuiFieldThenToAdventure() {
        if (this.gamePhase == GamePhase.NEW_HERO) {
            //System.out.println(this.view.getHeroNameValue());

            try {
                //System.out.println("this.view.getHeroNameValue(): " + this.view.getHeroNameValue());
                this.player.setName(this.view.getHeroNameValue());
                this.toAdventure(true);
            }
            catch (Exception e) {
                this.newHero(true);
            }
        }
        else if (this.gamePhase == GamePhase.CONTINUE_A) {
            this.toAdventure(true);
        }
        else {
            System.out.println("Undefined in getHeroNameFromGuiFieldThenToAdventure()");
        }
    }

    // CONSOLE MODE ONLY
    public void choseClass() {
        this.gamePhase = GamePhase.NEW_HERO_SET_CLASS;
        this.view.showGamePhase(GamePhase.NEW_HERO_SET_CLASS, this.mapData, this.messData);
    }

    // CONSOLE MODE ONLY
    public void choseName() {
        this.gamePhase = GamePhase.NEW_HERO_SET_NAME;
        this.view.showGamePhase(GamePhase.NEW_HERO_SET_NAME, this.mapData, this.messData);
    }

    // CONSOLE MODE ONLY
    public void selectName(String name) {
        try {
            this.player.setName(name);

            /*
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();

            // Validate object and get constraints failed
            Set<ConstraintViolation<Player>> violations = validator.validate(this.player);

            for (ConstraintViolation<Player> violation : violations) {
                System.out.println(violation.getMessage());
            }
            */
        }
        catch (Exception e){
            this.newHero(true);
        }

        this.newHero(false);
    }

    // --- CONTINUE ADVENTURE / LOAD HERO

    // [TODO] Set Player to selected Player
    public void selectHero(int index, boolean startAdventure) {
        // [TODO] Validate hero index
        if (this.heroList.size() > 0) {

            //int t = index + 1;
            //System.out.println("used index: " + t);
            this.player = this.heroList.get(index + 1);

            if (this.player == null)
                this.player = this.heroList.get(1);
            this.player.buildItemsFromSavedData();

            // CONSOLE MODE
            if (startAdventure) {
                if (index >= 0 && index - 1 < this.heroListArr.length)
                    this.toAdventure(true);
                else
                    this.continueA();
            } else {
                this.view.updateSelectedHero("level " + this.player.getLevel() + " " + this.player.getPlayerClass() + " | atk: " + this.player.getTotalAttack() + ", def: " + this.player.getTotalDefence() + ", hp: " + this.player.getTotalHitPoints());
            }
        }
    }

    // Execute / delete selected hero
    public void executeHero() {
        this.obliviateHero();
        this.mainMenu();
    }

    // --- ADVENTURE ---
    public void move(char direction) {
        this.gamePhase = GamePhase.ADVENTURE_MOVE; // in case you came here after finding am item, you have to set gamePhase

        switch (direction) {
            case 'w':
            case 'W':
                //System.out.println("Moving North");
                if (this.gameMap.getPlayerY() - 1 < 0) {
                    this.nextMap();
                }
                else if ((this.gameMap.getPlayerY() - 1 >= 0 && this.gameMap.getMapTile(this.gameMap.getPlayerX(), this.gameMap.getPlayerY() - 1) != 1))
                    this.gameMap.setPlayerY(this.gameMap.getPlayerY() - 1);
                // Healing on the move - when going to an undiscovered tile, heal for 20%
                healOnMove();
                //this.message = "THE DUNGEONS OF DOOM, maze " + GameMap.getMapNr() + ", depth " + this.gameMap.getMapLevel();
                //this.consoleOutput = ConsoleOutput.adventureMove(this.message, this.player, this.gameMap);
                break;
            case 'a':
            case 'A':
                //System.out.println("Moving West");
                if (this.gameMap.getPlayerX() - 1 < 0) {
                    this.nextMap();
                }
                else if ((this.gameMap.getPlayerX() - 1 >= 0 && this.gameMap.getMapTile(this.gameMap.getPlayerX() - 1, this.gameMap.getPlayerY()) != 1))
                    this.gameMap.setPlayerX(this.gameMap.getPlayerX() - 1);
                // Healing on the move - when going to an undiscovered tile, heal for 20%
                healOnMove();
                //this.message = "THE DUNGEONS OF DOOM, maze " + GameMap.getMapNr() + ", depth " + player.getLevel();
                //this.consoleOutput = ConsoleOutput.adventureMove(this.message, this.player, this.gameMap);
                break;
            case 's':
            case 'S':
                //System.out.println("Moving South");
                if (this.gameMap.getPlayerY() + 1 == this.gameMap.getMapSize()) {
                    this.nextMap();
                }
                else if ((this.gameMap.getPlayerY() + 1 < this.gameMap.getMapSize() && this.gameMap.getMapTile(this.gameMap.getPlayerX(), this.gameMap.getPlayerY() + 1) != 1))
                    this.gameMap.setPlayerY(this.gameMap.getPlayerY() + 1);
                // Healing on the move - when going to an undiscovered tile, heal for 20%
                healOnMove();
                //this.message = "THE DUNGEONS OF DOOM, maze " + GameMap.getMapNr() + ", depth " + player.getLevel();
                //this.consoleOutput = ConsoleOutput.adventureMove(this.message, this.player, this.gameMap);
                break;
            case 'd':
            case 'D':
                //System.out.println("Moving East");
                if (this.gameMap.getPlayerX() + 1 == this.gameMap.getMapSize()) {
                    this.nextMap();
                }
                else if ((this.gameMap.getPlayerX() + 1 < this.gameMap.getMapSize() && this.gameMap.getMapTile(this.gameMap.getPlayerX() + 1, this.gameMap.getPlayerY()) != 1))
                    this.gameMap.setPlayerX(this.gameMap.getPlayerX() + 1);
                // Healing on the move - when going to an undiscovered tile, heal for 20%
                healOnMove();
                //this.message = "THE DUNGEONS OF DOOM, maze " + GameMap.getMapNr() + ", depth " + player.getLevel();
                //this.consoleOutput = ConsoleOutput.adventureMove(this.message, this.player, this.gameMap);
                break;
        }
        String playerData = player.getPlayerData();

        //this.mapData = this.gameMap.dbg_getMap();
        this.mapData = this.getMap();
        this.messData = this.player.getWeapon().getInfo() + " | " + this.player.getArmor().getInfo() + " | " + this.player.getHelmet().getInfo();

        // If there is an enemy, go to COMBAT !
        if (this.gameMap.getMapTile(this.gameMap.getPlayerX(), this.gameMap.getPlayerY()) >= 3) {
            this.gamePhase = GamePhase.ADVENTURE_ENCOUNTER;
            this.enemy = this.generateEnemy(this.gameMap.getMapLevel()); // [TODO] take into account the type of enemy from the map !
            //this.message = "You are attacked by an angry " + enemy.getName() + " ! " + "Att: " + enemy.getAttack() + " Def: " + enemy.getDefence() + " Hp: " + enemy.getCurrentHp();
            //this.consoleOutput = ConsoleOutput.adventureEncounter(this.message, this.enemy, this.player);
            this.enemyImg = this.enemy.generateImg();
            this.enemyStats = this.enemy.generateStats();
            this.messData = this.enemyImg + this.enemyStats + "\n\n" + this.messData;
            this.view.showGamePhase(GamePhase.ADVENTURE_ENCOUNTER, this.mapData, this.messData);
        }
        else {
            this.view.showGamePhase(GamePhase.ADVENTURE_MOVE, this.mapData + "\n" + playerData, this.messData);
        }
    }

    private void healOnMove() {
        if (this.gameMap.getPlayerX() >= 0 && this.gameMap.getPlayerX() < this.gameMap.getMapSize() && this.gameMap.getPlayerY() >= 0 && this.gameMap.getPlayerY() < this.gameMap.getMapSize())
            if (gameMap.isFogAt(gameMap.getPlayerX(), gameMap.getPlayerY()))
                player.healOnMove();
    }

    private void nextMap() {
        //this.message = "You survived, hmmmm... but can you ESCAPE ? [evil laughter]";
        this.gameMap.generateMap(this.player.getLevel());
    }

    public void save() {
        if (this.player.getIsInserted())
            this.player.updatePlayer();
        else
            this.player.insertPlayer();

        //System.out.println("Save Hero and return to Main Menu");
        this.gamePhase = GamePhase.MAIN_MENU;
        this.mainMenu();
        //this.view.showGamePhase(GamePhase.MAIN_MENU, this.mapData, this.messData);

        try {
            this.player.setName(""); // RESET PLAYER
        }
        catch (Exception e) {

        }
    }

    public void fight() {
        Random random = new Random();
        int nr;
        double weaponArmorIgnore = 0;
        double weaponElementalDamage = 0;
        double weaponDblDamageChance = 60;
        double weaponHalfDamageChance = 90;
        int weaponCursedDamage = 0;

        // Hero WEAPON
        ItemType[] type = player.getWeapon().getType();
        for (int i = 0; i < 3; i++) {
            if (type[i] == ItemType.MARTIAL)
                weaponArmorIgnore = 0.25;
            else if (type[i] == ItemType.ELEMENTAL_FIRE || type[i] == ItemType.ELEMENTAL_FROST || type[i] == ItemType.ELEMENTAL_SHOCK) {
                weaponElementalDamage = this.player.getWeapon().getAttack() / 3.0;
                weaponElementalDamage = weaponElementalDamage >= 1 ? weaponElementalDamage : 1;
            }
            else if (type[i] == ItemType.CURSED) {
                nr = random.nextInt(100);
                if (nr > weaponDblDamageChance && nr <= weaponHalfDamageChance)
                    weaponCursedDamage = this.player.getTotalAttack();
                else if (nr > weaponHalfDamageChance)
                    weaponCursedDamage = -this.player.getTotalAttack() / 2;
            }
        }

        // Hero ARMOR
        int defenceBonus = 0;
        int armorElementalDamage = 0;
        double enemyAttacMultiplier = 1.0;
        type = player.getArmor().getType();

        for (int i = 0; i < 3; i++) {
            if (type[i] == ItemType.MARTIAL) {
                defenceBonus = this.player.getTotalDefence() / 4;
            }
            else if (type[i] == ItemType.ELEMENTAL_FIRE || type[i] == ItemType.ELEMENTAL_FROST || type[i] == ItemType.ELEMENTAL_SHOCK) {
                nr = random.nextInt(100);
                if (nr > 70) {
                    armorElementalDamage = this.player.getTotalAttack() / 4;
                    armorElementalDamage = armorElementalDamage >= 1 ? armorElementalDamage : 1;
                }
            }
            else if (type[i] == ItemType.CURSED) {
                nr = random.nextInt(100);
                if (nr > 80 && nr <= 95)
                    enemyAttacMultiplier = 0.5;
                else if (nr > 95)
                    enemyAttacMultiplier = 2.0;
            }
        }

        int hero_enemy_attack = this.player.getTotalAttack() - (int)(this.enemy.getDefence() * (1.0 - weaponArmorIgnore)) + (int)weaponElementalDamage + weaponCursedDamage + armorElementalDamage;
        int enemy_hero_attack = this.enemy.getAttack() - this.player.getTotalDefence() + defenceBonus;

        hero_enemy_attack = hero_enemy_attack > 0 ? hero_enemy_attack : 1;
        enemy_hero_attack = enemy_hero_attack > 0 ? enemy_hero_attack : 1;
        enemy_hero_attack *= enemyAttacMultiplier;

        // CLASS SPECIFIC STUFF
        String classSpecificMess = "";
        nr = random.nextInt(100);
        nr += 3 * (this.player.getLevel() - this.gameMap.getMapLevel());
        switch (this.player.getPlayerClass()) {
            case "warrior":
                if (nr > 50) {
                    hero_enemy_attack *= 1.25;
                    enemy_hero_attack *= 0.85;
                    classSpecificMess = "you make an expertise attack (bonus on damage and defence)";
                }
                break;
            case "shinobi":
                if (nr > 80) {
                    enemy_hero_attack /= 2;
                    classSpecificMess = "you dodge and avoid 1/2 damage";
                }
                else if (nr > 60) {
                    enemy_hero_attack /= 8;
                    classSpecificMess = "you dodge and avoid 1/8 damage";
                }
                else if (nr > 10 && nr < 15) {
                    hero_enemy_attack *= 10;
                    classSpecificMess = "you kick him in the nuts !";
                }
                break;
            case "berseker":
                if ((nr > 25 && nr < 45) || (nr > 75 && nr < 95)) {
                    hero_enemy_attack *= 4;
                    classSpecificMess = "you make a POWER ATTACK !";
                }
                break;
        }

        // Enemy specific stuff
        int enemyType = this.gameMap.getMapTile(this.gameMap.getPlayerX(), this.gameMap.getPlayerY());
        String enemySpecificMess = "";

        // has low chance to evade hero attack
        if (enemyType == 5) {
            nr = random.nextInt(100);
            if (nr > 85) {
                hero_enemy_attack = 0;
                enemySpecificMess = "The Shade evades your attack, lol";
            }
        }
        // has fair chance to steal 1/4 your current hp, and heal for half of that
        else if (enemyType == 6) {
            nr = random.nextInt(100);
            if (nr > 75) {
                enemy_hero_attack = 0;
                int toHeal = this.player.getCurrentHitPoints() / 8;
                this.player.takeDamage(toHeal * 2);
                this.enemy.setHp(toHeal);
                enemySpecificMess = "The Lich steals your vitality !!!";
            }
        }
        // has chance to eat you
        else {
            nr = random.nextInt(100);
            if (nr > 10 && nr < 15) {
                this.player.setCurrentHitPoints(this.player.getCurrentHitPoints() / 2);
                enemySpecificMess = "The Drake eats your leg, yummy !";
            }
            else if (nr > 99) {
                this.player.setCurrentHitPoints(0);
                enemySpecificMess = "The Drake eats you, yum yum !";
            }
        }


        if (this.player.getCurrentHitPoints() > 0) {
            this.enemy.takeDamage(hero_enemy_attack);
            this.player.takeDamage(enemy_hero_attack);
        }

        if (this.player.getCurrentHitPoints() <= 0) {
            this.playerDied = true;
            //this.removePlayer();
            this.messData = "You lived, you fought and you died. How original...";
            this.obliviateHero();
            this.gamePhase = GamePhase.MAIN_MENU;
            this.view.showGamePhase(this.gamePhase, this.mapData, this.messData);
        }
        else if (this.enemy.getCurrentHp() > 0) {
            this.messData = "You attack for " + hero_enemy_attack + " damage";
            if (weaponElementalDamage >= 1)
                this.messData += " (" + (int)weaponElementalDamage + " elemental)";
            if (weaponArmorIgnore > 0)
                this.messData += " you ignore " + (int)(weaponArmorIgnore * 100) + "% armor";

            this.messData += " | You are hit for " + enemy_hero_attack + " damage";
            if (armorElementalDamage > 0)
                this.messData += " Enemy takes " + armorElementalDamage + " damage from armor";
            if (enemyAttacMultiplier == 0.5)
                this.messData += " The enemy does half damage !";
            if (enemyAttacMultiplier == 2.0)
                this.messData += " The enemy does DOUBLE damage !";


            // [TODO] Take it as string from Player
            String playerData = player.getName() + " level " + player.getLevel() + " " + player.getPlayerClass() + " " + player.getXp() + " / " + player.getXpForNextLevel() + " | " + " Att: " + player.getTotalAttack() + " Def: " + player.getTotalDefence() + " Hp : " + player.getCurrentHitPoints() + " / " + player.getTotalHitPoints() + "\n";

            this.gamePhase = GamePhase.ADVENTURE_ENCOUNTER;
            this.view.showGamePhase(this.gamePhase, this.mapData + playerData, this.messData + "\n" + enemySpecificMess + "\n" + classSpecificMess);
        }
        else if (this.enemy.getCurrentHp() <= 0) {
            this.removeEnemy();
            this.player.AddXp( (int)(300 * player.getLevel() * Math.pow(2, (gameMap.getMapLevel() - player.getLevel()) * 0.5 )) );
            this.messData = "You won the fight, the enemy's blood and other fluids will feed the ground !";

            // Check enemy droped item
            nr = random.nextInt(100);
            if (nr >= 0)
            {
                nr = random.nextInt(100);
                if (nr > 33 && nr < 66) {
                    this.newItem = new Weapon.Builder().setLevel(this.gameMap.getMapLevel()).setRandomStats().build();
                    this.messData = "You have equiped:\n>> " + this.player.getWeapon().getInfo();
                }
                else if (nr >= 66) {
                    this.newItem = new Armor.Builder().setLevel(this.gameMap.getMapLevel()).setRandomStats().build();
                    this.messData = "You have equiped:\n>> " + this.player.getArmor().getInfo();
                }
                else {
                    this.newItem = new Helmet.Builder().setLevel(this.gameMap.getMapLevel()).setRandomStats().build();
                    this.messData = "You have equiped:\n>> " + this.player.getHelmet().getInfo();
                }

                this.messData += " <<\n\nYou can change it for: \n>> ";
                this.messData += this.newItem.getInfo() + " <<";

                this.gamePhase = GamePhase.LOOT;
            }
            else
                this.gamePhase = GamePhase.ADVENTURE_MOVE;

            this.view.showGamePhase(this.gamePhase, this.mapData, this.messData);
        }
    }

    // Replace your old item with the item from loot :)
    public void keepNewItem() {
        switch (this.newItem.getItemType()) {
            case "weapon":
                this.player.setWeapon((Weapon) this.newItem);
                break;
            case "armor":
                this.player.setArmor((Armor) this.newItem);
                break;
            case "helmet":
                this.player.setHelmet((Helmet) this.newItem);
                break;
        }
        //this.player.updatePlayer();
        this.gamePhase = GamePhase.ADVENTURE_MOVE;
        this.view.showGamePhase(this.gamePhase, this.mapData, this.messData);
    }

    public void run() {
        Random random = new Random();

        if (random.nextInt(100) < 50) {
            this.fight();
        }
        else {
            this.messData = "You managed to run away, hooray !";
            this.gamePhase = GamePhase.ADVENTURE_MOVE;
            this.view.showGamePhase(this.gamePhase, this.mapData, this.messData);
        }
    }
    // end of ACTIONS

    public void start() {

    }

    private Enemy generateEnemy(int playerLevel) {
        int enemyType = this.gameMap.getMapTile(this.gameMap.getPlayerX(), this.gameMap.getPlayerY());
        if (enemyType == 3)
            return new Enemy.Builder().
                    setName("goblin").
                    setAttack(playerLevel * 2 + 2).
                    setDefence((int)(playerLevel * 1.5 + 1)).
                    setHp((int)(playerLevel * 5.3 + 10)).
                    build();
        else if (enemyType == 4)
            return new Enemy.Builder().
                    setName("blackGuard").
                    setAttack(playerLevel * 2 + 3).
                    setDefence((int)(playerLevel * 2.5 + 2)).
                    setHp((int)(playerLevel * 7.3 + 10)).
                    build();
        else if (enemyType == 5) // has low chance to evade hero attack
            return new Enemy.Builder().
                    setName("shade").
                    setAttack(playerLevel * 2 + 4).
                    setDefence((int)(playerLevel * 2.5 + 1)).
                    setHp((int)(playerLevel * 3.3 + 10)).
                    build();
        else if (enemyType == 6) // has fair chance to steal 1/4 your current hp, and heal for half of that
            return new Enemy.Builder().
                    setName("lich").
                    setAttack(playerLevel * 2 + 2).
                    setDefence((int)(playerLevel * 2.5 + 1)).
                    setHp((int)(playerLevel * 8.3 + 15)).
                    build();

        // has chance to eat you
        return new Enemy.Builder().
                setName("drake").
                setAttack(playerLevel * 4 + 4).
                setDefence((int)(playerLevel * 6 + 3)).
                setHp((int)(playerLevel * 10 + 15)).
                build();
    }

    private void removeEnemy() {
        this.gameMap.removeEnemyTile();
    }

    private void obliviateHero() {
        if (this.player.getIsInserted()) {

            // OPEN CONNECTION
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("main-persistence-unit");
            EntityManager em = emf.createEntityManager();

            // DELETE
            Player toDel = em.find(Player.class, this.player.getId());
            em.remove(toDel);
            em.getTransaction().begin();
            em.getTransaction().commit();

            // CLOSE CONNECTION
            em.close();
            emf.close();
        }
    }

    // Generate Map string
    private String getMap() {
        if (this.gameMap.getPlayerY() >= 0 && gameMap.getPlayerX() >= 0 && gameMap.getPlayerY() < gameMap.getMapSize() && gameMap.getPlayerX() < gameMap.getMapSize()) {
            // Remove fog from current position
            gameMap.removeFogAt(gameMap.getPlayerX(),gameMap.getPlayerY());

            // If there is an obstacle close to current tile. show it
            if (gameMap.getPlayerX() - 1 >= 0 && gameMap.getMapTile(gameMap.getPlayerX() - 1, gameMap.getPlayerY()) == 1)
                gameMap.removeFogAt(gameMap.getPlayerX() - 1,gameMap.getPlayerY());
            if (gameMap.getPlayerX() + 1 < gameMap.getMapSize() && gameMap.getMapTile(gameMap.getPlayerX() + 1, gameMap.getPlayerY()) == 1)
                gameMap.removeFogAt(gameMap.getPlayerX() + 1,gameMap.getPlayerY());

            if (gameMap.getPlayerY() - 1 >= 0 && gameMap.getMapTile(gameMap.getPlayerX(), gameMap.getPlayerY() - 1) == 1)
                gameMap.removeFogAt(gameMap.getPlayerX(),gameMap.getPlayerY() - 1);

            if (gameMap.getPlayerY() + 1 < gameMap.getMapSize() && gameMap.getMapTile(gameMap.getPlayerX(), gameMap.getPlayerY() + 1) == 1)
                gameMap.removeFogAt(gameMap.getPlayerX(),gameMap.getPlayerY() + 1);
        }

        StringBuilder mapStr = new StringBuilder();
        int startY = gameMap.getPlayerY() - 20 < 0 ? 0 : gameMap.getPlayerY() - 20;
        int startX = gameMap.getPlayerX() - 20 < 0 ? 0 : gameMap.getPlayerX() - 20;
        int endY = gameMap.getPlayerY() + 20 >= gameMap.getMapSize() ? gameMap.getMapSize() : gameMap.getPlayerY() + 20;
        int endX = gameMap.getPlayerX() + 20 >= gameMap.getMapSize() ? gameMap.getMapSize() : gameMap.getPlayerX() + 20;

        mapStr.append("+");
        for (int j = startX; j < endX - 1; j++)
            mapStr.append("--");
        mapStr.append("-+\n");


        for (int i = startY; i < endY; i++) {
            mapStr.append("|");
            for (int j = startX; j < endX; j++) {
                if (gameMap.getPlayerY() == i && gameMap.getPlayerX() == j)
                    mapStr.append("X");
                else {
                    if (!gameMap.isFogAt(j, i)) {
                        if (gameMap.getMapTile(j, i) == 0)
                            mapStr.append(".");
                        else if (gameMap.getMapTile(j, i) == 1)
                            mapStr.append("#");
                        else if (gameMap.getMapTile(j, i) == 2)
                            mapStr.append(".");
                        else
                            mapStr.append("@");
                    }
                    else
                        mapStr.append(" ");
                }
                if (j < gameMap.getMapSize() - 1)
                    mapStr.append(" ");
            }
            mapStr.append("|\n");
        }

        mapStr.append("+");
        for (int j = startX; j < endX - 1; j++)
            mapStr.append("--");
        mapStr.append("-+\n");

        return mapStr.toString();
    }
}
