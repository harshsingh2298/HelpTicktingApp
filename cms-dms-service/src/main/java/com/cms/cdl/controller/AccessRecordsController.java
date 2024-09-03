package com.cms.cdl.controller;

import com.cms.cdl.serviceImplementation.AccessRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class AccessRecordsController {
    @Autowired
    AccessRecordsService accessRecordsService;



}
