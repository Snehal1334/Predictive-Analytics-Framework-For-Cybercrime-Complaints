package com.cybercrimeanalytics.controller;

import com.cybercrimeanalytics.dto.request.ComplaintRequest;
import com.cybercrimeanalytics.dto.request.ComplaintSearchRequest;
import com.cybercrimeanalytics.dto.response.ComplaintResponse;
import com.cybercrimeanalytics.exception.ApiError;
import com.cybercrimeanalytics.service.ComplaintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/complaints")
@RequiredArgsConstructor
@Tag(name = "Complaints", description = "Complaint creation, retrieval, filtering, pagination and lifecycle APIs.")
public class ComplaintController {
    private final ComplaintService complaintService;

    @Operation(
            summary = "Create complaint",
            description = "Creates a cybercrime complaint and stores ML prediction metadata.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Complaint created"),
                    @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = ApiError.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class)))
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ComplaintResponse create(@Valid @RequestBody ComplaintRequest request, Authentication authentication) {
        return complaintService.create(request, authentication.getName());
    }

    @Operation(
            summary = "Search complaints",
            description = "Returns paginated complaints with optional keyword, category, severity, status and date-range filters. Supports Spring pageable params: page, size, sort.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Complaints returned"),
                    @ApiResponse(responseCode = "400", description = "Invalid filter", content = @Content(schema = @Schema(implementation = ApiError.class)))
            }
    )
    @GetMapping
    Page<ComplaintResponse> search(@Parameter(description = "Filtering parameters") @ModelAttribute ComplaintSearchRequest search,
                                   @Parameter(description = "Pagination and sorting parameters") @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        return complaintService.search(search, pageable);
    }

    @Operation(
            summary = "Get complaint by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Complaint found"),
                    @ApiResponse(responseCode = "404", description = "Complaint not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
            }
    )
    @GetMapping("/{id}")
    ComplaintResponse get(@Parameter(description = "Complaint id") @PathVariable UUID id) {
        return complaintService.get(id);
    }

    @Operation(
            summary = "Update complaint",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Complaint updated"),
                    @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = ApiError.class))),
                    @ApiResponse(responseCode = "404", description = "Complaint not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
            }
    )
    @PutMapping("/{id}")
    ComplaintResponse update(@Parameter(description = "Complaint id") @PathVariable UUID id, @Valid @RequestBody ComplaintRequest request) {
        return complaintService.update(id, request);
    }

    @Operation(
            summary = "Soft delete complaint",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Complaint deleted"),
                    @ApiResponse(responseCode = "404", description = "Complaint not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
            }
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@Parameter(description = "Complaint id") @PathVariable UUID id) {
        complaintService.delete(id);
    }
}
