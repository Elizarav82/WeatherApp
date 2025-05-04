// Главный класс ответа от API
data class WeatherResponse(
    val name: String,       // Название города
    val main: Main,         // Основные данные
    val weather: List<Weather>  // Погодные условия
)

// Основные метеоданные
data class Main(
    val temp: Double,       // Температура
    val feels_like: Double, // Ощущается как
    val humidity: Int       // Влажность
)

// Описание погоды
data class Weather(
    val description: String, // Текстовое описание
    val icon: String        // Код иконки
)