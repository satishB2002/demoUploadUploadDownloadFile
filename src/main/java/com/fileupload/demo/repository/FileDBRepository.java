package com.fileupload.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fileupload.demo.entity.FileEntity;
@Repository
public interface FileDBRepository extends JpaRepository<FileEntity, String> {

	FileEntity findById(int id);

}
