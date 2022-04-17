package io.piral.feedservice.domain.repository;

import io.piral.feedservice.domain.model.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@NoRepositoryBean
public interface BaseRepository<E extends BaseEntity, ID extends Serializable> extends JpaRepository<E, ID> {
    boolean existsByUid(String uid);

    Optional<E> findByUid(String uid);

    List<E> findAllByUidIn(Set<String> uids);
}
