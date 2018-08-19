package swingy.model;

import java.util.ArrayList;
import java.util.Random;

public class GameMap {
    private int mapLevel;
    private int mapSize;
    private int middle;
    private static int mapNr = 0; // Nr of maps of current level
    private int[][] map; // row, column
    private int[][] mapFog; // Fog of War

    private int playerX;
    private int playerY;

    private int nrOfExits; // nr of exits from the maze
    private boolean exitTop; // map has an exit on top ...
    private boolean exitBottom;
    private boolean exitLeft;
    private boolean exitRight;

    // --- GENERATE MAP --
    /*
         * Generate the map
         * i is for row
         * 0 is free; 1 is wall; 2 is tower; 3 - 9 is enemies
     */
    public void generateMap(int playerLevel) {
        Random rand = new Random();
        Random randEnemy = new Random();

        this.mapLevel = playerLevel;
        GameMap.mapNr++;
        this.mapSize =  (playerLevel - 1) * 5 + 10 - (playerLevel % 2);
        this.middle = this.mapSize / 2;
        this.map = new int[this.mapSize][this.mapSize];

        this.playerX = this.middle;
        this.playerY = this.middle;
        this.initMapFog();

        // Generate walls && free tiles
        generateMaze(playerLevel);

        // Add enemies
        int maxTowers = (int)(0.1 * this.mapSize / 10);
        maxTowers = maxTowers < 1 ? 1 : maxTowers;
        maxTowers = maxTowers > 4 ? 3 : maxTowers;

        for (int i = 0; i < this.mapSize; i++) {
            for (int j = 0; j < this.mapSize; j++) {
                if (this.map[i][j] == 0) {
                    int n = rand.nextInt(30); // 0 - 20: free, 21 - 27: enemy, 28 - 29: obstacle
                    if (maxTowers > 0 &&  n > 10 && n <= 20) {
                        map[i][j] = 2;
                        maxTowers--;
                    }
                    else if (n > 20 && n <= 27) {
                        // 0 - 35 : goblin | 35 - 65 : blackGuard | 65 - 85 : shade | 85 - 95 : lich | 95 - 100 : drake
                        int enemyType = randEnemy.nextInt(100);
                        if (enemyType < 35)
                            map[i][j] = 3;
                        else if (enemyType >= 35 && enemyType < 65)
                            map[i][j] = 4;
                        else if (enemyType >= 65 && enemyType < 85)
                            map[i][j] = 5;
                        else if (enemyType >= 85 && enemyType < 95)
                            map[i][j] = 6;
                        else if (enemyType >= 95 && enemyType < 100)
                            map[i][j] = 7;
                    }
                }

                // Make sure center is free to place the player
                if (i == this.middle && j == this.middle)
                    map[i][j] = 0;
            }
        }
    }

    private void generateMaze(int playerLevel) {
        this.mapSize =  (playerLevel - 1) * 5 + 10 - (playerLevel % 2);
        this.middle = this.mapSize / 2;
        this.playerX = this.middle;
        this.playerY = this.middle;
        this.map = new int[this.mapSize][this.mapSize];

        // Initiate the maze with walls
        for (int i = 0; i < this.mapSize; i++) {
            for (int j = 0; j < this.mapSize; j++) {
                this.map[i][j] = 1;
            }
        }

        this.exitTop = false;
        this.exitBottom = false;
        this.exitLeft = false;
        this.exitRight = false;

        this.nrOfExits = 0;
        this.mazeGeneratorStep(this.middle, this.middle);
        Random rand = new Random();

        while (nrOfExits < 4) {
            this.mazeGeneratorStep(rand.nextInt(this.mapSize), rand.nextInt(this.mapSize));
        }
    }

    private void mazeGeneratorStep(int i, int j) {
        Random rand = new Random();
        // Set starting point in the middle of the map
        this.map[i][j] = 0;
        ArrayList<int[]> frontier = new ArrayList<>();
        do {
            // Add all neighbors that have walls
            if (j - 1 >= 0 && map[i][j - 1] == 1) {
                int[] v = new int[2];
                v[0] = i;
                v[1] = j - 1;
                if (!frontier.contains(v))
                    frontier.add(v);
            }
            if (j + 1 < this.mapSize && map[i][j + 1] == 1) {
                int[] v = new int[2];
                v[0] = i;
                v[1] = j + 1;
                if (!frontier.contains(v))
                    frontier.add(v);
            }
            if (i - 1 >= 0 && map[i - 1][j] == 1) {
                int[] v = new int[2];
                v[0] = i - 1;
                v[1] = j;
                if (!frontier.contains(v))
                    frontier.add(v);
            }
            if (i + 1 < this.mapSize && map[i + 1][j] == 1) {
                int[] v = new int[2];
                v[0] = i + 1;
                v[1] = j;
                if (!frontier.contains(v))
                    frontier.add(v);
            }

            //System.out.printf("Source tile: %d, %d\n", i, j);

            int ti = i;
            int tj = j;
            boolean done = false;
            while (!done && !frontier.isEmpty()) {
                int index = rand.nextInt(frontier.size());
                ti = frontier.get(index)[0];
                tj = frontier.get(index)[1];

                boolean isGood = true;
                if (this.nrOfExits > 0)
                    if (ti - 1 >= 0 && ti != i && tj != j && map[ti - 1][tj] == 0)
                        isGood = false;
                if (this.nrOfExits > 0)
                    if (isGood && ti + 1 < mapSize && ti != i && tj != j && map[ti + 1][tj] == 0)
                        isGood = false;
                if (isGood && tj - 1 >= 0 && ti != i && tj != j && map[ti][tj - 1] == 0)
                    isGood = false;
                if (isGood && tj + 1 < mapSize && ti != i && tj != j && map[ti][tj + 1] == 0)
                    isGood = false;


                //System.out.printf("Checking tile: %d, %d [%s]\n", ti, tj, isGood);

                if (isGood) {
                    done = true;
                    this.map[ti][tj] = 0;
                }
                frontier.remove(index);
            }
            i = ti;
            j = tj;

            // Check exits exists :)
            if (!this.exitTop && i - 1 < 0) {
                this.nrOfExits++;
                this.exitTop = true;
            }
            if (!this.exitBottom && i + 1 == this.mapSize) {
                this.nrOfExits++;
                this.exitBottom = true;
            }
            if (!this.exitLeft && j - 1 < 0) {
                this.nrOfExits++;
                this.exitLeft = true;
            }
            if (!this.exitRight && j + 1 == this.mapSize) {
                this.nrOfExits++;
                this.exitRight = true;
            }
        }while (!frontier.isEmpty());
    }
    // END OF GENERATE MAP

    // Init map fog after creating a new map
    private void initMapFog() {
        this.mapFog = new int[this.mapSize][this.mapSize];
        for (int i = 0; i < this.mapSize; i++) {
            for (int j = 0; j < this.mapSize; j++) {
                this.mapFog[i][j] = 0;
            }
        }
        this.mapFog[this.mapSize / 2][this.mapSize / 2] = 1;
    }

    // --- PUBLIC METHODS --
    // Get value of tile
    public int getMapTile(int x, int y) {
        return this.map[y][x];
    }

    // After defeating the enemy, the tile turns to FREE
    public void removeEnemyTile() {
        this.map[this.playerY][this.playerX] = 0;
    }

    // Check if tile is dicovered
    public boolean isFogAt(int x, int y) {
        return this.mapFog[y][x] == 0;
    }

    // Mark tile as discovered
    public void removeFogAt(int x, int y) {
        this.mapFog[y][x] = 1;
    }
    // END OF PUBLIC METHODS


    // --- GETTER / SETTER ---
    public int getMapLevel() {
        return mapLevel;
    }

    public static int getMapNr() {
        return mapNr;
    }

    public int getPlayerX() {
        return playerX;
    }

    public void setPlayerX(int playerX) {
        this.playerX = playerX;
    }

    public int getPlayerY() {
        return playerY;
    }

    public void setPlayerY(int playerY) {
        this.playerY = playerY;
    }

    public int getMapSize() {
        return mapSize;
    }
    // END OF GETTER / SETTER

    // --- DEBUG ---
    public String dbg_getMap() {
        String map = "";

        for (int i = 0; i < this.mapSize; i++) {
            for (int j = 0; j < this.mapSize; j++) {
                if (this.playerY == i && this.playerX == j)
                    map += "X";
                else {
                    if (this.map[i][j] == 0)
                        map += "."; // free
                    else if (this.map[i][j] == 1)
                        map += "#"; // wall
                    else if (this.map[i][j] == 2)
                        map += "!"; // tower
                    else
                        map += "@"; // enemies
                }
                if (j < this.mapSize - 1)
                    map += " ";
            }
            map += "\n";
        }

        return map;
    }

    public void dbg_printMap() {
        for (int i = 0; i < this.mapSize; i++) {
            for (int j = 0; j < this.mapSize; j++) {
                if (this.playerY == i && this.playerX == j)
                    System.out.print("X");
                else {
                    if (this.map[i][j] == 0)
                        System.out.print("."); // free
                    else if (this.map[i][j] == 1)
                        System.out.print("#"); // wall
                    else if (this.map[i][j] == 2)
                        System.out.print("."); // tower
                    else
                        System.out.print("@"); // enemies
                }
                if (j < this.mapSize - 1)
                    System.out.print(" ");
            }
            System.out.println();
        }
    }
}
