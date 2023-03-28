package testAnnotations.music2;

import di.container.annotations.Inject;
import di.container.annotations.Provider;

public class Player {
    @Inject
    private Provider<ClassicalMusic> musicProvider;

    public void getMusic() throws Exception {
        musicProvider.get().getGenre();
        musicProvider.get().getGenre();
    }
}
