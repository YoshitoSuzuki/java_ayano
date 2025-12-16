public class moving {
    public static void main(String[] args) {
        ICanvas c = new ICanvas();
        int windowSize[] = {600, 400}; 
        int padding[] = {50, 50};
        int playerSize[] = {25, 25};



        c.post(windowSize[0], windowSize[1]);
        c.setColor(0, 0, 0);
        c.addRect(padding[0], padding[1], windowSize[0] - padding[0] * 2, windowSize[1] - padding[1] * 2);
        int playerPos[] = {100, 100};

        int rectID = c.addRect(playerPos[0],playerPos[1],playerSize[0],playerSize[1]);

        String msg = "test";

        int msgID = c.addMsg(0, 0, msg, 20);

        while(true){
            int x = 0;
            int y = 0;
            char pressedKey = c.getPressedKey();
            if (pressedKey == 'w') {
                if (playerPos[1] > padding[1]){
                    y --;
                }
            }
            if (pressedKey == 's') {
                if (playerPos[1] < windowSize[1] - playerSize[1] - padding[1]){
                    y ++;
                }
            }
            if (pressedKey == 'a') {
                if (playerPos[0] > padding[0]){
                    x --;
                }
            }
            if (pressedKey == 'd') {
                if (playerPos[0] < windowSize[0] - playerSize[0] - padding[0]){
                    x ++;
                }
            }
            playerPos[0] += x;
            playerPos[1] += y;
            c.move(rectID, x, y);
            msg = "x:" + playerPos[0] + " y:" + playerPos[1];
            c.delete(msgID);
            msgID = c.addMsg(0, 0, msg, 20);
            c.wait(1);
        }
    }
}