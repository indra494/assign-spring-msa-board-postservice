package com.indra.postservice.service;

import com.indra.postservice.domain.PostDto;
import com.indra.postservice.domain.PostEntity;
import com.indra.postservice.domain.PostFolderEntity;
import com.indra.postservice.exception.BasicException;
import com.indra.postservice.repository.PostFolderRepository;
import com.indra.postservice.repository.PostRepository;
import com.indra.postservice.utils.ObjectUtil;
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
public class PostService {

    private final MessageSource messageSource;
    private final ModelMapper modelMapper;
    private final PostRepository postRepository;
    private final PostFolderRepository postFolderRepository;

    public Page<PostEntity> getPostList(long companyId, Pageable pageable) {
        return postRepository.findAllByCompanyId(companyId, pageable);
    }

    public PostEntity createPost(PostDto post) {

        PostFolderEntity folderEntity = postFolderRepository.findById(post.getFolderId());
        if(folderEntity==null) {
            Locale locale = LocaleContextHolder.getLocale();
            throw new BasicException(messageSource.getMessage("folder.error.modify_folder", null, locale));
        }

        PostEntity postEntity = modelMapper.map(post, PostEntity.class);
        postEntity.setPostFolder(folderEntity);
        postEntity.setModiId(postEntity.getRegId());
        return postRepository.save(postEntity);
    }

    public PostEntity modiPost(PostDto post) {
        PostEntity postEntity = postRepository.findById(post.getId());

        if(postEntity == null) {
            Locale locale = LocaleContextHolder.getLocale();
            throw new BasicException(messageSource.getMessage("post.error.modify_post",null,locale));
        }

        postEntity.setTitle(ObjectUtil.nvl(post.getTitle(),postEntity.getTitle()).toString());
        postEntity.setContents(ObjectUtil.nvl(post.getContents(),postEntity.getContents()).toString());
        postEntity.setModiId(post.getRegId());
        return postEntity;
    }

    public PostEntity modiPostFolder(PostDto post) {

        PostEntity postEntity = postRepository.findById(post.getId());
        if(postEntity == null) {
            Locale locale = LocaleContextHolder.getLocale();
            throw new BasicException(messageSource.getMessage("post.error.modify_post",null,locale));
        }

        PostFolderEntity folderEntity = postFolderRepository.findById(post.getFolderId());
        if(folderEntity==null) {
            Locale locale = LocaleContextHolder.getLocale();
            throw new BasicException(messageSource.getMessage("folder.error.modify_folder", null, locale));
        }

        postEntity.setPostFolder(folderEntity);
        postEntity.setModiId(post.getRegId());
        return postEntity;
    }

    public void deletePost(PostDto post){
        //PostEntity postEntity = modelMapper.map(post, PostEntity.class);
        postRepository.deleteById(post.getId());
    }


}
