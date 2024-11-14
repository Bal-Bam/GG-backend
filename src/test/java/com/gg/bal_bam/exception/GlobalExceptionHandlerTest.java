package com.gg.bal_bam.exception;

import com.gg.bal_bam.common.ResponseTemplate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleCustomException() {
        CustomException customException = new CustomException("CustomException test");
        ResponseTemplate<String> response = globalExceptionHandler.handleCustomException(customException);

        assertThat(response.isStatus()).isFalse(); //status가 false인지 확인
        assertThat(response.getMessage()).isEqualTo("요청에 실패했습니다."); //message가 일치하는지 확인
    }

    @Test
    void handleException() {
        Exception exception = new Exception("Exception test");
        ResponseTemplate<String> response = globalExceptionHandler.handleException(exception);

        assertThat(response.isStatus()).isFalse(); //status가 false인지 확인
        assertThat(response.getMessage()).isEqualTo("요청에 실패했습니다."); //message가 일치하는지 확인
    }
}