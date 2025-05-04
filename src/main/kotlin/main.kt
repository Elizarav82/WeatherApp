import com.formdev.flatlaf.FlatLightLaf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.withContext

import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.Image
import java.net.URL
import javax.imageio.ImageIO
import javax.swing.*


fun main() {
    // Устанавливаем красивый современный стиль окна
    FlatLightLaf.setup()

    // Создаем главное окно
    val frame = JFrame("Погодное приложение")
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE  // Закрытие программы при закрытии окна
    frame.size = Dimension(400, 300)  // Размер окна
    frame.layout = BorderLayout()  // Разметка окна

    // Панель для ввода города
    val inputPanel = JPanel(FlowLayout())  // Компоненты будут идти в ряд
    val cityField = JTextField(15)  // Поле для ввода (ширина 15 символов)
    val searchButton = JButton("Узнать погоду")  // Кнопка

    // Добавляем элементы на панель
    inputPanel.add(JLabel("Город:"))  // Надпись
    inputPanel.add(cityField)         // Поле ввода
    inputPanel.add(searchButton)      // Кнопка

    // Панель для отображения погоды
    val weatherPanel = JPanel()
    weatherPanel.layout = BoxLayout(weatherPanel, BoxLayout.Y_AXIS)  // Элементы будут в столбик
    weatherPanel.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)  // Отступы

    // Создаем элементы для отображения данных
    val cityLabel = JLabel(" ")       // Для названия города
    val tempLabel = JLabel(" ")       // Для температуры
    val feelsLikeLabel = JLabel(" ")  // Для "ощущается как"
    val humidityLabel = JLabel(" ")   // Для влажности
    val descriptionLabel = JLabel(" ") // Для описания погоды
    val weatherIcon = JLabel()        // Для иконки погоды

    // Добавляем все на панель
    weatherPanel.add(cityLabel)
    weatherPanel.add(tempLabel)
    weatherPanel.add(feelsLikeLabel)
    weatherPanel.add(humidityLabel)
    weatherPanel.add(descriptionLabel)
    weatherPanel.add(weatherIcon)


    searchButton.addActionListener {
        val city = cityField.text.trim()  // Получаем текст из поля ввода

        if (city.isNotEmpty()) {  // Если город введен
            runBlocking {  // Запускаем корутину (асинхронную операцию)
                try {
                    // Делаем запрос в фоновом потоке
                    val weather = withContext(Dispatchers.IO) {
                        RetrofitInstance.weatherApi.getCurrentWeather(city)
                    }

                    // Обновляем интерфейс в основном потоке
                    SwingUtilities.invokeLater {
                        cityLabel.text = "Город: ${weather.name}"
                        tempLabel.text = "Температура: ${weather.main.temp}°C"
                        feelsLikeLabel.text = "Ощущается как: ${weather.main.feels_like}°C"
                        humidityLabel.text = "Влажность: ${weather.main.humidity}%"

                        if (weather.weather.isNotEmpty()) {
                            val weatherInfo = weather.weather[0]
                            descriptionLabel.text = "Описание: ${weatherInfo.description}"

                            // Загружаем иконку погоды
                            val iconUrl = "https://openweathermap.org/img/wn/${weatherInfo.icon}@2x.png"
                            val image = ImageIO.read(URL(iconUrl))
                            val scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH)
                            weatherIcon.icon = ImageIcon(scaledImage)
                        }
                    }
                } catch (e: Exception) {
                    // Если произошла ошибка, показываем сообщение
                    SwingUtilities.invokeLater {
                        JOptionPane.showMessageDialog(frame,
                            "Ошибка: ${e.message}",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE)
                    }
                }
            }
        }
    }

    // Добавляем панели в окно
    frame.add(inputPanel, BorderLayout.NORTH)   // Панель ввода - сверху
    frame.add(weatherPanel, BorderLayout.CENTER) // Панель погоды - по центру

    // Делаем окно видимым
    frame.isVisible = true
}