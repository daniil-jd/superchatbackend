package ru.itpark.handler;

import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import ru.itpark.dto.exception.ErrorDto;
import ru.itpark.dto.exception.ExceptionResponseDto;
import ru.itpark.exception.AuthenticateTokenException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("${server.error.path:${error.path:/error}}")
public class DefaultExceptionHandler extends AbstractErrorController {
    private final ErrorAttributes errorAttributes;
    private final MessageSource messageSource;

    public DefaultExceptionHandler(
            ErrorAttributes errorAttributes,
            MessageSource messageSource
    ) {
        super(errorAttributes);
        this.errorAttributes = errorAttributes;
        this.messageSource = messageSource;
    }

    @RequestMapping
    public ResponseEntity<ExceptionResponseDto> exceptionHandle(HttpServletRequest request, Locale locale) {
        var webRequest = new ServletWebRequest(request);
        var exception = errorAttributes.getError(webRequest);
        var status = getStatus(request);
        var defaultMessage = "default message";

        if (exception == null) {
            return ResponseEntity.status(status).body(new ExceptionResponseDto("UNKNOWN_EXCEPTION", defaultMessage, Collections.emptyList()));
        }

        if (exception instanceof MethodArgumentNotValidException) {
            var fieldErrorsList = ((MethodArgumentNotValidException) exception).getBindingResult().getFieldErrors();
            List<ErrorDto> errorDtoList = new ArrayList<>();
            for (FieldError error : fieldErrorsList) {
                errorDtoList.add(new ErrorDto(error.getField(), error.getDefaultMessage()));
            }

            return ResponseEntity.status(status).body(
                    new ExceptionResponseDto(
                            "VALIDATION_EXCEPTION",
                            messageSource.getMessage(
                                    exception.getMessage(),
                                    null,
                                    defaultMessage,
                                    locale),
                            errorDtoList
                    )
            );
        } else if (exception instanceof AuthenticateTokenException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ExceptionResponseDto("INVALID_TOKEN", exception.getMessage(), null)
            );
        }

        return ResponseEntity.status(status).body(new ExceptionResponseDto("UNKNOWN_EXCEPTION", defaultMessage, Collections.emptyList()));
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
