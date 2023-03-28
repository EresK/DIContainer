package testAnnotations.music5;

import di.container.context.AnnotationApplicationContext;

public class Main {
    public static void main(String[] argc) throws Exception {
        AnnotationApplicationContext context = new AnnotationApplicationContext("test.music5");
        Player player = context.getBean(Player.class);
        Music instanceMusic1 = player.getMusic();
        Music instanceMusic2 = player.getMusic();
        System.out.println(instanceMusic1.hashCode() == instanceMusic2.hashCode());
    }
}
