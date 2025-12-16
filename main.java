import java.awt.Color;

public class main {
    public static void main(String[] args) {
        ICanvas c = new ICanvas();
        c.post(600, 400); // 画面サイズ

        // --- 1. マップを描く ---
        c.setBgColor(0, 180, 0); // 背景を緑（草原）にする
        
        // --- 2. キャラクター配置 ---
        // 村人（青い円）
        c.setColor("BLUE");
        int villagerX = 300;
        int villagerY = 200;
        int villagerID = c.addFillOval(villagerX, villagerY, 30, 30);
        
        // 主人公（赤い円）
        c.setColor("RED");
        int heroID = c.addFillOval(50, 50, 30, 30);
       
        // 操作説明
        c.setColor("WHITE");
        c.addMsg(10, 380, "矢印キーで移動、スペースで会話", 16);

        // --- 3. ゲームループ（動きの処理） ---
        while (true) {
            c.wait(30); // 少し待つ（アニメーション速度調整）

            // キー入力を取得
            char key = c.getPressedKey();

            // --- 移動処理 ---
            if (key == 'w') { c.move(heroID, 0, -5); } // 上
            if (key == 's') { c.move(heroID, 0, 5); }  // 下
            if (key == 'a') { c.move(heroID, -5, 0); } // 左
            if (key == 'd') { c.move(heroID, 5, 0); }  // 右

            // --- 会話判定 ---
            // 主人公と村人の座標を取得
            int hx = c.getX(heroID);
            int hy = c.getY(heroID);
            
            // 距離が近い かつ スペースキー(' ')が押されたら
            if (Math.abs(hx - villagerX) < 50 && Math.abs(hy - villagerY) < 50 && key == ' ') {
                
                // 会話ウィンドウを表示
                c.setColor("WHITE");
                int winID1 = c.addFillRect(50, 250, 500, 100); // 白枠
                c.setColor("BLACK");
                int winID2 = c.addFillRect(55, 255, 490, 90);  // 黒背景
                
                c.setColor("WHITE");
                int msgID = c.addMsg(70, 290, "こんにちは！ いい天気ですね。", 20);
                
                c.wait(1000); // 1秒またせる（連打防止）
                c.waitWhenKeyIn(); // 何かキーが押されるまで待つ
                
                // ウィンドウを消してゲーム再開
                c.delete(winID1);
                c.delete(winID2);
                c.delete(msgID);
            }
        }
    }
}