package io.piral.feedservice.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class JsonUtils {

    private static final ObjectMapper defaultMapper = new ObjectMapper();

    public static final String DATE_FORMAT = "EEE, d MMM yyyy HH:mm:ss z";

    static {
        defaultMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        defaultMapper.setDateFormat(new SimpleDateFormat(DATE_FORMAT));

        defaultMapper.registerModule(new Jdk8Module());
        defaultMapper.registerModule(new JavaTimeModule());

        // null are not serialized
        defaultMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static ObjectMapper getObjectMapper() {
        return defaultMapper;
    }

    public static <T> T toObject(String content, Class<T> tClass) throws JsonProcessingException {
        return defaultMapper.readValue(content, tClass);
    }

    public static String toJson(Object object) throws JsonProcessingException {
        return defaultMapper.writeValueAsString(object);
    }

    public static String toPrettyJson(Object object) throws JsonProcessingException {
        return defaultMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }

    public static String serializeStringHashMap(HashMap<String, String> object) throws JsonProcessingException {
        return defaultMapper.writeValueAsString(object);
    }

    public static String serializeStringArrayListValuedStringHashMap(HashMap<String, ArrayList<String>> object) throws JsonProcessingException {
        return defaultMapper.writeValueAsString(object);
    }

    public static HashMap<String, String> deserializeStringHashMap(String serializedObject) throws JsonProcessingException {
        return defaultMapper.readValue(serializedObject, HashMap.class);
    }

    public static HashMap<String, ArrayList<String>> deserializeStringArrayListValuedStringHashMap(String serializedObject) throws JsonProcessingException {
        return defaultMapper.readValue(serializedObject, HashMap.class);
    }

    public static JsonNode readJsonTree(String jsonString) throws JsonProcessingException {
        return defaultMapper.readTree(jsonString);
    }

    public static boolean isValidJson(String test) {
        try {
            defaultMapper.readTree(test);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }
}