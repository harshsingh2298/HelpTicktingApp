package com.cms.cdl.Entity;

import com.cms.cdl.static_classes.Source;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
 public class Document {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long docId;
    private String itemName;
    private String docType;
    private String docPath;
    private boolean compressed;
	private long empId;
    private String empGroup;
    private String empOrg;
    private String docTags;
    private String location;
    @CreationTimestamp
    private LocalDate createdDate;
    @UpdateTimestamp()
	@Column(insertable=false)
    private LocalDate lastUpdatedDate;
    private String itemType;
    private String createdByRole;
    @Enumerated(EnumType.STRING)
    private Source source;
    private String docCaption;
    @Column(columnDefinition = "TEXT")
    private String docDescription;
    private long parentDocId;
}
