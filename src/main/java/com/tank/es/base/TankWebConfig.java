package com.tank.es.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Description:
 * User： JinHuaTao
 * Date：2017/4/11
 * Time: 16:44
 */

public class TankWebConfig{

    private static final Logger logger = LoggerFactory.getLogger(TankWebConfig.class);

    private static Map<String, String> property = new HashMap<String, String>();

    private static TankWebConfig config;

    public static TankWebConfig getInstance(){
        if(config == null){
            synchronized (TankWebConfig.class){
                try {
                    config = new TankWebConfig();
                }catch (Exception e){
                    logger.error("Fail to load config: {}", e);
                }
            }
        }
        return config;
    }

    public TankWebConfig() throws IOException {
        super();
        loadProperties();
    }

    private void loadProperties() throws IOException{
        InputStream inputStream = TankWebConfig.class.getClassLoader().getResourceAsStream("spring/tank-web.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        for (Map.Entry<Object, Object> entry: properties.entrySet()){
            property.put((String)entry.getKey(), (String)entry.getValue());
        }

        inputStream.close();
    }

    public String[] getHosts(String name){
        return property.get(name).split(",");
    }

    public String getProperty(String name){
        return property.get(name);
    }

}
