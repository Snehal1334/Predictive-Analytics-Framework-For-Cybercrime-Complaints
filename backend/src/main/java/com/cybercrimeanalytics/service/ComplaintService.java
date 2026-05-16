package com.cybercrimeanalytics.service;

import com.cybercrimeanalytics.dto.request.ComplaintRequest;
import com.cybercrimeanalytics.dto.request.ComplaintSearchRequest;
import com.cybercrimeanalytics.dto.response.ComplaintResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ComplaintService {
    ComplaintResponse create(ComplaintRequest request, String username);
    ComplaintResponse update(UUID id, ComplaintRequest request);
    void delete(UUID id);
    ComplaintResponse get(UUID id);
    Page<ComplaintResponse> search(ComplaintSearchRequest request, Pageable pageable);
}
