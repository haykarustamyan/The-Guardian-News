package am.highapps.theguardiannews.data.entity;

import androidx.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import am.highapps.theguardiannews.data.db.DbFactory;

@Entity(tableName = DbFactory.FIELDS_TABLE)
public class Fields {

    @SerializedName("thumbnail")
    private String thumbnail;

    @SerializedName("bodyText")
    private String bodyText;

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getBodyText() {
        return bodyText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }
}
