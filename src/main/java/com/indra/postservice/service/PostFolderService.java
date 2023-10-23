package com.indra.postservice.service;

import com.indra.postservice.domain.PostFolderDto;
import com.indra.postservice.domain.PostFolderEntity;
import com.indra.postservice.exception.BasicException;
import com.indra.postservice.repository.PostFolderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PostFolderService {

    private final MessageSource messageSource;
    private final ModelMapper modelMapper;
    private final PostFolderRepository postFolderRepository;


    public Page<PostFolderEntity> getFolderList(Pageable pageable) {
        return postFolderRepository.findAll(pageable);
    }

    public PostFolderEntity createFolder(PostFolderDto postFolder) {
        PostFolderEntity postFolderEntity = modelMapper.map(postFolder, PostFolderEntity.class);
        postFolderEntity.setModiId(postFolderEntity.getRegId());
        return postFolderRepository.save(postFolderEntity);
    }

    public PostFolderEntity modiFolder(PostFolderDto postFolder) {
        PostFolderEntity postFolderEntity = postFolderRepository.findById(postFolder.getId());

        if(postFolderEntity == null) {
            Locale locale = LocaleContextHolder.getLocale();
            throw new BasicException(messageSource.getMessage("folder.error.modify_folder",null,locale));
        }

        postFolderEntity.setName(postFolder.getName());
        postFolderEntity.setModiId(postFolder.getRegId());
        return postFolderEntity;
    }



}
