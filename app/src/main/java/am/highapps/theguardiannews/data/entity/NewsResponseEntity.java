package am.highapps.theguardiannews.data.entity;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

import am.highapps.theguardiannews.data.db.DbFactory;

@Entity(tableName = DbFactory.NEWS_TABLE)
public class NewsResponseEntity   {

    @PrimaryKey
    @NonNull
    @SerializedName("id")
    private String id;

    @SerializedName("type")
    private String type;

    @SerializedName("sectionId")
    private String sectionId;

    @SerializedName("sectionName")
    private String sectionName;

    @SerializedName("webPublicationDate")
    private String webPublicationDate;

    @SerializedName("webTitle")
    private String webTitle;

    @SerializedName("webUrl")
    private String webUrl;

    @SerializedName("apiUrl")
    private String apiUrl;

    @SerializedName("fields")
    @Embedded
    private Fields fields;

    @SerializedName("isHosted")
    private boolean isHosted;

    @SerializedName("pillarId")
    private String pillarId;

    @SerializedName("pillarName")
    private String pillarName;

    private boolean isSaved;

    private boolean isPined;

    public NewsResponseEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getWebPublicationDate() {
        return webPublicationDate;
    }

    public void setWebPublicationDate(String webPublicationDate) {
        this.webPublicationDate = webPublicationDate;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public void setWebTitle(String webTitle) {
        this.webTitle = webTitle;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public Fields getFields() {
        return fields;
    }

    public void setFields(Fields fields) {
        this.fields = fields;
    }

    public boolean isIsHosted() {
        return isHosted;
    }

    public void setIsHosted(boolean isHosted) {
        this.isHosted = isHosted;
    }

    public String getPillarId() {
        return pillarId;
    }

    public void setPillarId(String pillarId) {
        this.pillarId = pillarId;
    }

    public String getPillarName() {
        return pillarName;
    }

    public void setPillarName(String pillarName) {
        this.pillarName = pillarName;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    public boolean isPined() {
        return isPined;
    }

    public void setPined(boolean pined) {
        isPined = pined;
    }

    @Override
    public String toString() {
        return "NewsResponseEntity{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", sectionId='" + sectionId + '\'' +
                ", sectionName='" + sectionName + '\'' +
                ", webPublicationDate='" + webPublicationDate + '\'' +
                ", webTitle='" + webTitle + '\'' +
                ", webUrl='" + webUrl + '\'' +
                ", apiUrl='" + apiUrl + '\'' +
                ", fields=" + fields +
                ", isHosted=" + isHosted +
                ", pillarId='" + pillarId + '\'' +
                ", pillarName='" + pillarName + '\'' +
                "} \n\n";
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NewsResponseEntity)) return false;
        else return this.id.equals(((NewsResponseEntity) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

//    public long getPk() {
//        return pk;
//    }
//
//    public void setPk(long pk) {
//        this.pk = pk;
//    }
}
