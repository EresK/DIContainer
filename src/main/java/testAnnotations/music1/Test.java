package testAnnotations.music1;

import di.container.context.AnnotationApplicationContext;

public class Test {

    public static void main(String[] argc) throws Exception {
        AnnotationApplicationContext applicationContext = new AnnotationApplicationContext("test.music1");
        Player player = applicationContext.getBean(Player.class);
        player.getGenreMusic();
    }
}
