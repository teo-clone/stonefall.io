package edu.brown.cs.stonefall.network;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.stonefall.game.Game;
import freemarker.template.Configuration;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

/**
 * Class which contains all frontend related handlers with gui.
 *
 * @author Theodoros
 */
public class SparkHandler {

  private static final Gson GSON = new Gson();
  private static Game game;

  /**
   * Constructor, spark engine if the options have gui.
   *
   * @param options
   *                  which were entered by user when running the program
   * @param game
   *                  instance of game
   */
  public SparkHandler(OptionSet options, Game game) {
    if (options.has("gui")) {
      runSparkServer((int) options.valueOf("port"));
    }
    SparkHandler.game = game;
    WebSockets.setGame(game);
  }

  @SuppressWarnings("unchecked")
  private void runSparkServer(int port) {
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    // Setup websockets
    WebSockets.setGame(game);
    Spark.webSocket("/sockets", WebSockets.class);

    Spark.get("/", (req, res) -> {
      Map<String, Object> variables = ImmutableMap.of("title", "Stonefall");
      return new ModelAndView(variables, "login.ftl");
    }, freeMarker);
    Spark.post("/", (req, res) -> {
      QueryParamsMap qm = req.queryMap();
      String name = qm.value("myName");
      if (WebSockets.isFull()) {
        Map<String, Object> variables = ImmutableMap.of("title", "Stonefall");
        return new ModelAndView(variables, "full.ftl");
      } else {
        Map<String, Object> variables = ImmutableMap.of("title", "Stonefall",
            "Name", name, "isFull", WebSockets.isFull());
        return new ModelAndView(variables, "main.ftl");
      }

    }, freeMarker);
    Spark.get("/gameover", (req, res) -> {
      QueryParamsMap map = req.queryMap();
      Map<String, Object> variables = ImmutableMap.of("title", "Stonefall",
          "maxScore", map.get("maxScore").value());
      return new ModelAndView(variables, "gameover.ftl");
    }, freeMarker);
    Spark.get("/instructions", (req, res) -> {
      Map<String, Object> variables = ImmutableMap.of("title", "Stonefall");
      return new ModelAndView(variables, "instructions.ftl");
    }, freeMarker);
    Spark.get("/hq", (req, res) -> {
      Map<String, Object> variables = ImmutableMap.of("title", "Stonefall");
      return new ModelAndView(variables, "about.ftl");
    }, freeMarker);

    // Setup Spark Routes for game page
    // Spark.get("/", new FrontHandler(), freeMarker);

    // Setup Spark Routes for JS Post requests
    // Spark.post("/gridblock", new GridBlockHandler());
    // Spark.post("/display", new GridBlockHandler());
  }

  // ---------------------------------------------------------
  // ------------------- STONEFFALL GUI ----------------------
  // ---------------------------------------------------------

  /**
   * Handle requests to the front page of our Stonefall website.
   *
   * @author fguyotsi
   */
  private static class FrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title", "Stonefall");
      return new ModelAndView(variables, "main.ftl");
    }
  }

  // -------------------- Post Handlers ------------------

  // /**
  // * Whenever a user on the frontend places a structure
  // * The backend needs to be updated
  // * And the frontend needs to know if it is valid placement
  // * @author fguyotsi
  // */
  // private static class GridBlockHandler implements Route {
  // @Override
  // public String handle(Request req, Response res) {
  // QueryParamsMap qm = req.queryMap();
  // int x = Integer.parseInt(qm.value("x"));
  // int y = Integer.parseInt(qm.value("y"));
  // //calculate if valid placement
  // //if valid, then add to update block
  // Map<String, Object> variables = ImmutableMap.of("valid", true);
  // return GSON.toJson(variables);
  // }
  // }

  // Handles display requests
  // This is a FORMER version
  // Now it is handled using websockets
  // /**
  // * Whenever a user on the frontend places a structure
  // * The backend needs to be updated
  // * And the frontend needs to know if it is valid placement
  // * @author fguyotsi
  // */
  // private static class DisplayHandler implements Route {
  // @Override
  // public String handle(Request req, Response res) {
  // QueryParamsMap qm = req.queryMap();
  // String x1 = qm.value("lat1");
  // String y1 = qm.value("lng1");
  // String x2 = qm.value("lat2");
  // String y2 = qm.value("lng2");
  //
  // List<Killable> allKillables = new ArrayList<>();
  //
  // Map<String, Object> variables = ImmutableMap.of("killables", allKillables);
  // return GSON.toJson(variables);
  // }
  // }

  // -------------------- SETUP --------------------

  private static FreeMarkerEngine createEngine() {
    @SuppressWarnings("deprecation")
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  /**
   *
   * @author fguyotsi
   */
  @SuppressWarnings("rawtypes")
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }

}
