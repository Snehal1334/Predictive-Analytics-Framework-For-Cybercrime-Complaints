package com.cybercrimeanalytics.service.impl;

import com.cybercrimeanalytics.dto.request.ComplaintRequest;
import com.cybercrimeanalytics.dto.request.ComplaintSearchRequest;
import com.cybercrimeanalytics.dto.response.ComplaintResponse;
import com.cybercrimeanalytics.entity.Complaint;
import com.cybercrimeanalytics.entity.Location;
import com.cybercrimeanalytics.exception.ApiException;
import com.cybercrimeanalytics.mapper.ComplaintMapper;
import com.cybercrimeanalytics.repository.ComplaintRepository;
import com.cybercrimeanalytics.repository.CrimeCategoryRepository;
import com.cybercrimeanalytics.repository.UserRepository;
import com.cybercrimeanalytics.service.ComplaintService;
import com.cybercrimeanalytics.service.PredictionService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ComplaintServiceImpl implements ComplaintService {
    private final ComplaintRepository complaintRepository;
    private final CrimeCategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ComplaintMapper complaintMapper;
    private final PredictionService predictionService;

    @Override
    @Transactional
    public ComplaintResponse create(ComplaintRequest request, String username) {
        var user = userRepository.findByEmailAndDeletedFalse(username)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "User not found"));
        Complaint complaint = new Complaint();
        apply(request, complaint);
        complaint.setCreatedBy(user);
        complaintRepository.save(complaint);
        var prediction = predictionService.predictAndSave(complaint);
        complaint.setPrediction(prediction);
        return complaintMapper.toResponse(complaint);
    }

    @Override
    @Transactional
    public ComplaintResponse update(UUID id, ComplaintRequest request) {
        Complaint complaint = findActive(id);
        apply(request, complaint);
        complaintRepository.save(complaint);
        var prediction = predictionService.predictAndSave(complaint);
        complaint.setPrediction(prediction);
        return complaintMapper.toResponse(complaint);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Complaint complaint = findActive(id);
        complaint.setDeleted(true);
        complaintRepository.save(complaint);
    }

    @Override
    @Transactional(readOnly = true)
    public ComplaintResponse get(UUID id) {
        return complaintMapper.toResponse(findActive(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ComplaintResponse> search(ComplaintSearchRequest request, Pageable pageable) {
        validateDateRange(request);
        return complaintRepository.findAll(spec(request), pageable).map(complaintMapper::toResponse);
    }

    private Complaint findActive(UUID id) {
        return complaintRepository.findById(id)
                .filter(c -> !c.isDeleted())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Complaint not found"));
    }

    private void apply(ComplaintRequest request, Complaint complaint) {
        complaint.setTitle(request.title());
        complaint.setDescription(request.description());
        complaint.setIncidentDate(request.incidentDate());
        complaint.setAttachments(request.attachments());
        if (request.severity() != null) complaint.setSeverity(request.severity());
        if (request.status() != null) complaint.setStatus(request.status());
        if (request.categoryId() != null) {
            complaint.setCategory(categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Invalid category")));
        }
        Location location = complaint.getLocation() == null ? new Location() : complaint.getLocation();
        location.setAddress(request.address());
        location.setCity(request.city());
        location.setState(request.state());
        location.setPostalCode(request.postalCode());
        location.setLatitude(request.latitude());
        location.setLongitude(request.longitude());
        complaint.setLocation(location);
    }

    private Specification<Complaint> spec(ComplaintSearchRequest request) {
        return (root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();
            predicates.add(cb.isFalse(root.get("deleted")));
            if (request.query() != null && !request.query().isBlank()) {
                String like = "%" + request.query().toLowerCase() + "%";
                predicates.add(cb.or(cb.like(cb.lower(root.get("title")), like), cb.like(cb.lower(root.get("description")), like)));
            }
            if (request.categoryId() != null) predicates.add(cb.equal(root.get("category").get("id"), request.categoryId()));
            if (request.severity() != null) predicates.add(cb.equal(root.get("severity"), request.severity()));
            if (request.status() != null) predicates.add(cb.equal(root.get("status"), request.status()));
            if (request.fromDate() != null) predicates.add(cb.greaterThanOrEqualTo(root.get("incidentDate"), request.fromDate()));
            if (request.toDate() != null) predicates.add(cb.lessThanOrEqualTo(root.get("incidentDate"), request.toDate()));
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    private void validateDateRange(ComplaintSearchRequest request) {
        if (request.fromDate() != null && request.toDate() != null && request.fromDate().isAfter(request.toDate())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "fromDate must be before or equal to toDate");
        }
    }
}
