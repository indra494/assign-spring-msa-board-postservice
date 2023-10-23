package com.indra.postservice.domain;

import com.indra.postservice.controller.PostFolderController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class PostFolderEntityModel extends EntityModel<PostFolderEntity> {
    public PostFolderEntityModel(PostFolderEntity postFolderEntity, Iterable<Link>... links){
        super(postFolderEntity);
        add(linkTo(PostFolderController.class).slash(postFolderEntity.getId()).withSelfRel());
    }
}