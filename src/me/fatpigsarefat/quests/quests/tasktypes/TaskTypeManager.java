package me.fatpigsarefat.quests.quests.tasktypes;

import me.fatpigsarefat.quests.Quests;
import me.fatpigsarefat.quests.quests.Quest;
import me.fatpigsarefat.quests.quests.Task;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.logging.Level;

public class TaskTypeManager {

    private ArrayList<TaskType> taskTypes = new ArrayList<>();

    public ArrayList<TaskType> getTaskTypes() {
        return taskTypes;
    }

    public void resetTaskTypes() {
        for (TaskType taskType : taskTypes) {
            taskType.getRegisteredQuests().clear();
        }
    }

    public void registerTaskType(TaskType taskType) {
        Bukkit.getPluginManager().registerEvents(taskType, Quests.getInstance());
        Quests.getInstance().getLogger().log(Level.INFO, "Task type " + taskType.getType() + " has been registered.");
        taskTypes.add(taskType);
    }

    public void registerQuestTasksWithTaskTypes(Quest quest) {
        for (Task task : quest.getTasks()) {
            for (TaskType taskType : taskTypes) {
                if (taskType.getType().equalsIgnoreCase(task.getType())) {
                    taskType.registerQuest(quest);
                }
            }
        }
    }
}
