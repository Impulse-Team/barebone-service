package es.fujitsu.barebone.utils;

import org.springframework.web.client.RestTemplate;

public class RestTemplateUtil {

	private RestTemplateUtil() {
		throw new IllegalStateException("Utility class");
	}

	public static RestTemplate getInstance() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new WxMappingJackson2HttpMessageConverter());
		return restTemplate;
	}
}
