package ru.catstack.auth.advice;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.catstack.auth.exception.*;
import ru.catstack.auth.model.payload.response.ApiErrorResponse;
import ru.catstack.auth.model.payload.response.ApiResponse;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@ControllerAdvice
public class AuthControllerAdvice extends ResponseEntityExceptionHandler {
    private final MessageSource messageSource;

    @Autowired
    public AuthControllerAdvice(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private String resolveLocalizedErrorMessage(ObjectError objectError) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        String localizedErrorMessage = messageSource.getMessage(objectError, currentLocale);
        logger.info(localizedErrorMessage);
        return localizedErrorMessage;
    }

    private String pathFromRequest(WebRequest request) {
        try {
            return ((ServletWebRequest) request).getRequest().getAttribute("javax.servlet.forward.request_uri").toString();
        } catch (Exception ex) {
            return null;
        }
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiResponse handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        var response = new ApiErrorResponse(ex.getMessage(), 404, ex.getClass().getName(), pathFromRequest(request));
        return new ApiResponse(response);
    }

    @ExceptionHandler(value = ResourceAlreadyInUseException.class)
    @ResponseStatus(HttpStatus.IM_USED)
    @ResponseBody
    public ApiResponse handleResourceAlreadyInUseException(ResourceAlreadyInUseException ex, WebRequest request) {
        var response = new ApiErrorResponse(ex.getMessage(), 226, ex.getClass().getName(), pathFromRequest(request));
        return new ApiResponse(response);
    }

    @ExceptionHandler(value = BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiResponse handleBadRequestException(BadRequestException ex, WebRequest request) {
        var response = new ApiErrorResponse(ex.getMessage(), 400, ex.getClass().getName(), pathFromRequest(request));
        return new ApiResponse(response);
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiResponse handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        var response = new ApiErrorResponse(ex.getMessage(), 404, ex.getClass().getName(), pathFromRequest(request));
        return new ApiResponse(response);
    }

    @ExceptionHandler(value = UserLoginException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ResponseBody
    public ApiResponse handleUserLoginException(UserLoginException ex, WebRequest request) {
        var response = new ApiErrorResponse(ex.getMessage(), 417, ex.getClass().getName(), pathFromRequest(request));
        return new ApiResponse(response);
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ResponseBody
    public ApiResponse handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        var response = new ApiErrorResponse(ex.getMessage(), 417, ex.getClass().getName(), pathFromRequest(request));
        return new ApiResponse(response);
    }

    @ExceptionHandler(value = UserRegistrationException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ResponseBody
    public ApiResponse handleUserRegistrationException(@NotNull UserRegistrationException ex, WebRequest request) {
        var response = new ApiErrorResponse(ex.getMessage(), 417, ex.getClass().getName(), pathFromRequest(request));
        return new ApiResponse(response);
    }

    @ExceptionHandler(value = TokenRefreshException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ResponseBody
    public ApiResponse handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
        var response = new ApiErrorResponse(ex.getMessage(), 417, ex.getClass().getName(), pathFromRequest(request));
        return new ApiResponse(response);
    }

    @ExceptionHandler(value = InvalidJwtTokenException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ResponseBody
    public ApiResponse handleInvalidJwtTokenException(TokenRefreshException ex, WebRequest request) {
        var response = new ApiErrorResponse( ex.getMessage(), 417, ex.getClass().getName(), pathFromRequest(request));
        return new ApiResponse(response);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        var errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse(ex.getMessage());
        var response = new ApiErrorResponse(errorMessage, 417, ex.getClass().getName(), pathFromRequest(request));
        return new ResponseEntity<>(new ApiResponse(response), HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler(value = JwtAuthenticationException.class)
    @ResponseBody
    public ApiResponse handleJwtAuthenticationException(JwtAuthenticationException ex, WebRequest request) {
        var response = new ApiErrorResponse(ex.getMessage(), 417, ex.getClass().getName(), pathFromRequest(request));
        return new ApiResponse(response);
    }

    @ExceptionHandler(value = NoContentException.class)
    @ResponseBody
    public ApiResponse handleNoContentException(NoContentException ex, WebRequest request) {
        var response = new ApiErrorResponse(ex.getMessage(), 417, ex.getClass().getName(), pathFromRequest(request));
        return new ApiResponse(response);
    }

    @ExceptionHandler(value = ApplicationAlreadySentException.class)
    @ResponseBody
    public ApiResponse handleApplicationAlreadySentException(ApplicationAlreadySentException ex, WebRequest request) {
        var response = new ApiErrorResponse(ex.getMessage(), 226, ex.getClass().getName(), pathFromRequest(request));
        return new ApiResponse(response);
    }

    @ExceptionHandler(value = UserAlreadyInTeamException.class)
    @ResponseBody
    public ApiResponse handleUserAlreadyInTeamException(UserAlreadyInTeamException ex, WebRequest request) {
        var response = new ApiErrorResponse(ex.getMessage(), 226, ex.getClass().getName(), pathFromRequest(request));
        return new ApiResponse(response);
    }

    @ExceptionHandler(value = ApplicationSendException.class)
    @ResponseBody
    public ApiResponse handleApplicationSendException(ApplicationSendException ex, WebRequest request) {
        var response = new ApiErrorResponse(ex.getMessage(), 417, ex.getClass().getName(), pathFromRequest(request));
        return new ApiResponse(response);
    }
}