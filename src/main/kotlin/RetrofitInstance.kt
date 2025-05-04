import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://api.openweathermap.org/"

    // Ленивая инициализация клиента Retrofit
    val weatherApi: WeatherApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)  // Базовый URL API
            .addConverterFactory(GsonConverterFactory.create())  // Конвертер JSON
            .build()
            .create(WeatherApi::class.java)  // Создаем реализацию нашего интерфейса
    }
}