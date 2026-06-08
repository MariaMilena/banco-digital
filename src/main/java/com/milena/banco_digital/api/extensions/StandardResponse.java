package com.milena.banco_digital.api.extensions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StandardResponse<R> {
    private boolean success;
    private int code;
    private R data;
    private String message;
    private List<String> errors;

    public static <R> ResponseEntity<StandardResponse<R>> success(R data) {
        return ResponseEntity.ok(
                new StandardResponse<>(true, 200, data, "Operação realizada com sucesso.", null));
    }

    public static <R> ResponseEntity<StandardResponse<R>> success() {
        return ResponseEntity.ok(
                new StandardResponse<>(true, 200, null, "Operação realizada com sucesso.", null));
    }

    public static <R> ResponseEntity<StandardResponse<R>> created(R data) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new StandardResponse<>(true, 201, data, "Recurso criado com sucesso.", null));
    }

    public static <R> ResponseEntity<StandardResponse<R>> notFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new StandardResponse<>(false, 404, null, "Recurso não encontrado.", null));
    }

    public static <R> ResponseEntity<StandardResponse<R>> badRequest(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new StandardResponse<>(false, 400, null, "Requisição inválida.",
                        Collections.singletonList(message)));
    }

    public static <R> ResponseEntity<StandardResponse<R>> badRequest(List<String> errors) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new StandardResponse<>(false, 400, null, "Requisição inválida.", errors));
    }

    public static <R> ResponseEntity<StandardResponse<R>> error(String message) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new StandardResponse<>(false, 500, null, message,
                        Collections.singletonList(message)));
    }
}