import java.lang.Math;

public class TypingGame {
    public static void main(String[] args) {
        ICanvas c = new ICanvas();

        int goalScore = 5;

        int windowSize[] = {800, 600};
        int randomkeyTextSize = 50;
        int timeTextSize = 30;
        int scoreTextSize = 50;
        
        int margin = 100;
        int padding = 15;

        int speed = 10;
        double timeParameter = 1.25;
        double randomTextPosParameter = 0.15;
        double timePosParameter = 3.0;
        double scorePosParameter = 2.0;
        double clearTextPosParameter = 3.0;

        double clearTextSize = randomkeyTextSize;

        // --------------------------------------------------------

        c.post(windowSize[0], windowSize[1]);

        char keysList[] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        int keysNum = keysList.length;

        boolean isTextExist = false;
        int textID = -1;

        char randomKey = ' ';

        int time = 0;
        int realTime = 0;
        int timeID = c.addMsg((int)(windowSize[0]/2 - timeTextSize*timePosParameter), (int)(windowSize[1]/2 + randomkeyTextSize + padding - margin), "Time: 0.000", timeTextSize);

        int score = 0;
        int scoreID = c.addMsg((int)(windowSize[0]/2 - scoreTextSize*scorePosParameter), (int)(windowSize[1]/2 + randomkeyTextSize + timeTextSize + padding*2 - margin), "Score: 0", scoreTextSize);

        while (true) {
            if (!isTextExist) {
                randomKey = keysList[(int)(Math.random() * keysNum)];
                c.setColor(0, 0, 0);
                textID = c.addMsg((int)(windowSize[0]/2 - randomkeyTextSize*randomTextPosParameter), (int)(windowSize[1]/2 - margin), String.valueOf(randomKey), randomkeyTextSize);
                isTextExist = true;
            }
            
            char pressedKey = c.getPressedKey();

            if (pressedKey == randomKey) {
                score ++;
                c.delete(textID);
                isTextExist = false;
            }

            time ++;
            realTime = (int)((speed * time) * timeParameter);
            c.setText(timeID, "Time: " + realTime/1000 + "." + realTime % 1000);

            c.setText(scoreID, "Score: " + score);

            if (score >= goalScore) {
                break;
            }

            c.wait(speed);
        }
        
        c.setColor(0, 0, 220);
        c.addMsg((int)(windowSize[0]/2 - clearTextSize*clearTextPosParameter), (int)(windowSize[1]/2 - margin), "Game Clear!", randomkeyTextSize);

    }
}