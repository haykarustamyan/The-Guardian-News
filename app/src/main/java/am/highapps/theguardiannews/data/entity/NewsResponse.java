package am.highapps.theguardiannews.data.entity;

import com.google.gson.annotations.SerializedName;

public class NewsResponse {

    @SerializedName("response")
    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
