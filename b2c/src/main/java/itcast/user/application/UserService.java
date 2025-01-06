package itcast.user.application;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import itcast.domain.user.User;
import itcast.domain.user.enums.ArticleType;
import itcast.domain.user.enums.Interest;
import itcast.domain.user.enums.SendingType;
import itcast.exception.ErrorCodes;
import itcast.exception.ItCastApplicationException;
import itcast.jwt.repository.UserRepository;
import itcast.user.dto.request.ProfileCreateRequest;
import itcast.user.dto.request.ProfileUpdateRequest;
import itcast.user.dto.response.ProfileCreateResponse;
import itcast.user.dto.response.ProfileUpdateResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional
    public ProfileCreateResponse createProfile(ProfileCreateRequest request, Long id) {
        User existingUser = findUserByIdOrThrow(id);
        validateSendingTypeConstraints(request.sendingType(), request.phoneNumber(), request.email());
        validateVerification(request);
        validateConstraints(request.nickname(), request.email(), request.phoneNumber());

        if (ArticleType.NEWS.equals(request.articleType())) {
            request = new ProfileCreateRequest(
                    request.nickname(),
                    request.articleType(),
                    Interest.NEWS,
                    request.sendingType(),
                    request.email(),
                    request.phoneNumber()
            );
        }
        User updatedUser = request.toEntity(existingUser);
        User savedUser = userRepository.save(updatedUser);
        redisTemplate.delete("VERIFIED_PHONE_NUMBER" + request.phoneNumber());  // 수정된 부분
        return ProfileCreateResponse.fromEntity(savedUser);
    }

    @Transactional
    public ProfileUpdateResponse updateProfile(ProfileUpdateRequest request, Long id) {
        User existingUser = findUserByIdOrThrow(id);
        validateSendingTypeConstraints(request.sendingType(), request.phoneNumber(), request.email());
        validateVerification(request); // 인증 검증 추가
        validateConstraints(request.nickname(), request.email(), request.phoneNumber());

        if (ArticleType.NEWS.equals(request.articleType())) {
            request = new ProfileUpdateRequest(
                    request.nickname(),
                    request.articleType(),
                    Interest.NEWS,
                    request.sendingType(),
                    request.email(),
                    request.phoneNumber()
            );
        }
        User updatedUser = request.toEntity(existingUser);
        User savedUser = userRepository.save(updatedUser);
        redisTemplate.delete("VERIFIED_PHONE_NUMBER" + request.phoneNumber());  // 수정된 부분
        return ProfileUpdateResponse.fromEntity(savedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = findUserByIdOrThrow(id);
        userRepository.delete(user);
    }

    private User findUserByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ItCastApplicationException(ErrorCodes.USER_NOT_FOUND));
    }

    private void validateConstraints(String nickname, String email, String phoneNumber) {
        if (userRepository.existsByNickname(nickname)) {
            throw new ItCastApplicationException(ErrorCodes.NICKNAME_ALREADY_EXISTS);
        }
        if (userRepository.existsByEmail(email)) {
            throw new ItCastApplicationException(ErrorCodes.EMAIL_ALREADY_EXISTS);
        }
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new ItCastApplicationException(ErrorCodes.PHONE_NUMBER_ALREADY_EXISTS);
        }
    }

    private void validateSendingTypeConstraints(SendingType sendingType, String phoneNumber, String email) {
        if (SendingType.MESSAGE.equals(sendingType) && (phoneNumber == null || phoneNumber.isEmpty())) {
            throw new ItCastApplicationException(ErrorCodes.PHONE_NUMBER_REQUIRED);
        }

        if (SendingType.EMAIL.equals(sendingType) && (email == null || email.isEmpty())) {
            throw new ItCastApplicationException(ErrorCodes.EMAIL_REQUIRED);
        }
    }

    public void validateVerification(ProfileCreateRequest request) {
        validateSendingTypeVerification(request.sendingType(), request.phoneNumber(), request.email());
    }

    public void validateVerification(ProfileUpdateRequest request) {
        validateSendingTypeVerification(request.sendingType(), request.phoneNumber(), request.email());
    }

    private void validateSendingTypeVerification(SendingType sendingType, String phoneNumber, String email) {
        if (SendingType.MESSAGE.equals(sendingType)) {
            if (!isVerifiedPhoneNumber(phoneNumber)) {
                throw new ItCastApplicationException(ErrorCodes.PHONE_NUMBER_VERIFICATION_REQUIRED);
            }
        } else if (SendingType.EMAIL.equals(sendingType)) {
            if (!isVerifiedEmail(email)) {
                throw new ItCastApplicationException(ErrorCodes.EMAIL_VERIFICATION_REQUIRED);
            }
        } else {
            throw new ItCastApplicationException(ErrorCodes.INVALID_SENDING_TYPE);
        }
    }

    private boolean isVerifiedPhoneNumber(String phoneNumber) {
        Boolean isVerified = (Boolean) redisTemplate.opsForValue().get("VERIFIED_PHONE_NUMBER" + phoneNumber);  // 수정된 부분
        return isVerified != null && isVerified;
    }

    private boolean isVerifiedEmail(String email) {
        Boolean isVerified = (Boolean) redisTemplate.opsForValue().get("VERIFIED_EMAIL" + email);  // 수정된 부분
        return isVerified != null && isVerified;
    }
}
}