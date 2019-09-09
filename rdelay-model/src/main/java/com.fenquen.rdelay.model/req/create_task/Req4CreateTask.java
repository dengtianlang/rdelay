package com.fenquen.rdelay.model.req.create_task;

import com.fenquen.rdelay.model.TaskType;
import com.fenquen.rdelay.model.annotation.Nullable;
import com.fenquen.rdelay.utils.TextUtils;

import java.lang.reflect.Field;

/**
 * request for create a task
 */
public abstract class Req4CreateTask {

    @Nullable
    public String bizTag;

    public Long executionTime;

    public Integer maxRetryCount = 3;

    /**
     * the application server address where this task is desired to be executed <br>
     * the field  only should be like http(s)://host[[/]|[:port[/]]]
     */
    public String executionAppSvrAddr;

    private String taskReceiveUrl;

    public void verifyFields() throws Exception {
        Field[] fields = getClass().getFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Nullable.class)) {
                continue;
            }

            field.setAccessible(true);
            if (null == field.get(this)) {
                throw new RuntimeException(field.getName() + "is null");
            }
        }

        TextUtils.verifyAndModifyHttpSvrAddr(executionAppSvrAddr);
        taskReceiveUrl = executionAppSvrAddr + "/rdelay/receiveTask/" + getTaskType().name();

        if (System.currentTimeMillis() >= executionTime) {
            throw new RuntimeException("not a valid executionTime,System.currentTimeMillis() >= executionTime");
        }

        verifyFieldsInternal();
    }

    public abstract TaskType getTaskType();

    abstract void verifyFieldsInternal();

    public String getTaskReceiveUrl() {
        return taskReceiveUrl;
    }
}
