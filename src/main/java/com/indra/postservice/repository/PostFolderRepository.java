package com.indra.postservice.repository;

import com.indra.postservice.domain.PostFolderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostFolderRepository extends JpaRepository<PostFolderEntity, Long>  {

    public Page<PostFolderEntity> findAll(Pageable pageable);

    PostFolderEntity findById(long id);

}
