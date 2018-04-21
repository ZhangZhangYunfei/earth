package com.yxedu.earth.payment.endpoint;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yxedu.earth.payment.domain.Order;
import com.yxedu.earth.payment.service.OrderService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = OrderEndpoint.class)
public class OrderEndpointTests {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private OrderService orderService;

  @Test
  public void shouldReturnOrder() throws Exception {
    Order order = Order.builder().merchantId(1L).no("1").build();
    given(orderService.queryOrder(any(), anyString())).willReturn(order);

    mockMvc.perform(get("/")
//        .session(makeAuthSession("yunfei", Constants.ROLE_ADMIN))
        .param("merchantId", order.getMerchantId().toString())
        .param("no", order.getNo()))
//        .andExpect(content().string("Hello Peter Pan!"))
        .andExpect(status().isUnauthorized());
  }
}
