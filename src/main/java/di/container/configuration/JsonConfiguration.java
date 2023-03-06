package di.container.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import di.container.beans.Bean;
import di.container.beans.BeanDefinition;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JsonConfiguration implements Configuration {
    private final String configurationPath;
    private final ObjectMapper mapper;

    public JsonConfiguration(String configurationPath) {
        this.configurationPath = configurationPath;
        mapper = new ObjectMapper();
    }

    @Override
    public List<BeanDefinition> getBeanDefinitions() throws IOException {
        return deserialize();
    }

    private List<BeanDefinition> deserialize() throws IOException {
        Bean[] beans = mapper.readValue(new File(configurationPath), Bean[].class);

        return Arrays.stream(beans).map(bean -> (BeanDefinition) bean).toList();
    }
}
