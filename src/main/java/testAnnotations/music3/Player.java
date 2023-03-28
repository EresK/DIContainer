package testAnnotations.music3;
import di.container.annotations.Inject;
import di.container.annotations.Named;

public class Player {
    private Music music;

    @Inject
    public void setMusic(@Named("pop") Music music) {
        this.music = music;
    }

    public Music getMusic() {
        return music;
    }
}
