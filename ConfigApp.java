
class ConfigurationManager {
    private static ConfigurationManager instance;
    private ConfigurationManager() {}
    public static synchronized ConfigurationManager getInstance() {
        if (instance == null) instance = new ConfigurationManager();
        return instance;
    }
    public String getConfig(String key) {
        
        return "ValueFor" + key;
    }
}


public class ConfigApp {
    public static void main(String[] args) {
        ConfigurationManager config = ConfigurationManager.getInstance();
        System.out.println(config.getConfig("api.url"));
    }
}
