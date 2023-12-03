package com.kjp.shoppingcart.services;

import com.kjp.shoppingcart.repositories.IReviewRepository;
import com.kjp.shoppingcart.repositories.IVoteRepository;

import java.util.UUID;

public class OpinionService {

    IVoteRepository voteRepository;
    IReviewRepository reviewRepository;

    public OpinionService(IVoteRepository voteRepository, IReviewRepository reviewRepository) {
        this.voteRepository = voteRepository;
        this.reviewRepository = reviewRepository;
    }

    public void voteProduct() {
    }

    public void getProductVotesCount() {
    }

    public void createReview() {
    }

    public void removeReview() {
    }

    public void updateReview() {
    }

    public void getReviewByUser() {
    }
}
