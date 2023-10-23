package com.indra.postservice.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtDto {

    private long id;

    private String accountId;

    private long companyId;

}
