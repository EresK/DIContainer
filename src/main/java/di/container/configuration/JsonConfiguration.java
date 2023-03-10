package di.container.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
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
        // TODO: check that if there is singleton bean of some class
        //  there are no another prototype or thread beans of such class
        // TODO: add cyclic dependencies detector
        BeanDefinition[] beans = mapper.readValue(new File(configurationPath), BeanDefinition[].class);
        return Arrays.stream(beans).toList();
    }
}
