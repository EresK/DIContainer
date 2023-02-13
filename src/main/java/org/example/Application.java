package org.example;

import org.example.client.NewYearOrganizer;
import org.example.context.ApplicationContext;
import org.example.factory.BeanFactory;

public class Application {

    public ApplicationContext run() {
        ApplicationContext applicationContext = new ApplicationContext();
        BeanFactory beanFactory = new BeanFactory(applicationContext);
        applicationContext.setBeanFactory(beanFactory);

        return applicationContext;
    }

    public static void main(String[] args) {
        Application application = new Application();
        ApplicationContext context = application.run();

        NewYearOrganizer organizer = context.getBean(NewYearOrganizer.class);
        organizer.prepareToCelebration();
    }
}
