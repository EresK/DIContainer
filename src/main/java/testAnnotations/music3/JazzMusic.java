package testAnnotations.music3;

import di.container.annotations.Named;

@Named("jazz")
public class JazzMusic implements Music {
    @Override
    public String getGenre() {
        return "Jazz music";
    }
}
