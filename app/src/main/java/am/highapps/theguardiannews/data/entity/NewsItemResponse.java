package am.highapps.theguardiannews.data.entity;

import com.google.gson.annotations.SerializedName;

public class NewsItemResponse {

    @SerializedName("response")
    private ResponseNewsItem response;

    public ResponseNewsItem getResponse() {
        return response;
    }

    public void setResponse(ResponseNewsItem response) {
        this.response = response;
    }
}
