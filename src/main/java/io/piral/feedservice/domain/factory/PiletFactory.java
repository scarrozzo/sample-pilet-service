package io.piral.feedservice.domain.factory;

import io.piral.feedservice.domain.dto.PostPiletDto;
import io.piral.feedservice.domain.model.Pilet;
import io.piral.feedservice.exception.ParsePackageException;
import io.piral.feedservice.exception.ServiceError;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class PiletFactory extends BaseFactory {

    @Autowired
    private PiletDtoMapper piletMapper;

    public Pilet createPilet(PostPiletDto postPiletDto, String appshell){
        Pilet entity = new Pilet();

        entity = piletMapper.map(entity, extractPackageContent(postPiletDto), appshell);

        entity.setUid(generateEntityUid());

        return entity;
    }

    @SneakyThrows
    protected Map<String, String> extractPackageContent(PostPiletDto dto) {
        TarArchiveInputStream packageFile = null;
        try {
            Map<String, String> packageInfo = new HashMap<>();

            packageFile = new TarArchiveInputStream(new GzipCompressorInputStream(new ByteArrayInputStream(dto.getFile().getBytes())));
            TarArchiveEntry currentEntry = packageFile.getNextTarEntry();
            BufferedReader br;
            while (currentEntry != null) {
                br = new BufferedReader(new InputStreamReader(packageFile));
                log.debug("For File = {}", currentEntry.getName());

                StringBuilder content = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    log.debug("line={}", line);
                    content.append(line);
                    content.append("\r\n");
                }

                packageInfo.put(currentEntry.getName(), content.toString());
                currentEntry = packageFile.getNextTarEntry();
            }

            return packageInfo;
        } catch (IOException ex) {
            log.error("Error while trying to extract package files", ex);
            throw new ParsePackageException(ServiceError.E0001.getMessage(), ex);
        } finally {
            if (packageFile != null) {
                packageFile.close();
            }
        }
    }

}
