package itcast;

import lombok.Getter;
import lombok.Setter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class ResponseTemplate<T> {
    int statusCode;
    String message;
    T data;
    HttpHeaders headers;

    public ResponseTemplate(HttpStatus httpStatus, String message, T data) {
        this.statusCode = httpStatus.value();
        this.message = message;
        this.data = data;
    }

    public ResponseTemplate(HttpStatus httpStatus, String message) {
        this.statusCode = httpStatus.value();
        this.message = message;
    }

}
