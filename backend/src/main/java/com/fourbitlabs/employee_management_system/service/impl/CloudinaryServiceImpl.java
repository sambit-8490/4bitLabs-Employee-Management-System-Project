package com.fourbitlabs.employee_management_system.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fourbitlabs.employee_management_system.service.interfaces.CloudinaryService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("Cannot upload an empty file");
        }
        
        // The file type validation is configured on the frontend. Cloudinary raw storage accepts any file type safely!

        try {
            return cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "auto",
                            "use_filename", true,
                            "unique_filename", true,
                            "filename_override", file.getOriginalFilename()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException("File upload failed: " + e.getMessage());
        }
    }

    @Override
    public void deleteFile(String publicId) {
        if (publicId == null || publicId.isEmpty()) {
            return;
        }
        try {
            // Cloudinary's "auto" upload may have classified it as "image" (PDFs, Images) or "raw" (Office Docs).
            // We try to destroy both just in case, to ensure complete removal.
            cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", "image"));
            cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", "raw"));
        } catch (IOException e) {
            System.err.println("Cloudinary file deletion failed for publicId: " + publicId + " - " + e.getMessage());
        }
    }
}
