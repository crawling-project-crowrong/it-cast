package itcast.application;

import com.opencsv.CSVWriter;
import itcast.domain.newsHistory.NewsHistory;
import itcast.domain.user.User;
import itcast.dto.response.AdminNewsHistoryResponse;
import itcast.exception.ErrorCodes;
import itcast.exception.ItCastApplicationException;
import itcast.jwt.repository.UserRepository;
import itcast.repository.AdminRepository;
import itcast.repository.NewsHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminNewsHistoryService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final NewsHistoryRepository newsHistoryRepository;

    public Page<AdminNewsHistoryResponse> retrieveNewsHistory(Long adminId, Long userId, Long newsId, LocalDate createdAt,
                                                              int page, int size
    ) {
        isAdmin(adminId);
        Pageable pageable = PageRequest.of(page, size);
        return newsHistoryRepository.findNewsHistoryListByCondition(userId, newsId, createdAt, pageable);
    }

    public String createCsvFile(Long adminId) {
        isAdmin(adminId);
        List<NewsHistory> newsHistoryList = newsHistoryRepository.findAll();
        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter);

        String[] header = new String[]{"id", "userId", "newsId", "createdAt", "updatedAt"};
        csvWriter.writeNext(header);

        for (NewsHistory newsHistory : newsHistoryList) {
            String[] data = {
                    String.valueOf(newsHistory.getId()),
                    String.valueOf(newsHistory.getUser().getId()),
                    String.valueOf(newsHistory.getNews().getId()),
                    String.valueOf(newsHistory.getCreatedAt()),
                    String.valueOf(newsHistory.getModifiedAt())
            };
            csvWriter.writeNext(data);
        }

        try {
            csvWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stringWriter.toString();
    }

    private void isAdmin(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ItCastApplicationException(ErrorCodes.USER_NOT_FOUND));
        String email = user.getKakaoEmail();
        if (!adminRepository.existsByEmail(email)) {
            throw new ItCastApplicationException(ErrorCodes.INVALID_USER);
        }
    }
}
