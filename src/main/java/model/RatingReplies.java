/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author VinhNTCE181630
 */
public class RatingReplies {
    private int ratingReplyID;
    private int staffID;
    private int rateID;
    private String answer;

    public RatingReplies() {
    }

    public RatingReplies(int ratingReplyID, int staffID, int rateID, String answer) {
        this.ratingReplyID = ratingReplyID;
        this.staffID = staffID;
        this.rateID = rateID;
        this.answer = answer;
    }

    public int getRatingReplyID() {
        return ratingReplyID;
    }

    public void setRatingReplyID(int ratingReplyID) {
        this.ratingReplyID = ratingReplyID;
    }

    public int getStaffID() {
        return staffID;
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    public int getRateID() {
        return rateID;
    }

    public void setRateID(int rateID) {
        this.rateID = rateID;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

   
}

