package com.tictactoe.logic;

import com.tictactoe.model.Game;
import org.springframework.beans.factory.ListableBeanFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGameChain {

    @Resource
    private ListableBeanFactory beanFactory;

    private List<AbstractGameCmd> commands;

    @PostConstruct
    public void postConstruct() {
        initCommands();
    }

    public void initCommands() {
        commands = new ArrayList<>();
        for (Class<?> cls : getCommandClasses()) {
            AbstractGameCmd cmd = (AbstractGameCmd) beanFactory.getBean(cls);
            commands.add(cmd);
        }
    }

    public boolean process(Game currentState, Game newState) {
        boolean success = false;
        for (AbstractGameCmd cmd : commands) {
            if (cmd.canHandle(currentState, newState)) {
                cmd.handle(currentState, newState);
                success = cmd.shouldUpdateDb();
            } else {
                break;
            }
        }
        return success;
    }

    protected abstract Class<?>[] getCommandClasses();
}