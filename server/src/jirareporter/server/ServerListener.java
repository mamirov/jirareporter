package jirareporter.server;

import jetbrains.buildServer.log.Loggers;
import jetbrains.buildServer.serverSide.BuildServerAdapter;
import jetbrains.buildServer.serverSide.BuildServerListener;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.util.EventDispatcher;
import org.jetbrains.annotations.NotNull;
import jirareporter.common.Util;

/**
 * Example server events listener
 */
public class ServerListener extends BuildServerAdapter {
  @NotNull
  private SBuildServer myServer;

  public ServerListener(@NotNull final EventDispatcher<BuildServerListener> dispatcher, @NotNull SBuildServer server) {
    dispatcher.addListener(this);
    myServer = server;
  }

  @Override
  public void serverStartup() {
    Loggers.SERVER.info("Plugin '" + Util.NAME + "'. Is running on server version " + myServer.getFullServerVersion() + ".");
  }
}
