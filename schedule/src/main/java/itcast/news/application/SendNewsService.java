package itcast.news.application;

import itcast.domain.news.News;
import itcast.domain.newsHistory.NewsHistory;
import itcast.domain.user.User;
import itcast.domain.user.enums.Interest;
import itcast.exception.ErrorCodes;
import itcast.exception.ItCastApplicationException;
import itcast.jwt.repository.UserRepository;
import itcast.mail.application.MailService;
import itcast.mail.dto.request.MailContent;
import itcast.mail.dto.request.SendMailRequest;
import itcast.message.application.MessageService;
import itcast.message.dto.request.MessageContent;
import itcast.message.dto.request.RecieverPhoneNumber;
import itcast.message.dto.request.SendMessageRequest;
import itcast.news.repository.NewsHistoryRepository;
import itcast.news.repository.NewsRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SendNewsService {

    private static final int ALARM_DAY = 2;

    private final NewsRepository newsRepository;
    private final UserRepository userRepository;
    private final NewsHistoryRepository newsHistoryRepository;
    private final MailService mailService;
    private final MessageService messageService;

    @Transactional
    public void selectNews(LocalDate yesterday) {
        List<News> newsList = newsRepository.findRatingTop3ByCreatedAt(yesterday);

        if (newsList == null || newsList.isEmpty()) {
            throw new ItCastApplicationException(ErrorCodes.INVALID_NEWS_CONTENT);
        }

        LocalDate sendAt = LocalDate.now().plusDays(ALARM_DAY);
        newsList.forEach(news -> {
            news.newsUpdate(sendAt);
        });
    }

    public void sendEmails() {
        List<News> sendNews = newsRepository.findAllBySendAt();

        if (sendNews == null || sendNews.isEmpty()) {
            throw new ItCastApplicationException(ErrorCodes.NOT_FOUND_SEND_DATA);
        }

        List<MailContent> mailContents = sendNews.stream()
                .map(news ->
                        MailContent.of(
                                news.getTitle(),
                                news.getContent(),
                                news.getLink(),
                                news.getThumbnail()))
                .toList();
        List<String> emails = retrieveUserEmails(Interest.NEWS);

        if (emails == null || emails.isEmpty()) {
            throw new ItCastApplicationException(ErrorCodes.NOT_FOUND_EMAIL);
        }

        SendMailRequest mailRequest = new SendMailRequest(emails, mailContents);
        mailService.send(mailRequest);
        createNewsHistoryByEmail(sendNews);
    }

    public List<String> retrieveUserEmails(Interest interest) {
        if (interest != Interest.NEWS) {
            throw new ItCastApplicationException(ErrorCodes.INVALID_INTEREST_TYPE_ERROR);
        }
        return userRepository.findAllByInterest(interest)
                .stream()
                .map(User::getEmail)
                .toList();
    }

    public void createNewsHistoryByEmail(List<News> sendNews) {
        List<User> users = userRepository.findAllByInterest(Interest.NEWS);
        List<NewsHistory> newsHistories = sendNews.stream()
                .flatMap(news -> users.stream()
                        .map(user -> NewsHistory.builder()
                                .user(user)
                                .news(news)
                                .build()))
                .toList();
        newsHistoryRepository.saveAll(newsHistories);
    }

    public void sendMessages() {
        List<News> sendNews = newsRepository.findAllBySendAt();

        if (sendNews == null || sendNews.isEmpty()) {
            throw new ItCastApplicationException(ErrorCodes.NOT_FOUND_SEND_DATA);
        }

        List<MessageContent> messageContents = sendNews.stream()
                .map(news -> new MessageContent(
                        news.getTitle(),
                        news.getContent(),
                        news.getLink()))
                .toList();
        List<RecieverPhoneNumber> phoneNumbers = retrieveUserPhoneNumbers(Interest.NEWS);

        if (phoneNumbers == null || phoneNumbers.isEmpty()) {
            throw new ItCastApplicationException(ErrorCodes.NOT_FOUND_PHONE_NUMBERS);
        }

        SendMessageRequest sendMessageRequest = new SendMessageRequest(messageContents, phoneNumbers);
        messageService.sendMessages(sendMessageRequest);
        createNewsHistoryByMessage(sendNews);
    }

    public List<RecieverPhoneNumber> retrieveUserPhoneNumbers(Interest interest) {
        if (interest != Interest.NEWS) {
            throw new ItCastApplicationException(ErrorCodes.INVALID_INTEREST_TYPE_ERROR);
        }
        return userRepository.findAllByInterest(interest)
                .stream()
                .map(user -> new RecieverPhoneNumber(user.getPhoneNumber()))
                .toList();
    }

    public void createNewsHistoryByMessage(List<News> sendNews) {
        List<User> users = userRepository.findAllByInterest(Interest.NEWS);
        List<NewsHistory> newsHistories = sendNews.stream()
                .flatMap(news -> users.stream()
                        .map(user -> NewsHistory.builder()
                                .user(user)
                                .news(news)
                                .build()))
                .toList();
        newsHistoryRepository.saveAll(newsHistories);
    }
}
