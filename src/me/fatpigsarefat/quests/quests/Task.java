package me.fatpigsarefat.quests.quests;

import java.util.HashMap;
import java.util.Map;

public class Task {

    private Map<String, Object> configValues = new HashMap<>();

    private String id;
    private String type;

    public Task(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Object getConfigValue(String key) {
        return configValues.getOrDefault(key, null);
    }

    public Map<String, Object> getConfigValues() {
        return configValues;
    }

    public void addConfigValue(String key, Object value) {
        configValues.put(key, value);
    }

}
