package com.quicktransfer.fundstransfer.util;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A utility class for handling JSON operations.
 * <p>
 * This class provides a single instance of {@link ObjectMapper}, which is used for JSON serialization
 * and deserialization. The class is designed as a utility with a private constructor to prevent
 * instantiation, and it provides a static method to access the shared {@link ObjectMapper} instance.
 * </p>
 * <p>
 * The class is not intended to be instantiated or extended. It is used solely to provide a
 * centralized {@link ObjectMapper} instance that can be reused throughout the application.
 * </p>
 */
public class JsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    // Private constructor to prevent instantiation.
    private JsonUtil() {
    }

    /**
     * Returns the shared {@link ObjectMapper} instance for JSON processing.
     * <p>
     * This method provides access to the singleton instance of {@link ObjectMapper} used by this class.
     * The {@link ObjectMapper} instance can be used for serializing Java objects to JSON and deserializing
     * JSON to Java objects.
     * </p>
     *
     * @return the shared {@link ObjectMapper} instance.
     */
    public static ObjectMapper getMapper() {
        return mapper;
    }


}
