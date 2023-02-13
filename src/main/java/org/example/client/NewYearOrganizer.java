package org.example.client;

import org.example.annotation.Inject;
import org.example.service.GiftPresenter;

public class NewYearOrganizer {
    @Inject
    private GiftPresenter giftPresenter;

    public void prepareToCelebration() {
        String person = "Ivan Ivanov";
        giftPresenter.present(person);
    }
}
