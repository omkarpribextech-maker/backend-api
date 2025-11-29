package com.om.demo.service;

import com.om.demo.model.Post;
import com.om.demo.model.User;
import com.om.demo.repository.PostRepository;
import com.om.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ExploreService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private UserService userService;

    // SEARCH (Users + Posts)
    public Map<String, Object> search(String q) {
        return Map.of(
                "users", userRepo.searchUsers(q),
                "posts", postRepo.searchPosts(q)
        );
    }

    // TRENDING POSTS
    public List<Post> trending() {
        return postRepo.findTrending(PageRequest.of(0, 50));
    }

    // RECOMMENDED POSTS BASED ON USER INTERESTS
    public List<Post> recommended(Long userId) {
        User user = userService.getUserEntity(userId);

        var interestIds = user.getInterests()
                .stream()
                .map(i -> i.getId())
                .toList();

        return postRepo.findByInterests(interestIds);
    }

    // SEARCH ONLY USERS
    public List<User> searchUsers(String query) {
        return userRepo.searchUsers(query);
    }
}
