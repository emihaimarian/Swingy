package swingy.model;

import swingy.common.ItemType;

import java.util.Random;

public class Helmet implements Item {
    private int id;
    private String name;
    private int sufixNr;
    private int hitPoints;
    private ItemType[] type = new ItemType[3];
    private String itemType; // weapon, armor, helmet [TODO] Maybe rename the other ItemType ?!

    private Helmet (Builder builder) {
        this.itemType = "helmet";
        this.name = builder.name;
        this.hitPoints = builder.hitPoints;
        this.type = builder.type;
        this.sufixNr = builder.sufixNr;
    }

    public Helmet(int id, String name, int hitPoints) {
        this.id = id;
        this.name = name;
        this.hitPoints = hitPoints;
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
        return this.getName() + ", hp: " + getValue();
    }

    // [TODO] Replace getHitPoints()
    public int getValue() {
        return hitPoints;
    }

    public int getHitPoints() {
        return hitPoints;
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
        private int hitPoints;

        public Builder setLevel(int level) {
            this.level = level;
            return this;
        }

        public Helmet build() {
            this.buildName();
            return new Helmet(this);
        }

        @Override
        public Builder setRandomStats() {
            Random random = new Random();
            int nr = random.nextInt(100);
            this.hitPoints = this.level * (100 + nr) / 100;
            this.sufixNr = random.nextInt(100);
            this.generateEffects();
            return this;
        }

        @Override
        public Builder setSpecificStats(int baseStat, int sufixNr, ItemType[] type) {
            this.hitPoints = baseStat;
            this.sufixNr = sufixNr;
            return this;
        }

        @Override
        protected void buildName() {
            this.name = "Leather Bandana";
            if (this.sufixNr > 60 && this.sufixNr <= 80)
                this.name = "Chain Skull Cap";
            else if (this.sufixNr > 80 && this.sufixNr < 100)
                this.name = "Obsidian Helmet";

            for (int i = 0; i < 3; i++) {
                if (this.type[i] == ItemType.MARTIAL)
                    this.name = "Blody " + this.name;
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
