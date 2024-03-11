package es.codeurjc.wonez.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    // Folder path to store images
    private static final Path FILES_FOLDER = Paths.get(System.getProperty("user.dir"), "images");

    // Method to create a file path based on image ID and folder
    private Path createFilePath(long imageId, Path folder) {
        return folder.resolve("image-" + imageId + ".jpg");
    }

    // Method to save an image to the specified folder
    public void saveImage(String folderName, long imageId, MultipartFile image) throws IOException {

        Path folder = FILES_FOLDER.resolve(folderName);

        // Create directories if they do not exist
        Files.createDirectories(folder);

        // Create a new file path for the image
        Path newFile = createFilePath(imageId, folder);

        // Transfer the image content to the new file
        image.transferTo(newFile);
    }

    // Method to create a response entity for retrieving an image by ID
    public ResponseEntity<Object> createResponseFromImage(String folderName, long imageId) throws MalformedURLException {

        Path folder = FILES_FOLDER.resolve(folderName);

        // Create a file path for the specified image ID
        Path imagePath = createFilePath(imageId, folder);

        // Create a resource from the image path
        Resource file = new UrlResource(imagePath.toUri());

        // Check if the file exists
        if (!Files.exists(imagePath)) {
            return ResponseEntity.notFound().build();
        } else {
            // Return a response entity with the image content
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg").body(file);
        }
    }

    // Method to delete an image by ID from the specified folder
    public void deleteImage(String folderName, long imageId) throws IOException {

        Path folder = FILES_FOLDER.resolve(folderName);

        // Create a file path for the specified image ID
        Path imageFile = createFilePath(imageId, folder);

        // Delete the image file if it exists
        Files.deleteIfExists(imageFile);
    }
}
