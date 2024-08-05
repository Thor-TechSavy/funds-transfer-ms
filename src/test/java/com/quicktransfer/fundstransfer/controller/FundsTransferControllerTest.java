package com.quicktransfer.fundstransfer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quicktransfer.fundstransfer.dto.FundsTransferRequestDto;
import com.quicktransfer.fundstransfer.entity.FundsTransferEntity;
import com.quicktransfer.fundstransfer.enums.FundsRequestStatus;
import com.quicktransfer.fundstransfer.service.FundsTransferService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FundsTransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private FundsTransferService fundsTransferService;

    private ObjectMapper objectMapper;

    private FundsTransferEntity fundsTransferEntity;
    private FundsTransferRequestDto fundsTransferRequestDto;

    @BeforeEach
    void setUp() {

        objectMapper = new ObjectMapper();

        fundsTransferEntity = new FundsTransferEntity();
        fundsTransferEntity.setFromOwnerID(UUID.randomUUID());
        fundsTransferEntity.setToOwnerID(UUID.randomUUID());
        fundsTransferEntity.setAmount(BigDecimal.TEN);
        fundsTransferEntity.setStatus(FundsRequestStatus.PENDING);

        fundsTransferRequestDto = new FundsTransferRequestDto();
        fundsTransferRequestDto.setFromOwnerId(fundsTransferEntity.getFromOwnerID());
        fundsTransferRequestDto.setToOwnerId(fundsTransferEntity.getToOwnerID());
        fundsTransferRequestDto.setAmount(fundsTransferEntity.getAmount());

    }

    @AfterEach
    void tearDown() {
        reset(
                fundsTransferService);
    }

    @Test
    void testCreateFundsTransferRequest() throws Exception {

        when(fundsTransferService.createFundsTransferRequest(any(FundsTransferEntity.class)))
                .thenReturn(fundsTransferEntity);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/funds-transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fundsTransferRequestDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateFundsTransferRequestInvalidRequest() throws Exception {
        FundsTransferRequestDto invalidDto = new FundsTransferRequestDto(); // Missing required fields

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/funds-transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

//    @Test
//    void testGetFundsTransferRequest() throws Exception {
//        UUID uuid = fundsTransferEntity.getFundsTransferRequestUUID();
//        when(fundsTransferService.getFundsTransferRequest(uuid)).thenReturn(fundsTransferEntity);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/v1/funds-transfer/" + uuid)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.requestIdentifier").value(fundsTransferEntity.getRequestIdentifier()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(fundsTransferEntity.getStatus().toString()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.fundsTransferRequestUUID").value(fundsTransferEntity.getFundsTransferRequestUUID().toString()));
//    }
}
