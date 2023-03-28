package testAnnotations.music5;

public class PopMusic implements Music {
    @Override
    public void getGenre() {
        System.out.println("Pop music");
    }
}
