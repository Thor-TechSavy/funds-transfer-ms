//package com.quicktransfer.microservice.fundstransfer.util;
//
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.google.common.io.CharStreams;
//import com.oyesk.microservice.faq.config.ErrorBody;
//import com.oyesk.microservice.faq.exception.FeignOyeskException;
//import feign.Response;
//import feign.codec.ErrorDecoder;
//import org.apache.commons.lang3.StringUtils;
//
//import java.io.IOException;
//import java.io.Reader;
//import java.nio.charset.StandardCharsets;
//
//
//public class FeignErrorDecoder implements ErrorDecoder {
//
//	public static final ErrorDecoder DEFAULT_ERROR_DECODER = new Default();
//	public static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());
//
//	static {
//		MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//	}
//
//
//	@Override
//	public Exception decode(String s, Response response) {
//		if (response != null && response.body() != null && response.status() >= 400 && response.status() <= 599) {
//
//			try (Reader reader = response.body().asReader(StandardCharsets.UTF_8)) {
//				String result = CharStreams.toString(reader);
//			//	log.error("Error Response: {}", result);
//				ErrorBody errorBody = MAPPER.readValue(result, ErrorBody.class);
//				if (errorBody.getMessage() != null) {
//					String message = StringUtils.isNotBlank(response.reason()) ? response.reason() :
//							errorBody.getMessage();
//					return new FeignOyeskException(422, message, errorBody);
//				}
//
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		return DEFAULT_ERROR_DECODER.decode(s, response);
//	}
//}
