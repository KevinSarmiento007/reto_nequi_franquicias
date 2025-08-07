package com.kass.prueba_nequi.franquicias_api.domain.exceptions;


import com.kass.prueba_nequi.franquicias_api.domain.enums.TechnicalMessage;
import lombok.Getter;

@Getter
public class BusinessException extends ProcessorException {

    public BusinessException(TechnicalMessage technicalMessage) {
        super(technicalMessage.getMessage(), technicalMessage);
    }
}
