package com.indra.postservice.controller;

import com.indra.postservice.domain.PostDto;
import com.indra.postservice.domain.PostEntity;
import com.indra.postservice.domain.PostEntityModel;
import com.indra.postservice.service.PostService;
import com.indra.postservice.support.BaseApiController;
import com.indra.postservice.support.JwtHeader;
import com.indra.postservice.support.ValidationObject;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Locale;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "posts", produces = MediaTypes.HAL_JSON_VALUE)
public class PostController extends BaseApiController {

    private final PostService postService;

    @GetMapping()
    public ResponseEntity getPostList(@PageableDefault(size = 10, sort = "regDate", direction = Sort.Direction.DESC) Pageable pageable,
                                      @RequestHeader(JwtHeader.companyId) long companyId,
                                      Locale locale) {

        Page<PostEntity> posts = postService.getPostList(companyId,pageable);

        PagedResourcesAssembler<PostEntity> assembler = new PagedResourcesAssembler<PostEntity>(null,null);
        RepresentationModelAssembler<PostEntity, PostEntityModel> postAssembler;
        postAssembler = e -> new PostEntityModel(e);
        var postEnitiyModels = assembler.toModel(posts, postAssembler);

        return makeDataMessageForSuccess (
                postEnitiyModels,
                locale
        );

    }

    @PostMapping()
    public ResponseEntity createPost(@RequestHeader(JwtHeader.id) long regId,
                                     @RequestHeader(JwtHeader.companyId) long companyId,
                                     @RequestBody @Valid PostDto post,
                                     BindingResult bindingResult,
                                     Locale locale) {


        ValidationObject validationObject = new ValidationObject();
        if(doFormValidation(bindingResult,validationObject)) {
            return makeDataMessageForValidation(validationObject, locale);
        }

        WebMvcLinkBuilder selfLink = linkTo(methodOn(PostController.class).createPost(regId, companyId, post, null, locale)).slash(post.getId());
        URI createUri = selfLink.toUri();

        post.setRegId(regId);
        post.setCompanyId(companyId);
        return makeDataMessageForCreated (
                createUri,
                postService.createPost(post),
                locale
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity modifyPost(@RequestHeader(JwtHeader.id) long regId,
                                     @RequestBody PostDto post,
                                     @PathVariable long id,
                                     Locale locale) {

        post.setId(id);
        post.setRegId(regId);
        PostEntity retPost = postService.modiPost(post);

        WebMvcLinkBuilder selfLink = linkTo(methodOn(PostController.class).modifyPost(regId, post, id, locale)).slash(post.getId());
        EntityModel entityModel = EntityModel.of(retPost);
        entityModel.add(selfLink.withRel("modify-post"));
        entityModel.add(linkTo(methodOn(this.getClass()).createPost(0, 0, post, null, locale)).withRel("create-post"));
        entityModel.add(selfLink.withSelfRel());

        return makeDataMessageForSuccess (
                entityModel,
                locale
        );
    }

    @PatchMapping("/{id}/folder")
    public ResponseEntity modifyPostFolder(@RequestHeader(JwtHeader.id) long regId,
                                           @RequestBody PostDto post,
                                           @PathVariable long id,
                                           Locale locale) {

        post.setId(id);
        post.setRegId(regId);
        PostEntity retPost = postService.modiPostFolder(post);

        WebMvcLinkBuilder selfLink = linkTo(methodOn(PostController.class).modifyPostFolder(regId,post,id,locale)).slash(post.getId());
        EntityModel entityModel = EntityModel.of(retPost);
        entityModel.add(selfLink.withRel("modify-post"));
        entityModel.add(linkTo(methodOn(this.getClass()).createPost(0, 0, post, null, locale)).withRel("create-post"));
        entityModel.add(selfLink.withSelfRel());

        return makeDataMessageForSuccess (
                entityModel,
                locale
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletePost(@PathVariable long id,
                                     Locale locale) {

        PostDto post = PostDto.builder().id(id).build();
        postService.deletePost(post);

        WebMvcLinkBuilder selfLink = linkTo(methodOn(PostController.class).deletePost(id,locale)).slash(post.getId());
        EntityModel entityModel = EntityModel.of(new HashMap<String,Object>());
        entityModel.add(selfLink.withRel("modify-post"));
        entityModel.add(linkTo(methodOn(this.getClass()).createPost(0, 0, post, null, locale)).withRel("create-post"));
        entityModel.add(selfLink.withSelfRel());

        return makeDataMessageForSuccess (
                entityModel,
                locale
        );
    }



}
