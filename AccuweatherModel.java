package HomeWork6;

import com.fasterxml.jackson.databind.ObjectMapper;
//import lesson7.project.entity.Weather;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

    public class AccuweatherModel {
        //http://dataservice.accuweather.com/forecasts/v1/daily/5day/{locationKey} -   надо вывести погоду на 5 дней вот по этому url
        private static final String PROTOCOL = "http";
        private static final String BASE_HOST = "dataservice.accuweather.com";
        private static final String FORECASTS = "forecasts";
        private static final String VERSION = "v1";
        private static final String DAILY = "daily";
        private static final String PERIOD = "5day";
        private static final String LOCATIONS = "locations";
        private static final String CITIES = "cities";
        private static final String AUTOCOMPLETE = "autocomplete";
        private static final String API_KEY = "pby0tkG4TGhWtJ5wCAYawOQAfASqomKb";

        static OkHttpClient okHttpClient = new OkHttpClient();

        static ObjectMapper objectMapper = new ObjectMapper();


        public static void getWeather(String selectedCity) throws IOException {
            HttpUrl httpUrl = new HttpUrl.Builder()
                    .scheme(PROTOCOL)
                    .host(BASE_HOST)
                    .addPathSegment(FORECASTS)
                    .addPathSegment(VERSION)
                    .addPathSegment(DAILY)
                    .addPathSegment(PERIOD)
                    .addPathSegment(getCityKey(selectedCity))
                    .addQueryParameter("apikey", API_KEY)
                    .addQueryParameter("language", "ru-ru")
                    .build();

            Request request = new Request.Builder()
                    .url(httpUrl)
                    .build();

            Response response = okHttpClient.newCall(request).execute();
            System.out.println(response.body().string());
        }

        public static String getCityKey(String city) throws IOException {
            //  http://dataservice.accuweather.com/locations/v1/cities/autocomplete - метод автозаполнения

            HttpUrl httpUrl = new HttpUrl.Builder()
                    .scheme(PROTOCOL)
                    .host(BASE_HOST)
                    .addPathSegments(LOCATIONS)
                    .addPathSegments(VERSION)
                    .addPathSegments(CITIES)
                    .addPathSegments(AUTOCOMPLETE)
                    .addQueryParameter("apikey", API_KEY)
                    .addQueryParameter("q", city)
                    .addQueryParameter("language", "ru-ru")
                    .build();


            Request request = new Request.Builder()
                    .url(httpUrl)
                    .build();

            Response response = okHttpClient.newCall(request).execute();
            String responseBody = response.body().string();


            String cityKey = objectMapper.readTree(responseBody).get(0).at("/Key").asText();
            return cityKey;
        }

        public static void main(String[] args) throws IOException {
            getWeather("Санкт-Петербург");

        }
    }

