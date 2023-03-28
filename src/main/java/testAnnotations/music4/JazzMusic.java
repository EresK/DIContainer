package testAnnotations.music4;

import di.container.annotations.Named;

@Named
public class JazzMusic implements Music{
    @Override
    public String getGenre() {
        return "JazzMusic";
    }
}
