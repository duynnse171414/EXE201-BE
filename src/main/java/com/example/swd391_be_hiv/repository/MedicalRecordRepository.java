package com.example.swd391_be_hiv.repository;

import com.example.swd391_be_hiv.entity.MedicalRecord;
import com.example.swd391_be_hiv.entity.TreatmentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    MedicalRecord findMedicalRecordByMedicalRecordId(Long medicalRecordId);

    List<MedicalRecord> findMedicalRecordsByCustomer_Id(Long customerId);

    List<MedicalRecord> findMedicalRecordsByDoctor_Id(Long doctorId);

    List<MedicalRecord> findMedicalRecordsByCd4CountBetween(Integer minCd4, Integer maxCd4);

    List<MedicalRecord> findMedicalRecordsByViralLoadBetween(Double minViralLoad, Double maxViralLoad);

    List<MedicalRecord> findMedicalRecordsByCd4CountGreaterThan(Integer cd4Count);

    List<MedicalRecord> findMedicalRecordsByCd4CountLessThan(Integer cd4Count);

    List<MedicalRecord> findMedicalRecordsByViralLoadGreaterThan(Double viralLoad);

    List<MedicalRecord> findMedicalRecordsByViralLoadLessThan(Double viralLoad);

    List<MedicalRecord> findMedicalRecordsByLastUpdatedAfter(LocalDateTime date);

    List<MedicalRecord> findMedicalRecordsByLastUpdatedBefore(LocalDateTime date);

    List<MedicalRecord> findMedicalRecordsByLastUpdatedBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<MedicalRecord> findMedicalRecordsByDoctor_DeletedFalse();

    @Query("SELECT mr FROM MedicalRecord mr WHERE mr.treatmentHistory LIKE %:keyword%")
    List<MedicalRecord> findMedicalRecordsByTreatmentHistoryContaining(@Param("keyword") String keyword);

    @Query("SELECT mr FROM MedicalRecord mr WHERE mr.lastUpdated = " +
            "(SELECT MAX(mr2.lastUpdated) FROM MedicalRecord mr2 WHERE mr2.customer.id = mr.customer.id)")
    List<MedicalRecord> findLatestMedicalRecordForEachCustomer();

    @Query("SELECT mr FROM MedicalRecord mr WHERE mr.cd4Count < 200")
    List<MedicalRecord> findCriticalCd4Records();

    @Query("SELECT mr FROM MedicalRecord mr WHERE mr.viralLoad < 50")
    List<MedicalRecord> findUndetectableViralLoadRecords();

}