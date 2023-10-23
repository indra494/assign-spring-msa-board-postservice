package com.indra.postservice.repository;

import com.indra.postservice.domain.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
    public Page<PostEntity> findAllByCompanyId(long companyId, Pageable pageable);

    PostEntity findById(long id);
}
