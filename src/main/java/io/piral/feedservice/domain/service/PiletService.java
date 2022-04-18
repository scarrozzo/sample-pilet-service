package io.piral.feedservice.domain.service;

import io.piral.feedservice.command.CreatePiletCommand;
import io.piral.feedservice.domain.dto.PostPiletDto;
import io.piral.feedservice.domain.model.File;
import io.piral.feedservice.domain.model.Pilet;
import io.piral.feedservice.domain.repository.PiletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Set;

import static io.piral.feedservice.config.CacheConfig.PILET_FILE_CACHE_NAME;

@Slf4j
@Service
public class PiletService {

    @Autowired
    private BeanFactory beanFactory;

    @Autowired
    private PiletRepository piletRepository;

    public Set<Pilet> getPilets(String appshell){
        return piletRepository.findAllByAppshell(appshell)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Pilet getPilet(String appshell, String piletName, String piletVersion){
        return piletRepository.findByAppshellAndNameAndPackageVersion(appshell, piletName, piletVersion)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Pilet savePilet(Pilet pilet){
        return piletRepository.save(pilet);
    }

    public void deletePilet(Pilet pilet, boolean flush){
        piletRepository.delete(pilet);
        if(flush) {
            piletRepository.flush();
        }
    }

    @Cacheable(value = PILET_FILE_CACHE_NAME, key = "#appshell+#piletName+#piletVersion+#fileName")
    public File getPiletFile(String appshell, String piletName, String piletVersion, String fileName){
        Pilet pilet = getPilet(appshell, piletName, piletVersion);
        return pilet.getFiles().stream().filter(file -> file.getFileName().equals(fileName)).findFirst()
                .orElseThrow(EntityNotFoundException::new);
    }

    public Pilet createPilet(PostPiletDto postPiletDto, String appshell, boolean forceUpload) {
        CreatePiletCommand command = beanFactory.getBean(CreatePiletCommand.class, postPiletDto, appshell, forceUpload);
        return command.execute();
    }
}
