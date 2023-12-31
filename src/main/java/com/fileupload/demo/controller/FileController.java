package com.fileupload.demo.controller;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import org.springframework.http.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fileupload.demo.entity.FileEntity;
import com.fileupload.demo.message.ResponseFile;
import com.fileupload.demo.message.ResponseMessage;
import com.fileupload.demo.service.FileService;

@Controller
@CrossOrigin("http://localhost:8080")
public class FileController {
	 @Autowired
 	 private  FileService fileService;

	    @PostMapping("/upload")
	    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
	      String message = "";
	      try {
	        fileService.store(file);
	        message = "Uploaded the file successfully: " + file.getOriginalFilename();
	        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
	      } catch (Exception e) {
	        message = "Could not upload the file: " + file.getOriginalFilename() + "!";
	        System.out.println(e);
	        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
	      }
	    }
	    @GetMapping("/files")
	    public ResponseEntity<List<ResponseFile>> getListFiles() {
	      List<ResponseFile> files = fileService.getAllFiles().map(dbFile -> {
	        String fileDownloadUri = ServletUriComponentsBuilder
	            .fromCurrentContextPath()
	            .path("/files/")
	          .port(dbFile.getId())	 //.path(dbFile.getId())
	            .toUriString();    

	        return new ResponseFile(
	                dbFile.getName(),
	                fileDownloadUri,
	                dbFile.getType(),
	                dbFile.getData().length);
	          }).collect(Collectors.toList());

	      return ResponseEntity.status(HttpStatus.OK).body(files);
	   
	    }
	    @GetMapping("/files/{id}")
	    public ResponseEntity<ByteArrayResource> getFileById(@PathVariable int id) {
	        Optional<FileEntity> optionalFileEntity = fileService.getFileById(id);

	        if (optionalFileEntity.isPresent()) {
	            FileEntity fileEntity = optionalFileEntity.get();

	            ByteArrayResource resource = new ByteArrayResource(fileEntity.getData());

	            return ResponseEntity.ok()
	                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileEntity.getName())
	                    .contentType(MediaType.parseMediaType(fileEntity.getType()))
	                    .contentLength(fileEntity.getData().length)
	                    .body(resource);
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    }


	   	  
}
