package testAnnotations.music2;

import di.container.annotations.Named;
@Named
public class ClassicalMusic implements Music {
    @Override
    public void getGenre() {
        System.out.println("Classical music");
    }
}
