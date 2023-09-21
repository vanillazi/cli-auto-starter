package cn.vanillazi.tool.config;

import cn.vanillazi.tool.StartupItem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AppConfigs {
    private static final Logger logger= LoggerFactory.getLogger(AppConfigs.class.getName());
    public static StartupItem newItem(){
        var si=new StartupItem();
        si.setArgs(Arrays.asList(ResourceBundles.newItemArgsPrefix()+1,ResourceBundles.newItemArgsPrefix()+2));
        si.setName(ResourceBundles.newItemName());
        si.setExecutable(ResourceBundles.newItemExecutable());
        si.setDescription(ResourceBundles.newItemDescription());
        si.setWorkDirectory(ResourceBundles.newItemWorkDirectory());
        si.setAutoStart(false);
        return si;
    }

    public static final Gson gson=new GsonBuilder().setPrettyPrinting().create();

    public static List<StartupItem> parseFromJson(String json){

        var type=new TypeToken<List<StartupItem>>(){}.getType();
        if(json.isEmpty() || json.isBlank()){
            return Collections.emptyList();
        }
        try {
            return gson.fromJson(json, type);
        }catch (Throwable e){
            logger.error(ResourceBundles.failedToParseConfigurationFile(),e);
        }
        return Collections.emptyList();
    }
}
