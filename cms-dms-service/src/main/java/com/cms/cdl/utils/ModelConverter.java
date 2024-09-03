package com.cms.cdl.utils;

import com.cms.cdl.Entity.Document;
import com.cms.cdl.dto.RequestDTO.DocReqDTO;
import com.cms.cdl.dto.ResponseDTO.DocResDTO;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ModelConverter {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static Document convertToDocument(DocReqDTO documentDTO) {
        return modelMapper.map(documentDTO, Document.class);
    }

    public static List<DocResDTO> convertToDocumentResDTOList(List<Document> documents) {
        return documents.stream()
                .map(document -> modelMapper.map(document, DocResDTO.class))
                .collect(Collectors.toList());
    }

    public static DocResDTO convertToDocumentResDTO(Document document){
        return modelMapper.map(document, DocResDTO.class);
    }
}
