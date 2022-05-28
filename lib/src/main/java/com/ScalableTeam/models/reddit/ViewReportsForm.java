package models.reddit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ViewReportsForm {
    private String modId;
    private String redditId;

    public String getModId() {
        return modId;
    }

    public void setModId(String modId) {
        this.modId = modId;
    }

    public String getRedditId() {
        return redditId;
    }

    public void setRedditId(String redditId) {
        this.redditId = redditId;
    }

    @Override
    public String toString() {
        return "ViewReportsForm{" +
                "modId='" + modId + '\'' +
                ", redditId='" + redditId + '\'' +
                '}';
    }
}
