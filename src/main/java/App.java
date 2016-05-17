import java.util.*;
import java.util.Map;
import java.util.HashMap;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;
import org.sql2o.*;
import java.util.List;
import java.util.ArrayList;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("cats", Cat.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/cats", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/cats.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/cat/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Cat newCat = Cat.find(Integer.parseInt(request.params(":id")));
      model.put("cat", newCat);
      model.put("template", "templates/cat.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    // post("cats/new", (request, response) -> {
    //   Map<String, Object> model = new HashMap<String, Object>();
    //   Cat newCat = new Cat(name, status, location, date, description)
    //   String name = request.queryParams("catName");
    //   String location = request.queryParams("catLocation");
    //   String name = request.queryParams("catName");
    //   response.redirect("/cats")
    //   return null;
    // })
  }
}



// model.put("template", "templates/index.vtl");
// = new Review;
//  newReview.saveReviewToRestaurant(":id")
//  model.put(":id", request.params(:id))
