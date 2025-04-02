package spring.boot.apis.provider;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import spring.boot.apis.dto.user.UserResponse;
import spring.boot.constant.HttpMessage;
import spring.boot.exception.ServiceException;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JWTProvider implements ITokenBasedProvider<SignedJWT, UserResponse> {
    @Override
    public String buildScope(UserResponse user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(
                    role -> {
                        stringJoiner.add("ROLE_" + role.getName());
                        if (!CollectionUtils.isEmpty(role.getPrivileges()))
                            role.getPrivileges().forEach(permission -> stringJoiner.add(permission.getName()));
                    });
        return stringJoiner.toString();
    }

    @Override
    public String sign(UserResponse user, long expiredTime, String secretKey, String id, String referId) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(expiredTime, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(id)
                .claim("scope", buildScope(user))
                .claim("userId", user.getId())
                .claim("referId", referId)
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(secretKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new ServiceException(HttpMessage.SIGN_TOKEN);
        }
    }

    @Override
    public SignedJWT verify(String token, String secretKey) {
        try {
            JWSVerifier verifier = new MACVerifier(secretKey.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);
            boolean isVerify = signedJWT.verify(verifier);
            if (!isVerify) {
                throw new ServiceException(HttpMessage.ILL_LEGAL_TOKEN);
            }
            return signedJWT;
        } catch (ParseException e) {
            throw new ServiceException(HttpMessage.PARSE_TOKEN);
        } catch (JOSEException e) {
            throw new ServiceException(HttpMessage.ILL_LEGAL_TOKEN);
        }
    }
}
