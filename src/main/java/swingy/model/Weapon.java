package swingy.model;

import swingy.common.ItemType;

import java.util.Random;

public class Weapon implements Item {
    private int id;
    private String name;
    private int sufixNr;
    private int attack;
    private ItemType[] type = new ItemType[3];
    private String itemType; // weapon, armor, helmet [TODO] Maybe rename the other ItemType ?!

    private Weapon (Builder builder) {
        this.itemType = "weapon";
        this.name = builder.name;
        this.attack = builder.attack;
        this.type = builder.type;
        this.sufixNr = builder.sufixNr;
    }

    public Weapon(int id, String name, int attack) {
        this.id = id;
        this.name = name;
        this.attack = attack;
    }

    public ItemType[] getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return this.getName() + ", atk: " + getValue();
    }

    // [TODO] Replace getAttack()
    public int getValue() {
        return attack;
    }

    public int getAttack() {
        return attack;
    }

    public int getSufixNr() {
        return sufixNr;
    }

    @Override
    public String getItemType() {
        return itemType;
    }

    // --- BUILDER ---
    public static class Builder extends ItemBuild{
        private int level;
        private String name;
        private int attack;

        public Builder setLevel(int level) {
            this.level = level;
            return this;
        }

        // [OBSOLETE] assigned in setSpecificStats
        public Builder setType(ItemType[] type) {
            return this;
        }

        public Weapon build() {
            this.buildName();
            return new Weapon(this);
        }

        @Override
        public Builder setRandomStats() {
            Random random = new Random();
            int nr = random.nextInt(100);
            this.attack = this.level * (100 + nr) / 100;
            this.sufixNr = random.nextInt(100);
            this.generateEffects();
            return this;
        }

        @Override
        public Builder setSpecificStats(int baseStat, int sufixNr, ItemType[] type) {
            this.attack = baseStat;
            this.sufixNr = sufixNr;
            return this;
        }

        @Override
        protected void buildName() {
            this.name = "Sword";
            if (this.sufixNr > 60 && this.sufixNr <= 80)
                this.name = "Axe";
            else if (this.sufixNr > 80 && this.sufixNr < 100)
                this.name = "Mace";

            for (int i = 0; i < 3; i++) {
                if (this.type[i] == ItemType.MARTIAL)
                    this.name = "Keen " + this.name;
                else if (this.type[i] == ItemType.ELEMENTAL_FIRE)
                    this.name += " of Fire";
                else if (this.type[i] == ItemType.ELEMENTAL_FROST)
                    this.name += " of Frost";
                else if (this.type[i] == ItemType.ELEMENTAL_SHOCK)
                    this.name += " of Shock";
                else if (this.type[i] == ItemType.CURSED)
                    this.name += " of DOOM";
            }

        }
    }
}
