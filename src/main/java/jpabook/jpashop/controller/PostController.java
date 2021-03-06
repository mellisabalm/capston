package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Post;
import jpabook.jpashop.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/posts/new")
    public String createForm(Model model) {
        model.addAttribute("form", new PostForm());
        return "posts/CreatePostForm";
    }

    @PostMapping("/posts/new")
    public String create(PostForm form) {

        Post post = new Post();
        post.setTitle(form.getTitle());
        post.setContents(form.getContents());
        post.setProduct_name(form.getProduct_name());
        post.setStartBid(form.getStartBid());
        post.setWinningBid(form.getWinningBid());
        post.setUnitBid(form.getUnitBid());
        post.setCurrentBid(form.getCurrentBid());
        //post.setStartAuctionTime();
        post.setAuctionPeriod(form.getAuctionPeriod());
        post.setStatus(form.getStatus());
        post.setCurrentBidId(form.getCurrentBidId());


        postService.saveItem(post);
        return "redirect:/";
    }

    @GetMapping("/posts")
    public String list(Model model) {
        List<Post> posts = postService.findItems();
        model.addAttribute("posts", posts);
        return "posts/postList";
    }

    @GetMapping("/post/{id}")
    public String auctionItem(@PathVariable("id") Long id, Model model) {
        Post form = postService.findOne(id);
        model.addAttribute("form", form);
        return "posts/updatePostForm";
    }

    @GetMapping("post/{id}/auction")//test
    public String auctionItemForm(@PathVariable("id") Long itemId, Model model) {
        Post post = postService.findOne(itemId);

        PostForm form = new PostForm();
        form.setId(post.getId());
        form.setTitle(post.getTitle());
        form.setContents(post.getContents());
        form.setProduct_name(post.getProduct_name());
        form.setStartBid(post.getStartBid());
        form.setWinningBid(post.getWinningBid());
        form.setUnitBid(post.getUnitBid());
        form.setCurrentBid(post.getCurrentBid());
        form.setAuctionPeriod(post.getAuctionPeriod());
        form.setStatus(post.getStatus());

        model.addAttribute("form", form);
        return "posts/updatePostForm";
    }

    @PostMapping("/post/{id}/auction")
    public String auctionItem(@ModelAttribute("form")  PostForm form){

        Post post = new Post();
        post.setId(form.getId());
        post.setTitle(form.getTitle());
        post.setContents(form.getContents());
        post.setProduct_name(form.getProduct_name());
        post.setStartBid(form.getStartBid());
        post.setWinningBid(form.getWinningBid());
        post.setUnitBid(form.getUnitBid());
        post.setCurrentBid(form.getCurrentBid());
        post.setAuctionPeriod(form.getAuctionPeriod());
        post.setStatus(form.getStatus());

        // ?????? ????????? ???????????? ?????? ??????, ?????? ???????????? ????????? ??????????????? ???????????????. ?????? id??? ??????, ????????? ????????? ?????? ?????????
        if(post.getCurrentBid() == post.getWinningBid()){
            post.setStatus("?????? ??????");
        }

        if(post.getCurrentBid() < post.getWinningBid()){
            post.setCurrentBid(post.getCurrentBid() + post.getUnitBid());
        }

        // ?????? ?????? ?????? ????????? ??? ????????? ??????

        postService.saveItem(post); // service??? transaction=false??? ??????, repository??? saveItem??? em.flush()??? ??????
        // db??? ????????? ????????????.

        return "redirect:/";
    }


}
