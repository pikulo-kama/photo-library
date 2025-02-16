package com.adrabazha.photo_library.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Table(name = "DIRECTORY")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Directory {

    @Id
    @Column(name = "DIRECTORY_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "directory_seq")
    @SequenceGenerator(name = "directory_seq", sequenceName = "S_DIRECTORY", allocationSize = 1)
    private Long directoryId;

    @Column(name = "DIRECTORY_UUID", unique = true, nullable = false, updatable = false)
    private String directoryUUID;

    @Column(name = "DIRECTORY_NAME")
    private String directoryName;

    @Column(name = "PARENT_DIRECTORY_ID", insertable = false, updatable = false)
    private Long parentDirectoryId;

    @ManyToOne
    @JoinColumn(name = "PARENT_DIRECTORY_ID")
    private Directory parentDirectory;

    @Column(name = "OWNER", nullable = false, updatable = false)
    private String owner;
}
