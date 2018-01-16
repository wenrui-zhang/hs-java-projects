package Jarkanoid;

import Physics.PhysicsUtils;
import Physics.PhysicsWorld;

public class Levels {
    private int level = 0, width, height, brickCount;
    private boolean levelArray[][][] = {
            {

                    {true, true, true, false, true, true, true, false, true, true, true, false, true, true, false},
                    {true, false, false, false, true, false, true, false, true, false, true, false, true, false, true},
                    {true, false, true, false, true, false, true, false, true, false, true, false, true, false, true},
                    {true, false, true, false, true, false, true, false, true, false, true, false, true, false, true},
                    {true, true, true, false, true, true, true, false, true, true, true, false, true, true, false},

                    {false, false, false, false, false, false, false, false, false, false, false, false, false},

                    {true, false, false, false, true, false, true, false, true, true, true, false, true, false, true},
                    {true, false, false, false, true, false, true, false, true, false, false, false, true, true, true},
                    {true, false, false, false, true, false, true, false, true, false, false, false, true, true, false},
                    {true, false, false, false, true, false, true, false, true, false, false, false, true, true, true},
                    {true, true, true, false, true, true, true, false, true, true, true, false, true, false, true},

                    {false, false, false, false, false, false, false, false, false, false, false, false, false},
                    {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},

                    {true, false, true, false, true, false, true, false, true, false, true, false, true, false, true},
                    {true, false, true, false, true, false, true, false, true, false, true, false, true, false, true},
                    {true, false, true, false, true, false, true, false, true, false, true, false, true, false, true},
                    {true, false, true, false, true, false, true, false, true, false, true, false, true, false, true},
                    {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},

                    {true, false, true, false, true, false, true, false, true, false, true, false, true, false, true},
                    {true, false, true, false, true, false, true, false, true, false, true, false, true, false, true},
                    {true, false, true, false, true, false, true, false, true, false, true, false, true, false, true},
                    {true, false, true, false, true, false, true, false, true, false, true, false, true, false, true},
                    {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},

                    {true, false, true, false, true, false, true, false, true, false, true, false, true, false, true},
                    {true, false, true, false, true, false, true, false, true, false, true, false, true, false, true},
                    {true, false, true, false, true, false, true, false, true, false, true, false, true, false, true},
                    {true, false, true, false, true, false, true, false, true, false, true, false, true, false, true},
                    {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
            },
            {
                    {false, false, false, false, true, false, false, false, false, false, true, false, false, false, false},
                    {false, false, false, false, true, false, false, false, false, false, true, false, false, false, false},
                    {false, false, false, false, false, true, false, false, false, true, false, false, false, false, false},
                    {false, false, false, false, false, true, false, false, false, true, false, false, false, false, false},
                    {false, false, false, false, true, true, true, true, true, true, true, false, false, false, false},
                    {false, false, false, false, true, true, true, true, true, true, true, false, false, false, false},
                    {false, false, false, true, true, true, true, true, true, true, true, true, false, false, false},
                    {false, false, false, true, true, true, true, true, true, true, true, true, false, false, false},
                    {false, false, true, true, true, true, true, true, true, true, true, true, true, false, false},
                    {false, false, true, true, true, true, true, true, true, true, true, true, true, false, false},
                    {false, false, true, true, true, true, true, true, true, true, true, true, true, false, false},
                    {false, false, true, false, true, true, true, true, true, true, true, false, true, false, false},
                    {false, false, true, false, true, false, false, false, false, false, true, false, true, false, false},
                    {false, false, true, false, true, false, false, false, false, false, true, false, true, false, false},
                    {false, false, false, false, false, true, true, false, true, true, false, false, false, false, false},
                    {false, false, false, false, false, true, true, false, true, true, false, false, false, false, false},
            },
            {
                    {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
                    {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                    {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                    {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                    {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                    {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                    {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                    {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
                    {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                    {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                    {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                    {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                    {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                    {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
                    {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                    {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                    {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                    {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                    {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
                    {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                    {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                    {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                    {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
                    {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                    {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                    {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
                    {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                    {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
            },
    };


    public Levels(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Levels(int width, int height, int start) {
        this(width, height);
        level = start;
    }

    public int reduceBrick(PhysicsWorld world) {
        brickCount--;
        if (brickCount <= 0) {
            level++;
            spawnObjects(world);
        }
        return brickCount;
    }

    public void spawnObjects(PhysicsWorld world) {
        if (level > levelArray.length) {
            level = levelArray.length;
        }
        brickCount = 0;
        for (int j = 0; j < levelArray[level].length; j++) {
            for (int i = 0; i < levelArray[level][j].length; i++) {
                if(level == levelArray.length) {
                    if(Math.random()<0.45) {
                        brickCount++;
                        int jj = levelArray[level].length - j - 1;
                        Brick brick = new Brick(30 + width * i, 300 + height * jj, width - 2, height - 2, (int) PhysicsUtils.random(1, 7));
                        world.add(brick);
                    }
                }else {
                    if (levelArray[level][j][i]) {
                        brickCount++;
                        int jj = levelArray[level].length - j - 1;
                        Brick brick = new Brick(30 + width * i, 300 + height * jj, width - 2, height - 2, (int) PhysicsUtils.random(1, 7));
                        world.add(brick);
                    }
                }
            }
        }
    }
}
