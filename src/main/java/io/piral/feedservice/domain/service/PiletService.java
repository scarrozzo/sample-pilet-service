package io.piral.feedservice.domain.service;

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

@Slf4j
@Service
public class PiletService {

    @Autowired
    private BeanFactory beanFactory;

    @Autowired
    private PiletRepository piletRepository;

    public Set<Pilet> getPilets(String appshell){
        return piletRepository.findAllByAppshell(appshell);
    }

    public Pilet getPilet(String appshell, String piletName, String piletVersion){
        return piletRepository.findByAppshellAndNameAndPackageVersion(appshell, piletName, piletVersion);
    }

    @Cacheable(value = "piletFileCache", key = "#appshell+#piletName+#piletVersion+#fileName")
    public File getPiletFile(String appshell, String piletName, String piletVersion, String fileName){
        Pilet pilet = getPilet(appshell, piletName, piletVersion);
        return pilet.getFiles().stream().filter(file -> file.getFileName().equals(fileName)).findFirst().orElseThrow(EntityNotFoundException::new);
    }
}
