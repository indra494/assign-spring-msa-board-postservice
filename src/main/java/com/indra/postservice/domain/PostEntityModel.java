package com.indra.postservice.domain;

import com.indra.postservice.controller.PostController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class PostEntityModel extends EntityModel<PostEntity> {
    public PostEntityModel(PostEntity postEntity, Iterable<Link>... links){
        super(postEntity);
        add(linkTo(PostController.class).slash(postEntity.getId()).withSelfRel());
    }
}