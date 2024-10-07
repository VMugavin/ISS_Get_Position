import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;


public class Main {
    public static void main(String[] args) throws IOException {
        InputStream inputStream = null;
        try {
            //Удаление файла из предывдущего выполнения программы если такой имеется
            if (Files.exists(Path.of("iss.json"))) {
                Files.delete(Path.of("iss.json"));
            }
            //Получение Json с координатами станции
            URL url = new URL("http://api.open-notify.org/iss-now.json");
            inputStream = url.openStream();
            Files.copy(inputStream, new File("iss.json").toPath());
            inputStream.close();
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader("iss.json"));
            JSONObject iss = (JSONObject) obj;
            //Вывод в консоль времени из timestamp
            long issTime = Long.parseLong(iss.get("timestamp").toString()) ;
            Timestamp issDate = new Timestamp(issTime * 1000);
            System.out.println("Дата: " + issDate.toString());
            //Преобразование и вывод в консоль координат
            JSONObject pos = (JSONObject) iss.get("iss_position");
            String longitude = (String) pos.get("longitude");
            String latitude = (String) pos.get("latitude");
            checkrevert(latitude, longitude);
            //Формирование и октрытие ссылки на местоположение станции
            Desktop.getDesktop().browse(new URL("https://yandex.ru/maps/?text=" + pos.get("latitude").toString() + "," + pos.get("longitude").toString()).toURI());
        } catch (Exception ex) {
            System.out.println("Ошибка " + ex);
            throw new RuntimeException(ex);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            }catch(Exception e){
                System.out.println("Ошибка при закрытии " + e);
            }
            }
        }

        public static void checkrevert(String latitude, String longitude){
        float lat = Float.parseFloat(latitude);
        float lon = Float.parseFloat(longitude);
        if (lat < 0){
            lat *= -1;
            System.out.println(lat + " градусов южной широты");
        }else{
            System.out.println(lat  + " градусов северной широты");
        }
        if (lon < 0){
            lon *= -1;
            System.out.println(lon + " градусов западной долготы");
        }else {
            System.out.println(lon + " градусов восточной долготы");
        }
        }
    }

