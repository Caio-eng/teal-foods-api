package br.com.foods.teal.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Serviço para imahens do produto
 * 
 * @author Caio Pereira Leal
 */
@Service
public class ImageStorageService {

    @Value("${app.upload.dir:${user.home}}")
    private String uploadDir;

    /**
     * Retorna lista de imagens do armazenamento
     * 
     * @param imageFiles
     * 				lista de arquivos de imagem
     * 
     * @return arquivo com nome
     */
    public List<String> storeImages(List<MultipartFile> imageFiles) {
        List<String> fileNames = new ArrayList<>();
        
        try {
            Path uploadPath = Paths.get(uploadDir + "/product-images");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            for (MultipartFile file : imageFiles) {
                if (file.isEmpty()) continue;
                
                String fileName = file.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                fileNames.add("/product-images/" + fileName);
            }
        } catch (IOException e) {
            throw new RuntimeException("Falha ao armazenar imagens", e);
        }
        
        return fileNames;
    }
}
