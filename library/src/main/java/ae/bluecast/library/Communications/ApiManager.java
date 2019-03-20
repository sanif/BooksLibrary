package ae.bluecast.library.Communications;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ApiManager {
//    public static final String BASE_URL = "http://demo2475640.mockable.io/";//Test server
    public static final String BASE_URL = " http://demo6366304.mockable.io/";//Test server




    private static OkHttpClient okClient;
    private static ApiService service;
    private static OkHttpClient.Builder httpClient;


    private static ApiManager apiManager = null;



    private ApiManager() {

    }

    public static ApiManager getApi() {
        if (apiManager == null) {
            apiManager = new ApiManager();

//            httpClient = new OkHttpClient.Builder();
            httpClient = new OkHttpClient.Builder()
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS);
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(httpLoggingInterceptor);
//            if (AppUtils.isUserLoggedIn()) {
            httpClient.interceptors().add(new HttpInterceptor());
//            }
            okClient = httpClient.build();
//            resetUploadService();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            service = retrofit.create(ApiService.class);
        }
        return apiManager;
    }



    private static void resetUploadService() {
//        UploadService.HTTP_STACK = new OkHttpStack(okClient);
    }

    public static void resetApi() {
        apiManager = null;
    }


    public ApiService getService() {
        return service;
    }



    private static class HttpInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            //Build new request
            Request.Builder builder = request.newBuilder();
            builder.header("Accept", "application/json"); //if necessary, say to consume JSON

            request = builder.build(); //overwrite old request
            Response response = chain.proceed(request); //perform request, here original request will be executed



            return response;
        }

        private void setAuthHeader(Request.Builder builder, String token) {
            if (token != null) //Add Auth token to each request if authorized
                builder.header("Authorization", "Bearer " + token);
        }

        private static Request.Builder requestBuild(Request request) {
            return request.newBuilder()
                    .header("Accept", "application/json")
                    .method(request.method(), request.body());
        }

//        private static Request.Builder requestBuild(Request request, LoginData auth) {
//            return request.newBuilder()
//                    .header("Accept", "application/json")
//                    .header("Authorization", "Bearer " + auth.getAccessToken())
//                    .method(request.method(), request.body());
//        }

    }
}
