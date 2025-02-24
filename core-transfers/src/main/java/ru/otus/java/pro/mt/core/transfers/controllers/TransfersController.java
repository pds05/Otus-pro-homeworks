package ru.otus.java.pro.mt.core.transfers.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.java.pro.mt.core.transfers.dtos.ExecuteTransferDtoRq;
import ru.otus.java.pro.mt.core.transfers.dtos.TransferDto;
import ru.otus.java.pro.mt.core.transfers.dtos.TransfersPageDto;
import ru.otus.java.pro.mt.core.transfers.entities.Transfer;
import ru.otus.java.pro.mt.core.transfers.exceptions_handling.ErrorDto;
import ru.otus.java.pro.mt.core.transfers.exceptions_handling.ResourceNotFoundException;
import ru.otus.java.pro.mt.core.transfers.services.TransfersService;

import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transfers")
@Tag(name = "Переводы", description = "Методы работы с переводами")
public class TransfersController {
    private final TransfersService transfersService;

    private static final Function<Transfer, TransferDto> ENTITY_TO_DTO = t -> new TransferDto(t.getId(), t.getClientId(), t.getTargetClientId(), t.getSourceAccount(), t.getTargetAccount(), t.getMessage(), t.getAmount());

    @GetMapping
    @Operation(
            summary = "Запрос списка переводов клиента",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransfersPageDto.class))
                    )
            }
    )
    public TransfersPageDto getAllTransfers(
            @Parameter(description = "Идентификатор клиента", required = true, schema = @Schema(type = "string", maxLength = 10, example = "1234567890"))
            @RequestHeader(name = "client-id") String clientId
    ) {
        return new TransfersPageDto(
                transfersService
                        .getAllTransfers(clientId)
                        .stream()
                        .map(ENTITY_TO_DTO).collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Запрос перевода клиента по идентификатору перевода",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransferDto.class))
                    ),
                    @ApiResponse(
                            description = "Указанный перевод не найден", responseCode = "404",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class))
                    )
            }
    )
    public TransferDto getTransferById(
            @Parameter(description = "Идентификатор клиента", required = true, schema = @Schema(type = "string", maxLength = 10, example = "1234567890"))
            @RequestHeader(name = "client-id") String clientId,

            @Parameter(description = "Идентификатор перевода", required = true, schema = @Schema(type = "string", maxLength = 36, example = "bde76ffa-f133-4c23-9bca-03618b2a94b2"))
            @PathVariable String id
    ) {
        return ENTITY_TO_DTO.apply(transfersService.getTransferById(id, clientId).orElseThrow(() -> new ResourceNotFoundException("Перевод не найден")));
    }

    @PostMapping
    @Operation(summary = "Запрос на исполнение перевода")
    public void executeTransfer(
            @Parameter(description = "Идентификатор клиента", required = true, schema = @Schema(type = "string", maxLength = 10, example = "1234567890"))
            @RequestHeader(name = "client-id") String clientId,

//            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные для выполнения перевода", required = true)
            @Parameter(description = "Данные для выполнения перевода", required = true)
            @RequestBody ExecuteTransferDtoRq executeTransferDtoRq
    ) {
        transfersService.execute(clientId, executeTransferDtoRq);
    }
}
