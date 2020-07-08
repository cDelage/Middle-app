package com.epsi.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.epsi.entity.FileKeyValueDTO;
import com.epsi.services.IFileReaderService;

@Controller
public class MiddleController {
	private static final String FILE2 = "%FILE%";
	private static final String FILE_SEND_ERROR = "Votre fichier %FILE% n'a pas été envoyé, veuillez contacter votre administrateur";
	private static final String MESSAGE = "MESSAGE";
	private static final String FILE_SEND = "Votre fichier %FILE% à bien été envoyé";
	private static final String XML = "XML";
	private static final String CSV = "CSV";
	private static final String FAIL_TO_LOAD_FILE = "Fail to load file, catch exception : ";
	private static final String TRY_TO_UPLOAD_NEW_FILE = "Try to upload new file";
	private static final String UPLOADED_SAVED = "Uploaded Work, saved at : {} , file type : {}, emmeteur : {}";
	private static final Logger LOG = LogManager.getLogger(MiddleController.class);
	private static final String PAGES_INDEX = "pages/index";
	private static final String LOAD_PATH = "/load";
	private static final String UPLOADED_FILE = "file";
	private static final String EMPTY_PATH = "/";
	
	@Value("${upload.path}")
	private String uploadPath;
	
	@Autowired
	private IFileReaderService fileReaderService;

	@RequestMapping(EMPTY_PATH)
	public String homepage() {
		return PAGES_INDEX;
	}
	
	@PostMapping(LOAD_PATH)
	public String postMapping(@RequestParam(UPLOADED_FILE) MultipartFile file, @ModelAttribute("type") String type, @ModelAttribute("emmeteur") String emmeteur, ModelMap modelMap) {
		LOG.info(TRY_TO_UPLOAD_NEW_FILE);
		try {
			// If folder not exist -> create.
			File folder = new File(uploadPath);
			if (!folder.exists()) {
				folder.mkdirs();
			}else {
				folder.delete();
				folder.mkdirs();
			}
			StringBuilder filePathString = new StringBuilder(uploadPath).append(file.getOriginalFilename());
			Path filePath = Paths.get(uploadPath, file.getOriginalFilename());
			file.transferTo(filePath);
			LOG.info(UPLOADED_SAVED, filePathString.toString(), type, emmeteur);
			String statusEnvoi = new String();
			switch(type) {
			case CSV:
				FileKeyValueDTO fileSend = fileReaderService.readCSVFile(filePathString.toString(), emmeteur);
				statusEnvoi = fileSend.getStatusEnvoi();
				break;
			case XML:
				FileKeyValueDTO fileSendXML = fileReaderService.readXMLFile(filePathString.toString(), emmeteur);
				statusEnvoi = fileSendXML.getStatusEnvoi();
				break;
			}
			
			if(statusEnvoi == "DONE") {
				modelMap.put(MESSAGE, FILE_SEND.replace(FILE2, file.getOriginalFilename()));	
			}else {
				modelMap.put(MESSAGE, FILE_SEND_ERROR.replace(FILE2, file.getOriginalFilename()));
			}
		}catch(IOException e) {
			LOG.error(FAIL_TO_LOAD_FILE, e);
		}
		return PAGES_INDEX;
	}
}
