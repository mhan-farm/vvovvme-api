package io.mhan.springjpatest2.posts.controller;

import io.mhan.springjpatest2.base.response.SuccessResponse;
import io.mhan.springjpatest2.images.service.ImageService;
import io.mhan.springjpatest2.posts.dto.PostDto;
import io.mhan.springjpatest2.posts.repository.vo.PostKeyword;
import io.mhan.springjpatest2.posts.repository.vo.PostKeywordType;
import io.mhan.springjpatest2.posts.request.PostCreateRequest;
import io.mhan.springjpatest2.posts.request.PostUpdateRequest;
import io.mhan.springjpatest2.posts.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static io.mhan.springjpatest2.base.init.InitData.USER_ID;
import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class ApiV1PostControllerImpl implements ApiV1PostController {

    private final PostService postService;
    private final ImageService imageService;

    @GetMapping
    public SuccessResponse<Page<PostDto>> getAll(
            @RequestParam(required = false, value = "title,content") String keywordType,
            @RequestParam(required = false, value = "") String keyword,
            @PageableDefault(sort = "created", direction = DESC) Pageable pageable) {

        PostKeyword postKeyword = PostKeyword.builder()
                .type(PostKeywordType.of(keywordType))
                .value(keyword)
                .build();

        Page<PostDto> page = postService.getPostDtoAll(postKeyword, pageable);

        return SuccessResponse.ok("게시글 전체 조회에 성공하셨습니다.", page);
    }

    @PostMapping
    public SuccessResponse<PostDto> newPost(@RequestBody PostCreateRequest request) {

        Long postId = postService.registerPost(USER_ID, request);

        PostDto postDto = postService.getPostDtoById(postId);

        return SuccessResponse.create("게시글 생성에 성공하셨습니다.", postDto);
    }

    @PostMapping("/{postId}")
    public SuccessResponse<PostDto> updatePost(@PathVariable Long postId,
                                               @RequestBody PostUpdateRequest request) {

        postService.update(request.getTitle(), request.getContent(), postId, USER_ID, request.getTags());

        PostDto postDto = postService.getPostDtoById(postId);

        return SuccessResponse.ok("게시글 수정에 성공하셨습니다.", postDto);
    }

    @GetMapping("/{postId}")
    public SuccessResponse<PostDto> getPost(@PathVariable Long postId) {

        PostDto postDto = postService.getPostDtoByIdAndIncreaseViews(postId);

        return SuccessResponse.ok("게시글 조회에 성공하셨습니다.", postDto);
    }

    @GetMapping("/my")
    public SuccessResponse<Page<PostDto>> getMyPosts(
            @RequestParam(required = false, value = "title,content") String keywordType,
            @RequestParam(required = false, value = "") String keyword,
            @PageableDefault(sort = "created", direction = DESC) Pageable pageable) {

        PostKeyword postKeyword = PostKeyword.builder()
                .type(PostKeywordType.of(keywordType))
                .value(keyword)
                .build();

        Page<PostDto> postDtos = postService.getMyPostDtoAll(USER_ID, postKeyword, pageable);

            return SuccessResponse.ok("자신의 게시글 전체 조회에 성공하셨습니다.", postDtos);
    }

    @DeleteMapping("/{postId}")
    public SuccessResponse<Void> softDeleteMyPost(@PathVariable Long postId) {

        postService.softDeleteMyPost(postId, USER_ID);

        return SuccessResponse.noContent("자신의 게시글 삭제를 성공하셨습니다.");
    }

    @PostMapping("posts/images")
    public SuccessResponse<String> createPostImage(@RequestParam("file") MultipartFile file) {

        String url = imageService.upload(file, "p/" + UUID.randomUUID());

        return SuccessResponse.ok("게시글 이미지를 생성하였습니다.", url);
    }
}
