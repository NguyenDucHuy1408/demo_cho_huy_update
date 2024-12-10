package library1;
public class Review {
    private final String userId;
    private final int rating; // Đánh giá từ 1 đến 5
    private final String comment;

    public Review(String userId, int rating, String comment) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public void printReview() {
        //System.out.println("User: " + userId + ", Rating: " + rating + "/5, Comment: " + comment);
        System.out.printf("User: %s, Rating: %d/5, Comment: %s%n", userId, rating, comment);

    }
}
