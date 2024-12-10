package itcast.user.dto.response;

import itcast.domain.user.enums.ArticleType;
import itcast.domain.user.enums.Interest;
import itcast.domain.user.enums.SendingType;

public record ProfileCreateResponse(
	Long id,
	String kakaoEmail,
	String nickname,
	ArticleType articleType,
	Interest interest,
	SendingType sendingType,
	String email
) {}

