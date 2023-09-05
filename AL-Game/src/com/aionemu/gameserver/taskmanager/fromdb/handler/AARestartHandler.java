/*
 * AionLight project
 */
package com.aionemu.gameserver.taskmanager.fromdb.handler;

import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.taskmanager.fromdb.trigger.TaskFromDBTrigger;
import com.aionemu.gameserver.taskmanager.fromdb.trigger.TaskFromDBTriggerHolder;
import java.util.ArrayList;

/**
 *
 * @author Alex
 */
public class AARestartHandler {

    public static ArrayList<TaskFromDBTrigger> getTasks() {
        final ArrayList<TaskFromDBTrigger> result = new ArrayList<>();
        int id = 1;
        try {
            TaskFromDBTrigger trigger = TaskFromDBTriggerHolder.valueOf("FIXED_IN_TIME").getTriggerClass().newInstance();
            TaskFromDBHandler handler = TaskFromDBHandlerHolder.valueOf(GSConfig.TASK_SERVER_TYPE_PARAMS).getTaskClass().newInstance();
            handler.setTaskId(id);
            String execParamsResult = GSConfig.RESTART_SERVER_EXEC_PARAMS;
            if (execParamsResult != null) {
                handler.setParams(execParamsResult.split(" "));
            }
            trigger.setHandlerToTrigger(handler);
            String triggerParamsResult = GSConfig.RESTART_SERVER;
            if (triggerParamsResult != null) {
                trigger.setParams(GSConfig.RESTART_SERVER.split(","));
            }
            result.add(trigger);
        } catch (InstantiationException | IllegalAccessException ex) {
        }
        return result;
    }
}
