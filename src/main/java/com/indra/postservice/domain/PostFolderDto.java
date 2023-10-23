package com.indra.postservice.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostFolderDto {

    private long id;

    @NotNull(message = "폴더명 입력은 필수 입니다.")
    @Size(min = 1, max=200, message = "폴더명은 최소 1글자 이상, 최대 200자 이하로 입력해야합니다.")
    private String name;

    private long regId;

}
