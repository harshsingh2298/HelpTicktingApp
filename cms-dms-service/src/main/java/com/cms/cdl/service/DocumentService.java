package com.cms.cdl.service;

import com.cms.cdl.Entity.Document;
import com.cms.cdl.dto.RequestDTO.DocReqDTO;
import com.cms.cdl.dto.ResponseDTO.DocResDTO;
import com.cms.cdl.static_classes.FileAndContentType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface DocumentService {
    public List<DocResDTO> uploadDocument(DocReqDTO documentReqDTO, List<MultipartFile> files, String uploadDirectory) throws IOException;

    public List<DocResDTO> uploadDocumentsFromOtherServices(Document document, List<MultipartFile> files, String uploadDirectory) throws IOException;

    public DocResDTO uploadFormBuilderDocument(String uploadDirectory, MultipartFile file) throws IOException;

    public String updateDocService(MultipartFile file, long docId, String userRole, String documentDirectory) throws IOException;

    public DocResDTO createFolder(String folderDirectory, Document document) throws IOException;

    public boolean deleteFolder(String folderDirectory, String folderName) throws IOException;

    public boolean deleteFile(String fileDirectory, String fileName);

    public boolean deleteFileFromOtherServices(long docId);

    public List<String> getCompanyPolicies();

    public List<DocResDTO> getMyDocuments(long empId);
    public DocResDTO updateDocFromOtherService(DocReqDTO docReqDTO, MultipartFile file, long docId, String userRole, String uploadDirectory) throws IOException;
    public List<DocResDTO> getDocuments(String sourceDirectory, long empId);

    public List<DocResDTO> getFolders(String sourceDirectory);

    public FileAndContentType openFileByDocId(long docId) throws IOException;

    FileAndContentType downloadMultipleFiles(List<Long> docIds) throws IOException;

    public FileAndContentType openCompanyPolicies(String itemName) throws IOException;

    public Map<String, Object> getPropertiesOfFileOrFolder(String sourceDirectory, String fileOrFolderName) throws IOException;

    public DocResDTO getFileDataByCurrentDirectory(String currentDirectory, String itemName);

    public List<DocResDTO> renameFolder(String currentDirectory, String oldFolderName, String newFolderName, String loggedInUserID) throws IOException;

    public List<String> searchFileAndFolders(String currentDirectory, String searchTerm);
}
