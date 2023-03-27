package test.music5;

import di.container.annotations.Named;

@Named
public class PopMusic implements Music {
    @Override
    public void getGenre() {
        System.out.println("Pop music " + this.hashCode());
    }
}
