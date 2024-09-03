package com.cms.employeeservice.utils;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.internet.MimeMessage;

@Component
public class EmailUtils {

	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private TemplateEngine templateEngine;
	

	public boolean sendEmail(String to, String subject, String body) {
		
                   boolean isSent = false;
                   
                   
                   
                   Context context = new Context();
                   context.setVariable("body",body);
                   
                   
                   
//                   File imagename=new File("D://images//Ticket.png");
//                   
//                   String path = imagename.getAbsolutePath();
//                 FileSystemResource f= new FileSystemResource(path);
//                 
//                 System.out.println(path);
//                   
//                   context.setVariable("imagename", path);
                   
                   String process = templateEngine.process("MailTemplate.html", context);
		
                  try {
			              MimeMessage mimeMessage = mailSender.createMimeMessage();
			              MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
			              helper.setTo(to);
			              helper.setSubject(subject);
			              helper.setText(process, true);
			              
			             // helper.addInline("imagename",imagename);
			              
			              
			              mailSender.send(mimeMessage);
			              isSent = true;
		             } 

                catch (Exception e) 
                {
			e.printStackTrace();
		}
                  
                  
                 


		return isSent;
	}
	
	
	
	
	
	
	
	
	
	
	
}
