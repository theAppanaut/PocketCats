import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import org.sql2o.*;
import java.util.*;

public class Cat {
  private int id;
  private String name;
  private Boolean status;
  private String location;
  private Date date;
  private String description;
  private int user_id;
  private String lat;
  private String lng;
  private String image;

  public Cat(String name, String description, String location, Boolean status, int user_id, String image) {
    this.id = id;
    this.name = name;
    this.status = status;
    this.location = location;
    this.date = date;
    this.description = description;
    this.user_id = user_id;
    this.image = image;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Boolean getStatus() {
    return status;
  }

  public String getLocation() {
    return location;
  }

  public Date getDate() {
    return date;
  }

  public Date createDate() {
    java.util.Date newDate = new java.util.Date();
    return newDate;
  }

  public String getDescription() {
    return description;
  }

  // public User getUser() {
  //   String sql = "SELECT users.* FROM cats " +
  //     "JOIN cats_users ON (cats.id = cats_users.cat_id) " +
  //     "JOIN users ON (users.id = cats_users.user_id) " +
  //     "WHERE cats.id = :id;";
  //   try (Connection con = DB.sql2o.open()) {
  //     User user = con.createQuery(sql)
  //       .addParameter("id" = this.id)
  //       .executeAndFetchFirst(User.class);
  //     return user;
  //   }
  //
  // }


  public String getImage() {
      return image;
  }

  public static List<Cat> all() {
    String sql = "SELECT id, name, status, location, date, description, image FROM cats";
    try (Connection con = DB.sql2o.open()) {
      return con.createQuery(sql)
        .executeAndFetch(Cat.class);
    }
  }

  @Override
  public boolean equals (Object otherCat) {
    if(!(otherCat instanceof Cat)) {
      return false;
    } else {
      Cat newCat = (Cat) otherCat;
      return this.getName().equals(newCat.getName()) &&
             this.getDescription().equals(newCat.getDescription()) &&
             this.getId() == newCat.getId();
    }
  }


  public void save() {
    String sql = "INSERT INTO cats (name, status, location, date, description, image) VALUES (:name, :status, :location, :date, :description, :image)";
    try (Connection con = DB.sql2o.open()) {
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.getName())
        .addParameter("status", this.getStatus())
        .addParameter("location", this.getLocation())
        .addParameter("date", this.createDate())
        .addParameter("description", this.getDescription())
        .addParameter("image", this.getImage())
        .executeUpdate()
        .getKey();
    }
  }

  public static Cat find(int id) {
    String sql = "SELECT id, name, status, location, date, description, image FROM cats WHERE id=:id";
    try (Connection con = DB.sql2o.open()) {
      Cat cat = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Cat.class);
      return cat;
    }
  }

  public void updateName(String update) {
    String sql = "UPDATE cats SET name=:name WHERE id=:id";
    try(Connection con = DB.sql2o.open()) {
      con.createQuery(sql)
        .addParameter("name", update)
        .addParameter("id", this.getId())
        .executeUpdate();
    }
  }

  public void updateStatus(Boolean status) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE cats SET status=:status WHERE id=:id";
        con.createQuery(sql)
          .addParameter("id", this.getId())
          .addParameter("status", status)
          .executeUpdate();
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
    String deleteQuery = "DELETE FROM cats WHERE id=:id";
      con.createQuery(deleteQuery)
        .addParameter("id", this.getId())
        .executeUpdate();

    String joinDeleteQuery = "DELETE FROM cats_users WHERE cat_id=:cat_id";
      con.createQuery(joinDeleteQuery)
        .addParameter("cat_id", this.getId())
        .executeUpdate();
    }
  }

  // Cat has many to many with users
  public void addUser(User user){
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO cats_users(cat_id, user_id) VALUES (:cat_id, :user_id)";
      con.createQuery(sql)
        .addParameter("cat_id", this.getId())
        .addParameter("user_id", user.getId())
        .executeUpdate();
    }
  }

  // Cat has many to many with usersv
  public List<User> getUsers() {
    String sql = "SELECT user_id FROM cats_users WHERE cat_id=:cat_id";
    try(Connection con = DB.sql2o.open()) {
      List<Integer> userIds = con.createQuery(sql)
        .addParameter("cat_id", this.getId())
        .executeAndFetch(Integer.class);

      List<User> users = new ArrayList<User>();

      for (Integer userId : userIds) {
        String stringQuery = "SELECT * FROM users WHERE id=:id";
        User newUser = con.createQuery(stringQuery)
          .addParameter("id", userId)
          .executeAndFetchFirst(User.class);
          users.add(newUser);
        }
        return users;
      }
    }

  // Cat has one to many with Comments
  public List<Comment> getComments() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM comments WHERE cat_id=:id";
      return con.createQuery(sql)
        .addParameter("id", this.id)
        .executeAndFetch(Comment.class);
    }
  }

  public static List<Cat> getByStyle(String style) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM cats WHERE description=:description";
      return con.createQuery(sql)
        .addParameter("description", style)
        .executeAndFetch(Cat.class);
    }
  }

  public static List<String> allLocations() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT location FROM cats";
      return con.createQuery(sql)
        .executeAndFetch(String.class);
    }
  }

}
