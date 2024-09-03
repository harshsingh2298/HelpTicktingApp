package com.cms.cdl.utils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.cms.cdl.dto.DocumentDTO;
import com.cms.cdl.dto.TicketDocumentsResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@Component
public class DocumentOperations {



	 
	 public int i;
	 
	 private static final int BUFFER_SIZE = 16 * 1024 * 1024;
	
	
    
    public List<DocumentDTO> uploadDocument(DocumentDTO documentDTO, List<MultipartFile> filesList,String postAPI,String uploaddirectory) throws IOException {
    	
    	List<DocumentDTO> docDTOList=new ArrayList<DocumentDTO>();
    	
    	WebClient webClient = WebClient.create();
        
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();

        ObjectMapper objectMapper = new ObjectMapper();
        String documentDTOJson = null;
        try {
            documentDTOJson = objectMapper.writeValueAsString(documentDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // Create HttpEntity for the DocumentDTO JSON
        HttpHeaders jsonHeaders = new HttpHeaders();
        jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> jsonEntity = new HttpEntity<>(documentDTOJson, jsonHeaders);

        // Create HttpEntity for the MultipartFile
        HttpHeaders fileHeaders = new HttpHeaders();
        
        
        for( i =0; i<filesList.size(); i++) {
        	
        	System.out.println(i);
        	final int index = i;
            ByteArrayResource fileAsResource = new ByteArrayResource(filesList.get(index).getBytes()) {
            	@Override
                public String getFilename() {
                    return filesList.get(index).getOriginalFilename();
                }
            };
            HttpEntity<ByteArrayResource> fileEntity = new HttpEntity<>(fileAsResource, fileHeaders);
            
            formData.add("files", fileEntity);
        }
        
        
        formData.add("documentDTO", jsonEntity);
     

        Mono<List<DocumentDTO>>  documentDTOResponseEntity = webClient.post()
                .uri(postAPI+uploaddirectory)
                .body(BodyInserters.fromMultipartData(formData))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<DocumentDTO>>() {});

        List<DocumentDTO> documentDTOResponseList = documentDTOResponseEntity.block();

        if(documentDTOResponseList != null && !documentDTOResponseList.isEmpty()){
        	
        	System.out.println("Result");
        	
        
        	for(DocumentDTO docDto : documentDTOResponseList) {
        		LocalDate createdDate = docDto.getCreatedDate();

        		docDto.setCreatedDate(createdDate);
        		
                        	
                    docDTOList.add( docDto);
        	}
        	
                
                return docDTOList;
        }
        else{
            return null;
        }
    }

    
    public FileAndContentTypeBean openAndDownloadDocument(Long docId,String fetchAPI) {   	
    	
    	WebClient webClient = WebClient.builder()
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(BUFFER_SIZE))
                        .build())
                .build();
    	
    	
        ResponseEntity<byte[]> responseEntity = webClient.get()
                .uri(fetchAPI + docId)
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .exchangeToMono(response -> response.toEntity(byte[].class))
                .block();

        // Extract content type from the response headers
        String contentType = responseEntity.getHeaders().getContentType().toString();

        // Extract file data from the response body
        byte[] fileData = responseEntity.getBody();

        return new FileAndContentTypeBean(fileData, contentType);
    }
    
    
    
    public FileAndContentTypeBean downloadMultipleFiles(List<Long> docsList,String downloadMultipleFilesAPI) {
           	
    	
    	WebClient webClient = WebClient.builder()
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(BUFFER_SIZE))
                        .build())
                .build();
    	
        
    	Mono<ResponseEntity<byte[]>>  documentDTOResponseEntity = webClient.post()
                .uri(downloadMultipleFilesAPI)
                .body(BodyInserters.fromValue(docsList))
                .retrieve()
                .toEntity(byte[].class);
        
        ResponseEntity<byte[]> responseEntity = documentDTOResponseEntity.block();

        byte[] fileData = responseEntity.getBody();
        String contentType = responseEntity.getHeaders().getContentType().toString();

        return new FileAndContentTypeBean(fileData, contentType);
    }
    
    
    
    
    
    
    
    

}
