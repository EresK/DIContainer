package testAnnotations.music1;

import di.container.annotations.Inject;
import di.container.annotations.Named;

public class Player {

    private final Music music;

    @Inject
    public Player(@Named("classical") Music music) {
        this.music = music;
    }

    public void getGenreMusic() {
        System.out.println(music.getMusic());
    }
}
