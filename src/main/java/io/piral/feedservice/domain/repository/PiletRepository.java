package io.piral.feedservice.domain.repository;

import io.piral.feedservice.domain.model.Pilet;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PiletRepository extends BaseRepository<Pilet, Long> {
    Optional<Set<Pilet>> findAllByAppshellOrderByLastModifiedDateDescLastModifiedTimeDesc(String appshell);
    Optional<Pilet> findByAppshellAndNameAndPackageVersion(String appshell, String name, String packageVersion);
    boolean existsByAppshellAndNameAndPackageVersion(String appshell, String name, String packageVersion);
}
