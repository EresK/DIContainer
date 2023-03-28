package testAnnotations.music1;

import di.container.annotations.Named;

@Named("classical")
public class ClassicalMusic implements Music{
    @Override
    public String getMusic() {
        return "Classical music";
    }
}
