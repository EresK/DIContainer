package test.music;

import di.container.annotations.Inject;

public class Player {
    @Inject
    private Music music;

    public Music getMusic() {
        return music;
    }
}
