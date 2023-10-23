package com.indra.postservice.controller;

import com.indra.postservice.domain.PostFolderDto;
import com.indra.postservice.domain.PostFolderEntity;
import com.indra.postservice.domain.PostFolderEntityModel;
import com.indra.postservice.service.PostFolderService;
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
import java.util.Locale;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "folders", produces = MediaTypes.HAL_JSON_VALUE)
public class PostFolderController extends BaseApiController {

    private final PostFolderService postFolderService;

    @GetMapping()
    public ResponseEntity getFolderList(@PageableDefault(size = 10, sort = "regDate", direction = Sort.Direction.DESC) Pageable pageable,
                                        Locale locale) {
        Page<PostFolderEntity> folders = postFolderService.getFolderList(pageable);

        PagedResourcesAssembler<PostFolderEntity> assembler = new PagedResourcesAssembler<PostFolderEntity>(null,null);
        RepresentationModelAssembler<PostFolderEntity, PostFolderEntityModel> folderAssembler;
        folderAssembler = e -> new PostFolderEntityModel(e);
        var folderEnitiyModels = assembler.toModel(folders, folderAssembler);

        return makeDataMessageForSuccess (
                folderEnitiyModels,
                locale
        );

    }

    @PostMapping()
    public ResponseEntity createFolder(@RequestHeader(JwtHeader.id) long regId,
                                       @RequestBody @Valid PostFolderDto postFolder,
                                       BindingResult bindingResult,
                                       Locale locale) {

        ValidationObject validationObject = new ValidationObject();
        if(doFormValidation(bindingResult,validationObject)) {
            return makeDataMessageForValidation(validationObject, locale);
        }

        WebMvcLinkBuilder selfLink = linkTo(methodOn(PostFolderController.class).createFolder(regId, postFolder, null, locale)).slash(postFolder.getId());
        URI createUri = selfLink.toUri();

        postFolder.setRegId(regId);
        return makeDataMessageForCreated (
                createUri,
                postFolderService.createFolder(postFolder),
                locale
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity modifyFolder(@RequestHeader(JwtHeader.id) Long regId,
                                       @RequestBody @Valid PostFolderDto postFolder,
                                       @PathVariable long id,
                                       BindingResult bindingResult,
                                       Locale locale) {

        postFolder.setId(id);
        postFolder.setRegId(regId);
        PostFolderEntity retFolder = postFolderService.modiFolder(postFolder);

        WebMvcLinkBuilder selfLink = linkTo(methodOn(PostFolderController.class).modifyFolder(regId, postFolder, id, null, locale)).slash(postFolder.getId());
        EntityModel entityModel = EntityModel.of(retFolder);
        entityModel.add(selfLink.withRel("modify-folder"));
        entityModel.add(linkTo(methodOn(this.getClass()).createFolder(0, postFolder, null, locale)).withRel("create-folder"));
        entityModel.add(selfLink.withSelfRel());

        ValidationObject validationObject = new ValidationObject();
        if(doFormValidation(bindingResult,validationObject)) {
            return makeDataMessageForValidation(validationObject, locale);
        }

        return makeDataMessageForSuccess (
                entityModel,
                locale
        );
    }


}
