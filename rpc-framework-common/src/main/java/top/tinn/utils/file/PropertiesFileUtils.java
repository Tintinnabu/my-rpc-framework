package top.tinn.utils.file;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @Author: Tinn
 * @Date: 2020/8/11 15:24
 */
@Slf4j
public final class PropertiesFileUtils {
    private PropertiesFileUtils(){}

    public static Properties readPropertiesFile(String fileName){
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String rpcConfigPath = rootPath + fileName;
        Properties properties = null;
        try(FileInputStream fileInputStream = new FileInputStream(rpcConfigPath)){
            properties = new Properties();
            properties.load(fileInputStream);
        }catch (IOException e){
            log.error("occur exception when read properties file [{}]", fileName);
        }
        return properties;
    }

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getContextClassLoader().getResource("").getPath());
    }
}
