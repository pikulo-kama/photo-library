package com.adrabazha.photo_library.repository;

import com.adrabazha.photo_library.model.Directory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface DirectoryRepository extends JpaRepository<Directory, Long> {

    Optional<Directory> findByOwnerAndParentDirectoryIdIsNull(String owner);

    Optional<Directory> findByDirectoryUUIDAndOwner(String directoryUUID, String owner);

    List<Directory> findByParentDirectoryIdAndOwner(Long parentDirectoryId, String owner);

    boolean existsByParentDirectoryIdAndDirectoryNameAndOwner(Long parentDirectoryId, String directoryName, String owner);
}
