package com.metacore.address.address;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.RequestBodySpec;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.web.reactive.function.BodyInserters;

import com.metacore.address.address.resource.AddressResourceRequest;

@RunWith(SpringRunner.class)
@WebFluxTest(AddressController.class)
@ExtendWith({ RestDocumentationExtension.class, SpringExtension.class })
public class AddressControllerTest {

  @Rule
  public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

  @Autowired
  private ApplicationContext context;

  WebTestClient client;

  @MockBean
  AddressService addressService;

  @Before
  public void setUp() {
    client = WebTestClient.bindToApplicationContext(context).configureClient()
        .filter(documentationConfiguration(restDocumentation)).build();
  }

  @Test
  public void create_noRequestBody_expectBadRequestError() throws Exception {
    post().exchange().expectStatus().isBadRequest();
  }

  @Test
  public void create_emptyRequestBody_expectBadRequestError() throws Exception {
    post().body(BodyInserters.fromObject(new AddressResourceRequest())).exchange().expectStatus().isBadRequest();
  }

  @Test
  public void create_missingMandatoryFields_expectBadRequestError() {
    AddressResourceRequest resource = createAddressResourceRequest();
    resource.number = null;
    post().body(BodyInserters.fromObject(resource)).exchange().expectStatus().isBadRequest().expectBody()
        .jsonPath("$.errors").isArray();
  }

  @Test
  public void create_requestWithAllFields_expectOk() throws Exception {
    AddressResourceRequest request = createAddressResourceRequest();
    Address expected = createAddress(request);

    when(addressService.create(any(Address.class))).thenReturn(expected);

    assertResponseBody(expected, post().body(BodyInserters.fromObject(request)).exchange().expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8).expectHeader().exists("location"));
  }

  private RequestBodySpec post() {
    return client.post().uri("/v1/address").accept(MediaType.APPLICATION_JSON_UTF8);
  }

  @Test
  public void get_missingId_expect405() throws Exception {
    client.get().uri("/v1/address").accept(MediaType.APPLICATION_JSON_UTF8).exchange().expectStatus()
        .isEqualTo(HttpStatus.METHOD_NOT_ALLOWED).expectBody().isEmpty();
  }

  @Test
  public void get_invalidId_expect404() throws Exception {
    client.get().uri("/v1/address/badId").accept(MediaType.APPLICATION_JSON_UTF8).exchange().expectStatus().isNotFound()
        .expectBody().isEmpty();
  }

  @Test
  public void get_requstWithAllFields_expectOk() throws Exception {
    AddressResourceRequest request = createAddressResourceRequest();
    Address expected = createAddress(request);

    when(addressService.create(any(Address.class))).thenReturn(expected);
    when(addressService.get(anyString())).thenReturn(Optional.of(expected));

    post().body(BodyInserters.fromObject(request));

    assertResponseBody(expected, client.get().uri("/v1/address/addressId").accept(MediaType.APPLICATION_JSON_UTF8)
        .exchange().expectStatus().isOk().expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8));
  }

  @Test
  public void delete_missingId_expect405() {
    client.delete().uri("/v1/address").accept(MediaType.APPLICATION_JSON_UTF8).exchange().expectStatus()
        .isEqualTo(HttpStatus.METHOD_NOT_ALLOWED).expectBody().isEmpty();
  }

  @Test
  public void delete_badId_expect202() {
    client.delete().uri("/v1/address/badId").accept(MediaType.APPLICATION_JSON_UTF8).exchange().expectStatus()
        .isAccepted().expectBody().isEmpty();
  }

  @Test
  public void delete_goodId_expect202() {
    client.delete().uri("/v1/address/goodId").accept(MediaType.APPLICATION_JSON_UTF8).exchange().expectStatus()
        .isAccepted().expectBody().isEmpty();
  }

  @Test
  public void post_documentation() {
    AddressResourceRequest request = createAddressResourceRequest();
    Address expected = createAddress(request);

    when(addressService.create(any(Address.class))).thenReturn(expected);

    client.post().uri("/v1/address").accept(MediaType.APPLICATION_JSON_UTF8).body(BodyInserters.fromObject(request))
        .exchange().expectStatus().isCreated().expectBody()
        .consumeWith(document("post-address", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
            requestFields(fieldWithPath("number").description("The address street number"),
                fieldWithPath("unit").description("The address unit number"),
                fieldWithPath("street").description("The address street name"),
                fieldWithPath("city").description("The address city"),
                fieldWithPath("zip").description("The address postal number"),
                fieldWithPath("state").description("The address state or province"),
                fieldWithPath("country").description("The address country")),
            responseFields(fieldWithPath("id").description("The unique id of the address"),
                fieldWithPath("number").description("The address street number"),
                fieldWithPath("unit").description("The address unit number"),
                fieldWithPath("street").description("The address street name"),
                fieldWithPath("city").description("The address city"),
                fieldWithPath("zip").description("The address postal number"),
                fieldWithPath("state").description("The address state or province"),
                fieldWithPath("country").description("The address country")),
            responseHeaders(headerWithName("location").description("Locaton of created resource"))));
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

  private static Address createAddress(AddressResourceRequest resource) {
    Address address = toDomainObject(resource);
    address.setId("addressId");
    return address;
  }

  private static Address toDomainObject(AddressResourceRequest resource) {
    return AddressAssembler.toDomainObject(resource);
  }

  private static void assertResponseBody(Address expected, ResponseSpec actual) {
    actual.expectBody().jsonPath("$.id").isEqualTo(expected.getId()).jsonPath("$.number")
        .isEqualTo(expected.getNumber()).jsonPath("$.unit").isEqualTo(expected.getUnit()).jsonPath("$.street")
        .isEqualTo(expected.getStreet()).jsonPath("$.city").isEqualTo(expected.getCity()).jsonPath("$.state")
        .isEqualTo(expected.getState()).jsonPath("$.zip").isEqualTo(expected.getZip()).jsonPath("$.country")
        .isEqualTo(expected.getCountry());
  }
}