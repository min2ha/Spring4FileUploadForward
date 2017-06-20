package com.websystique.springmvc.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.websystique.springmvc.model.FileBucket;
import com.websystique.springmvc.model.MultiFileBucket;
import com.websystique.springmvc.util.FileValidator;
import com.websystique.springmvc.util.MultiFileValidator;

@Controller
public class FileUploadController {

	private static String UPLOAD_LOCATION="/Users/min2ha/temp/";

	@Autowired
	FileValidator fileValidator;

	@Autowired
	MultiFileValidator multiFileValidator;

	@InitBinder("fileBucket")
	protected void initBinderFileBucket(WebDataBinder binder) {
		binder.setValidator(fileValidator);
	}

	@InitBinder("multiFileBucket")
	protected void initBinderMultiFileBucket(WebDataBinder binder) {
		binder.setValidator(multiFileValidator);
	}

	@RequestMapping(value = { "/", "/welcome" }, method = RequestMethod.GET)
	public String getHomePage(ModelMap model) {
		return "welcome";
	}

	@RequestMapping(value = "/singleUpload", method = RequestMethod.GET)
	public String getSingleUploadPage(ModelMap model) {
		FileBucket fileModel = new FileBucket();
		model.addAttribute("fileBucket", fileModel);
		return "singleFileUploader";
	}

	@RequestMapping(value = "/singleUpload", method = RequestMethod.POST)
	public String singleFileUpload(@Valid FileBucket fileBucket,
								   BindingResult result,
								   ModelMap model) throws IOException {

		if (result.hasErrors()) {
			System.out.println("validation errors");
			return "singleFileUploader";
		} else {
			System.out.println("Fetching file");
			MultipartFile multipartFile = fileBucket.getFile();

			// Now do something with file...
			FileCopyUtils.copy(fileBucket.getFile().getBytes(),
					new File( UPLOAD_LOCATION + fileBucket.getFile().getOriginalFilename()));
			String fileName = multipartFile.getOriginalFilename();
			model.addAttribute("fileName", fileName);
			model.addAttribute("name", "John");
			model.addAttribute("userid", "1231321");
			model.addAttribute("name", "John");


			//return "success";

			//pass path parameter
			return "forward:/test3";
		}
	}

	@RequestMapping(value="/test3")
	public String showTestPage(HttpServletRequest request) {

		System.out.println("Attribute fileName = " + request.getAttribute("fileName"));
		//UPLOAD_LOCATION
		System.out.println("Attribute path + fileName = " + UPLOAD_LOCATION + request.getAttribute("fileName"));

		/*
		Enumeration params = request.getParameterNames();
		System.out.println("Parameters..." + params.toString());
		Enumeration attributes = request.getAttributeNames();
		System.out.println("Attributes..." + attributes.toString());
		while(params.hasMoreElements()){
			String paramName = (String)params.nextElement();
			System.out.println("Parameter Name - "+paramName+", Value - "+request.getParameter(paramName));
		}

		while(attributes.hasMoreElements()){
			String paramName = (String)attributes.nextElement();
			System.out.println("Attribute Name - "+paramName+", Value - "+request.getAttribute(paramName));
		}

		request.setAttribute("param1", "foo");
		request.setAttribute("param2", "bar");
		*/
		return "test3";
	}

	@RequestMapping(value="/test2")
	public String showTestPage2(HttpServletRequest request) {
		String param1 = (String) request.getAttribute("param1");
		String param2 = (String) request.getAttribute("param2");
		return "testPageView";
	}


	@RequestMapping(value = "/multiUpload", method = RequestMethod.GET)
	public String getMultiUploadPage(ModelMap model) {
		MultiFileBucket filesModel = new MultiFileBucket();
		model.addAttribute("multiFileBucket", filesModel);
		return "multiFileUploader";
	}

	@RequestMapping(value = "/multiUpload", method = RequestMethod.POST)
	public String multiFileUpload(@Valid MultiFileBucket multiFileBucket,
								  BindingResult result,
								  ModelMap model) throws IOException {

		if (result.hasErrors()) {
			System.out.println("validation errors in multi upload");
			return "multiFileUploader";
		} else {
			System.out.println("Fetching files");
			List<String> fileNames = new ArrayList<String>();
			// Now do something with file...
			for (FileBucket bucket : multiFileBucket.getFiles()) {
				FileCopyUtils.copy(bucket.getFile().getBytes(), new File(UPLOAD_LOCATION + bucket.getFile().getOriginalFilename()));
				fileNames.add(bucket.getFile().getOriginalFilename());
			}

			model.addAttribute("fileNames", fileNames);
			return "multiSuccess";
		}
	}

}
