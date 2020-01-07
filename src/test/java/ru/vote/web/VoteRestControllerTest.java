package ru.vote.web;

import ru.vote.Utils;
import ru.vote.model.Vote;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.vote.service.VoteService;
import ru.vote.json.JsonUtil;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.vote.Data_test.*;
import static ru.vote.Utils.userHttpBasic;
import static ru.vote.projectUtils.exception.ErrorType.*;


class VoteRestControllerTest extends AbstractControllerTest {

    private static final String VOTE_URL = VoteRestController.REST_URL + '/';

    @Autowired
    VoteService voteService;

    @Test
    void getAllActual() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(VOTE_URL)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(VOTE3, VOTE2, VOTE1));
    }

    @Test
    void getAllHistory() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(VOTE_URL)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(VOTE3, VOTE2, VOTE1));
    }

    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(VOTE_URL + 111)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(VOTE1));
    }

    @Test
    void getNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(VOTE_URL + 1)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void createWithLocation() throws Exception {
        Vote expected = NEW_VOTE;
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(VOTE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(expected)))
                .andExpect(status().isCreated());

        Vote returned = Utils.readFromJson(action, Vote.class);
        expected.setId(returned.getId());
        assertEquals(returned, expected);
    }

    @Test
    void createInvalid() throws Exception {
        Vote expected = new Vote(null, LocalDate.now(), null, null);
        mockMvc.perform(MockMvcRequestBuilders.post(VOTE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(expected)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR))
                .andDo(print());
    }

    @Test
    void updateInvalid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/rest/admin/users/localtime")
                .with(userHttpBasic(ADMIN))
                .param("localTime", "23:59:00"));

        Vote updated = new Vote(VOTE2);
        updated.setRest_id(null);
        mockMvc.perform(MockMvcRequestBuilders.put(VOTE_URL + 112)
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
        mockMvc.perform(MockMvcRequestBuilders.delete(VOTE_URL + 112)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatchVote(voteService.getAllActual(), VOTE3, VOTE1);
    }

    @Test
    void deleteNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(VOTE_URL + 1)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void deleteForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(VOTE_URL + 112)
                .with(userHttpBasic(USER)))
                .andExpect(status().isForbidden());
    }

    @Test
    void update() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/rest/admin/users/localtime")
                .with(userHttpBasic(ADMIN))
                .param("localTime", "23:59:00"));

        Vote updated = new Vote(VOTE2);
        updated.setRest_id(102);
        mockMvc.perform(MockMvcRequestBuilders.put(VOTE_URL + 112)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        assertMatchVote(voteService.get(112), updated);
    }

    @Test
    void updateAfter11AM() throws Exception {
        Vote updated = new Vote(VOTE2);
        updated.setRest_id(104);
        mockMvc.perform(MockMvcRequestBuilders.put(VOTE_URL + 112)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicate() throws Exception {
        Vote updated = new Vote(VOTE1);
        updated.setUser_id(101);
        mockMvc.perform(MockMvcRequestBuilders.put(VOTE_URL + 111)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isUnavailableForLegalReasons())
                .andExpect(errorType(APP_ERROR))
                .andDo(print());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() throws Exception {
        Vote expected = new Vote(VOTE1);
        expected.setId(null);
        mockMvc.perform(MockMvcRequestBuilders.post(VOTE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(expected)))
                .andExpect(status().isConflict())
                .andExpect(errorType(DATA_ERROR));


    }
}