import java.lang.Math;

public class shooting {
    public static void main(String[] args) {
        ICanvas c = new ICanvas();
        int windowSize[] = {600, 400};
        int padding[] = {50, 50};
        int playerSize[] = {25, 25};
        int bulletSize[] = {10, 10};
        int targetSize[] = {10, 10};
        int bulletSpeed = 5;
        int playerPos[] = {100, windowSize[1] - padding[1] - playerSize[1]};

        c.post(windowSize[0], windowSize[1]);
        c.setColor(0, 0, 0);
        c.addRect(padding[0], padding[1], windowSize[0] - padding[0] * 2, windowSize[1] - padding[1] * 2);
        c.setColor("BLUE");
        int playerID = c.addFillRect(playerPos[0], playerPos[1], playerSize[0], playerSize[1]);

        boolean isTargetExist = true;
        int targetPos[] = new int[2];
        int targetID;

        int bulletPos[] = new int[2];
        int bulletID;

        while (true) {
            if (isTargetExist) {
                targetPos[0] = (int)(Math.random()*(windowSize[0] - targetSize[0] - padding[0]*2)) + padding[0];
                targetPos[1] = padding[1];
                targetID = c.addFillRect(targetPos[0], targetPos[1], targetSize[0], targetSize[1]);
                isTargetExist = false;
            }
            
            bulletPos[0] = playerPos[0] + playerSize[0]/2 - bulletSize[0]/2;
            bulletPos[1] = playerPos[1] - bulletSize[1];
            bulletID = c.addFillRect(bulletPos[0], bulletPos[1], bulletSize[0], bulletSize[1]);

            int p_x  = 0;
            char pressedKey = c.getPressedKey();
            switch (pressedKey) {
                case 'a':
                    if (playerPos[0] > padding[0]) {
                        p_x --;
                    }
                    break;
                case 'd':
                    if (playerPos[0] < windowSize[0] - playerSize[0] - padding[0]) {
                        p_x ++;
                    }
                    break;
            }
            playerPos[0] += p_x;
            c.move(playerID, p_x, 0);
            c.wait(10);
        }
    }
}