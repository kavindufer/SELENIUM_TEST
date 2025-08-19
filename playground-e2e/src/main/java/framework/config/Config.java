package framework.config;

import org.aeonbits.owner.ConfigFactory;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.Config.Key;
import org.aeonbits.owner.Config.DefaultValue;

@Sources({"system:properties", "classpath:config.properties"})
public interface Config extends org.aeonbits.owner.Config {

    @Key("baseUrl")
    @DefaultValue("http://localhost:3000")
    String baseUrl();

    @Key("browser")
    @DefaultValue("chrome")
    String browser();

    @Key("headless")
    @DefaultValue("true")
    boolean headless();

    @Key("downloadsDir")
    @DefaultValue("./downloads")
    String downloadsDir();

    @Key("gridUrl")
    String gridUrl();

    static Config get() {
        return ConfigFactory.create(Config.class, System.getProperties());
    }
}
