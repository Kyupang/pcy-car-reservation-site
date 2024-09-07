package reservationsystem.reservationcar.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import reservationsystem.reservationcar.DTO.PostDTO;
import reservationsystem.reservationcar.domain.Image;
import reservationsystem.reservationcar.domain.Post;
import reservationsystem.reservationcar.repository.ImageRepository;
import reservationsystem.reservationcar.repository.PostRepository;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final ImageRepository imageRepository;

    private final S3Service s3Service;  // S3 서비스 추가

    @Transactional
    public PostDTO savePost(PostDTO postDTO, List<MultipartFile> files, String boardType) throws IOException {
        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setAuthor(postDTO.getAuthor());
        post.setBoardType(boardType);
        post.setTimestamp(postDTO.getTimestamp());

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
                            // S3에 파일 저장
                            String url = s3Service.uploadFile(file, "post"); // "post" 폴더에 저장
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

    public Page<PostDTO> getAllPosts(String boardType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postPage = postRepository.findByBoardType(boardType, pageable);
        return postPage.map(this::convertToDTO);
    }

    private PostDTO convertToDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setContent(post.getContent());
        postDTO.setAuthor(post.getAuthor());
        postDTO.setTimestamp(post.getTimestamp());
        postDTO.setImageUrls(post.getImages().stream()
                .map(Image::getUrl)
                .collect(Collectors.toList()));
        return postDTO;
    }
}