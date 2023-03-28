package testAnnotations.music4;

import di.container.context.AnnotationApplicationContext;

public class Main {
    public static void main(String[] argc) {
        AnnotationApplicationContext context = new AnnotationApplicationContext("test.music4");
        try {
            Player player = context.getBean(Player.class);
            System.out.print(player.getMusic().getGenre());
        } catch (Exception e) {
            System.out.println("Several realization of Music interface without Named annotation");
            System.out.println(e);
        }
    }
}
