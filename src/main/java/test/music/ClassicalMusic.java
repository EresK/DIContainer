package test.music;

import di.container.annotations.Named;

@Named("classicalMusic")
public class ClassicalMusic implements Music{
    @Override
    public String getMusic() {
        return "Classical music";
    }
}
