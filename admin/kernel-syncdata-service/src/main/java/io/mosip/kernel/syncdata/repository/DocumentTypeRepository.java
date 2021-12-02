package io.mosip.kernel.syncdata.repository;

import java.time.LocalDateTime;
import java.util.List;

import io.mosip.kernel.syncdata.dto.EntityDtimes;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.mosip.kernel.syncdata.entity.DocumentType;

/**
 * 
 * @author Abhishek Kumar
 * @author Uday Kumar
 * @since 1.0.0
 *
 */
@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, String> {
	/**
	 * Method to find list of DocumentType created , updated or deleted time is
	 * greater than lastUpdated timeStamp.
	 * 
	 * @param lastUpdated      timeStamp - last updated timestamp
	 * @param currentTimeStamp - currentTimestamp
	 * @return list of {@link DocumentType} - list of document type
	 */
	@Cacheable(cacheNames = "initial-sync", key = "'doc_type'", condition = "#a0.getYear() <= 1970")
	@Query("FROM DocumentType WHERE (createdDateTime BETWEEN ?1 AND ?2) OR (updatedDateTime BETWEEN ?1 AND ?2)  OR (deletedDateTime BETWEEN ?1 AND ?2)")
	List<DocumentType> findAllLatestCreatedUpdateDeleted(LocalDateTime lastUpdated, LocalDateTime currentTimeStamp);

	@Cacheable(cacheNames = "delta-sync", key = "'doc_type'")
	@Query(value = "select new io.mosip.kernel.syncdata.dto.EntityDtimes(max(aam.createdDateTime), max(aam.updatedDateTime), max(aam.deletedDateTime)) from DocumentType aam ")
	EntityDtimes getMaxCreatedDateTimeMaxUpdatedDateTime();

}
