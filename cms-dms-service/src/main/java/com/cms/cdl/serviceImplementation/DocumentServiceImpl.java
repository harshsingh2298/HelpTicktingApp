package com.cms.cdl.serviceImplementation;

import com.cms.cdl.Entity.Document;
import com.cms.cdl.Repository.DocumentRepository;
import com.cms.cdl.dto.RequestDTO.DocReqDTO;
import com.cms.cdl.dto.ResponseDTO.DocResDTO;
import com.cms.cdl.service.DocumentService;
import com.cms.cdl.static_classes.FileAndContentType;
import com.cms.cdl.static_classes.Source;
import com.cms.cdl.utils.DocumentNameFormatter;
import com.cms.cdl.utils.ModelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    DocumentRepository repo;

    @Autowired
    AccessRecordsService recordsService;

    @Autowired
    private JedisPool jedisPool;


    @Value("${upload.directory}")
    private String uploadDirectory;

    @Value("${company.policy.documents.path}")
    private String companyPolicyDocumentsPath;

    @Value("${my.documents.path}")
    private String myDocumentsPath;

    private final String fileSeparator;
    private final String rootSeparator;

    public DocumentServiceImpl() {
        if (System.getProperty("os.name").startsWith("Windows")) {
            fileSeparator = "\\\\";
            rootSeparator = "\\\\\\\\";
        } else {
            fileSeparator = "/";
            rootSeparator = "/";
        }
    }

    @CacheEvict(value = "documentsCache", allEntries = true)
    public List<DocResDTO> uploadDocument(DocReqDTO documentReqDTO, List<MultipartFile> files, String uploadDirectory) throws IOException {
        Document document = ModelConverter.convertToDocument(documentReqDTO);

        List<Document> documents = new ArrayList<>();

        uploadDirectory = uploadDirectory.replace("-", fileSeparator);

        Document doc = new Document();

        long parentId;

        parentId = uploadDirectory.equalsIgnoreCase("root") ? 0 : repo.findByDocPath(uploadDirectory.replaceFirst("^root" + rootSeparator, "")).getDocId();

        for (MultipartFile file : files) {
            String uniqueID = UUID.randomUUID().toString();
            String name = file.getOriginalFilename();
            String fileName = document.getEmpId() + "-" + uniqueID + "-" + name;

            String contentType = Objects.requireNonNull(file.getContentType()).replaceAll("application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                    "application/msword");

            Document d = new Document();

            Path path = Paths.get(uploadDirectory);
            Files.createDirectories(path);
            Path filePath = path.resolve(fileName);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, filePath);
            }

            String[] empGroup = document.getEmpOrg().split("\\.");

            d.setItemName(fileName);
            d.setDocType(contentType);
            d.setEmpId(document.getEmpId());
            d.setEmpOrg(document.getEmpOrg().toUpperCase());
            d.setLocation(document.getLocation().toUpperCase());
            d.setDocTags((document.getDocTags() != null) ? document.getDocTags().toUpperCase() : "");
            d.setEmpGroup(empGroup[empGroup.length - 1].toUpperCase());
            d.setDocPath(filePath.toString().replace("\\", fileSeparator).replaceFirst("^root" + rootSeparator, ""));
            d.setItemType("FILE");
            d.setCreatedByRole("ADMIN");
            d.setParentDocId(parentId);
            d.setSource(Source.CMS);
            d.setDocCaption(document.getDocCaption());
            d.setDocDescription(document.getDocDescription());

            doc = repo.save(d);

            documents.add(doc);
        }

        return ModelConverter.convertToDocumentResDTOList(documents);
    }

    @CacheEvict(value = "documentsCache", allEntries = true)
    public List<DocResDTO> uploadDocumentsFromOtherServices(Document document, List<MultipartFile> files, String uploadDirectory) throws IOException {
        uploadDirectory = uploadDirectory.replace("-", fileSeparator);

        List<Document> documents = new ArrayList<>();

        Document doc = new Document();
        long parentId;

        String folderName = repo.findByItemName(uploadDirectory.replaceFirst("^root" + rootSeparator, ""), String.valueOf(document.getEmpId()));
        System.out.println(document.getEmpId());

        if (folderName == null) {
            Document createFolderData = new Document();

            createFolderData.setItemName(String.valueOf(document.getEmpId()));
            createFolderData.setEmpId(document.getEmpId());
            createFolderData.setEmpGroup("ADMIN");
            createFolderData.setEmpOrg(document.getEmpOrg());
            createFolderData.setLocation(document.getLocation());
            createFolderData.setItemType("FOLDER");
            createFolderData.setCreatedByRole("ADMIN");
            createFolderData.setDocCaption(String.valueOf(document.getEmpId()));
            createFolderData.setDocDescription(document.getDocDescription());

            DocResDTO documentDTO = createFolder(uploadDirectory, createFolderData);
            uploadDirectory = uploadDirectory + fileSeparator + document.getEmpId();

            parentId = uploadDirectory.equalsIgnoreCase("root") ? 0 : repo.findByDocPath(uploadDirectory.replaceFirst("^root" + rootSeparator, "")).getDocId();
        } else {
            uploadDirectory = uploadDirectory + fileSeparator + document.getEmpId();
            parentId = uploadDirectory.equalsIgnoreCase("root") ? 0 : repo.findByDocPath(uploadDirectory.replaceFirst("^root" + rootSeparator, "")).getDocId();
        }

        for (MultipartFile file : files) {
            String uniqueID = UUID.randomUUID().toString();
            String name = file.getOriginalFilename();
            String fileName = document.getEmpId() + "-" + uniqueID + "-" + name;

            String contentType = Objects.requireNonNull(file.getContentType()).replaceAll("application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                    "application/msword");

            Document d = new Document();

            Path path = Paths.get(uploadDirectory);
            Files.createDirectories(path);
            Path filePath = path.resolve(fileName);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, filePath);
            }

            String[] empGroup = document.getEmpOrg().split("\\.");

            d.setItemName(fileName);
            d.setDocType(contentType);
            d.setEmpId(document.getEmpId());
            d.setEmpOrg(document.getEmpOrg().toUpperCase());
            d.setLocation(document.getLocation().toUpperCase());
            d.setDocTags((document.getDocTags() != null) ? document.getDocTags().toUpperCase() : "");
            d.setEmpGroup(empGroup[empGroup.length - 1].toUpperCase());
            d.setDocPath(filePath.toString().replace("\\", fileSeparator).replaceFirst("^root" + rootSeparator, ""));
            d.setItemType("FILE");
            d.setCreatedByRole("ADMIN");
            d.setParentDocId(parentId);
            d.setSource(Source.CMS);
            d.setDocCaption(file.getOriginalFilename());
            d.setDocDescription(document.getDocDescription());

            doc = repo.save(d);

            documents.add(doc);
        }

        return ModelConverter.convertToDocumentResDTOList(documents);

    }




    @CacheEvict(value = "documentsCache", allEntries = true)
    public DocResDTO uploadFormBuilderDocument(String uploadDirectory, MultipartFile file) throws IOException {
        uploadDirectory = uploadDirectory.replace("-", fileSeparator);

        long parentId = uploadDirectory.equalsIgnoreCase("root") ? 0 : repo.findByDocPath(uploadDirectory.replaceFirst("^root" + rootSeparator, "")).getDocId();


        String uniqueID = UUID.randomUUID().toString();
        String name = file.getOriginalFilename();
        String fileName = uniqueID + "-" + name;

        String contentType = Objects.requireNonNull(file.getContentType()).replaceAll("application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "application/msword");

        Document doc = new Document();

        Path path = Paths.get(uploadDirectory);
        Files.createDirectories(path);
        Path filePath = path.resolve(fileName);

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath);
        }

        doc.setItemName(fileName);
        doc.setDocType(contentType);
        doc.setDocPath(filePath.toString().replace("\\", fileSeparator).replaceFirst("^root" + rootSeparator, ""));
        doc.setItemType("FILE");
        doc.setCreatedByRole("ADMIN");
        doc.setParentDocId(parentId);
        doc.setSource(Source.CMS);

        Document document = repo.save(doc);


        return ModelConverter.convertToDocumentResDTO(document);
    }





//    public String setDocumentID() {
//        String docid = repo.findByDocID();
//
//        if (docid == null) {
//            return "DOC301";
//        } else {
//            int val = Integer.parseInt(docid.substring(3));
//            return "DOC" + (val + 1);
//        }
//    }

    /**
     * @param file
     * @param docId
     * @param userRole
     * @param uploadDirectory
     * @return
     * @throws IOException
     */
    @CacheEvict(value = "documentsCache", allEntries = true)
    public String updateDocService(MultipartFile file, long docId, String userRole, String uploadDirectory) throws IOException {

        if (userRole.equalsIgnoreCase("Employee") || userRole.equalsIgnoreCase("Admin")) {

            Document d = repo.findByDocId(docId);

            String oldFileName = d.getItemName();
            String[] oldFileNameSplit = oldFileName.split("[\\.-]");

            String uniqueID = UUID.randomUUID().toString();
            String newFileName = d.getEmpId() + "-" + uniqueID + "-" + file.getOriginalFilename();
            String[] newFileNameSplit = newFileName.split("[\\.-]");

            if (oldFileNameSplit[oldFileNameSplit.length - 2].equals(newFileNameSplit[newFileNameSplit.length - 2])) {
                Path path = Paths.get(uploadDirectory.replace("-", fileSeparator));
                Path newFilePath = path.resolve(newFileName);
                Path oldFilePath = path.resolve(oldFileName);

                File uploadedFile = oldFilePath.toFile();
                uploadedFile.delete();

                try (InputStream inputStream = file.getInputStream()) {
                    Files.copy(inputStream, newFilePath);
                }

                d.setItemName(newFileName);
                d.setDocType(file.getContentType());
                d.setDocPath(newFilePath.toString().replace("\\", fileSeparator).replaceFirst("^root" + rootSeparator, ""));

                repo.save(d);

                return "Updated Successfully";
            } else {
                return "File Name Should Match With Old FIle Name";
            }
        } else {
            return "Authorized Person Only Update";
        }

    }
    @CacheEvict(value = "documentsCache", allEntries = true)
    public DocResDTO updateDocFromOtherService(DocReqDTO docReqDTO, MultipartFile file, long docId, String userRole, String uploadDirectory) throws IOException {

        if (userRole.equalsIgnoreCase("Employee") || userRole.equalsIgnoreCase("Admin")) {
            Document document = ModelConverter.convertToDocument(docReqDTO);

            uploadDirectory = uploadDirectory.replace("-", fileSeparator);

            List<Document> documents = new ArrayList<>();
            long parentId;

            String folderName = repo.findByItemName(uploadDirectory.replaceFirst("^root" + rootSeparator, ""), String.valueOf(document.getEmpId()));

            if (folderName == null) {
                Document createFolderData = new Document();

                createFolderData.setItemName(String.valueOf(document.getEmpId()));
                createFolderData.setEmpId(document.getEmpId());
                createFolderData.setEmpGroup("ADMIN");
                createFolderData.setEmpOrg(document.getEmpOrg());
                createFolderData.setLocation(document.getLocation());
                createFolderData.setItemType("FOLDER");
                createFolderData.setCreatedByRole("ADMIN");
                createFolderData.setDocCaption(String.valueOf(document.getEmpId()));
                createFolderData.setDocDescription(document.getDocDescription());

                DocResDTO documentDTO = createFolder(uploadDirectory, createFolderData);
                uploadDirectory = uploadDirectory + fileSeparator + document.getEmpId();

                parentId = uploadDirectory.equalsIgnoreCase("root") ? 0 : repo.findByDocPath(uploadDirectory.replaceFirst("^root" + rootSeparator, "")).getDocId();
            } else {
                uploadDirectory = uploadDirectory + fileSeparator + document.getEmpId();
                parentId = uploadDirectory.equalsIgnoreCase("root") ? 0 : repo.findByDocPath(uploadDirectory.replaceFirst("^root" + rootSeparator, "")).getDocId();
            }

            Document d = repo.findByDocId(docId);

            String oldFileName = d.getItemName();

            String uniqueID = UUID.randomUUID().toString();
            String newFileName = d.getEmpId() + "-" + uniqueID + "-" + file.getOriginalFilename();

            Path path = Paths.get(uploadDirectory.replace("-", fileSeparator));
            Path newFilePath = path.resolve(newFileName);
            Path oldFilePath = path.resolve(oldFileName);

            File uploadedFile = oldFilePath.toFile();
            uploadedFile.delete();

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, newFilePath);
            }

            d.setItemName(newFileName);
            d.setDocType(file.getContentType());
            d.setDocPath(newFilePath.toString().replace("\\", fileSeparator).replaceFirst("^root" + rootSeparator, ""));
            d.setParentDocId(parentId);

            Document doc = repo.save(d);

            return ModelConverter.convertToDocumentResDTO(doc);
        } else {
            return null;
        }

    }
    /**
     * @param folderDirectory
     * @param document
     * @return
     * @throws IOException
     */
    @CacheEvict(value = "documentsCache", allEntries = true)
//    @CachePut(value = "documentsCache", key = "#currentDirectory")
    public DocResDTO createFolder(String folderDirectory, Document document) throws IOException {
        long parentId;
        Document doc = new Document();

        folderDirectory = folderDirectory.replace("-", fileSeparator);

        if (folderDirectory.equalsIgnoreCase("root")) {
            parentId = 0;
        } else {
            Document d = repo.findByDocPath(folderDirectory.replaceFirst("^root" + rootSeparator, ""));
            parentId = d.getDocId();
        }

        Path path = Paths.get(folderDirectory + File.separator + document.getItemName());

        if (!Files.exists(path)) {
            Files.createDirectory(path);

//            doc.setDocID(setDocumentID());
            doc.setItemName(document.getItemName());
            doc.setDocPath((folderDirectory + fileSeparator + document.getItemName()).replaceFirst("^root" + rootSeparator, ""));
            doc.setEmpId(document.getEmpId());
            doc.setEmpGroup(document.getEmpGroup().toUpperCase());
            doc.setEmpOrg(document.getEmpOrg().toUpperCase());
            doc.setLocation(document.getLocation().toUpperCase());
            doc.setItemType(document.getItemType().toUpperCase());
            doc.setCreatedByRole(document.getCreatedByRole().toUpperCase());
            doc.setSource(Source.CMS);
//            doc.setFileOrFolderId(autoIncrementFileOrFolderId());
            doc.setParentDocId(parentId);
            doc.setDocCaption(document.getDocCaption());
            doc.setDocDescription(document.getDocDescription());

            Document document1 = repo.save(doc);

            return ModelConverter.convertToDocumentResDTO(document1);
        } else {
            return null;
        }

    }

//    public int autoIncrementFileOrFolderId() {
//        return repo.findByFileOrFolderId()+1;
//    }

    @CacheEvict(value = "documentsCache", allEntries = true)
    public boolean deleteFolder(String folderDirectory, String folderName) throws IOException {

        folderDirectory = folderDirectory.replace("-", fileSeparator);

        Path path = Paths.get(folderDirectory);
        Path folderPath = path.resolve(folderName);
        File delFolder = folderPath.toFile();

        String documentPath = folderDirectory + fileSeparator + folderName;

        deleteDirectory(delFolder);
        delFolder.delete();

        repo.deleteById(repo.findByDocPath(documentPath.replaceFirst("^root" + rootSeparator, "")).getDocId());

        return true;

    }

    @CacheEvict(value = "documentsCache", allEntries = true)
    public void deleteDirectory(File file) {

        for (File subFile : Objects.requireNonNull(file.listFiles())) {
            if (subFile.isDirectory()) {
                deleteDirectory(subFile);
            }

            Document document = repo.findByDocPath(subFile.getPath().replace("\\", fileSeparator).replaceFirst("^root" + rootSeparator, ""));

            boolean deletionSuccess = subFile.delete();

            if (deletionSuccess) {
                repo.deleteById(document.getDocId());
            }

        }
    }

    @CacheEvict(value = "documentsCache", allEntries = true)
    public boolean deleteFile(String fileDirectory, String fileName) {
        fileDirectory = fileDirectory.replace("-", fileSeparator);

        Path path = Paths.get(fileDirectory);
        Path folderPath = path.resolve(fileName);
        File delFolder = folderPath.toFile();

        String documentPath = fileDirectory + fileSeparator + fileName;

        delFolder.delete();
        repo.deleteById(repo.findByDocPath(documentPath.replaceFirst("^root" + rootSeparator, "")).getDocId());

        return true;
    }

    @CacheEvict(value = "documentsCache", allEntries = true)
    public boolean deleteFileFromOtherServices(long docId) {
        Optional<Document> document = repo.findById(docId);

        if(document.isPresent()){
            String filePath = "root"+fileSeparator+document.get().getDocPath();
            Path path = Paths.get(filePath);
            File delFile = path.toFile();

            delFile.delete();

            repo.deleteById(docId);

            return true;
        }
        else {
            return false;
        }
    }


    public List<String> getCompanyPolicies() {
        String currentDirectory = companyPolicyDocumentsPath.replace("-", fileSeparator);
        Path directory = Paths.get(currentDirectory);
        try {
            List<String> pathList = new ArrayList<>(Files.walk(directory)
                    .filter(path -> !path.equals(directory))
                    .map(directory::relativize)
                    .map(Path::toString)
                    .map(path -> path.split(fileSeparator)[0])
                    .distinct()
                    .toList());

            pathList.sort(String.CASE_INSENSITIVE_ORDER);

            return pathList;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<DocResDTO> getMyDocuments(long empId) {
        String sourceDirectory = myDocumentsPath.replace("-", fileSeparator) + empId;

        Document doc = repo.findByDocPath(sourceDirectory.replaceFirst("^root" + rootSeparator, ""));

        List<Document> document = repo.findByParentDocId(doc.getDocId());
        return ModelConverter.convertToDocumentResDTOList(document);
    }

    @Override
    public List<DocResDTO> getDocuments(String sourceDirectory, long empId) {
        sourceDirectory = sourceDirectory.replace("-", fileSeparator).replaceFirst("^root" + rootSeparator, "");
        List<Document> document = repo.findByEmpID(sourceDirectory, empId);
        System.out.println(document);
        return ModelConverter.convertToDocumentResDTOList(document);
    }

    @Cacheable(value = "documentsCache", key = "#sourceDirectory")
    public List<DocResDTO> getFolders(String sourceDirectory) {
        List<Document> documents;
        if (sourceDirectory.equalsIgnoreCase("root")) {
            documents = repo.findByParentDocId(0);
        } else {
            String currDirForGettingPaths = sourceDirectory.replace("-", fileSeparator);
            Document doc = repo.findByDocPath(currDirForGettingPaths.replaceFirst("^root" + rootSeparator, ""));
            documents = repo.findByParentDocId(doc.getDocId());
        }

        List<Document> formattedName = documents.stream()
                .sorted(Comparator.comparing(doc -> DocumentNameFormatter.formatItemName(doc.getItemName()).toLowerCase()))
                .map(doc -> {
                    doc.setItemName(DocumentNameFormatter.formatItemName(doc.getItemName()));
                    return doc;
                })
                .toList();

        return ModelConverter.convertToDocumentResDTOList(formattedName);
    }

    public FileAndContentType openFileByDocId(long docId) throws IOException {

        Optional<Document> document = repo.findById(docId);
        Document doc = new Document();

        if (document.isPresent()) {
            doc = document.get();
        }

        String docPath = uploadDirectory + fileSeparator + doc.getDocPath();

        Path fileDIr = Paths.get(docPath);

        byte[] file = Files.readAllBytes(new File(fileDIr.toString().replace("\\", fileSeparator)).toPath());

        String contentType = doc.getDocType();

        return new FileAndContentType(file, contentType);
    }

    @Override
    public FileAndContentType downloadMultipleFiles(List<Long> docIds) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {

            for (Long docId : docIds) {
                Optional<Document> document = repo.findById(docId);
                Document doc = document.orElseThrow(() -> new IOException("Document not found for ID: " + docId));

                String docPath = uploadDirectory + fileSeparator + doc.getDocPath();
                Path fileDir = Paths.get(docPath);

                try {
                    byte[] file = Files.readAllBytes(new File(fileDir.toString().replace("\\", fileSeparator)).toPath());
                    zipOutputStream.putNextEntry(new ZipEntry(doc.getDocPath()));
                    zipOutputStream.write(file);
                    zipOutputStream.closeEntry();
                } catch (NoSuchFileException e) {
                    System.err.println("File not found: " + docPath);
                }
            }

            zipOutputStream.finish();
            return new FileAndContentType(byteArrayOutputStream.toByteArray(), "application/zip");
        }
    }

    public FileAndContentType openCompanyPolicies(String itemName) throws IOException {
        String contentType = "";

        String currentDirectory = companyPolicyDocumentsPath.replace("-", fileSeparator);

        String docPath = currentDirectory + fileSeparator + itemName;

        Path fileDIr = Paths.get(docPath);

        byte[] file = Files.readAllBytes(new File(fileDIr.toString().replace("\\", fileSeparator)).toPath());

        if (itemName.contains(".pdf")) {
            contentType = "application/pdf";
        } else if (itemName.contains(".jpg") || itemName.contains(".jpeg")) {
            contentType = "image/jpeg";
        }
        return new FileAndContentType(file, contentType);
    }

    @Cacheable(value = "sizeCache", key = "#sourceDirectory")
    public Map<String, Object> getPropertiesOfFileOrFolder(String sourceDirectory, String fileOrFolderName) throws IOException {
        Map<String, Object> response = new HashMap<>();

        sourceDirectory = sourceDirectory.replace("-", fileSeparator);

        String docPath = sourceDirectory + fileSeparator + fileOrFolderName;

        Document document = repo.findByDocPath(docPath.replaceFirst("^root" + rootSeparator, ""));

        Path path = Paths.get(sourceDirectory);
        Path folderPath = path.resolve(fileOrFolderName);

        String size = formatSize(calculateSize(folderPath));

        response.put("document", document);
        response.put("size", size);

        return response;
    }

    private String formatSize(long size) {
        if (size >= 1024 * 1024) {
            return String.format("%.2f MB", size / (1024.0 * 1024));
        } else {
            return String.format("%.2f KB", size / 1024.0);
        }
    }

    private long calculateSize(Path folderPath) throws IOException {
        if (Files.isDirectory(folderPath)) {
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(folderPath)) {
                long size = 0;
                for (Path entry : directoryStream) {
                    size += calculateSize(entry);
                }
                return size;
            }
        } else {
            return Files.size(folderPath);
        }
    }

    public DocResDTO getFileDataByCurrentDirectory(String sourceDirectory, String itemName) {
        sourceDirectory = sourceDirectory.replace("-", fileSeparator);
        String docPath = sourceDirectory + fileSeparator + itemName;
        Document document = repo.findByDocPath(docPath.replaceFirst("^root" + rootSeparator, ""));
        return ModelConverter.convertToDocumentResDTO(document);
    }

    @CacheEvict(value = "documentsCache", allEntries = true)
    public List<DocResDTO> renameFolder(String currentDirectory, String oldFolderName, String newFolderName, String loggedInUserID) throws IOException {
        currentDirectory = currentDirectory.replace("-", fileSeparator);
        String oldDocPath = currentDirectory + fileSeparator + oldFolderName;
        String newDocPath = currentDirectory + fileSeparator + newFolderName;
        List<Document> documents = new ArrayList<>();


        Document document = repo.findByDocPath(oldDocPath.replaceFirst("^root" + rootSeparator, ""));
        List<Document> docList = repo.findByDocPaths((oldDocPath.replaceFirst("^root" + rootSeparator, "") + fileSeparator).replace(fileSeparator, rootSeparator));

        Document checkFolderExistsOrNot = repo.findByDocPath(newDocPath.replaceFirst("^root" + rootSeparator, ""));

        if (checkFolderExistsOrNot == null) {
            Path currentPath = Paths.get(oldDocPath);
            Path newPath = Paths.get(newDocPath);

            Files.move(currentPath, newPath, StandardCopyOption.REPLACE_EXISTING);

            document.setDocPath(newPath.toString().replace("\\", fileSeparator).replaceFirst("^root" + rootSeparator, ""));
            document.setItemName(newFolderName);

            repo.save(document);

            for (Document doc : docList) {
                String docPath = doc.getDocPath();
                if (docPath.contains(oldDocPath.replaceFirst("^root" + rootSeparator, ""))) {
                    String newDocPath1 = docPath.replace(oldFolderName, newFolderName);
                    doc.setDocPath(newDocPath1.replaceFirst("^root" + rootSeparator, ""));
                    documents.add(repo.save(doc));
                }
            }

            recordsService.trackAccessRecord("RENAME FOLDER", document.getDocId(), loggedInUserID);

//            updateDocumentCacheKeys(currentDirectory, oldFolderName, newFolderName);

            return ModelConverter.convertToDocumentResDTOList(documents);
        } else {
            return null;
        }
    }

    public List<String> searchFileAndFolders(String currentDirectory, String searchTerm) {
        currentDirectory = currentDirectory.replace("-", fileSeparator);
        long parentDocId;

        if (currentDirectory.equalsIgnoreCase("root")) {
            parentDocId = 0;
        } else {
            Document document = repo.findByDocPath(currentDirectory.replaceFirst("^root" + rootSeparator, ""));
            parentDocId = document.getDocId();
        }

        List<String> filesAndFolderNames = repo.findBySearchTermAndParentDocID(searchTerm, parentDocId);

        return filesAndFolderNames;
    }


//    private void updateDocumentCacheKeys(String currentDirectory, String oldFolderName, String newFolderName) {
//        try (Jedis jedis = jedisPool.getResource()) {
//            // Update main folder key
//            String mainOldKey = "documentsCache::" + currentDirectory + "-" + oldFolderName;
//            String mainNewKey = "documentsCache::" + currentDirectory + "-" + newFolderName;
//            String mainValue = jedis.get(mainOldKey);
//            if (mainValue != null) {
//                jedis.set(mainNewKey, mainValue);
//                jedis.del(mainOldKey);
//            }
//
//            // Pattern to match all nested keys starting with the old folder name
//            String oldKeyPattern = "documentsCache::" + currentDirectory + "-" + oldFolderName + "*";
//            Set<String> keys = jedis.keys(oldKeyPattern);
//
//            // To avoid infinite recursion, keep track of already processed keys
////            Set<String> processedKeys = new HashSet<>();
//
//            for (String key : keys) {
//                // Only replace the first occurrence of the old folder name in the key
//                if (!key.contains(newFolderName)) {
//                    String newKey = key.replaceFirst(oldFolderName, newFolderName);
//                    String value = jedis.get(key);
//                    jedis.set(newKey, value);
//                    jedis.del(key);
//
//                    // Recursively update nested keys
//                    updateNestedKeys(jedis, newKey, oldFolderName, newFolderName);
//                }
//            }
//        }
//    }
//        private void updateNestedKeys (Jedis jedis, String parentKey, String oldFolderName, String newFolderName){
//            // Pattern to match nested keys
//            String nestedOldKeyPattern = parentKey + "*";
//            Set<String> nestedKeys = jedis.keys(nestedOldKeyPattern);
//
//            for (String nestedKey : nestedKeys) {
//                // Only replace the first occurrence of the old folder name in the nested key
//                if (!nestedKey.contains(newFolderName)) {
//                    String nestedNewKey = nestedKey.replaceFirst(oldFolderName, newFolderName);
//                    String nestedValue = jedis.get(nestedKey);
//                    jedis.set(nestedNewKey, nestedValue);
//                    jedis.del(nestedKey);
//
//                    // Recursively update further nested keys
//                    updateNestedKeys(jedis, nestedNewKey, oldFolderName, newFolderName);
//                }
//            }
//        }

}
