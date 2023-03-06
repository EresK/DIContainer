package di.container.beans;

import lombok.Getter;

import java.util.List;

@Getter
public class ListableBean {
    private final List<BeanDefinition> beanList;

    public ListableBean (List<BeanDefinition> beanList) {
        this.beanList = beanList;
    }

    public BeanDefinition getBeanByName(String name) throws Exception {
        List<BeanDefinition> beans = beanList.stream().filter(bean -> bean.getBeanName().equals(name)).toList();

        if (beans.size() != 1)
            throw new Exception(String.format("There are %d beans with name: %s", beans.size(), name));

        return beans.stream().findFirst().get();
    }

    public BeanDefinition getBeanByType(Class<?> requiredType) throws Exception {
        List<BeanDefinition> beans = beanList.stream().filter(bean -> bean.getBeanClass() == requiredType).toList();

        if (beans.size() != 1)
            throw new Exception(String.format("There are %d beans of class: %s", beans.size(), requiredType));

        return beans.stream().findFirst().get();
    }
}
