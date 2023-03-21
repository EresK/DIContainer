package test.music;

import di.container.annotations.Named;

@Named("rockMusic")
public class RockMusic implements Music{
    @Override
    public String getMusic() {
        return "Rock music";
    }
}
