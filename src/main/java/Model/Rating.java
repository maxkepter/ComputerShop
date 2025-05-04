package Model;

import java.sql.Timestamp;

public class Rating {

    private int userId;
    private String userName;
    private double rating;
    private String comment;
    private Timestamp createdAt; // thêm dòng này

    public Rating(int userId, String userName, double rating, String comment, Timestamp createdAt) {
        this.userId = userId;
        this.userName = userName;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    // Getter
    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public double getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    // Setter
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
