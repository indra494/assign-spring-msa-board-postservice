package com.indra.postservice.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostDto {

    private long id;

    @NotNull(message = "제목 입력은 필수 입니다.")
    @Size(min = 1, max=200, message = "제목은 최소 1글자 이상, 최대 200자 이하로 입력해야합니다.")
    private String title;

    @NotNull(message = "내용 입력은 필수 입니다.")
    private String contents;

    @NotNull(message = "폴더 입력은 필수 입니다.")
    private long folderId;

    private long companyId;

    private long regId;

}
