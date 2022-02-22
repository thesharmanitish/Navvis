package com.navvis.test;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.navvis.db.PhoneNumberRepository;
import com.navvis.db.PhoneNumbers;
import com.navvis.model.GPPRequest;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
        @AutoConfigureMockMvc
        public class GermanPhoneParserTest {

            @Autowired
            private MockMvc mockMvc;
            @MockBean
            private PhoneNumberRepository repository;

            @Test
            public void postPhoneNumbers() throws Exception {
                GPPRequest request = new GPPRequest("src/main/resources/test/phone_numbers_1.txt");
                this.mockMvc.perform(post("/api/process/file").content(new Gson().toJson(request))
                        .header("Content-Type","application/json")
                        .header("accept","application/json")).andDo(print()).andExpect(status().isOk())
                        .andExpect(status().isOk());
            }
            @Test
            public void postInvalidPhoneNumbers() throws Exception {
                GPPRequest request = new GPPRequest("src/main/resources/test/phone_numbers1.txt");
                this.mockMvc.perform(post("/api/process/file").content(new Gson().toJson(request))
                        .header("Content-Type","application/json")
                        .header("accept","application/json")).andDo(print()).andExpect(status().isOk())
                        .andExpect(content().string("{\"msg\":\"src/main/resources/test/phone_numbers1.txt\",\"errorCode\":\"400\"}"));
            }
            @Test
            public void getPhoneNumberViaTaskID() throws Exception {
                PhoneNumbers phoneNumbers = new PhoneNumbers("1","+91000000");
                Mockito.when(repository.findById( any(String.class)
                )).thenReturn(Optional.of(phoneNumbers));
                Mockito.when(repository.save(phoneNumbers)).thenReturn(phoneNumbers);

                this.mockMvc.perform(get("/api/process/file/"+phoneNumbers.getTaskID())).andDo(print()).andExpect(status().isOk())
                        .andExpect(content().string(containsString("+91000000")));
            }

    @Test
    public void getAllTaskIDs() throws Exception {
        PhoneNumbers phoneNumbers1 = new PhoneNumbers("1","+91000000");
        PhoneNumbers phoneNumbers2 = new PhoneNumbers("2","+91000001");
        List<PhoneNumbers> phoneNumbersList = new ArrayList<>();
        phoneNumbersList.add(phoneNumbers1);
        phoneNumbersList.add(phoneNumbers2);
        Mockito.when(repository.findAll()).thenReturn(phoneNumbersList);

        this.mockMvc.perform(get("/api/tasks")).andDo(print()).andExpect(status().isOk())
                .andExpect(result -> result.getResponse().getContentAsString().contains("2"));
    }
}
