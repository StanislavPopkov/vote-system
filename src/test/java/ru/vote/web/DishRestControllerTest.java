package ru.vote.web;

import ru.vote.model.Dish;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.vote.service.DishService;
import ru.vote.json.JsonUtil;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.vote.Data_test.*;
import static ru.vote.Utils.readFromJson;
import static ru.vote.Utils.userHttpBasic;
import static ru.vote.projectUtils.exception.ErrorType.VALIDATION_ERROR;
import static ru.vote.web.DishRestController.REST_URL;

class DishRestControllerTest extends AbstractControllerTest{

    private static final String DISH_URL = DishRestController.REST_URL + '/';

    @Autowired
    DishService dishService;

    @Test
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(DISH1, DISH2, DISH3, DISH4, DISH5, DISH6));
    }

    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(DISH_URL + 105)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(DISH1));
    }

    @Test
    void getNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(DISH_URL + 1)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void createWithLocation() throws Exception {
        Dish expected = NEW_DISH1;
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(DISH_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(expected)))
                .andExpect(status().isCreated());

        Dish returned = readFromJson(action, Dish.class);
        expected.setId(returned.getId());
        assertEquals(returned, expected);
    }

    @Test
    void createInvalid() throws Exception {
        Dish expected = new Dish(null, null, null, 1.0, 1);
        mockMvc.perform(MockMvcRequestBuilders.post(DISH_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(expected)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR))
                .andDo(print());
    }

    @Test
    void updateInvalid() throws Exception {
        Dish updated = new Dish(DISH1);
        updated.setDish_name("");
        mockMvc.perform(MockMvcRequestBuilders.put(DISH_URL + 106)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(errorType(VALIDATION_ERROR))
                .andDo(print());
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(DISH_URL + 110)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatchDish(dishService.getAll(), DISH1, DISH2, DISH3, DISH4, DISH5);
    }

    @Test
    void deleteNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(DISH_URL + 1)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void deleteForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(DISH_URL + 110)
                .with(userHttpBasic(USER)))
                .andExpect(status().isForbidden());
    }

    @Test
    void update() throws Exception {
        Dish updated = new Dish(DISH1);
        updated.setDish_name("Soap");
        mockMvc.perform(MockMvcRequestBuilders.put(DISH_URL + 105)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        assertMatchDish(dishService.get(105), updated);
    }
}