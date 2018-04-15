package me.fatpigsarefat.quests.quests.tasktypes;

import me.fatpigsarefat.quests.Quests;
import me.fatpigsarefat.quests.player.QPlayer;
import me.fatpigsarefat.quests.player.questprogressfile.QuestProgress;
import me.fatpigsarefat.quests.player.questprogressfile.QuestProgressFile;
import me.fatpigsarefat.quests.player.questprogressfile.TaskProgress;
import me.fatpigsarefat.quests.quests.Quest;
import me.fatpigsarefat.quests.quests.Task;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;

public final class MobkillingTaskType extends TaskType {

    public MobkillingTaskType() {
        super("mobkilling", "fatpigsarefat", "Kill a set amount of entities.");
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMobKill(EntityDeathEvent event) {
        Entity killer = event.getEntity().getKiller();
        Entity mob = event.getEntity();

        if (mob instanceof Player) {
            return;
        }

        if (killer == null) {
            return;
        }

        Player player = event.getEntity().getKiller();

        QPlayer qPlayer = Quests.getPlayerManager().getPlayer(player.getUniqueId());
        QuestProgressFile questProgressFile = qPlayer.getQuestProgressFile();

        for (Quest quest : super.getRegisteredQuests()) {
            if (questProgressFile.hasStartedQuest(quest)) {
                QuestProgress questProgress = questProgressFile.getQuestProgress(quest);

                for (Task task : quest.getTasksOfType(super.getType())) {
                    TaskProgress taskProgress = questProgress.getTaskProgress(task.getId());

                    if (taskProgress.isCompleted()) {
                        continue;
                    }

                    boolean hostilitySpecified = false;
                    boolean hostile = false;
                    if (task.getConfigValue("hostile") != null) {
                        hostilitySpecified = true;
                        hostile = (boolean) task.getConfigValue("hostile");
                    }

                    if (hostilitySpecified) {
                        if (!hostile && !(mob instanceof Animals)) {
                            continue;
                        } else if (hostile && !(mob instanceof Monster)) {
                            continue;
                        }
                    }

                    int mobKillsNeeded = (int) task.getConfigValue("amount");

                    int progressKills;
                    if (taskProgress.getProgress() == null) {
                        progressKills = 0;
                    } else {
                        progressKills = (int) taskProgress.getProgress();
                    }

                    taskProgress.setProgress(progressKills + 1);

                    if (((int) taskProgress.getProgress()) >= mobKillsNeeded) {
                        taskProgress.setCompleted(true);
                    }
                }
            }
        }
    }

}
