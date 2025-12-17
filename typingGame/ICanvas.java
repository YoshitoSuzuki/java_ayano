// 画像を簡単に描画するためのクラス
//                                       by Isamu Takahashi, since 2007/11/1
//                                              (new Version)since 2009/ 1/7
//                                              (new Comment)since 2010/11/8
//                                              (last update) 2020/10/20
//  このプログラムは開発中です。将来的に変更される可能性があります。
//  北里大学、情報科学Ｂ関係者以外の方も、中身を変更しない限り自由に
//  利用していただいてかまいませんが、２次配布はしないでください。
import java.util.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;

/** 画像を描くためのキャンバス。
  <p>
  線や楕円や四角形や文字を書いたり移動したりできるキャンバスです。
  オブジェクトを作成して命令を出すことで簡単に線を引いたり円を書いたり
  画像ファイルを貼り付けたりできます。<BR>
  ※以後の例では、このクラスのインスタンスがcanvasという名前の変数に
  割り当てられているものとします。
  </p>
  <pre>例：
    ICanvas canvas;
    canvas = new ICanvas();
    canvas.post(600,400);                //600x400のウインドウを作って割り付ける
    canvas.setColor("RED");              //「ペンの色」を赤に設定する
    canvas.addLine(20,20,100,100);       //(x,y)=(20,20)から(100,100)に線を引く
    canvas.setColor("BLUE");             //「ペンの色」を青に設定する
    canvas.addMsg(20,50,"こんにちは",16);//(x,y)=(20,50)から大きさ16で文字を書く
    canvas.addImg(2,40,"img.jpg");       //(x,y)=(2,40)にimg.jpgの画像を貼る
  </pre>
  <h3>アニメーション</h3>
  <p>
  一度描いた画像を動かしたり、前面に移動したり、背面に移動したりして、
  アニメーションをさせることができます。描いたもの（以後アイテムと呼びます）には
  全て通し番号（ID番号）がふられています。ID番号は、アイテムを描画するメソッド
  （addLine, addRect, addFillRect, addOval, addFillOval, addButton, addMsg）を
  呼び出したときに、戻り値として得ることができます。これを使って、どのアイテムを
  動かすかを決めます。
  また、アニメーションをさせるために、一定時間待つメソッドwaitが用意されて
  います。これを使わないと、一瞬で全てのアイテムが動いてしまい、アニメーションに
  ならないので注意してください。
  </p>
  <pre>例：
    int ovID;
    ovID = canvas.addFillOval(100,50,30,30); //(x,y)=(100,50)の位置にサイズ30の
                                            //円を描き，その通し番号をovIDに代入する。
    canvas.wait(2000);        //２秒待つ。
    canvas.move(ovID,5,0);    //変数ovIDに記録されたID番号のアイテムをx方向へ 5 移動する。
    canvas.wait(2000);        //２秒待つ。
    canvas.move(ovID,0,-10);  //変数ovIDに記録されたID番号のアイテムをy方向へ -10 移動する。
  </pre>
  
  <h3>ボタンクリックの把握</h3>
  <p>
  マウスのボタンがクリックされるまで待つには次のように書きます。
  </p>
  <pre>例：
    // ここにクリックされる前に実行する処理を書く．
    canvas.waitWhenMouseClicked();   //←マウスボタンがクリックされるまで待つ命令
    // ここにクリックされた後で実行する処理を書く
  </pre>

  <p>
  addButtonを使って画面上にボタンを作っておくと、メソッドgetPointedButtonMsgを
  使ってマウスポインタ（マウスカーソル）がボタンを指しているかどうか、
  指している場合には、ボタンに何が書かれているのか、を、調べることができます。
  </p>
  <pre>例：
    canvas.addButton(10,10,60,20,"テストボタン");
    String focussed;
    focussed=canvas.getPointedButtonMsg();  
  </pre>
  <p>
  上の例では、上記の３行目の命令が実行されたときに、マウスポインタが
  １行目で作成したボタンを指していれば、変数focussedに"テストボタン"が代入されます。
  マウスポインタがどのボタンも指していなかった場合にはfocussedには""が代入されます。
  ただし、この例では、ボタンを作った直後にマウスポインタが指している先を調べて
  いるため、たまたまボタンが作成された瞬間にマウスポインタがボタンを指していた
  場合を除いて、変数focussedには""が代入される結果になってしまいます。
  </p>  
  <p>
  マウスでボタンが押されているか調べるには、上記の２つを組み合わせる必要があります。
  下記の例では、テストボタンが表示された状態で動作が止まり、マウスがクリックされると、
  動作が進みます。このとき、マウスポインタがボタンを指していなければfocussedには""が代入され、
  ボタンを指していたときには"テストボタン"が代入されます。
  </p>
  <pre>例：
    canvas.addButton(10,10,60,20,"テストボタン");
    canvas.waitWhenMouseClicked(); 
    String focussed;
    focussed=canvas.getPointedButtonMsg();  
  </pre>
  <p>
  あらかじめ2つのボタンを画面に表示しておき、マウスがクリックされるまで待って、
  クリックされたときにマウスポインタが指していたボタンに応じて動作を変えるには、
  例えば次のようにします。
  </p>
  <pre>例：
    String focussed; // マウスポインタが指しているボタンの文字を記録する変数を作成。
    canvas.addButton(10,10,20,20,"あ"); // 「あ」と書かれたボタンを作る。
    canvas.addButton(50,10,20,20,"い"); // 「い」と書かれたボタンを作る。
    canvas.waitWhenMouseClicked();          //  マウスがクリックされるまで待つ。
    focussed=canvas.getPointedButtonMsg();	// マウスポインタが指してるボタンに
	                                        // 書かれた文字列をfocussedに代入。
    if (focussed.equals("あ")) {
		// 「あ」と書かれたボタンがクリックされたときの処理
    }
    if (focussed.equals("い")) {
		// 「い」と書かれたボタンがクリックされたときの処理
    }
  </pre>
  <p>
  同じ文字が書かれている２つ以上のボタンを作成して、それらを区別したい場合には、
  getPointedButtonMsgは利用できません。addButtonを実行したときに得られる通し番号を
  変数に記録しておき、getPointedButtonの戻り値と一致するか比較してください。
  </p>

  <h3>ボタンの状態のリアルタイムな把握</h3>
  <p>
  リアルタイムに処理をするゲームのようなプログラムをつくるために、
  マウスのボタンが押されているかどうかを判定する命令が用意されています。
  </p>
  <pre>例：
    int mousebtn;
    mousebtn=canvas.getPressedMouse();
  </pre>
  <p>
  上の例では、上記の２行目の命令が実行された瞬間に、マウスの左ボタンが
  押されている状態であれば、mousebtnに1が代入されます。
  マウスの右ボタンが押されている状態であれば2が代入されます。
  マウスのボタンがおされていなければ0が代入されます。
  </p>
  リアルタイムに処理する仕組みを用いて、テストボタンが押されるまで待つ仕組みは
  例えば下記のように書きます。
  <pre>例：
    int mousebtn;
    String focussed;
    canvas.addButton(10,10,60,20,"テストボタン");
    while (true) {
      canvas.wait(10);            // ←繰り返しの際にはこれを必ず入れること
      mousebtn=canvas.getPressedMouse();
      focussed=canvas.getPointedButtonMsg();
      if ((mousebtn==1)&&(focussed.equals("テストボタン"))) {
         // ボタンが押されているときの処理
      }
    }
  </pre>
  
  <h3>キーボードの状態の把握</h3>
  <p>
  キーボードの押されている文字を得ることができます。認識方法は３つあります。
  キーが押されるまでプログラムを一時停止して待つメソッド（waitWhenKeyIn）、
  指定された時間の間プログラムの動作を止め、その待ち時間の間で最初に押された
  キーを得るメソッド（waitWithKey）、メソッドが呼び出された時点で押されている
  キーを得るメソッド（getPressedKey)の３つです。
  対話的にやりとりする場合はwaitWhenKeyIn、ゲームなどのリアルタイムの処理の
  途中で押されたキーを得たい場合waitWithKey、さらに細かいリアルタイム制御を
  したい場合はgetPressedKeyを使うと良いでしょう。
  ただし、２つ以上のキーが同時に押された場合には、一方しか得ることができない
  ので注意してください。
  </p>
  <pre>例１：
    char key;
    key = canvas.waitWhenKeyIn();  // 何かキーが押されるまで待ちます．
    if (key=='u') { ・・・ }       // 押されたキーが u だったら ・・・を実行．
  </pre>
  <pre>例２：
    char key;
    key = canvas.waitWithKey(1000);  // １秒待ちます（キーを押したかどうかに
                                     // かかわらず１秒後に次の処理へ移ります）
    if (key=='u') { ・・・ }         // 上記の１秒間の間の最初に u キーが
                                     // 押されていた場合，・・・を実行する．
  </pre>
  <pre>例３：
    while (true) {                   // 下記を永遠に繰り返す．
      canvas.wait(10);               //   １０ミリ秒待つ．
      key=canvas.getPressedKey();    //   今押されているキーを得る．
      if (key=='u') { ・・・ }       //   それが u だったら ・・・を実行．
    }
  </pre>
  ICanvasのオブジェクトをコンテナに入れた場合，キー入力判定用のリスナーを
  そのコンテナのリスナーとして登録しないと正しくキー入力判定機能が動作しません。
  このような場合，getKeyListenerメソッドの戻り値を，コンテナのaddKeyListenerで
  リスナーに追加してください（詳細はgetKeyListenerの説明を参照）。
  *@see #getKeyListener
 **/


public class ICanvas extends JPanel{
  
  ArrayList<Item> itemList=new ArrayList<Item>();
  Color canvasColor = Color.BLACK;
  int itemIndex=0;
  Color bgColor=Color.WHITE;
  ICanvas targetCanvas=this;
  JFrame window=null;
  /** キーボードのキーが押されていない状況を意味する文字 **/
  public static char NULLKEY='\u0000';
  char pressedKey=NULLKEY;
  char storedKey=NULLKEY;
  int pressedMouse=0;
  KeyChecker keychecker=new KeyChecker();
  MouseChecker mousechecker=new MouseChecker();
  
  /** ICanvasのインスタンスを作成する．
   ** このコンストラクタでICanvasを作成するだけでは利用できない．
   ** JFrameに配置するなどの方法で画面に表示させる必要があるので注意すること．
   **/
  public ICanvas() {
    this.addKeyListener(keychecker);
    this.addMouseListener(mousechecker);
  }

  /**
   * 親のJFrameを探す。なければnullを返す。
   */
  private JFrame findJFrame() {
    Container parent = this;
    do {
      parent = parent.getParent();
    }while (!(parent instanceof JFrame) && parent !=null);
    if (parent!=null){
      return (JFrame)parent;
    }else{
      return null;
    }
  }

  /** 画像を描画するための本体のメソッド（一般のユーザはこのメソッドを使う必要はありません）．
   **/
  public void paintComponent(Graphics g) {
    //親のJFrameがなければ探す。あればクリックしたときに終了するように設定する
    if (window==null) {
      window=findJFrame();
      if (window!=null) {
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      }
    }
    //描画処理
    g.setColor(bgColor);
    g.fillRect(0,0,this.getWidth(), this.getHeight());
    for (int n=0;n<itemList.size(); n++) {
      Item i=itemList.get(n);
      g.setColor(i.color);
      i.draw(g);
    }
  }
  //=================================================================
  /** ウインドウを作成し，その中にICanvasを割り付けて表示する．
   ** このメソッドを使うことで，キャンバスのみのウインドウを
   ** 簡単に作ることができる．
   ** ウインドウの中にラベルなどICanvas以外のコンテナも配置したい場合には
   ** このメソッドは使わずに，JFrameなどのコンテナにICanvasを割り付ける
   ** プログラムを記述する必要がある．
   ** @param width ウインドウの幅
   ** @param height ウインドウの高さ
   ** @return 生成されたJFrameのウインドウ
   **/
  public JFrame post(int width, int height) {
    if (window==null) {
      window=new JFrame();
     // window.addKeyListener(keychecker);
      window.addKeyListener(this.getKeyListener());
      window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //バツボタンで終了するための命令
    }
    window.setSize(width,height);
    window.setLayout(new BorderLayout());
    window.add("Center",this);
    window.setVisible(true);
    return window;
  }
  /** キー入力判定用のリスナーを得る．
   ** ICanvasをコンテナ（JFrameなど）に配置した場合には，事前に
   ** そのコンテナのaddKeyListenerメソッドを用いて，このgetKeyListenerの戻り値を
   ** リスナーに追加しておく必要がある．
   ** <PRE>例：
      ICanvas c;
      c = new ICanvas();
      JFrame f;
      f = new JFrame();
      f.add(c);
      f.addKeyListener(c.getKeyListener());  //←これを実行する必要がある．．
      </PRE>
   ** @see #getPressedKey
   ** @see #waitWithKey
   ** @see #waitWhenKeyIn
   ** @return リスナーに与える監視用オブジェクト
   **/
  public KeyAdapter getKeyListener() {
    return keychecker;
  }
  
  //=================================================================
  /** キャンバスに直線を追加する．
   ** 引数で指定した２つの座標の間に直線が引かれる．
   ** 線の色には事前にsetColorで指定した「ペンの色」が使われる．
   ** 追加した直線の通し番号（ＩＤ）が戻り値として得られる．
   ** 直線はアイテムの一種として扱われる．
   ** @return 作成したアイテムのＩＤ
   ** @param x1 始点のx座標
   ** @param y1 始点のy座標
   ** @param x2 終点のx座標
   ** @param y2 終点のy座標
   ** @see #setColor(int r, int g, int b)
   ** @see #setColor(String key)
   **/
  public int addLine(int x1, int y1, int x2, int y2) {
    itemList.add(new Line(x1,y1,x2,y2));
    this.repaint();
    return itemIndex;
  }
  /** キャンバスに文字列を追加する．
   ** 引数で指定した座標に文字列が書かれる．
   ** 文字の色には事前にsetColorで指定した「ペンの色」が使われる．
   ** 改行文字は無視される（複数行にまたがる文字列を表示したい場合は
   ** 表示する座標を変えてこのメソッドを複数回呼び出すこと）．
   ** 追加した文字列の通し番号（ＩＤ）が戻り値として得られる．
   ** 文字列はアイテムの一種として扱われる．
   ** メソッドsetTextを利用することで，一度表示した文字列を
   ** 別の文字列に変更することができる．
   ** @return 作成したアイテムのＩＤ
   ** @param x 表示する位置のx座標
   ** @param y 表示する位置のy座標
   ** @param message 表示する文字列
   ** @param fontSize 文字の大きさ
   ** @see #setColor(int r, int g, int b)
   ** @see #setColor(String key)
   ** @see #setText
   **/
  public int addMsg(int x, int y, String message, int fontSize) {
    itemList.add(new Msg(x,y,message,fontSize));
    this.repaint();
    return itemIndex;
  }
  /** キャンバスに四角形を追加する．
   ** 引数で指定した座標に指定した大きさの四角形が描かれる．
   ** 線の色には事前にsetColorで指定した「ペンの色」が使われる．
   ** 追加した四角形の通し番号（ＩＤ）が戻り値として得られる．
   ** 四角形はアイテムの一種として扱われる．
   ** @return 作成したアイテムのＩＤ
   ** @param x 表示する位置のx座標
   ** @param y 表示する位置のy座標
   ** @param width 四角形の幅
   ** @param height 四角形の高さ
   ** @see #setColor(int r, int g, int b)
   ** @see #setColor(String key)
   **/
  public int addRect(int x, int y, int width, int height) {
    itemList.add(new Rect(x,y,width,height, false));
    this.repaint();
    return itemIndex;
  }
  /** キャンバスに塗りつぶした四角形を追加する．
   ** 塗りつぶす色には事前にsetColorで指定した「ペンの色」が使われる．
   ** （基本的な動作はaddRectと同じため詳細はaddRectの説明を参照）
   ** @return 作成したアイテムのＩＤ
   ** @param x 表示する位置のx座標
   ** @param y 表示する位置のy座標
   ** @param width 四角形の幅
   ** @param height 四角形の高さ
   ** @see #addRect(int x, int y, int width, int height)
   ** @see #setColor(int r, int g, int b)
   ** @see #setColor(String key)
   **/
  public int addFillRect(int x, int y, int width, int height) {
    itemList.add(new Rect(x,y,width,height, true));
    this.repaint();
    return itemIndex;
  }
  /** キャンバスに楕円を追加する．
   ** 引数で指定した座標に指定した大きさの楕円が描かれる．
   ** 線の色には事前にsetColorで指定した「ペンの色」が使われる．
   ** 追加した楕円の通し番号（ＩＤ）が戻り値として得られる．
   ** 楕円はアイテムの一種として扱われる．
   ** @return 作成したアイテムのＩＤ
   ** @param x 表示する位置のx座標
   ** @param y 表示する位置のy座標
   ** @param width 楕円の幅
   ** @param height 楕円の高さ
   ** @see #setColor(int r, int g, int b)
   ** @see #setColor(String key)
   **/
  public int addOval(int x, int y, int width, int height) {
    itemList.add(new Oval(x,y,width,height,false));
    this.repaint();
    return itemIndex;
  }
  /** キャンバスに塗りつぶした楕円を追加する．
   ** 塗りつぶす色には事前にsetColorで指定した「ペンの色」が使われる．
   ** （基本的な動作はaddOvalと同じため詳細はaddOvalの説明を参照）
   ** @return 作成したアイテムのＩＤ
   ** @param x 表示する位置のx座標
   ** @param y 表示する位置のy座標
   ** @param width 楕円の幅
   ** @param height 楕円の高さ
   ** @see #addOval(int x, int y, int width, int height)
   ** @see #setColor(int r, int g, int b)
   ** @see #setColor(String key)
   **/
  public int addFillOval(int x, int y, int width, int height) {
    itemList.add(new Oval(x,y,width,height,true));
    this.repaint();
    return itemIndex;
  }
  /** キャンバスにファイルから読み込んだ画像を追加する．
   ** 指定されたファイルの画像を読み込み，指定した位置に貼り付ける．
   ** JPEG形式，GIF形式，PNG形式の画像ファイルが使える．
   ** ただし，利用する画像ファイルは基本的にはプログラムを実行した
   ** フォルダの中に保存しておく必要がある点に注意すること．
   ** 追加した画像の通し番号（ＩＤ）が戻り値として得られる．
   ** 追加した画像はアイテムの一種として扱われる．
   ** @return 作成したアイテムのＩＤ
   ** @param x 表示する位置のx座標
   ** @param y 表示する位置のy座標
   ** @param fileName 読み込む画像のファイル名（JPEG, GIF, PNG 形式）
   **/
  public int addImg(int x, int y, String fileName) {
    itemList.add(new Img(x,y,fileName));
    this.repaint();
    return itemIndex;
  }

  /** キャンバスにボタンを追加する．
   ** 指定した位置と大きさの四角形の中に指定した文字列が書かれたボタンが表示される．
   ** 別のいくつかのメソッドとともに利用することで，ボタンがクリックされたら
   ** 何かするという動作を記述できる（詳しくは下記の関連項目等を参照すること）．
   ** ボタンの背景の色には事前にsetColorで指定した「ペンの色」が使われる．
   ** ボタンの文字の色は黒で変更することはできない．
   ** 一度もsetColorを実行せずにこのメソッドを使うと背景が黒くなるので注意すること．
   ** 文字の大きさは，ボタンの縦の長さ（高さ）に基づいて自動的に設定される．
   ** 文字の大きさや位置や色を変えたい場合や画像が描かれたボタンを作りたいときは，
   ** 空白の文字(" "など)を引数msgに与えてボタンを追加し，addMsgやaddImgで
   ** そのボタンに重ねるように文字や絵を作る必要がある．
   ** 追加したボタンの通し番号（ＩＤ）が戻り値として得られる．
   ** 追加したボタンはアイテムの一種として扱われる．
   ** 下記に，ボタンがクリックされたら何かするという動作の例を示す．
   ** <PRE>例(ICanvasの変数名が「c」の場合）
      c.setColor("RED");                   // ペンの色を赤に変更
      c.addButton(10,10,80,20,"はい");     //「はい」と書かれたボタンを追加
      c.addButton(100,10,80,20,"いいえ");  //「いいえ」と書かれたボタンを追加
      c.waitWhenMousePressed();            // マウスがクリックされるまで待つ
      String pressedMsg;                   // 文字列を記録する変数を作成．
      pressedMsg=c.getPointedButtonMsg();  // マウスが指したボタンの文字を代入
      if (pressedMsg.equals("はい")) {
        //「はい」のボタンが押されたときの処理
      }
      if (pressedMsg.equals("いいえ")) {
        //「いいえ」のボタンが押されたときの処理
      }
      </PRE>
   ** @return 作成したアイテムのＩＤ
   ** @param x 表示する位置のx座標
   ** @param y 表示する位置のy座標
   ** @param width ボタンの幅
   ** @param height ボタンの高さ(ボタンに付けるメッセージのフォントの大きさ）
   ** @param msg ボタンに付けるメッセージ
   ** @see #getPointedButton
   ** @see #getPointedButtonMsg
   ** @see #setColor(String key)
   ** @see #setColor(int r, int g, int b)
   ** @see #setText
   ** @see #addMsg
   ** @see #addImg
   **/
  public int addButton(int x, int y, int width, int height, String msg) {
    itemList.add(new Button(x,y,width,height,msg));
    this.repaint();
    return itemIndex;
  }
  

  //=================================================================
  /** 番号からアイテムを得る **/
  private Item find(int id) {
    Item item=null;
    for(int i=0; i<itemList.size(); i++) {
      if (itemList.get(i).getID()==id) {
	  	item=itemList.get(i);
	  	break;
	  }
    }
    return item;
  }

  //=================================================================
  /** 指定した時間だけ動作を止めて待つ．
    引数timeの単位は ミリ秒．１秒待つなら1000を与える．
   ** @param time 動作を止めて待つ時間（単位はms）
   **/
  public void wait(int time) {
    this.repaint();
    try{Thread.sleep((long)time);}catch(Exception e){}
    this.repaint();
  }
  /** キー入力を監視しながら指定した時間だけ待つ．
   ** 引数timeの単位は ミリ秒．１秒待つなら1000を与える．
   ** 待ちはじめてから指定した時間が経過するまでの間に押されたキーのうち，
   ** 最初に押されたキーボードのキーを戻り値として得る．キーが押されて
   ** いない場合はICanvas.NULLKEY が得られる．
   ** このメソッドを実行すると，キーが押されたか否かにかかわらず，必ず指定した
   ** 時間だけ待つ動作が行われる．
   ** この命令を使うには，ICanvasを配置しているコンテナ（JFrameなど）にキーリスナーを
   ** 追加する必要がある（詳細はgetKeyListenerの項を参照すること）．
   ** @param time 動作を止めて待つ時間（単位はms）
   ** @return 押されたキーの文字
   ** @see #getKeyListener
   **/
  public char waitWithKey(int time) {
    storedKey=NULLKEY;
    try{Thread.sleep((long)time);}catch(Exception e){}
    return storedKey;
  }
  /** キーボードのキーが押されるまで待つ．
   ** この命令を使うには，ICanvasを配置しているコンテナ（JFrameなど）にキーリスナーを
   ** 追加する必要がある（詳細はgetKeyListenerの項を参照すること）．
   ** @return 押されたキーの文字
   ** @see #getKeyListener
   **/
  public char waitWhenKeyIn() {
    storedKey=NULLKEY;
    while(pressedKey==NULLKEY)  {
      wait(10);
    }
    this.repaint();
    return storedKey;
  }
  /** マウスの左ボタンか右ボタンが押されるまで待つ．
   ** この命令が実行される前にマウスボタンが押しっぱなしになっていたときは，
   ** この命令はスキップされる．
   ** @return 押されたボタンに対応する番号（左なら１，右なら２）
   **/
  public int waitWhenMousePressed() {
    while(pressedMouse==0)  {
      wait(10);
    }
    return pressedMouse;
  }
  /** マウスの左ボタンか右ボタンがクリックされるまで待つ．
   ** この命令が実行される前にマウスボタンが押しっぱなしになっているときは，
   ** 一度マウスのボタンがはなされてからボタンが押されるまで待つ．
   ** @return クリックされたボタンに対応する番号（左なら１，右なら２）
   **/
  public int waitWhenMouseClicked() {
  	while(pressedMouse!=0) {
		wait(10);
  	}
    while(pressedMouse==0)  {
      wait(10);
    }
    return pressedMouse;
  }
  

  /** 指定した通し番号（ID）のアイテムを指定した量だけ移動する．
   ** @param id 移動したいアイテムのID
   ** @param movex ｘ方向への移動量（正の値の場合は左へ移動する）
   ** @param movey ｙ方向への移動量（正の値の場合は下へ移動する）
   **/
  public void move(int id, int movex, int movey) {
    Item item= find(id);
    if (item==null) {
      System.err.println("ERROR: IDが"+id+"のアイテムが存在しません．");
    } else {
      item.move(movex,movey);
      this.repaint();
    }
  }
  /** 指定した通し番号（ID）のアイテムの表示位置を指定した座標に変更する．
   ** @param id 移動したいアイテムのID
   ** @param x 表示位置のｘ座標
   ** @param y 表示位置のｙ座標
   **/
  public void setPos(int id, int x, int y) {
    Item item= find(id);
    if (item==null) {
      System.err.println("ERROR: IDが"+id+"のアイテムが存在しません．");
    } else {
      item.set(x,y);
      this.repaint();
    }
  }
  /** 指定した通し番号（ID）のアイテムに表示されている文字列を変更する．
   * この命令はaddButtonまたはaddMsgによって作られたアイテムに対してだけ利用できる．
   * 第１引数で指定したアイテムに表示されている文字列が，第２引数で指定した文字列に
   * 変更される．
   * @param id 文字列を設定したいアイテムのID（ボタンまたはメッセージのID）
   * @param t 設定する文字列
   * @see #addMsg
   * @see #addButton
   */
  public void setText(int id, String t) {
    Item item= find(id);
    if (item==null) {
      System.err.println("ERROR: IDが"+id+"のアイテムが存在しません．");
    } else {
      item.setText(t);
      this.repaint();
    }
    this.repaint();
  }
  /** 指定した通し番号（ID）のアイテムに設定されている文字列を得る．
   * addButtonかaddMsgで作成したアイテムのみ利用できる．それ以外のアイテムに
   * 対してこのメソッドを実行した場合には空文字列（""）が得られる．
   * @param id 文字列を取得したいアイテムのID．
   * @return アイテムに設定されている文字列
   * @see #addMsg
   * @see #addButton
   * @see #setText
   */
  public String getText(int id) {
    Item item= find(id);
    if (item==null) {
      System.err.println("ERROR: IDが"+id+"のアイテムが存在しません．");
      return "";
    } else {
      return item.getText();
    }
  }
  
  
  /** 指定した通し番号（ID）のアイテムをキャンバスの最前面へ移動する．
   ** @param id 最前面へ移動したいアイテムのID
   **/
  public void raise(int id) {
    Item item= find(id);
    if (item==null) {
      System.err.println("ERROR: IDが"+id+"のアイテムが存在しません．");
    } else {
      itemList.remove(item);
      itemList.add(item);
      this.repaint();
    }
  }
  /** 指定した通し番号（ID）のアイテムをキャンバスを最背面へ移動する．
   ** @param id 最背面へ移動したいアイテムのID
   **/
  public void lower(int id) {
    Item item= find(id);
    if (item==null) {
      System.err.println("ERROR: IDが"+id+"のアイテムが存在しません．");
    } else {
      itemList.remove(item);
      itemList.add(0,item);
      this.repaint();
    }
  }
  /** 指定した通し番号（ID）のアイテムをキャンバス上から削除する．
   ** 存在しないIDを指定した場合には，標準出力にエラーメッセージが表示される．
   ** 一度削除したアイテムのIDはそれ以降使うことができなくなる．
   ** @param id 削除したいアイテムのID
   **/
  public void delete(int id) {
    Item item= find(id);
    if (item==null) {
      System.err.println("ERROR: IDが"+id+"のアイテムが存在しません．");
    } else {
      itemList.remove(item);
      this.repaint();
    }
  }
  /** 全てのアイテムをキャンバス上から削除する．
  **/
  public void deleteAll() {
    itemList.clear();
    this.repaint();
  }
  /** 指定した通し番号（ID）のアイテムの表示位置のx座標を得る．
   ** 直線や四角形などのアイテムごとに，どの位置を返すかが異なる．
   ** もし，指定したIDのアイテムが存在しなければ-1を返す．
   * @param id 座標を取得したいアイテムのID．
   * @return そのアイテムの表示位置のx座標の値
   **/
  public int getX(int id) {
    Item item= find(id);
    if (item==null) {
      System.err.println("ERROR: IDが"+id+"のアイテムが存在しません．");
      return -1;
    }
    return item.getX();
  }
  /** 指定した通し番号（ID）のアイテムの表示位置のy座標を得る．
   ** 直線や四角形などのアイテムごとに，どの位置を返すかが異なる．
   ** もし，指定したIDのアイテムが存在しなければ-1を返す．
   * @param id 座標を取得したいアイテムのID．
   * @return そのアイテムの表示位置のy座標の値
   **/
  public int getY(int id) {
    Item item= find(id);
    if (item==null) {
      System.err.println("ERROR: IDが"+id+"のアイテムが存在しません．");
      return -1;
    }
    return item.getY();
  }
  
  private Color getColor(String key) {
    Color ans=Color.BLACK;
    if ("BLACK".equals(key)) {ans=Color.BLACK;}
    if ("WHITE".equals(key)) {ans=Color.WHITE;}
    if ("GRAY".equals(key)) {ans=Color.GRAY;}
    if ("RED".equals(key)) {ans=Color.RED;}
    if ("BLUE".equals(key)) {ans=Color.BLUE;}
    if ("GREEN".equals(key)) {ans=Color.GREEN;}
    if ("YELLOW".equals(key)) {ans=Color.YELLOW;}
    if ("ORANGE".equals(key)) {ans=Color.ORANGE;}
    if ("PINK".equals(key)) {ans=Color.PINK;}
    return ans;
  }
  /** 「ペンの色」を設定する． **/
  private void setColor(Color c) {
    canvasColor=c;
  }
  /** 「ペンの色」を変更する（光の３原色（赤緑青）の強さで指定する）．
   ** 光の強さは0～255の数字で指定する．
   ** 例えば，暗い緑色にしたいなら(r,g,b)=(0,128,0)．
   ** この命令を実行した後でaddLineやaddMsgなどの命令を実行すると，
   ** この色で線や文字などが描かれる．
   ** @param r 赤の光の強さ（0～255）
   ** @param g 緑の光の強さ（0～255）
   ** @param b 青の光の強さ（0～255）
   **  @see #addLine
   **  @see #addRect
   **  @see #addFillRect
   **  @see #addOval
   **  @see #addFillOval
   **  @see #addButton
   **  @see #addMsg
   **/
  public void setColor(int r, int g, int b) {
    canvasColor=new Color(r,g,b);
  }
  /** 「ペンの色」を変更する（文字列で指定する）．
   **  この命令を実行した後でaddLineやaddMsgなどの命令を実行すると，
   **  この色で線や文字などが作られる．
   **  keyには次の文字列を指定することができる
   **  「BLACK, WHITE, GRAY RED BLUE GREEN YELLOW ORANGE PINK 」．
   ** 文字列で色を指定する場合にはこれらの色しか使うことができない．
   ** これら以外の色を使う場合には，setColor(int r, int g, int b)のメソッドを
   ** 利用すること．
   ** @param key "BLACK" "WHITE" "GRAY" "RED" "BLUE" "GREEN" "YELLOW" "ORANGE" "PINK" のどれかの文字列
   ** @see #setColor(int r, int g, int b)
   **/
  public void setColor(String key) {
    canvasColor=getColor(key);
  }
  /** 「キャンバスの背景の色」を変更する（文字列で指定する）．
   **  keyには次の文字列を指定することができる
   **  「BLACK, WHITE, GRAY RED BLUE GREEN YELLOW ORANGE PINK」．
   ** 文字列で色を指定する場合にはこれらの色しか使うことができない．
   ** これら以外の色を使う場合には，setBgColor(int r, int g, int b)のメソッドを
   ** 利用すること．
   ** @param key "BLACK" "WHITE" "GRAY" "RED" "BLUE" "GREEN" "YELLOW" "ORANGE" "PINK" のどれかの文字列
   **  @see #setBgColor(int r, int g, int b)
   **/
  public void setBgColor(String key) {
    bgColor=getColor(key);
    this.repaint();
  }
  /** 「キャンバスの背景の色」を変更する（光の３原色（赤緑青）の強さで指定する）．
   ** 光の強さは0～255の数字で指定する．
   ** 例えば，淡い黄色にしたいなら(r,g,b)=(255,255,128)．
   ** @param r 赤の光の強さ（0～255）
   ** @param g 緑の光の強さ（0～255）
   ** @param b 青の光の強さ（0～255）
   **/
  public void setBgColor(int r, int g, int b) {
    bgColor=new Color(r,g,b);
    this.repaint();
  }
  /** キーボードの押されているキーの文字を得る．
   ** メソッドが実行された瞬間に押されていたキーの文字が戻り値として得られる．
   ** 何も押されていない場合は ICanvas.NULLKEY が戻り値として得られる．．
   ** この命令を使うには，ICanvasを配置しているコンテナ（JFrameなど）にキーリスナーを
   ** 追加する必要がある（詳細はgetKeyListenerの項を参照すること）．
   ** @return 押されていたキーの文字
   ** @see #getKeyListener
   **/
  public char getPressedKey() {
    return pressedKey;
  }

  /** マウスポインタ（マウスカーソル）が指しているボタンの通し番号（ID）を得る．
   ** メソッド addButton で追加されたボタンの上にマウスカーソルがあると，
   ** そのボタンのIDが戻り値として得られる．もしマウスカーソルの下のボタンが
   ** ない場合は-1が得られる．
   ** @return マウスポインタが指しているボタンのID（なければ-1）
   ** @see #getPointedButtonMsg()
   */
  public int getPointedButton() {
    Point p = getMousePosition(true);
    int ans=-1;
    if (p!=null) {
      Item item=null;
      for(int i=0; i<itemList.size(); i++) {
        Item tmpItem=itemList.get(i);
        if (tmpItem.isButton) {
          if (((Button)tmpItem).isPointed(p)) {
            item=tmpItem;
          }
        }
      }
      if (item!=null) {ans=item.getID();}
    }
    return ans;
  }
  /** マウスポインタ（マウスカーソル）が指しているボタンに書かれたメッセージを得る．
   ** 基本的な動作はgetPointedButtonと同じだが，こちらのメソッドの戻り値は
   ** ボタン上に書かれているメッセージの文字列となる．
   ** もし，ボタンがない場合は何もない文字列（""）が得られる．
   ** @return マウスポインタが指しているボタンに書かれた文字列
   ** @see #getPointedButton()
   */
  public String getPointedButtonMsg() {
    int id=getPointedButton();
    if (id>=0) {
      return ((Button)find(id)).getMsg();
    } else {
      return "";
    }
  }
  /** マウスのボタンが押されているかどうかを判定する．
   ** このメソッドが実行された瞬間に，マウスのボタンが押されていなければ0，
   ** 左ボタンが押されていれば1，右ボタンが押されていれば2が戻り値として得られる．
   ** @return 押されているマウスボタンの番号（押されていなければ 0 ）
   **/
  public int getPressedMouse() {
    return pressedMouse;
  }
  //=================================================================
  private class Item {
    Color color= Color.BLACK;
    int id;
    int posx, posy;
    boolean active=false;
    public boolean isButton=false;
    String text;
    public Item() {
      itemIndex++; id=itemIndex;
      color=canvasColor;
    }
    public int getID() {return id;}
    public int getX() {return posx;}
    public int getY() {return posy;}
    public String getText() {return text;}
    public void setText(String t) {text=t;}
    public void draw(Graphics g) {
    }
    public void move(int movex, int movey) {
      posx=posx+movex; posy=posy+movey;
    }
    public void set(int x, int y) {
      posx=x; posy=y;
    }
  }
  private class Line extends Item {
    int beginX, beginY, endX, endY;
    public Line(int x1, int y1, int x2, int y2) {
      super();beginX=x1; beginY=y1; endX=x2; endY=y2;
    }
    public void draw(Graphics g) { g.drawLine(beginX, beginY, endX, endY);}
    public void move(int movex, int movey) {
      beginX=beginX+movex; beginY=beginY+movex; 
      endX=endX+movex; endY=endY+movex;
      posx=beginX; posy=beginY;
    }
    public void set(int x, int y) {
      move(x-beginX, y-beginY);
    }
  }
  private class Msg extends Item {
    String msg;
    int baseLine;
    Font font;
    public Msg(int x, int y, String message, int fontSize) {
      super();posx=x; posy=y;msg=message;text=message;
      font= new Font(null,0,fontSize);
      baseLine=(int)(0.8*fontSize);  // このベースラインの計算は手抜き
    }
    public void setText(String t) {
      super.setText(t);
      msg=t;
    }
    public void draw(Graphics g) {
      g.setFont(font);
      g.drawString(msg, posx,posy+baseLine);
    }
  }
  private class Rect extends Item {
    int w, h;
    boolean fill = false;
    public Rect(int x, int y, int width, int height, boolean isFill) {
      super();posx=x; posy=y; w=width; h=height;fill=isFill;
    }
    public void draw(Graphics g) {
      if (fill) {g.fillRect(posx,posy,w,h);}else{g.drawRect(posx,posy,w,h);}
    }
  }
  private class Oval extends Item {
    int w, h;
    boolean fill = false;
    public Oval(int x, int y, int width, int height, boolean isFill) {
      super();posx=x; posy=y; w=width; h=height;fill=isFill;
    }
    public void draw(Graphics g) {
      if (fill) {g.fillOval(posx,posy,w,h);}else{g.drawOval(posx,posy,w,h);}
    }
  }
  private class Img extends Item {
    Image img;
    public Img(int x, int y, String filename) {
      super();posx=x; posy=y;
      img=(new ImageIcon(filename)).getImage();
    }
    public void draw(Graphics g) {
      if (img!=null) {
        g.drawImage(img,posx,posy,targetCanvas);
      }
    }
  }
  private class Button extends Item {
    int w, h;
    Font font;
    int baseLine;
    String message;
    public Button(int x, int y, int width, int height, String msg) {
      super();posx=x; posy=y; w=width; h=height;
      font= new Font(null,0,height-4);
      baseLine=(int)(0.9*height);  // このベースラインの計算は手抜き
      message=msg;text=message;
      isButton=true;
    }
    public String getMsg() {return message;}
    public void setText(String t) {
      super.setText(t);
      message=t;
    }
    public boolean isPointed(Point p) {
      if ((p.x>posx)&&(p.x<(posx+w))&&(p.y>posy)&&(p.y<(posy+h))) {
        return true;
      } else {
        return false;
      }
    }
    public void draw(Graphics g) {
      g.fillRect(posx,posy,w,h);
      if (active==true) {
        g.setColor(Color.BLUE);
      } else {
        g.setColor(Color.BLACK);
      }
      g.drawRect(posx,posy,w,h);
      g.setFont(font);
      g.drawString(message, posx,posy+baseLine);
    }
  }
  
  private class KeyChecker extends KeyAdapter {
    public void keyPressed(KeyEvent e) {
      pressedKey=e.getKeyChar();
      if (storedKey==NULLKEY) {storedKey=e.getKeyChar();}
    }
    public void keyReleased(KeyEvent e) {
      pressedKey=NULLKEY;
    }
    
  }
  private class MouseChecker extends MouseAdapter {
    public void mouseClick(MouseEvent e) {
      pressedMouse=0;
      if (e.getButton()==MouseEvent.BUTTON1) {
        pressedMouse=1;
      }
      if (e.getButton()==MouseEvent.BUTTON3) {
        pressedMouse=pressedMouse+2;
      }
      if (e.getButton()==MouseEvent.BUTTON2) {
        pressedMouse=pressedMouse+4;
      }
    }
    public void mousePressed(MouseEvent e) {
      pressedMouse=0;
      if (e.getButton()==MouseEvent.BUTTON1) {
        pressedMouse=1;
      }
      if (e.getButton()==MouseEvent.BUTTON3) {
        pressedMouse=pressedMouse+2;
      }
      if (e.getButton()==MouseEvent.BUTTON2) {
        pressedMouse=pressedMouse+4;
      }
    }
    public void mouseReleased(MouseEvent e) {
      pressedMouse=0;
    }
  }


  /** 動作のデモを起動する． 
   ** @param args コマンドライン引数
   * **/
  public static void main(String[] args) {
    ICanvas ic = new ICanvas();
    JFrame f=ic.post(600,480);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    ic.setColor("BLUE");
    int i=ic.addMsg(160,60,"ICanvas デモ",40);
    int startmsg=ic.addMsg(300,380,"キーを押すとスタートします．",10);
    ic.setColor("RED");
    ic.waitWhenKeyIn();
    ic.delete(startmsg);
    int ovalID=ic.addFillOval(100,200,30,30);
    int counter=0;
    ic.setColor("BLUE");
    ic.addMsg(200,380,"左に移動しているときだけ u と d のキーで上下に動きます",10);
    while (true) {
      while(counter<50) {
        counter=counter+1;
        ic.move(ovalID,6,0);
        ic.wait(50);
      }
      ic.setBgColor("YELLOW");
      if (ic.getPressedKey()==' ') {ic.setBgColor("WHITE");}
      while(counter>0) {
        counter=counter-1;
        ic.move(ovalID,-6,0);
        char key=ic.waitWithKey(50);
        if (key=='u') {ic.move(ovalID,0,-3);}
        if (key=='d') {ic.move(ovalID,0,3);}
      }
      ic.setBgColor("PINK");
      if (ic.getPressedKey()==' ') {ic.setBgColor("WHITE");}
    }
  }

}

