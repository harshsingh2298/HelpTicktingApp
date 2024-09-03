package com.cms.cdl.serviceImplementation;

import com.cms.cdl.Entity.AccessRecords;
import com.cms.cdl.Repository.AccessRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccessRecordsService {
    @Autowired
    AccessRecordRepository accessRecordRepository;

    public void trackAccessRecord(String activity, long documentID, String loggedInUserID){
        AccessRecords accessRecords = new AccessRecords();
        accessRecords.setActivity(activity);
        accessRecords.setDocumentId(documentID);
        accessRecords.setLoggedInUserID(loggedInUserID.toUpperCase());
        accessRecordRepository.save(accessRecords);
    }

}
