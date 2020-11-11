
package com.wl.tools.util.parse.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.SAXException;

import com.wl.tools.util.parse.controller.util.Archive;
import com.wl.tools.util.parse.controller.util.Util;

@Controller
public class ValidationController {
	private final Logger logger;
	@Value("${welcome.message:test}")
	private String message;
	private Util util;

	public ValidationController() {
		logger = LoggerFactory.getLogger(ValidationController.class);
		this.message = "Hello World";
		util = new Util();
	}

	@RequestMapping( "/")
	public ModelAndView welcome(final Map<String, Object> model) {
		model.put("message", this.message);
		return new ModelAndView("welcome", "archive", (Object) new Archive());
	}

	@RequestMapping({ "/parse" })
	public String parseXml(final Map<String, Object> model) {
		model.put("message", "PARSEANDO ...");
		return "parse";
	}

	@RequestMapping(value = { "/loadFiles" }, method = { RequestMethod.POST })
	public String submit( @ModelAttribute("archive") final Archive archiveForm, final BindingResult result) {
		if (result.hasErrors()) {
			return "error";
		}
		final String name = archiveForm.getName();
		final String path = archiveForm.getPath();
		logger.info("name: " + name);
		logger.info("path: " + path);
		return "";
	}

	@PostMapping(value = "/uploadFiles")
	public String uploadFileMulti(
			@RequestParam("files") final MultipartFile[] uploadfiles, 
			Model model) {
		
		logger.info("Multiple file upload!");
		try {
			final String uploadedFileName = Arrays.stream(uploadfiles)
					.map(x -> x.getOriginalFilename() + "-" + x.getName())
					.filter(x -> !StringUtils.isEmpty((Object) x))
					.collect(Collectors.joining(" , "));
			
			logger.info("file name: "+uploadedFileName);
			
			if (StringUtils.isEmpty((Object) uploadedFileName)) {
				model.addAttribute("respuesta", (Object) "Por favor seleccione un archivo!");
			}
			
			List<MultipartFile> content =  Arrays.asList(uploadfiles);
			
			
			
			logger.info("file content: "+content);
			
			List<String> dataReturn = util.formatTable(util.saveUploadedFiles(content));
			model.addAttribute("respuesta", dataReturn.get(0));
			model.addAttribute("extras", dataReturn.get(1));
			model.addAttribute("nombreArchivo", uploadedFileName);
			model.addAttribute("exito", (Object) "Bien hecho!. Carga sin errores");
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
			model.addAttribute("error", (Object) ("Error: " + e.getMessage()));
		}
		return "respuesta";
	}
}