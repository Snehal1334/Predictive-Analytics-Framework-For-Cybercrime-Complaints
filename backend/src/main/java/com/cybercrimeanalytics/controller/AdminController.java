package com.cybercrimeanalytics.controller;

import com.cybercrimeanalytics.entity.CrimeCategory;
import com.cybercrimeanalytics.repository.CrimeCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final CrimeCategoryRepository crimeCategoryRepository;

    @GetMapping("/categories")
    List<CrimeCategory> categories() {
        return crimeCategoryRepository.findAll().stream().filter(c -> !c.isDeleted()).toList();
    }
}
