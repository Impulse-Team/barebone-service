package es.fujitsu.barebone.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class ModelMapperConfig {

	@Bean
	public ModelMapper mapper() {

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);

		return modelMapper;
	}

	private void logMapping(Class<?> source, Class<?> destination) {
		log.trace("Mapping {} to {}", source.getCanonicalName(), destination.getCanonicalName());
	}

}
