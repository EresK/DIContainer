package test.music1;

import di.container.annotations.Inject;
import di.container.annotations.Named;

public class Player {

    private Music music;

    @Inject
    public Player(@Named("classicalMusic") Music music) {
        this.music = music;
    }

    public Music getMusic() {
        return music;
    }
}
