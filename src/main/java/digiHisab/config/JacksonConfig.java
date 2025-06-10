package digiHisab.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion( com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL );

        SimpleFilterProvider filters = new SimpleFilterProvider()
                .addFilter(
                        "customFilter",
                        SimpleBeanPropertyFilter.serializeAllExcept( "nullProperty" )
                );
        mapper.setFilterProvider( filters );

        return mapper;
    }
}
