import javax.swing.*;
import java.lang.Math;

public class TypingGame {
    public static void main(String[] args) {
        JFrame window;
        JLabel rabe;
        rabe = new JLabel("Rabe");

        ICanvas c = new ICanvas();

        // --------------------------------------------------------
        // 初期設定
        // --------------------------------------------------------

        int goalScore = 5;

        // ウィンドウサイズ
        int windowSize[] = {800, 600};

        // テキストサイズ
        int randomkeyTextSize = 50;
        int timeTextSize = 30;
        int scoreTextSize = 50;
        
        // テキスト等の位置、間隔調整用
        int margin = 100;
        int padding = 15;

        // ゲームのスピード調整用
        int speed = 10;
        double timeParameter = 1.25;

        // テキスト等の位置、間隔調整用
        double randomTextPosParameter = 0.15;
        double timePosParameter = 3.0;
        double scorePosParameter = 2.0;
        double clearTextPosParameter = 3.0;

        // クリアテキストの大きさ調整用
        double clearTextSize = randomkeyTextSize;

        // --------------------------------------------------------
        // ゲーム本体
        // --------------------------------------------------------

        window = c.post(windowSize[0], windowSize[1]);

        window.setTitle("Typing Game");
        window.add(rabe, "North");

        // 使用するキーのリスト
        char keysList[] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        int keysNum = keysList.length;

        boolean isTextExist = false;
        int textID = -1;

        // 生成された文字
        char randomKey = ' ';

        int time = 0;
        int realTime = 0;
        int timeID = c.addMsg((int)(windowSize[0]/2 - timeTextSize*timePosParameter), (int)(windowSize[1]/2 + randomkeyTextSize + padding - margin), "Time: 0.000", timeTextSize);

        // 現在のスコア
        int score = 0;
        int scoreID = c.addMsg((int)(windowSize[0]/2 - scoreTextSize*scorePosParameter), (int)(windowSize[1]/2 + randomkeyTextSize + timeTextSize + padding*2 - margin), "Score: 0", scoreTextSize);

        while (true) {
            // 生成された文字が表示されていない場合
            if (!isTextExist) {
                randomKey = keysList[(int)(Math.random() * keysNum)];
                c.setColor(0, 0, 0);
                textID = c.addMsg((int)(windowSize[0]/2 - randomkeyTextSize*randomTextPosParameter), (int)(windowSize[1]/2 - margin), String.valueOf(randomKey), randomkeyTextSize);
                isTextExist = true;
            }

            char pressedKey = c.getPressedKey();

            // 正解
            if (pressedKey == randomKey) {
                score ++;
                c.delete(textID);
                isTextExist = false;
            }

            // タイム
            time ++;
            realTime = (int)((speed * time) * timeParameter);
            c.setText(timeID, "Time: " + realTime/1000 + "." + realTime % 1000);

            c.setText(scoreID, "Score: " + score);

            // ゴール
            if (score >= goalScore) {
                break;
            }

            c.wait(speed);
        }
        
        // クリア表示
        c.setColor(0, 0, 220);
        c.addMsg((int)(windowSize[0]/2 - clearTextSize*clearTextPosParameter), (int)(windowSize[1]/2 - margin), "Game Clear!", randomkeyTextSize);

    }
}