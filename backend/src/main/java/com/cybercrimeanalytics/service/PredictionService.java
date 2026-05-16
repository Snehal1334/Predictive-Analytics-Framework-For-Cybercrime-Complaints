package com.cybercrimeanalytics.service;

import com.cybercrimeanalytics.entity.Complaint;
import com.cybercrimeanalytics.entity.Prediction;

public interface PredictionService {
    Prediction predictAndSave(Complaint complaint);
}
