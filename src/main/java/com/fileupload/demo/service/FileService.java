package com.fileupload.demo.service;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fileupload.demo.entity.FileEntity;
import com.fileupload.demo.repository.FileDBRepository;
import org.springframework.util.StringUtils;

@Service
public class FileService {
	 @Autowired
	  private FileDBRepository fileDBRepository;
	 public FileEntity store(MultipartFile file) throws IOException {
		    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		    FileEntity FileDB = new FileEntity(101, fileName, file.getContentType(), file.getBytes());

		    return fileDBRepository.save(FileDB);
		  }
	 public Stream<FileEntity> getAllFiles() {
		    return fileDBRepository.findAll().stream();
		  }
	
//	public Optional<FileEntity> getFileById(int id) {
//	    return fileDBRepository.findAllById(id);
//	}
	public FileEntity getFile(int id) {
	    return fileDBRepository.findById(id);
	  }
	public Optional<FileEntity> getFileById(int id) {
		// TODO Auto-generated method stub
		return Optional.ofNullable(fileDBRepository.findById(id));
	}
	
	
	 
}
