package testAnnotations.music5;

public class ClassicalMusic implements Music {
    @Override
    public void getGenre() {
        System.out.println("Classical music");
    }
}
