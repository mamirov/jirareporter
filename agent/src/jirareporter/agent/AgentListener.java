package jirareporter.agent;

import jetbrains.buildServer.agent.AgentLifeCycleAdapter;
import jetbrains.buildServer.agent.AgentLifeCycleListener;
import jetbrains.buildServer.agent.BuildAgent;
import jetbrains.buildServer.log.Loggers;
import jetbrains.buildServer.util.EventDispatcher;
import org.jetbrains.annotations.NotNull;
import jirareporter.common.Util;

/**
 * Example agent class.
 */
public class AgentListener extends AgentLifeCycleAdapter {
  public AgentListener(@NotNull EventDispatcher<AgentLifeCycleListener> dispatcher) {
    dispatcher.addListener(this);
  }

  @Override
  public void agentInitialized(@NotNull final BuildAgent agent) {
    Loggers.AGENT.info("Plugin '" + Util.NAME + "'. is running.");
  }
}
