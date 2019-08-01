package am.highapps.theguardiannews.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response {

    @SerializedName("status")
    private String status;

    @SerializedName("userTier")
    private String userTier;

    @SerializedName("total")
    private int total;

    @SerializedName("startIndex")
    private int startIndex;

    @SerializedName("pageSize")
    private int pageSize;

    @SerializedName("currentPage")
    private int currentPage;

    @SerializedName("pages")
    private int pages;

    @SerializedName("orderBy")
    private String orderBy;

    @SerializedName("results")
    private List<NewsResponseEntity> newsResponseEntities = null;

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

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public List<NewsResponseEntity> getNewsResponseEntities() {
        return newsResponseEntities;
    }

    public void setNewsResponseEntities(List<NewsResponseEntity> newsResponseEntities) {
        this.newsResponseEntities = newsResponseEntities;
    }

}
