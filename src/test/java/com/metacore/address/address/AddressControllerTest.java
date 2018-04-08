package com.metacore.address.address;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacore.address.address.resource.AddressResourceRequest;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration
@WebAppConfiguration
public class AddressControllerTest {

  MockMvc mockMvc;

  @Rule
  public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

  private RestDocumentationResultHandler documentationHandler;

  @Autowired
  private WebApplicationContext context;

  @Autowired
  ObjectMapper objectMapper;

  @InjectMocks
  AddressController controller;

  @Before
  public void setup() {
    setupDocumentationHandler();

    this.mockMvc = webAppContextSetup(context).apply(documentationConfiguration(restDocumentation))
        .alwaysDo(documentationHandler).build();
  }

  private void setupDocumentationHandler() {
    documentationHandler = document("{method-name}", preprocessRequest(prettyPrint()),
        preprocessResponse(prettyPrint()));
  }

  @Test
  public void create_noRequestBody_expectBadRequestError() throws Exception {
    mockMvc.perform(post("/v1/address")).andExpect(status().isBadRequest());
  }

  @Test
  public void create_emptyRequestBody_expectBadRequestError() throws Exception {
    postAddress(new AddressResourceRequest()).andExpect(status().isBadRequest());
  }

  @Test
  public void create_requestWithAllFields_expectOk() throws Exception {
    AddressResourceRequest request = createAddressResourceRequest();

    postAddress(request).andExpect(status().isOk()).andExpect(jsonPath("$.number").value(request.number.toUpperCase()))
        .andExpect(jsonPath("$.unit").value(request.unit.toUpperCase()))
        .andExpect(jsonPath("$.street").value(request.street.toUpperCase()))
        .andExpect(jsonPath("$.city").value(request.city.toUpperCase()))
        .andExpect(jsonPath("$.state").value(request.state.toUpperCase()))
        .andExpect(jsonPath("$.zip").value(request.zip.toUpperCase().replace(" ", "")))
        .andExpect(jsonPath("$.country").value(request.country.toUpperCase()));
  }

  @Test
  public void get_missingId_expect405() throws Exception {
    mockMvc.perform(get("/v1/address")).andExpect(status().isMethodNotAllowed());
  }

  @Test
  public void get_invalidId_expect404() throws Exception {
    mockMvc.perform(get("/v1/address/badId")).andExpect(status().isNotFound());
  }

  @Ignore
  public void get_requstWithAllFields_expectOk() throws Exception {
    AddressResourceRequest request = createAddressResourceRequest();

    ResultActions actions = postAddress(request).andExpect(status().isOk());

  }

  private static AddressResourceRequest createAddressResourceRequest() {
    AddressResourceRequest request = new AddressResourceRequest();
    request.number = "300";
    request.unit = "434";
    request.street = "March Rd.";
    request.city = "Kanata";
    request.state = "ON";
    request.zip = "K2K 2E2";
    request.country = "CA";
    return request;
  }

  private static Address toDomainObject(AddressResourceRequest resource) {
    return AddressAssembler.toDomainObject(resource);
  }

  private ResultActions postAddress(AddressResourceRequest request) throws Exception {
    return mockMvc.perform(post("/v1/address").contentType(MediaType.APPLICATION_JSON).content(asJsonString(request)))
        .andDo(print());
  }

  String asJsonString(final Object obj) {
    try {
      return objectMapper.writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}