package test.music1;

import di.container.annotations.Named;

@Named("classicalMusic")
public class ClassicalMusic implements Music{
    @Override
    public String getMusic() {
        return "Classical music";
    }
}
