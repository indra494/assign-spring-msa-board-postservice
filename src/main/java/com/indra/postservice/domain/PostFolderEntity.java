package com.indra.postservice.domain;

import com.indra.postservice.support.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "folder")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostFolderEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 200)
    private String name;

}
