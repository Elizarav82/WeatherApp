import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    // Запрос на получение текущей погоды
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,      // Город
        @Query("units") units: String = "metric",  // Единицы измерения (метрические)
        @Query("appid") apiKey: String = "982b8e5f25a8c803a21c5cbc4e20d38c"  // Ключ API
    ): WeatherResponse
}