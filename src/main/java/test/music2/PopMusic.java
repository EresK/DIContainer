package test.music2;

import di.container.annotations.Named;

@Named
public class PopMusic implements Music{
    @Override
    public String getGenre() {
        return "Pop music";
    }
}
