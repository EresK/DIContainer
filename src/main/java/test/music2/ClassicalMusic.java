package test.music2;

import di.container.annotations.Named;
@Named
public class ClassicalMusic implements Music {
    @Override
    public String getGenre() {
        return "Classical music";
    }
}
