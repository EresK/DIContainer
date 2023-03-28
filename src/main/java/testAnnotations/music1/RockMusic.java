package testAnnotations.music1;

import di.container.annotations.Named;

@Named("rock")
public class RockMusic implements Music{
    @Override
    public String getMusic() {
        return "Rock music";
    }
}
