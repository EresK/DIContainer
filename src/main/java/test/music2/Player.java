package test.music2;

import di.container.annotations.Inject;
import di.container.annotations.Named;
import test.music2.Music;

public class Player {
    @Inject
    private @Named("classicalMusic") Music music;

    public Music getMusic() {
        return music;
    }
}
