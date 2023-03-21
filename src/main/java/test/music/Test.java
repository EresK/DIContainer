package test.music;

import di.container.context.AnnotationApplicationContext;

public class Test {

    public static void main(String[] argc) throws Exception {
        AnnotationApplicationContext applicationContext = new AnnotationApplicationContext("test.music");
        Player player = applicationContext.getBean(Player.class);
        System.out.println(player.getMusic().getMusic());
    }
}
