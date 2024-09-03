package com.cms.cdl.static_classes;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileAndContentType {
    private final byte[] file;
    private final String contentType;
}
