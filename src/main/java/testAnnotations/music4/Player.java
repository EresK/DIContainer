package testAnnotations.music4;

import di.container.annotations.Inject;

public class Player {
    private Music music;
    @Inject
    public void setMusic(Music music) {
        this.music = music;
    }

    public Music getMusic() {
        return music;
    }
}
