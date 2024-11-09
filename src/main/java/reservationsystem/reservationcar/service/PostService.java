package reservationsystem.reservationcar.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import reservationsystem.reservationcar.dto.PostDTO;
import reservationsystem.reservationcar.domain.Image;
import reservationsystem.reservationcar.domain.Post;
import reservationsystem.reservationcar.repository.ImageRepository;
import reservationsystem.reservationcar.repository.PostRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final S3Service s3Service;  // S3 서비스 추가

    @Transactional
    public void savePost(PostDTO postDTO, List<MultipartFile> files, String boardType) throws IOException {
        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setAuthor(postDTO.getAuthor());
        post.setBoardType(boardType);
        post.setTimestamp(postDTO.getTimestamp());

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
            post.setImages(images);
        } else {
            images = files.stream()
                    .map(file -> {
                        try {
                            // S3에 파일 저장
                            String url = s3Service.uploadFile(file, "post"); // "post" 폴더에 저장
                            Image image = new Image();
                            image.setImageUrl(url);
                            image.setPost(post);
                            return image;
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to store image", e);
                        }
                    }).collect(Collectors.toList());
            imageRepository.saveAll(images);
            post.setImages(images);
        }

        postRepository.save(post);
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
                .map(Image::getImageUrl)
                .collect(Collectors.toList()));
        return postDTO;
    }
}