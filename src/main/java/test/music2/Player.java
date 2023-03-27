package test.music2;

import di.container.annotations.Inject;
import di.container.annotations.Provider;

public class Player {
    @Inject
    private Provider<PopMusic> musicProvider;

    public void getMusic() throws Exception {
        musicProvider.get().getGenre();
        musicProvider.get().getGenre();
    }
}
