package com.cms.cdl.Repository;

import com.cms.cdl.Entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

	@Query(value = "select * from document where doc_path like ?1% AND empid = ?2", nativeQuery = true)
	List<Document> findByEmpID(String sourceDirectory, long empId);

	Document findByDocId(long docId);

//	@Query(value = "select max(docid) from document", nativeQuery = true)
//	public String findByDocId();

	@Query(value = "select item_name from document where doc_path like :directory% AND item_name = :itemName", nativeQuery = true)
	String findByItemName(String directory, String itemName);

	Document findByDocPath(String documentsPath);

	@Query(value = "select * from document where doc_path like ?%", nativeQuery = true)
	List<Document> findByDocPaths(String documentsPath);

	@Query(value = "SELECT * FROM Document WHERE parent_doc_id = :parentDocId", nativeQuery = true)
	List<Document> findByParentDocId(@Param("parentDocId") long parentDocId);

	@Query(value = "select * from document where doc_tags like ?%", nativeQuery = true)
	List<Document> findByDocTags(String docTags);

	@Query(value = "SELECT * FROM document WHERE created_date BETWEEN ?1 AND ?2"
			, nativeQuery = true)
	List<Document> findBetweenFromAndToDates(LocalDate fromDate, LocalDate toDate);

//	@Query(value = "SELECT COALESCE(MAX(file_or_folder_id), 0) FROM document", nativeQuery = true)
//	public int findByFileOrFolderId();

	@Query(value = "select item_name FROM document WHERE parent_docid = :parentDocId AND item_name like %:searchTerm%", nativeQuery = true)
	List<String> findBySearchTermAndParentDocID(@Param("searchTerm") String searchTerm, @Param("parentDocId") long parentDocId);

}
