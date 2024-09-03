package com.cms.cdl.controller;

import com.cms.cdl.Entity.Document;
import com.cms.cdl.dto.RequestDTO.DocReqDTO;
import com.cms.cdl.dto.ResponseDTO.DocResDTO;
import com.cms.cdl.service.DocumentService;
import com.cms.cdl.static_classes.FileAndContentType;
import com.cms.cdl.utils.ModelConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin("*")
//@CrossOrigin(origins = { "http://localhost:3000" }, allowCredentials = "true")
public class DocumentController {


    @Autowired
    DocumentService documentService;

    // http://localhost:9091/upload/
    @PostMapping(value = "/upload/{uploadDirectory}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.ALL_VALUE})
    public ResponseEntity<List<DocResDTO>> uploadDocument(@RequestParam("documentDTO") String documentDTO, @RequestPart("files") List<MultipartFile> files, @PathVariable(name = "uploadDirectory") String uploadDirectory) throws IOException {
        DocReqDTO docDTO = new ObjectMapper().readValue(documentDTO, DocReqDTO.class);
        return new ResponseEntity<List<DocResDTO>>(documentService.uploadDocument(docDTO, files, uploadDirectory), HttpStatus.OK);
    }

    // http://192.168.249.45:9091/upload/doc/
    // http://localhost:9091/upload/doc/
    @PostMapping(value = "/upload/doc/{uploadDirectory}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.ALL_VALUE})
    public ResponseEntity<?> uploadDocumentFromOtherService(@RequestParam("documentDTO") String documentDTO, @RequestPart("files") List<MultipartFile> files, @PathVariable(name = "uploadDirectory") String uploadDirectory) throws IOException {
        DocReqDTO docDTO = new ObjectMapper().readValue(documentDTO, DocReqDTO.class);
        Document document = ModelConverter.convertToDocument(docDTO);
        return new ResponseEntity<>(documentService.uploadDocumentsFromOtherServices(document, files, uploadDirectory), HttpStatus.OK);
    }

    // for saving file using form builder
    // http://192.168.249.45:9091/upload/form-builder/doc/
    @PostMapping(value = "/upload/form-builder/doc/{uploadDirectory}")
    public ResponseEntity<?> uploadFormBuilderDocument(@PathVariable(name = "uploadDirectory") String uploadDirectory, @RequestPart("file") MultipartFile file) throws IOException {
        System.out.println(uploadDirectory);
        return new ResponseEntity<>(documentService.uploadFormBuilderDocument(uploadDirectory, file), HttpStatus.OK);
    }
    // http://localhost:9091/updatedoc/from/other/service///
    @PutMapping(value = "/updatedoc/from/other/service/{docId}/{userRole}/{uploadDirectory}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.ALL_VALUE})
    public ResponseEntity<?> updateDocumentFromOtherService(@RequestParam("documentDTO") String documentDTO, @RequestPart("file") MultipartFile file, @PathVariable(name = "docId") long docId, @PathVariable(name = "userRole") String userRole, @PathVariable(name = "uploadDirectory") String uploadDirectory) throws IOException {
        DocReqDTO docReqDTO = new ObjectMapper().readValue(documentDTO, DocReqDTO.class);
        return new ResponseEntity<>(documentService.updateDocFromOtherService(docReqDTO, file, docId, userRole, uploadDirectory), HttpStatus.OK);
    }
    // http://localhost:9091/updatedoc//
    @PutMapping(value = "/updatedoc/{docId}/{userRole}/{uploadDirectory}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.ALL_VALUE})
    public ResponseEntity<String> updateDocument(@RequestPart("file") MultipartFile file, @PathVariable(name = "docId") long docId, @PathVariable(name = "userRole") String userRole, @PathVariable(name = "uploadDirectory") String uploadDirectory) throws IOException {
        return new ResponseEntity<>(documentService.updateDocService(file, docId, userRole, uploadDirectory), HttpStatus.OK);
    }

    // http://localhost:9091/create/folder//
    @PostMapping("/create/folder/{folderDirectory}")
    public ResponseEntity<DocResDTO> createFolder(@PathVariable("folderDirectory") String folderDirectory, @RequestBody DocReqDTO documentDTO) throws IOException {
        Document document = ModelConverter.convertToDocument(documentDTO);
        return new ResponseEntity<>(documentService.createFolder(folderDirectory, document), HttpStatus.OK);
    }

    // http://localhost:9091/delete/folder//
    @DeleteMapping("/delete/folder/{folderDirectory}/{folderName}")
    public ResponseEntity<?> deleteFolder(@PathVariable("folderDirectory") String folderDirectory, @PathVariable("folderName") String folderName) throws IOException {
        return new ResponseEntity<>(documentService.deleteFolder(folderDirectory, folderName), HttpStatus.OK);
    }

    // http://localhost:9091/delete/file//
    @DeleteMapping("/delete/file/{fileDirectory}/{fileName}")
    public ResponseEntity<?> deleteFile(@PathVariable("fileDirectory") String fileDirectory, @PathVariable("fileName") String fileName) {
        return new ResponseEntity<>(documentService.deleteFile(fileDirectory, fileName), HttpStatus.OK);
    }

    // delete file from other services

    // http://localhost:9091/delete/file/api/
    @DeleteMapping("/delete/file/api/{docId}")
    public ResponseEntity<?> deleteFileFromOtherServices(@PathVariable("docId") long docId) {
        return new ResponseEntity<>(documentService.deleteFileFromOtherServices(docId), HttpStatus.OK);
    }

    // http://localhost:9091/get/comPoly
    @GetMapping("/get/comPoly")
    public ResponseEntity<?> getCompanyPolicies() {
        return new ResponseEntity<>(documentService.getCompanyPolicies(), HttpStatus.OK);
    }

    // http://localhost:9091/get/myDocs/
    @GetMapping("/get/myDocs/{empId}")
    public ResponseEntity<?> getMyDocs(@PathVariable("empId") long empId) {
        return new ResponseEntity<>(documentService.getMyDocuments(empId), HttpStatus.OK);
    }

    // http://localhost:9091/get/documents/
    @GetMapping("/get/documents/{sourceDirectory}/{empId}")
    public ResponseEntity<?> getDocuments(@PathVariable("sourceDirectory") String sourceDirectory, @PathVariable("empId") long empId) {
        return new ResponseEntity<>(documentService.getDocuments(sourceDirectory, empId), HttpStatus.OK);
    }

    // http://localhost:9091/get/folders/
    @GetMapping("/get/folders/{sourceDirectory}")
    public ResponseEntity<?> getFolders(@PathVariable("sourceDirectory") String sourceDirectory) {
        return new ResponseEntity<>(documentService.getFolders(sourceDirectory), HttpStatus.OK);
    }

    // http://localhost:9091/open/fileByDocId/
    @GetMapping("/open/fileByDocId/{docId}")
    public ResponseEntity<?> openFileByDocId(@PathVariable("docId") Long docId) throws IOException {
        FileAndContentType file = documentService.openFileByDocId(docId);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf(file.getContentType())).body(file.getFile());
    }

    // http://192.168.249.45:9091/download/multiple/files
    // http://localhost:9091/download/multiple/files
    @PostMapping("/download/multiple/files")
    public ResponseEntity<?> downloadMultipleFiles(@RequestBody List<Long> docIds) throws IOException {
        FileAndContentType file = documentService.downloadMultipleFiles(docIds);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=documents.zip");
        return ResponseEntity.status(HttpStatus.OK)
                .headers(headers)
                .contentType(MediaType.valueOf(file.getContentType()))
                .body(file.getFile());
    }

    // http://localhost:9091/open/companyPolicies/
    @GetMapping("/open/companyPolicies/{itemName}")
    public ResponseEntity<?> openCompanyPolicies(@PathVariable("itemName") String itemName) throws IOException {
        FileAndContentType file = documentService.openCompanyPolicies(itemName);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf(file.getContentType())).body(file.getFile());
    }

    // http://localhost:9091/get/properties//
    @GetMapping("/get/properties/{sourceDirectory}/{folderNameOrFileName}")
    public ResponseEntity<?> getPropertiesOfFileOrFolder(@PathVariable("sourceDirectory") String sourceDirectory, @PathVariable("folderNameOrFileName") String folderNameOrFileName) throws IOException {
        return new ResponseEntity<>(documentService.getPropertiesOfFileOrFolder(sourceDirectory, folderNameOrFileName), HttpStatus.OK);
    }

    // http://localhost:9091/get/fileData/
    @GetMapping("/get/fileData/{sourceDirectory}/{itemName}")
    public ResponseEntity<?> getFileDataByFileName(@PathVariable("sourceDirectory") String sourceDirectory, @PathVariable("itemName") String itemName) {
        return new ResponseEntity<>(documentService.getFileDataByCurrentDirectory(sourceDirectory, itemName), HttpStatus.OK);
    }

    // http://localhost:9091/rename/folder//
    @PutMapping("/rename/folder/{currentDirectory}/{oldFolderName}/{newFolderName}/{loggedInUserID}")
    public ResponseEntity<?> renameFolder(@PathVariable("currentDirectory") String currentDirectory, @PathVariable("oldFolderName") String oldFolderName, @PathVariable("newFolderName") String newFolderName, @PathVariable("loggedInUserID") String loggedInUserID) throws IOException {
        return new ResponseEntity<>(documentService.renameFolder(currentDirectory, oldFolderName, newFolderName, loggedInUserID), HttpStatus.OK);
    }

    // http://localhost:9091/search//
    @GetMapping("/search/{currentDirectory}/{searchTerm}")
    public ResponseEntity<?> searchTerm(@PathVariable("currentDirectory") String currentDirectory, @PathVariable("searchTerm") String searchTerm) {
        return new ResponseEntity<>(documentService.searchFileAndFolders(currentDirectory, searchTerm), HttpStatus.OK);
    }

}
