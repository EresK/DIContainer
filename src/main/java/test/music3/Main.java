package test.music3;

import di.container.context.AnnotationApplicationContext;
public class Main {
    public static void main(String[] argc) throws Exception {
        AnnotationApplicationContext context = new AnnotationApplicationContext("test.music3");
        Player player = context.getBean(Player.class);
        System.out.print(player.getMusic().getGenre());
    }
}
