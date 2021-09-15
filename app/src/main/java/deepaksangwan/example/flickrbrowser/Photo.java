package deepaksangwan.example.flickrbrowser;

import java.io.Serializable;

class Photo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String mTitle;
    private String mAuthor;
    private String mAuthorId;
    private String mLink;
    private String mTag;
    private String mImage;

    public Photo(String title, String author, String authorId, String link, String tag, String image) {
        this.mTitle = title;
        this.mAuthor = author;
        this.mAuthorId = authorId;
        this.mLink = link;
        this.mTag = tag;
        this.mImage = image;
    }

    String getTitle() {
        return mTitle;
    }

    String getAuthor() {
        return mAuthor;
    }

    String getAuthorId() {
        return mAuthorId;
    }

    String getLink() {
        return mLink;
    }

    String getTag() {
        return mTag;
    }

    String getImage() {
        return mImage;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "mTitle='" + mTitle + '\'' +
                ", mAuthor='" + mAuthor + '\'' +
                ", mAuthorId='" + mAuthorId + '\'' +
                ", mLink='" + mLink + '\'' +
                ", mTag='" + mTag + '\'' +
                ", mImage='" + mImage + '\'' +
                '}';
    }
}
