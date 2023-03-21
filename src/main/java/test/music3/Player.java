package test.music3;

import di.container.annotations.Inject;

public class Player {
    private Music music;

    @Inject
    public void setMusic(PopMusic music) {
        this.music = music;
    }

    public Music getMusic() {
        return music;
    }
}
