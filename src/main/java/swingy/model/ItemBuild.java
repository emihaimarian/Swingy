package swingy.model;

import swingy.common.ItemType;

import java.util.Random;

abstract public class ItemBuild {
    protected ItemType[] type = new ItemType[3];
    int nrOfEffects = 0;
    int sufixNr;

    protected void generateEffects() {
        Random random = new Random();
        int nr;
        boolean hasMartial = false;
        boolean hasElemental = false;
        boolean hasCursed = false;

        for (int i = 0; i < 3; i++) {
            type[i] = ItemType.NONE;
            if (nrOfEffects < 2) { // max 2 effects
                nr = random.nextInt(100);
                if (nr > 50 + nrOfEffects * 15) {
                    nr = random.nextInt(100);
                    if (!hasMartial && nr <= 33) {
                        type[i] = ItemType.MARTIAL;
                        hasMartial = true;
                        nrOfEffects++;
                    }
                    else if (!hasElemental && !hasCursed && nr > 33 && nr <= 66) {
                        if (nr > 33 && nr <= 44)
                            type[i] = ItemType.ELEMENTAL_FIRE;
                        else if (nr > 44 && nr <= 55)
                            type[i] = ItemType.ELEMENTAL_FROST;
                        else if (nr > 55 && nr <= 66)
                            type[i] = ItemType.ELEMENTAL_SHOCK;
                        hasElemental = true;
                        nrOfEffects++;
                    }
                    else if (!hasElemental && !hasCursed && nr > 66 && nr < 100) {
                        type[i] = ItemType.CURSED;
                        hasCursed = true;
                        nrOfEffects++;
                    }
                }
            }
        }
    }

    public ItemType[] getType() {
        return type;
    }

    public int getSufixNr() {
        return sufixNr;
    }

    abstract protected ItemBuild setRandomStats();
    abstract protected ItemBuild setSpecificStats(int baseStat, int sufixNr, ItemType[] type);
    abstract protected void buildName();
}
