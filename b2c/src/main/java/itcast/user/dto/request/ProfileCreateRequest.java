package itcast.user.dto.request;
import itcast.domain.user.enums.ArticleType;
import itcast.domain.user.enums.Interest;
import itcast.domain.user.enums.SendingType;


public record ProfileCreateRequest(
	String nickname,
	ArticleType articleType,
	Interest interest,
	SendingType sendingType,
	String email
) {}