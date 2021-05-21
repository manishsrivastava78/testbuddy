package com.tcs.apitest;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.tcs.eas.api.tools.testbuddy.model.ApiDetail;
import com.tcs.eas.api.tools.testbuddy.parser.Main;


@Service
public class FileService {

	@Value("${app.upload.dir:{user.home}}")
	public String uploadDir;
	
	
	public ApiDetail uploadFile(MultipartFile file) {
		try {
			Path copyLocation = Paths.get(uploadDir+ File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
			Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
			System.out.println("******************************************");
			Main main = new Main();
			ApiDetail apiDetail = main.getApiDetail(copyLocation.toString());
			System.out.println(copyLocation.toString());
			return apiDetail;
	} catch (Exception e) {
        e.printStackTrace();
        throw new FileStorageException("Could not store file " + file.getOriginalFilename()
            + ". Please try again!");
    }
	}
	
	
}
