package jirareporter.server;

import jetbrains.buildServer.controllers.BaseController;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.ModelAndView;
import jirareporter.common.Util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Example custom page controller
 */
public class Controller extends BaseController {
  @NotNull
  private PluginDescriptor myPluginDescriptor;

  public Controller(@NotNull PluginDescriptor pluginDescriptor, @NotNull WebControllerManager manager) {
    myPluginDescriptor = pluginDescriptor;
    // this will make the controller accessible via <teamcity_url>\jirareporter.html
    manager.registerController("/jirareporter.html", this);
  }

  @Override
  protected ModelAndView doHandle(@NotNull final HttpServletRequest request, @NotNull final HttpServletResponse response) throws Exception {
    ModelAndView view = new ModelAndView(myPluginDescriptor.getPluginResourcesPath("jirareporter.jsp"));
    final Map<String, Object> model = view.getModel();
    model.put("name", Util.NAME);
    return view;
  }
}
