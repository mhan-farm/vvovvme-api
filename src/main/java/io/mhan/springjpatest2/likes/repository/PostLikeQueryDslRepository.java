package io.mhan.springjpatest2.likes.repository;

import io.mhan.springjpatest2.likes.entity.PostLike;
import io.mhan.springjpatest2.posts.repository.vo.PostKeyword;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface PostLikeQueryDslRepository {
    List<PostLike> findByUserId(Long userId, Sort sort, PostKeyword keyword);
}
