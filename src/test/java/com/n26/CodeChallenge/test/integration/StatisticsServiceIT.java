package com.n26.CodeChallenge.test.integration;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.web.context.WebApplicationContext;
import com.n26.CodeChallenge.Application;
import com.n26.CodeChallenge.model.Transaction;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class StatisticsServiceIT {
  private static final Logger log = LoggerFactory.getLogger(StatisticsServiceIT.class);

  private MediaType mediaType = new MediaType(MediaType.APPLICATION_JSON.getType(),
      MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @SuppressWarnings("rawtypes")
  private HttpMessageConverter mappingJackson2HttpMessageConverter;

  @Autowired
  void setConverters(HttpMessageConverter<?>[] converters) {

    this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
        .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().orElse(null);

    assertNotNull("the JSON message converter must not be null",
        this.mappingJackson2HttpMessageConverter);
  }

  @Before
  public void setup() throws Exception {
    mockMvc = webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void transactionThenStatisticsTest() throws Exception {
    Transaction transaction = new Transaction();

    transaction.setAmount(200d);
    transaction.setTimestamp(System.currentTimeMillis());
    mockMvc.perform(post("/transactions/").content(this.jsonSting(transaction)).contentType(mediaType))
        .andExpect(status().isCreated());

    transaction.setAmount(100d);
    transaction.setTimestamp(System.currentTimeMillis());
    mockMvc.perform(post("/transactions/").content(this.jsonSting(transaction)).contentType(mediaType))
        .andExpect(status().isCreated());

    mockMvc.perform(get("/statistics/")).andExpect(status().isOk())
        .andExpect(content().contentType(mediaType)).andDo(new ResultHandler() {
          @Override
          public void handle(MvcResult result) throws Exception {
            log.debug("result.getResponse().getContentAsString() = {}",
                result.getResponse().getContentAsString());
          }
        }).andExpect(jsonPath("$.sum", is(300d))).andExpect(jsonPath("$.avg", is(150d)))
        .andExpect(jsonPath("$.max", is(200d))).andExpect(jsonPath("$.min", is(100d)))
        .andExpect(jsonPath("$.count", is(2)));

    Thread.sleep(61000);
    mockMvc.perform(get("/statistics/")).andExpect(status().isOk())
        .andExpect(content().contentType(mediaType)).andDo(new ResultHandler() {
          @Override
          public void handle(MvcResult result) throws Exception {
            log.debug("result.getResponse().getContentAsString() = {}",
                result.getResponse().getContentAsString());
          }
        }).andExpect(jsonPath("$.sum", is(0d))).andExpect(jsonPath("$.avg", is(0d)))
        .andExpect(jsonPath("$.max", is(Double.MIN_VALUE))).andExpect(jsonPath("$.count", is(0)));
  }

  @SuppressWarnings("unchecked")
  protected String jsonSting(Object o) throws IOException {
    MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
    this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON,
        mockHttpOutputMessage);
    return mockHttpOutputMessage.getBodyAsString();
  }
}
