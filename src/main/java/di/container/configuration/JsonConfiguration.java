package di.container.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import di.container.beans.BeanDefinition;
import di.container.beans.ListableBean;
import di.container.configuration.value.ConstructorValue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class JsonConfiguration implements Configuration {
    private final String schemaUriPath = "src/main/resources/schema.json";
    private final String configurationPath;
    private final ObjectMapper mapper;

    public JsonConfiguration(String configurationPath) {
        this.configurationPath = configurationPath;
        mapper = new ObjectMapper();
    }

    @Override
    public List<BeanDefinition> getBeanDefinitions() throws Exception {
        // TODO: check that if there is singleton bean of some class
        //  there are no another prototype or thread beans of such class

        File configuration = new File(configurationPath);

        validate(configuration);

        BeanDefinition[] beans = mapper.readValue(configuration, BeanDefinition[].class);

        /* checking correctness */
        checkCyclicDependencies(beans);
        checkParameterCorrectness(beans);

        return Arrays.stream(beans).toList();
    }

    private void validate(File configuration) throws IOException {
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);

        JsonSchema schema = factory.getSchema(new File(schemaUriPath).toURI());
        JsonNode node = mapper.readTree(configuration);
        Set<ValidationMessage> errors = schema.validate(node);

        if (errors.size() > 0)
            throw new IllegalArgumentException(String.format(
                    "Configuration file: %s is not match to schema", configurationPath));
    }

    private void checkCyclicDependencies(BeanDefinition[] beans) throws Exception {
        ListableBean listableBean = new ListableBean(Arrays.stream(beans).toList());

        for (BeanDefinition bean : beans) {
            List<ConstructorValue> referenceArguments = bean.getConstructorArguments().stream()
                    .filter(ConstructorValue::isBeanReference)
                    .toList();

            /* Для каждого непримитивного аргумента конструктора бина проверяем,
            что этот аргумент повторно не ссылается на текущий бин как на аргумент своего конструктора */
            for (ConstructorValue value : referenceArguments) {
                BeanDefinition referenceBean = listableBean.getBeanByName(value.getBeanReference());

                long cyclicReferences = referenceBean.getConstructorArguments().stream()
                        .filter(ConstructorValue::isBeanReference)
                        .filter(constructorValue -> bean.getBeanName().equals(constructorValue.getBeanReference()))
                        .count();

                if (cyclicReferences > 0)
                    throw new IllegalStateException(String.format(
                            "Met incorrect cycle in configuration: %s, with bean: %s",
                            configurationPath, bean.getBeanName()));
            }
        }
    }

    private void checkParameterCorrectness(BeanDefinition[] beans) {
        for (BeanDefinition bean : beans) {
            if (bean.getConstructorArguments().stream().anyMatch(arg -> !arg.isCorrect()) ||
                    bean.getPropertyArguments().stream().anyMatch(arg -> !arg.isCorrect()))
                throw new IllegalStateException(String.format(
                        "Bean: %s has incorrect state of the argument", bean.getBeanName()));
        }
    }
}
