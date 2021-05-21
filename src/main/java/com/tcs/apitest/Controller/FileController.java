package com.tcs.apitest.Controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcs.eas.api.tools.testbuddy.model.ApiDetail;
import com.tcs.eas.api.tools.testbuddy.model.FormData;
import com.tcs.eas.api.tools.testbuddy.model.HttpHeader;
import com.tcs.eas.api.tools.testbuddy.model.QueryParam;
import com.tcs.eas.api.tools.testbuddy.test.Main;

@Controller
public class FileController {

	@Autowired
	private com.tcs.apitest.FileService fileService;
	
	@Value("${report.server.url}")
	private String url;
	
	@GetMapping("/")
	public String index() {
		return  "upload";
	}
	
	@PostMapping("/uploadFile")
	public String uploadFile(@RequestParam("file") MultipartFile file, Model model)
	{
		System.out.println("there");
		if (file.isEmpty()) {
           model.addAttribute("message", "Please select a file to upload");
            return "redirect:uploadFile";
        }
		
		ApiDetail apiDetail = fileService.uploadFile(file);
		
		model.addAttribute("message", "You successfully uploaded" + file.getOriginalFilename());
		
		model.addAttribute("apiDetail", apiDetail);
		return "parsedSwagger";
	}
	
	@PostMapping(value="/testResource",
			consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public String testResource( @RequestBody FormData formData, Model model)
	{	String reportUrl;
		try {
		ObjectMapper mapper = new ObjectMapper();
		
		QueryParam[] q =null;
		if(formData.getQueryParam() !=null)
		{
			q = mapper.readValue(formData.getQueryParam(), QueryParam[].class);
			formData.setQueryParams(Arrays.asList(q));
			System.out.println("----"+formData.getQueryParams());
		}
		
		//Handle Headers
		HttpHeader[] headers =null;
		if(formData.getHttpHeader() !=null)
		{
			headers = mapper.readValue(formData.getHttpHeader(), HttpHeader[].class);
			formData.setHttpHeaders(Arrays.asList(headers));
			System.out.println("----"+formData.getHttpHeaders());
		}
		//set request and response true
		formData.setShowRequest(true);
		formData.setShowResponse(true);
		Main m = new Main(formData);
		reportUrl = m.run();
		model.addAttribute("message", "You successfully uploaded" );
		}catch(Exception e) {
			e.printStackTrace();
			return "error";
		}
		//model.addAttribute("apiDetail", apiDetail);
		
		// {"url":"value"}
		//String json = "{\"name\":\""+test+"\"}";
		//System.out.println("madarmmmmmmmmmmmmmmmmmmmmmmmmmmmm="+url);
		return "{\"url\":\""+(url+reportUrl)+"\"}";   
	}
	
}
