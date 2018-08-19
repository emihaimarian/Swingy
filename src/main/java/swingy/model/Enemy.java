package swingy.model;

public class Enemy {
    private String name; // also type
    private int attack;
    private int defence;
    private int hp;
    private int currentHp;

    public String getName() {
        return name;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefence() {
        return defence;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public void takeDamage(int damage) {
        this.currentHp -= damage;
    }

    private Enemy(Builder builder) {
        this.name = builder.name;
        this.attack = builder.attack;
        this.defence = builder.defence;
        this.hp = builder.hp;
        this.currentHp = builder.hp;
    }

    // Generate ascii img data as a string
    public String generateImg() {
        String enemyAscii = "";
        switch (name) {
            case "drake":
                enemyAscii = ImgAscii.getDrake();
                break;
            case "shade":
                enemyAscii = ImgAscii.getShade();
                break;
            case "blackGuard":
                enemyAscii = ImgAscii.getBlackGuard();
                break;
            case "goblin":
                enemyAscii = ImgAscii.getGoblin();
                break;
            case "lich":
                enemyAscii = ImgAscii.getLich();
                break;

        }

        return enemyAscii;
    }

    // Generate stats as a string
    public String generateStats() {
        return
                "You are attacked by an angry " + name + " ! " + "Att: " + attack + " Def: " + defence + " Hp: " + currentHp;
    }

    // --- BUILDER ---
    public static class Builder {
        private String name = "Goblin";
        private int attack = 5;
        private int defence = 3;
        private int hp = 10;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setAttack(int attack) {
            this.attack = attack;
            return this;
        }

        public Builder setDefence(int defence) {
            this.defence = defence;
            return this;
        }

        public Builder setHp(int hp) {
            this.hp = hp;
            return this;
        }

        public Enemy build() {
            return new Enemy(this);
        }
    }
}
