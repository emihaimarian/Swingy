package swingy.model;

import org.hibernate.validator.constraints.NotEmpty;
import swingy.common.ItemType;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Transactional
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty(message = "Name can not be empty")
    @Size(min = 2, max = 20, message = "Name length must be between 2 and 20")
    private String name = "";

    private String playerClass;
    private int level = 1;
    private int xp = 0;
    private int baseHitPoints;
    private int baseAttack;
    private int baseDefence;
    private int totalHitPoints;
    private int totalAttack;
    private int totalDefence;
    private int currentHitPoints;

    @Transient
    private int xpForNextLevel;

    @Transient
    private boolean isInserted = false;

    @Transient
    private Weapon weapon;
    private int weaponSufixNr;
    private int weaponAttack;
    @Enumerated(EnumType.STRING)
    private ItemType weaponType1;
    @Enumerated(EnumType.STRING)
    private ItemType weaponType2;
    @Enumerated(EnumType.STRING)
    private ItemType weaponType3;

    @Transient
    private Armor armor;
    private int armorSufixNr;
    private int armorDefence;
    @Enumerated(EnumType.STRING)
    private ItemType armorType1;
    @Enumerated(EnumType.STRING)
    private ItemType armorType2;
    @Enumerated(EnumType.STRING)
    private ItemType armorType3;

    @Transient
    private Helmet helmet;
    private int helmetSufixNr;
    private int helmetHitPoints;
    @Enumerated(EnumType.STRING)
    private ItemType helmetType1;
    @Enumerated(EnumType.STRING)
    private ItemType helmetType2;
    @Enumerated(EnumType.STRING)
    private ItemType helmetType3;

    @Transient
    private static int heroNr;

    public Player(){

    }

    // [TODO] make it private, DON'T USE / use BUILDER pattern
    public Player(String name, int xp, String playerClass, Weapon weapon, Helmet helmet, Armor armor) {
        this.name = name;
        this.xp = xp;
        this.playerClass = playerClass;
        this.weapon = weapon;
        this.helmet = helmet;
        this.armor = armor;
        this.calculateXpForNextLevel();
        this.updateLevel();
        this.healFull();

        this.updateItemsTypes();
    }

    // Used to save items data after creating a new Player / Hero
    private void updateItemsTypes() {
        this.weaponSufixNr = this.weapon.getSufixNr();
        this.weaponAttack = this.weapon.getAttack();
        this.weaponType1 = this.weapon.getType()[0];
        this.weaponType2 = this.weapon.getType()[1];
        this.weaponType3 = this.weapon.getType()[2];

        this.armorSufixNr = this.armor.getSufixNr();
        this.armorDefence = this.armor.getDefence();
        this.armorType1 = this.armor.getType()[0];
        this.armorType2 = this.armor.getType()[1];
        this.armorType3 = this.armor.getType()[2];

        this.helmetSufixNr = this.helmet.getSufixNr();
        this.helmetHitPoints = this.helmet.getHitPoints();
        this.helmetType1 = this.helmet.getType()[0];
        this.helmetType2 = this.helmet.getType()[1];
        this.helmetType3 = this.helmet.getType()[2];
    }

    public void takeDamage(int value) {
        this.currentHitPoints -= value;
    }

    public void AddXp(int value) {
        this.xp += value;
        this.updateLevel();
    }

    private void updateLevel() {
        while (this.xp >= this.xpForNextLevel) {
            this.level++;
            this.calculateXpForNextLevel();
        }
        this.calculateBaseStats();
        this.calculateFinalStats();
    }

    private void calculateXpForNextLevel() {
        this.xpForNextLevel = this.level * 1000 + (int) Math.pow(this.level - 1, 2) * 450;
    }

    /*
     * Calculate base stats based on level and class
     * Replace new base hp in current hp
     */
    private void calculateBaseStats() {
        // [TODO] Use player class to calculate stats
        this.currentHitPoints -= this.baseHitPoints;
        this.baseHitPoints = (int)(5 + 3.5 * this.level);
        this.currentHitPoints += this.baseHitPoints;

        this.baseAttack = (int)(2 + 3.5 * this.level);
        this.baseDefence = (int)(2 + 3.5 * this.level);

        switch (this.playerClass) {
            case "warrior":
                this.baseDefence *= 1.5;
                break;
            case "shinobi":
                this.baseAttack *= 1.5;
                break;
            case "berseker":
                this.baseHitPoints *= 1.5;
                break;
        }
    }

    private void calculateFinalStats() {
        this.totalAttack = this.baseAttack + this.weapon.getAttack();
        this.totalHitPoints = this.baseHitPoints + this.helmet.getHitPoints();
        this.totalDefence = this.baseDefence + this.armor.getDefence();

    }

    public void healFull() {
        this.currentHitPoints = this.totalHitPoints;
    }

    public void healOnMove() {
        int heal = (int)(this.totalHitPoints * 0.2);
        heal = heal >= 1 ? heal : 1;
        this.currentHitPoints += heal;
        this.currentHitPoints = this.currentHitPoints <= this.totalHitPoints ? this.currentHitPoints : this.totalHitPoints;
    }

    static public Player generateStartingPlayer() {
        String symbols = "abcdefghijklmnopqrstuvwxyz";
        Random ran = new Random();
        int len = ran.nextInt(10) + 6;

        StringBuilder name = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            name.append(symbols.charAt(ran.nextInt(symbols.length())));

        String playerClass = "warrior";
        int itemId = 0; // 0 - starting items for warrior, 1 - for shinobi, 2 - for berseker
        len = ran.nextInt(1000);
        if (len > 400 && len < 600) {
            playerClass = "shinobi";
            itemId = 1;
        }
        else if (len >= 600) {
            playerClass = "berseker";
            itemId = 2;
        }

        return new Player(Character.toUpperCase(name.charAt(0)) + name.toString().substring(1), 0, playerClass,
                new Weapon.Builder().setLevel(1).setRandomStats().build(),
                new Helmet.Builder().setLevel(1).setRandomStats().build(),
                new Armor.Builder().setLevel(1).setRandomStats().build());
    }

    public String getPlayerData() {
        return this.name + " level " + this.level + " " + this.playerClass + " " + this.xp + " / " + this.xpForNextLevel + " | " + " Att: " + this.totalAttack + " Def: " + this.totalDefence + " Hp : " + this.currentHitPoints + " / " + this.totalHitPoints;
    }

    // DATA BASE OPERATIONS
    public void insertPlayer() {
        // OPEN CONNECTION
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("main-persistence-unit");
        EntityManager em = emf.createEntityManager();

        // INSERT
        em.getTransaction().begin();
        em.persist(this);
        em.getTransaction().commit();

        // CLOSE CONNECTION
        em.close();
        emf.close();

        this.isInserted = true;
    }

    public void updatePlayer() {
        // OPEN CONNECTION
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("main-persistence-unit");
        EntityManager em = emf.createEntityManager();

        // UPDATE
        //MainEntity toUpdate = em.find(MainEntity.class, (long)3);
        Player toUpdate = em.find(Player.class, this.id);
        toUpdate.copyFrom(this);
        em.getTransaction().begin();
        em.getTransaction().commit();

        // CLOSE CONNECTION
        em.close();
        emf.close();
    }

    public static LinkedHashMap<Integer, Player> loadPlayers() {
        LinkedHashMap<Integer, Player> heroList = new LinkedHashMap<>();
        int i = 1;

        // OPEN CONNECTION
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("main-persistence-unit");
        EntityManager em = emf.createEntityManager();

        // SELECT
        List<Player> list = em.createQuery("SELECT e FROM Player e", Player.class).getResultList();
        for (Player el : list) {
            el.isInserted = true;
            heroList.put(i++, el);
        }
        Player.heroNr = i;

        // CLOSE CONNECTION
        em.close();
        emf.close();

        return heroList;
    }

    public void buildItemsFromSavedData() {
        ItemType[] weaponType = new ItemType[3];
        ItemType[] armorType = new ItemType[3];
        ItemType[] helmetType = new ItemType[3];

        weaponType[0] = this.weaponType1;
        weaponType[1] = this.weaponType2;
        weaponType[2] = this.weaponType3;

        armorType[0] = this.armorType1;
        armorType[1] = this.armorType2;
        armorType[2] = this.armorType3;

        helmetType[0] = this.helmetType1;
        helmetType[1] = this.helmetType2;
        helmetType[2] = this.helmetType3;

        this.weapon = new Weapon.Builder().setSpecificStats(this.weaponAttack, this.weaponSufixNr, weaponType).build();
        this.armor = new Armor.Builder().setSpecificStats(this.armorDefence, this.armorSufixNr, armorType).build();
        this.helmet = new Helmet.Builder().setSpecificStats(this.helmetHitPoints, this.helmetSufixNr, helmetType).build();
    }

    private void copyFrom(Player source) {
        name = source.getName();
        playerClass = source.getPlayerClass();
        level = source.getLevel();
        xp = source.getXp();
        baseHitPoints = source.getBaseHitPoints();
        baseAttack = source.getBaseAttack();
        baseDefence = source.getBaseDefence();
        totalHitPoints = source.getTotalHitPoints();
        totalAttack = source.getTotalAttack();
        totalDefence = source.getTotalDefence();
        currentHitPoints = source.currentHitPoints;

        weapon = source.getWeapon();
        weaponSufixNr = source.getWeaponSufixNr();
        weaponAttack = source.getWeaponAttack();
        weaponType1 = source.getWeaponType1();
        weaponType2 = source.getWeaponType2();
        weaponType3 = source.getWeaponType3();

        armor = source.getArmor();
        armorSufixNr = source.getArmorSufixNr();
        armorDefence = source.getArmorDefence();
        armorType1 = source.getArmorType1();
        armorType2 = source.getArmorType2();
        armorType3 = source.getArmorType3();

        helmet = source.getHelmet();
        helmetSufixNr = source.getHelmetSufixNr();
        helmetHitPoints = source.getHelmetHitPoints();
        helmetType1 = source.getHelmetType1();
        helmetType2 = source.getHelmetType2();
        helmetType3 = source.getHelmetType3();
    }

    public int getPlayerClassIndex() {
        int result = 0;
        if (this.playerClass.equals("shinobi"))
            result = 1;
        else if (this.playerClass.equals("berseker"))
            result = 2;
        return result;
    }

    // GETTER && SETTER


    public long getId() {
        return id;
    }

    public int getWeaponSufixNr() {
        return weaponSufixNr;
    }

    public int getWeaponAttack() {
        return weaponAttack;
    }

    public int getArmorSufixNr() {
        return armorSufixNr;
    }

    public int getArmorDefence() {
        return armorDefence;
    }

    public int getHelmetSufixNr() {
        return helmetSufixNr;
    }

    public int getHelmetHitPoints() {
        return helmetHitPoints;
    }

    public static int getHeroNr() {
        return heroNr;
    }

    public ItemType getWeaponType1() {
        return weaponType1;
    }

    public ItemType getWeaponType2() {
        return weaponType2;
    }

    public ItemType getWeaponType3() {
        return weaponType3;
    }

    public ItemType getArmorType1() {
        return armorType1;
    }

    public ItemType getArmorType2() {
        return armorType2;
    }

    public ItemType getArmorType3() {
        return armorType3;
    }

    public ItemType getHelmetType1() {
        return helmetType1;
    }

    public ItemType getHelmetType2() {
        return helmetType2;
    }

    public ItemType getHelmetType3() {
        return helmetType3;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws Exception {
        this.name = name;

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Player>> constraintViolations = validator.validate(this);

        if (constraintViolations.size() > 0) {
            throw new RuntimeException("Name is not valid: \n");
        }
    }

    public String getPlayerClass() {
        return playerClass;
    }

    public void setPlayerClass(String playerClass) {
        this.playerClass = playerClass;
        this.calculateBaseStats();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        calculateXpForNextLevel();
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
        this.updateLevel();
    }

    public int getXpForNextLevel() {
        return xpForNextLevel;
    }

    public void setXpForNextLevel(int xpForNextLevel) {
        this.xpForNextLevel = xpForNextLevel;
    }

    public int getTotalHitPoints() {
        return totalHitPoints;
    }

    public int getTotalAttack() {
        return totalAttack;
    }

    public int getTotalDefence() {
        return totalDefence;
    }

    public int getCurrentHitPoints() {
        return currentHitPoints;
    }

    public void setCurrentHitPoints(int currentHitPoints) {
        this.currentHitPoints = currentHitPoints;
    }

    public int getBaseHitPoints() {
        return baseHitPoints;
    }

    public int getBaseAttack() {
        return baseAttack;
    }

    public int getBaseDefence() {
        return baseDefence;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
        this.updateItemsTypes();
    }

    public Helmet getHelmet() {
        return helmet;
    }

    public void setHelmet(Helmet helmet) {
        this.helmet = helmet;
        this.updateItemsTypes();
    }

    public Armor getArmor() {
        return armor;
    }

    public void setArmor(Armor armor) {
        this.armor = armor;
        this.updateItemsTypes();
    }

    public boolean getIsInserted() {
        return isInserted;
    }
}
