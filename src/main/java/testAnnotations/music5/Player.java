package testAnnotations.music5;

import di.container.annotations.Inject;
import di.container.annotations.Provider;

public class Player {
    private Provider<PopMusic> musicProvider;
    @Inject
    public void setMusic(Provider<PopMusic> musicProvider) {
        this.musicProvider = musicProvider;
    }

    public void getMusic() throws Exception {
        musicProvider.get().getGenre();
        musicProvider.get().getGenre();
    }
}
