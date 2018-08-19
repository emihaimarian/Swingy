package swingy.model;

import swingy.common.ItemType;

import java.util.Random;

public class Armor implements Item {
    private int id;
    private String name;
    private int sufixNr;
    private int armor;
    private ItemType[] type = new ItemType[3];
    private String itemType; // weapon, armor, helmet [TODO] Maybe rename the other ItemType ?!

    private Armor (Builder builder) {
        this.itemType = "armor";
        this.name = builder.name;
        this.armor = builder.armor;
        this.type = builder.type;
        this.sufixNr = builder.sufixNr;
    }

    public Armor(int id, String name, int armor) {
        this.id = id;
        this.name = name;
        this.armor = armor;
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
        return this.getName() + ", def: " + getValue();
    }

    // [TODO] Replace getDefence()
    public int getValue() {
        return armor;
    }

    public int getDefence() {
        return armor;
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
        private int armor;

        public Builder setLevel(int level) {
            this.level = level;
            return this;
        }

        public Armor build() {
            this.buildName();
            return new Armor(this);
        }

        @Override
        public Builder setRandomStats() {
            Random random = new Random();
            int nr = random.nextInt(100);
            this.armor = this.level * (100 + nr) / 100;
            this.sufixNr = random.nextInt(100);
            this.generateEffects();
            return this;
        }

        @Override
        public Builder setSpecificStats(int baseStat, int sufixNr, ItemType[] type) {
            this.armor = baseStat;
            this.sufixNr = sufixNr;
            return this;
        }

        @Override
        protected void buildName() {
            this.name = "Leather Armor";
            if (this.sufixNr > 60 && this.sufixNr <= 80)
                this.name = "Chain Armor";
            else if (this.sufixNr > 80 && this.sufixNr < 100)
                this.name = "Plate Armor";

            for (int i = 0; i < 3; i++) {
                if (this.type[i] == ItemType.MARTIAL)
                    this.name = "Sturdy " + this.name;
                else if (type[i] == ItemType.ELEMENTAL_FIRE)
                    this.name = "Burning " + this.name;
                else if (type[i] == ItemType.ELEMENTAL_FROST)
                    this.name = "Freezing " + this.name;
                else if (type[i] == ItemType.ELEMENTAL_SHOCK)
                    this.name = "Shocking " + this.name;
                else if (type[i] == ItemType.CURSED)
                    this.name += " of DOOM";
            }

        }
    }
}
