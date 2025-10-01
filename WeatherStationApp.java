import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
interface WeatherObserver {
    void update(double temperature, double humidity, double pressure, String location);
    String getDisplayName();
    void weatherAlert(String alertMessage, AlertSeverity severity);

    enum AlertSeverity {
        LOW, MEDIUM, HIGH, CRITICAL
    }
}
class WeatherStation {
    private final List<WeatherObserver> observers = new CopyOnWriteArrayList<>();
    private double temperature, humidity, pressure;
    private String location;

    public WeatherStation(String location) { this.location = location; }

    public void registerObserver(WeatherObserver observer) {
        observers.add(observer);
    }
    public void unregisterObserver(WeatherObserver observer) {
        observers.remove(observer);
    }
    public void setMeasurements(double temp, double hum, double pres) {
        this.temperature = temp; this.humidity = hum; this.pressure = pres;
        notifyObservers();
    }
    private void notifyObservers() {
        for (WeatherObserver o : observers)
            o.update(temperature, humidity, pressure, location);
    }
}
class CurrentConditionsDisplay implements WeatherObserver {
    private final String name;
    public CurrentConditionsDisplay(String name) { this.name = name; }
    public void update(double temperature, double humidity, double pressure, String location) {
        System.out.printf("[%s] Conditions@%s: Temp=%.1fC Hum=%.1f%% Pres=%.1f hPa\n", name, location, temperature, humidity, pressure);
    }
    public String getDisplayName() { return name; }
    public void weatherAlert(String msg, AlertSeverity severity) {
        System.out.printf("[%s] ALERT(%s): %s\n", name, severity, msg);
    }
}
public class WeatherStationApp {
    public static void main(String[] args) {
        WeatherStation ws = new WeatherStation("City Center");
        WeatherObserver display = new CurrentConditionsDisplay("MainDisplay");

        ws.registerObserver(display);

        ws.setMeasurements(25.0, 60.0, 1013.0);
        ws.setMeasurements(30.5, 54.2, 1009.5);
    }
}
