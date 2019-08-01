package am.highapps.theguardiannews.data.api;

import am.highapps.theguardiannews.data.entity.NewsResponse;
import am.highapps.theguardiannews.data.entity.NewsItemResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface TheGuardianNewsAPI {

    @GET("search?show-fields=thumbnail,bodyText")
    Call<NewsResponse> getNewsList(
            @Query("page") int page,
            @Query("page-size") int requestedLoadSize);


    @GET("{id}?show-fields=thumbnail,bodyText")
    Call<NewsItemResponse> getNewsItem(
            @Path(value = "id", encoded = true) String id);


    @GET("/search?show-fields=thumbnail,bodyText")
    Call<NewsResponse> getNewsItemsFromDate(
            @Query("from-date") String fromDate,
            @Query("page") int pageNumber,
            @Query("page-size") int pageSize);

}