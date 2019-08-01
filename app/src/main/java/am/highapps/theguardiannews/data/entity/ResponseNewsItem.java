package am.highapps.theguardiannews.data.entity;

import com.google.gson.annotations.SerializedName;

public class ResponseNewsItem {

    @SerializedName("status")
    private String status;

    @SerializedName("userTier")
    private String userTier;

    @SerializedName("total")
    private int total;

    @SerializedName("content")
    private NewsResponseEntity content;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserTier() {
        return userTier;
    }

    public void setUserTier(String userTier) {
        this.userTier = userTier;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public NewsResponseEntity getContent() {
        return content;
    }

    public void setContent(NewsResponseEntity content) {
        this.content = content;
    }

}
