package reservationsystem.reservationcar.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import reservationsystem.reservationcar.DTO.PostDTO;
import reservationsystem.reservationcar.domain.Image;
import reservationsystem.reservationcar.domain.Post;
import reservationsystem.reservationcar.repository.ImageRepository;
import reservationsystem.reservationcar.repository.PostRepository;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Value("${upload.path}")
    private String uploadPath;

    @Transactional
    public PostDTO savePost(PostDTO postDTO, List<MultipartFile> files, String boardType) throws IOException {
        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setAuthor(postDTO.getAuthor());
        post.setBoardType(boardType);

        // Save post
        Post savedPost = postRepository.save(post);

        // Save images
        List<Image> images = new ArrayList<>();

        boolean allFilesEmpty = true;
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                allFilesEmpty = false;
                break;
            }
        }

        if (allFilesEmpty) {
            savedPost.setImages(images);
            postRepository.save(savedPost); // Save updated post with images

            return convertToDTO(savedPost);
        }

        if (files != null && !files.isEmpty()) {
            images = files.stream()
                    .map(file -> {
                        try {
                            String url = saveFile(file); // Assuming saveFile method handles the file storage
                            Image image = new Image();
                            image.setUrl(url);
                            image.setPost(savedPost);
                            return image;
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to store image", e);
                        }
                    }).collect(Collectors.toList());
            imageRepository.saveAll(images); // Save all images to the database
        }

        // Update the post with images
        savedPost.setImages(images);
        postRepository.save(savedPost); // Save updated post with images

        return convertToDTO(savedPost);
    }

    public PostDTO getPost(Long id) {
        return postRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public List<PostDTO> getAllPosts(String boardType) {
        List<Post> posts = postRepository.findByBoardType(boardType);
        return posts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private PostDTO convertToDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setContent(post.getContent());
        postDTO.setAuthor(post.getAuthor());
        postDTO.setImageUrls(post.getImages().stream()
                .map(Image::getUrl)
                .collect(Collectors.toList()));
        return postDTO;
    }

    private String saveFile(MultipartFile file) throws IOException {
        // Define the path to save the file
        Path filePath = Paths.get(uploadPath, "post", file.getOriginalFilename());

        // Create directories if not exist
        Files.createDirectories(filePath.getParent());

        // Save file to the specified location
        file.transferTo(filePath.toFile());

        // Return the URL for accessing the file
        return "/post/" + file.getOriginalFilename();
    }
}