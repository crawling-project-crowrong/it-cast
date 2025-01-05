package itcast.application;

import itcast.domain.mailEvent.MailEvents;
import itcast.dto.response.MailResponse;
import itcast.repository.MailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminMailService {

    private final MailRepository mailRepository;
    private final AdminCheckService adminCheckService;

    public Page<MailResponse> retrieveMailEvent(Long adminId, int page, int size) {
        adminCheckService.isAdmin(adminId);
        PageRequest pageable = PageRequest.of(page, size);
        Page<MailEvents> mailEventsPage = mailRepository.findAll(pageable);

        Map<Long, Map<LocalDate, List<MailResponse.MailContent>>> groupedData = mailEventsPage.getContent().stream()
                .collect(Collectors.groupingBy(
                        event -> event.getUser().getId(),
                        Collectors.groupingBy(
                                event -> event.getCreatedAt().toLocalDate(),
                                Collectors.mapping(
                                        event -> new MailResponse.MailContent(
                                                event.getId(),
                                                event.getTitle(),
                                                event.getSummary(),
                                                event.getOriginalLink(),
                                                event.getThumbnail()
                                        ),
                                        Collectors.toList()
                                )
                        )
                ));

        List<MailResponse> mailResponses = groupedData.entrySet().stream()
                .flatMap(userEntry -> userEntry.getValue().entrySet().stream()
                        .map(dateEntry -> new MailResponse(
                                userEntry.getKey(),
                                dateEntry.getKey(),
                                dateEntry.getValue()
                        )))
                .collect(Collectors.toList());

        return new PageImpl<>(mailResponses, pageable, mailEventsPage.getTotalElements());
    }
}