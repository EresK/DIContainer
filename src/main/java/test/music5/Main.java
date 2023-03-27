package test.music5;

import di.container.context.AnnotationApplicationContext;

public class Main {
    public static void main(String[] argc) throws Exception {
        AnnotationApplicationContext context = new AnnotationApplicationContext("test.music2");
        Player player = context.getBean(Player.class);
        player.getMusic();
    }
}
