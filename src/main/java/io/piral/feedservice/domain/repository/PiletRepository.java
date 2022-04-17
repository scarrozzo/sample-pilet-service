package io.piral.feedservice.domain.repository;

import io.piral.feedservice.domain.model.Pilet;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PiletRepository extends BaseRepository<Pilet, Long> {
    Set<Pilet> findAllByAppshell(String appshell);
    Pilet findByAppshellAndNameAndPackageVersion(String appshell, String name, String packageVersion);
}
