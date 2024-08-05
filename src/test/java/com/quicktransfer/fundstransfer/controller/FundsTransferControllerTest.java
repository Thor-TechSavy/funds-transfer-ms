package com.quicktransfer.fundstransfer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quicktransfer.fundstransfer.dto.FundsTransferRequestDto;
import com.quicktransfer.fundstransfer.entity.FundsTransferEntity;
import com.quicktransfer.fundstransfer.enums.FundsRequestStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FundsTransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

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


    @Test
    void testCreateFundsTransferRequest() throws Exception {

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

}
