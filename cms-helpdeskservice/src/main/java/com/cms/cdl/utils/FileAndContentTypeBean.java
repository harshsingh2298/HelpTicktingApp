package com.cms.cdl.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileAndContentTypeBean {
    private final byte[] file;
    private final String contentType;
}