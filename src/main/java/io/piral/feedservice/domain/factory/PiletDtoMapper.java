package io.piral.feedservice.domain.factory;


import com.fasterxml.jackson.databind.JsonNode;
import io.piral.feedservice.domain.model.File;
import io.piral.feedservice.domain.model.Pilet;
import io.piral.feedservice.exception.ParsePackageException;
import io.piral.feedservice.exception.ServiceError;
import io.piral.feedservice.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class PiletDtoMapper {
    /**
     * We support only V2 pilet version for now
     */
    public static final String JS_FILE = "index.js";
    public static final String LICENSE_FILE_NAME = "LICENSE";
    public static final String PACKAGE_FILE_NAME = "package.json";
    public static final String PACKAGE_JSON_FIELD_NAME = "name";
    public static final String PACKAGE_JSON_FIELD_VERSION = "version";
    public static final String PACKAGE_JSON_FIELD_DESCRIPTION = "description";
    public static final String PACKAGE_JSON_FIELD_AUTHO = "author";
    public static final String PACKAGE_JSON_FIELD_LICENSE = "license";
    public static final String PACKAGE_JSON_DEFAULT_VALUE_LICENSE = "ISC";
    public static final String DEFAULT_VERSION = "v2";
    public static final String VERSION2_REGEX = "^\\/\\/\\s*@pilet\\s+v:2\\s*\\(([A-Za-z0-9\\_\\:\\-]+)\\s*,\\s*(.*)\\)";

    public Pilet map(Pilet entity, Map<String, String> fileNamesAndContent, String appshell) {
        try {
            String indexFileKey = fileNamesAndContent.keySet()
                    .stream()
                    .filter(files -> files.contains(JS_FILE))
                    .findFirst()
                    .orElseThrow(() -> new ParsePackageException("Cannot read index js file"));

            Optional<String> indexLicenseKey = fileNamesAndContent.keySet()
                    .stream()
                    .filter(files -> files.contains(LICENSE_FILE_NAME))
                    .findFirst();

            String packageJsonFileName = fileNamesAndContent.keySet()
                    .stream()
                    .filter(files -> files.contains(PACKAGE_FILE_NAME))
                    .findFirst()
                    .orElseThrow(() -> new ParsePackageException("Cannot read package json file"));
            JsonNode packageJson = JsonUtils.readJsonTree(fileNamesAndContent.get(packageJsonFileName));

            entity.setAppshell(appshell);
            entity.setName(packageJson.has(PACKAGE_JSON_FIELD_NAME) ? packageJson.get(PACKAGE_JSON_FIELD_NAME).asText() : StringUtils.EMPTY);
            entity.setPackageVersion(packageJson.has(PACKAGE_JSON_FIELD_VERSION) ? packageJson.get(PACKAGE_JSON_FIELD_VERSION).asText() : StringUtils.EMPTY);
            entity.setDescription(packageJson.has(PACKAGE_JSON_FIELD_DESCRIPTION) ? packageJson.get(PACKAGE_JSON_FIELD_DESCRIPTION).asText() : StringUtils.EMPTY);
            entity.setAuthor(packageJson.has(PACKAGE_JSON_FIELD_AUTHO) ? packageJson.get(PACKAGE_JSON_FIELD_AUTHO).asText() : StringUtils.EMPTY);
            entity.setLicenseType(packageJson.has(PACKAGE_JSON_FIELD_LICENSE) ? packageJson.get(PACKAGE_JSON_FIELD_LICENSE).asText() : PACKAGE_JSON_DEFAULT_VALUE_LICENSE);
            entity.setLicenseText(indexLicenseKey.isPresent() ? fileNamesAndContent.get(indexLicenseKey) : StringUtils.EMPTY);

            List<File> files = new ArrayList<>();
            for (String key : fileNamesAndContent.keySet()) {
                files.add(File.builder()
                        .fileName(key.substring(key.lastIndexOf('/') + 1))
                        .fileContent(fileNamesAndContent.get(key))
                        .build());
            }
            entity.setFiles(files);
            entity.setType(DEFAULT_VERSION);

            Pattern versionPattern = Pattern.compile(VERSION2_REGEX);
            Matcher versionMatcher = versionPattern.matcher(fileNamesAndContent.get(indexFileKey));
            if (versionMatcher.find()) {
                entity.setRequireRef(versionMatcher.group(1));
                entity.setDependencies(versionMatcher.group(2).substring(0, versionMatcher.group(2).indexOf("})") + 1));
            }

            MessageDigest digester = MessageDigest.getInstance("SHA-256");
            log.debug(fileNamesAndContent.get(packageJsonFileName));
            digester.update(fileNamesAndContent.get(packageJsonFileName).getBytes(StandardCharsets.UTF_8));
            entity.setIntegrity("sha256-" + Base64.getEncoder().encodeToString(digester.digest()));

            log.debug("Pilet entity: {}", entity);

            return entity;
        } catch (Exception ex) {
            log.error("Error while trying to build entity from dto", ex);
            throw new ParsePackageException(ServiceError.E0001.getMessage());
        }
    }
}
