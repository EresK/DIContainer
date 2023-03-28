package testAnnotations.music3;

import di.container.annotations.Named;

@Named("pop")
public class PopMusic implements Music {
    @Override
    public String getGenre() {
        return "Pop music";
    }
}
