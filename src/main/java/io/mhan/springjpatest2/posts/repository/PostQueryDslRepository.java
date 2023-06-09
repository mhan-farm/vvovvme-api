package io.mhan.springjpatest2.posts.repository;

import io.mhan.springjpatest2.posts.entity.Post;
import io.mhan.springjpatest2.posts.repository.vo.PostKeyword;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface PostQueryDslRepository {
    List<Post> findAll(PostKeyword keyword, Sort sort);
    Page<Post> findAll(PostKeyword keyword, Pageable pageable);
    Page<Post> findByAuthorId(Long authorId, PostKeyword keyword, Pageable pageable);
    Optional<Post> findActiveById(Long postId);

    List<Post> findActiveAllByAuthorIdAndParentPostIsNull(Long authorId);
    long countActiveAllByAuthorIdAndParentPostIsNull(Long authorId);
}
