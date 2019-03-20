package ae.bluecast.library.Communications;

import ae.bluecast.library.Model.CategoryResponse;
import ae.bluecast.library.Model.GetBooksResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("getCategoriesLongList")
    Call<CategoryResponse> getCategories();
    @GET("getBookList")
    Call<GetBooksResponse> getBooks();
}
