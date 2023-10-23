package com.indra.postservice.domain;

import com.indra.postservice.support.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition="TEXT")
    private String contents;

    @Column(nullable = false)
    private long companyId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="folder_id", nullable = false)
    private PostFolderEntity PostFolder;
}
