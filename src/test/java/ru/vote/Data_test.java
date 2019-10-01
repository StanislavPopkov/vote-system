package ru.vote;

import ru.vote.model.*;
import org.springframework.test.web.servlet.ResultMatcher;
import ru.vote.json.JsonUtil;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;


public class Data_test {


    public static final User USER = new User(100, "User", "Userovich", "user@yandex.ru", "password",  Role.ROLE_USER);
    public static final User ADMIN = new User(101, "Admin", "Adminov", "admin@gmail.com", "admin",  Role.ROLE_ADMIN, Role.ROLE_USER);
    public static final User USER2 = new User(102, "User2", "Userovich2", "user2@yandex.ru", "password",  Role.ROLE_USER);

    public static final Dish DISH1 = new Dish(105, LocalDate.now(), "Sopa De Pollo", 50.0, 103);
    public static final Dish DISH2 = new Dish(106, LocalDate.now(), "Garden salad", 20.0, 103);
    public static final Dish DISH3 = new Dish(107, LocalDate.now(), "Chiken", 35.5, 103);

    public static final Dish DISH4 = new Dish(108, LocalDate.now(), "Sopa De Res", 55.0, 104);
    public static final Dish DISH5 = new Dish(109, LocalDate.now(), "Taco salad", 22.0, 104);
    public static final Dish DISH6 = new Dish(110, LocalDate.now(), "Super Nachos", 36.0, 104);

    public static List<Dish> listDish = Arrays.asList(DISH1, DISH2, DISH3);

    public static final Dish NEW_DISH1 = new Dish(null, LocalDate.now(), "Sopa De Pollo2", 60.0, 103);
    public static final Dish NEW_DISH2 = new Dish(null, LocalDate.now(), "Garden salad2", 30.0, 103);
    public static final Dish NEW_DISH3 = new Dish(null, LocalDate.now(), "Chiken2", 45.5, 104);

    public static List<Dish> listRestaurant = Arrays.asList(DISH4, DISH5, DISH6);
    public static List<Dish> listNewDish = Arrays.asList(NEW_DISH1, NEW_DISH2, NEW_DISH3);

    public static final Restaurant BELKIN = new Restaurant(103,"Belkin", listDish);
    public static final Restaurant KARL_FRIDRICH = new Restaurant(104,"Karl Fridrich", listRestaurant);
    public static final Restaurant BELKIN2 = new Restaurant(null,"Belkin2", null);

    public static final Vote VOTE1 = new Vote(111, LocalDate.now(), 100, 103);
    public static final Vote VOTE2 = new Vote(112, LocalDate.now(), 101, 104);
    public static final Vote VOTE3 = new Vote(113, LocalDate.now(), 102, 103);
    public static final Vote NEW_VOTE = new Vote(null, LocalDate.now(), null, 103);

    public static void assertMatch(User actual, User expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "registered", "password");
    }

    public static void assertMatch(Iterable<User> actual, User... expected) {
        assertMatch(actual, List.of(expected));
    }

    public static void assertMatch(Iterable<User> actual, Iterable<User> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("registered", "password").isEqualTo(expected);
    }


    public static void assertMatchRestaurant(Restaurant actual, Restaurant expected) {
        assertThat(actual).isEqualTo(expected);
    }

    public static void assertMatchRestaurant(Iterable<Restaurant> actual, Restaurant... expected) {
        assertMatchRestaurant(actual, List.of(expected));
    }

    public static void assertMatchRestaurant(Iterable<Restaurant> actual, Iterable<Restaurant> expected) {
        assertThat(actual).isEqualTo(expected);
    }



    public static void assertMatchDish(Dish actual, Dish expected) {
        assertThat(actual).isEqualTo(expected);
    }

    public static void assertMatchDish(Iterable<Dish> actual, Dish... expected) {
        assertMatchDish(actual, List.of(expected));
    }

    public static void assertMatchDish(Iterable<Dish> actual, Iterable<Dish> expected) {
        assertThat(actual).isEqualTo(expected);
    }


    public static void assertMatchVote(Vote actual, Vote expected) {
        assertThat(actual).isEqualTo(expected);
    }

    public static void assertMatchVote(Iterable<Vote> actual, Vote... expected) {
        assertMatchVote(actual, List.of(expected));
    }

    public static void assertMatchVote(Iterable<Vote> actual, Iterable<Vote> expected) {
        assertThat(actual).isEqualTo(expected);
    }


    public static String jsonWithPassword(User user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }

    public static ResultMatcher contentJson(AbstractBaseEntity... expected) {
        List<? extends AbstractBaseEntity> entities = new ArrayList<>(Arrays.asList(expected));
        AbstractBaseEntity abstractBaseEntityTest = entities.get(0);

        if(abstractBaseEntityTest instanceof User){
            return result -> assertMatch(Utils.readListFromJsonMvcResult(result, User.class), (List<User>) entities);
        }
        if(abstractBaseEntityTest instanceof Restaurant){
            return result -> assertMatchRestaurant(Utils.readListFromJsonMvcResult(result, Restaurant.class), (List<Restaurant>) entities);
        }
        if(abstractBaseEntityTest instanceof Dish){
            return result -> assertMatchDish(Utils.readListFromJsonMvcResult(result, Dish.class), (List<Dish>) entities);
        }
        else{
            return result -> assertMatchVote(Utils.readListFromJsonMvcResult(result, Vote.class), (List<Vote>) entities);
        }
    }

    public static ResultMatcher contentJson(AbstractBaseEntity expected) {
        if(expected instanceof User){
            User expectedUser = (User) expected;
            return result -> assertMatch(Utils.readFromJsonMvcResult(result, User.class), expectedUser);
        }
        if(expected instanceof Restaurant){
            Restaurant expectedRestaurant = (Restaurant) expected;
            return result -> assertMatchRestaurant(Utils.readFromJsonMvcResult(result, Restaurant.class), expectedRestaurant);
        }
        if(expected instanceof Dish){
            Dish expectedDish = (Dish) expected;
            return result -> assertMatchDish(Utils.readFromJsonMvcResult(result, Dish.class), expectedDish);
        }
        else{
            Vote expectedVote = (Vote) expected;
            return result -> assertMatchVote(Utils.readFromJsonMvcResult(result, Vote.class), expectedVote);
        }
    }
}
