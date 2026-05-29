package com.fourbitlabs.employee_management_system.service.interfaces;

import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

public interface CloudinaryService {
    Map<String, Object> uploadFile(MultipartFile file);
    void deleteFile(String publicId);
}
