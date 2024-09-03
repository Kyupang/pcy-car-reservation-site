package reservationsystem.reservationcar.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reservationsystem.reservationcar.DTO.PostDTO;
import reservationsystem.reservationcar.domain.Post;
import reservationsystem.reservationcar.service.PostService;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 목록을 보여주는 페이지
    @GetMapping("/post/carImagePostList")
    public String showCarPosts(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               Model model) {
        Page<PostDTO> postPage = postService.getAllPosts("carImagePost", page, size);
        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postPage.getTotalPages());
        model.addAttribute("totalItems", postPage.getTotalElements());
        return "post/carImagePosts"; // carImagePosts.html 페이지로 반환
    }

    @GetMapping("/post/wishPostList")
    public String showWishPosts(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                Model model) {
        Page<PostDTO> postPage = postService.getAllPosts("wishPost", page, size);
        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postPage.getTotalPages());
        model.addAttribute("totalItems", postPage.getTotalElements());
        return "post/wishPosts"; // wishPosts.html
    }

    // 새 게시글 작성 페이지를 보여주는 매핑
    @GetMapping("/post/newPost/{boardType}")
    public String showCreatePostForm(@PathVariable String boardType, Model model) {
        // 모델에 필요한 데이터를 추가할 수 있습니다.
        model.addAttribute("post", new PostDTO());
        model.addAttribute("boardType", boardType);

        if ("carImagePost".equals(boardType)) {
            return "post/carImagePostForm"; // 촬영 게시판 폼 템플릿 이름
        } else if ("wishPost".equals(boardType)) {
            return "post/wishPostForm"; // 건의 게시판 폼 템플릿 이름
        } else {
            // 잘못된 boardType이 전달된 경우, 기본 폼을 반환하거나 오류 페이지로 리다이렉트
            return "redirect:/error/404"; // 예를 들어, 404 오류 페이지로 리다이렉트
        }
    }
    // 게시글 작성 로직
    @PostMapping("/post/new/{boardType}")
    public String createPost(@PathVariable String boardType,
                             @RequestParam("title") String title,
                             @RequestParam("content") String content,
                             @RequestParam("author") String author,
                             @RequestParam(value = "files", required = false) List<MultipartFile> files,
                             Model model) throws IOException {
        PostDTO postDTO = new PostDTO();
        postDTO.setTitle(title);
        postDTO.setContent(content);
        postDTO.setAuthor(author);
        postDTO.setTimestamp(LocalDateTime.now());
        PostDTO createdPostDTO = postService.savePost(postDTO, files, boardType);

        model.addAttribute("post", createdPostDTO);
        return "redirect:/post/" + boardType + "List"; // 게시글 목록 페이지로 리다이렉트
    }

    // 게시글 상세 조회 페이지
    @GetMapping("/post/getPost/{boardType}/{id}")
    public String getPost(@PathVariable String boardType, @PathVariable Long id, Model model) {
        PostDTO postDTO = postService.getPost(id);
        if (postDTO != null) {
            model.addAttribute("post", postDTO);
            return "post/"+ boardType +"Detail"; //
        } else {
            return "error/404"; // 404 에러 페이지로 반환 (옵션)
        }
    }
}