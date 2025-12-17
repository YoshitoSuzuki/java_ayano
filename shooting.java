import java.lang.Math;

public class shooting {
    public static void main(String[] args) {
        ICanvas c = new ICanvas();
        int goalScore = 20;
        int windowSize[] = {600, 400};
        int padding[] = {50, 70};
        int playerSize[] = {25, 25};
        int bulletSize[] = {7, 15};
        int targetSize[] = {30, 10};
        int speed = 4;
        int bulletSpeed = speed * 2;
        int bulletSpeedParameter = bulletSpeed;

        int textSize = 20;
        int clearTextSize = windowSize[1]/5;
        double timeParameter = 1.25;
        int playerPos[] = {100, windowSize[1] - padding[1] - playerSize[1]};

        // ---------------------------------------------------

        c.post(windowSize[0], windowSize[1]);
        c.setColor(0, 0, 0);
        c.addRect(padding[0], padding[1], windowSize[0] - padding[0] * 2, windowSize[1] - padding[1] * 2);
        c.setColor("BLUE");
        int playerID = c.addFillRect(playerPos[0], playerPos[1], playerSize[0], playerSize[1]);

        boolean isTargetExist = false;
        int targetPos[] = new int[2];
        int targetID = -1;

        int bulletPos[] = new int[2];
        int bulletID = -1;
        boolean isBulletExist = false;

        int bulletIndex = 0;

        int score = 0;
        c.setColor(0, 0, 0);
        int scoreID = c.addMsg(0, 0, "Score: 0", textSize);

        boolean auto = false;
        c.setColor(110, 110, 110);
        int autoID = c.addMsg(0, textSize, "Auto: " + auto, textSize);

        c.setColor(0, 0, 0);
        int timeID = c.addMsg(0, windowSize[1] - padding[1] + 10, "Time: 0", textSize);
        int time = 0;
        int realTime = 0;

        while (true) {

            // Target
            if (!isTargetExist) {
                targetPos[0] = (int)(Math.random()*(windowSize[0] - targetSize[0] - padding[0]*2)) + padding[0];
                targetPos[1] = padding[1];
                c.setColor(225, 0, 0);
                targetID = c.addFillRect(targetPos[0], targetPos[1], targetSize[0], targetSize[1]);
                isTargetExist = true;
            }

            // Bullet
            if (!isBulletExist) {
                bulletPos[0] = playerPos[0] + playerSize[0]/2 - bulletSize[0]/2;
                bulletPos[1] = playerPos[1] - bulletSize[1];
                c.setColor(110, 110, 110);
                bulletID = c.addFillRect(bulletPos[0], bulletPos[1], bulletSize[0], bulletSize[1]);
                isBulletExist = true;
            } else {
                if (bulletIndex % bulletSpeedParameter == 0) {
                    c.move(bulletID, 0, -bulletSpeed);
                    bulletPos[1] -= bulletSpeed;
                    if (bulletPos[1] < padding[1]) {
                        c.delete(bulletID);
                        isBulletExist = false;
                    }
                    bulletIndex = 0;
                }
                bulletIndex ++;
            }

            // Collision
            if (bulletPos[0] < targetPos[0] + targetSize[0] && bulletPos[0] + bulletSize[0] > targetPos[0] && bulletPos[1] < targetPos[1] + targetSize[1] && bulletPos[1] + bulletSize[1] > targetPos[1]) {
                c.delete(targetID);
                isTargetExist = false;
                c.delete(bulletID);
                isBulletExist = false;
                score ++;
                c.setText(scoreID, "Score: " + score);
            }
            
            // Player
            int p_x  = 0;
            if (auto) {
                if (targetPos[0] < playerPos[0]) {
                    p_x --;
                } else if (targetPos[0] > playerPos[0]) {
                    p_x ++;
                } else {
                    p_x = 0;
                }
            } else {
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
            }

            char pressedKey = c.getPressedKey();
            if (pressedKey == ' ') {
                auto = !auto;
                c.setText(autoID, "Auto: " + auto);
            }

            if (score >= goalScore) {
                break;
            }

            playerPos[0] += p_x;
            c.move(playerID, p_x, 0);
            c.wait(speed);
            time ++;
            realTime = (int)((speed * time)  * timeParameter);
            c.setText(timeID, "Time: " + realTime/1000 + "." + realTime % 1000);
        }

        // ---------------------------------------------------

        c.setColor("GREEN");
        c.addMsg(padding[0] + 10, windowSize[1]/2 - clearTextSize/2, "Game Clear!", clearTextSize);
        c.delete(timeID);
        c.delete(autoID);
        c.setColor(0, 0, 0);
        c.addMsg(padding[0] + 10, windowSize[1]/2 - clearTextSize/2 + clearTextSize, "Clear Time: " + realTime/1000 + "." + realTime % 1000, textSize);
    }
}