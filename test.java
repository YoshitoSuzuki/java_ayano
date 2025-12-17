public class test {
    public static void main(String[] args) {
        ICanvas c = new ICanvas();
        c.post(600, 400);
        
        char key;
        key = c.waitWhenKeyIn();

        if (key == 'u') {
            System.out.println("you entered u");
        }
    }
}